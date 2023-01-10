package com.mubeendroid.scientificcalculator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mubeendroid.scientificcalculator.databinding.ActivityMainBinding
import java.math.BigInteger
import javax.script.ScriptEngineManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var calculateString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding the layout using viewBinding feature
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            //button click actions
            button0.setOnClickListener { appendNumber("0") }

            button1.setOnClickListener { appendNumber("1") }

            button2.setOnClickListener { appendNumber("2") }

            button3.setOnClickListener { appendNumber("3") }

            button4.setOnClickListener { appendNumber("4") }

            button5.setOnClickListener { appendNumber("5") }

            button6.setOnClickListener { appendNumber("6") }

            button7.setOnClickListener { appendNumber("7") }

            button8.setOnClickListener { appendNumber("8") }

            button9.setOnClickListener { appendNumber("9") }

            buttonClear.setOnClickListener {
                secondaryText.text = ""
                primaryText.text = ""
                calculateString = ""
            }

            buttonBackspace.setOnClickListener {
                try {
                    val text: String = primaryText.text.toString()
                    if (text.isNotEmpty()) {
                        if (text[text.length - 1] == '(') {
                            if (text.substring(text.length - 3, text.length) == "ln(") {
                                primaryText.text = text.substring(0, text.length - 3)
                                calculateString =
                                    calculateString.substring(0, calculateString.length - 3)
                            } else {
                                primaryText.text = text.substring(0, text.length - 4)
                                calculateString =
                                    calculateString.substring(0, calculateString.length - 4)
                            }
                        } else {
                            primaryText.text = text.substring(0, text.length - 1)
                            calculateString =
                                calculateString.substring(0, calculateString.length - 1)
                        }
                    }
                } catch (_: Exception) {}
            }

            buttonBracketOpen.setOnClickListener {
                val text: String = primaryText.text.toString()
                try {
                    val subText = text.substring(text.length - 4, text.length)
                    if (!subText.contains("sin") && !subText.contains("cos") && !subText.contains("tan")
                        && !subText.contains("log") && !subText.contains("ln")) {
                        primaryText.text = text.plus("(")
                        calculateString = "$calculateString("
                    }
                }catch (_: Exception) {
                    primaryText.text = text.plus("(")
                    calculateString = "$calculateString("
                }
            }

            buttonBracketClose.setOnClickListener {
                if (primaryText.text.isNotEmpty()) {
                    primaryText.text = primaryText.text.toString().plus(")")
                    calculateString = "$calculateString)"
                }
            }

            buttonSin.setOnClickListener { appendString("sin(", "Math.sin(") }

            buttonCos.setOnClickListener { appendString("cos(", "Math.cos(") }

            buttonTan.setOnClickListener { appendString("tan(", "Math.tan(") }

            buttonLog.setOnClickListener { appendString("log(", "Math.log(") }

            buttonLn.setOnClickListener { appendString("ln(", "Math.ln(") }

            buttonFactorial.setOnClickListener {
                try {
                    val d = primaryText.text.toString().toBigInteger()
                    var fact = BigInteger("1")
                    for (i in 1..d.toInt()) {
                        fact *= i.toBigInteger()
                    }
                    primaryText.text = primaryText.text.toString().plus("!")
                    calculateString = fact.toString()
                } catch (_: java.lang.Exception) {
                }
            }

            buttonPower.setOnClickListener {
                if (primaryText.text.isNotEmpty()) {
                    primaryText.text = primaryText.text.toString().plus("^")
                    calculateString =
                        "Math.pow(" + calculateString[calculateString.length - 1] + ","
                }
            }

            buttonRoot.setOnClickListener {
                try{
                    if (primaryText.text.toString().isNotEmpty()) {
                        primaryText.text = "\u221A".plus(primaryText.text)
                        val d =
                            primaryText.text.toString().substring(1, primaryText.text.length).toDouble()
                        calculateString =
                            calculateString.replace(d.toInt().toString(), "") + "Math.sqrt(" + d + ")"
                    }
                } catch(_: Exception) {}
            }

            buttonInverse.setOnClickListener {
                primaryText.text = "1/".plus(primaryText.text)
                calculateString = calculateString + "1/" + primaryText.text
            }

            buttonMod.setOnClickListener {
                if (calculateString.isNotEmpty()) {
                    calculateString = "($calculateString)"
                }
                if (primaryText.text.toString().isNotEmpty()) {
                    calculateString += "%"
                    primaryText.text = primaryText.text.toString().plus(getString(R.string.percent))
                }
            }

            buttonDivision.setOnClickListener {
                if (primaryText.text.toString().isNotEmpty()) {
                    calculateString += "/"
                    primaryText.text = primaryText.text.toString().plus(getString(R.string.division))
                }
            }

            buttonMultiplication.setOnClickListener {
                if (primaryText.text.toString().isNotEmpty()) {
                    calculateString += "*"
                    primaryText.text = primaryText.text.toString().plus(getString(R.string.multiplication))
                }
            }

            buttonSubtraction.setOnClickListener {
                if (primaryText.text.toString().isNotEmpty()) {
                    calculateString += "-"
                    primaryText.text = primaryText.text.toString().plus(getString(R.string.subtraction))
                }
            }

            buttonAddition.setOnClickListener {
                if (primaryText.text.toString().isNotEmpty()) {
                    calculateString += "+"
                    primaryText.text = primaryText.text.toString().plus(getString(R.string.addition))
                }
            }

            buttonDot.setOnClickListener {
                primaryText.text = primaryText.text.toString().plus(getString(R.string.dot))
                calculateString += "."
            }

            buttonPi.setOnClickListener {
                primaryText.text = primaryText.text.toString().plus(Math.PI.toString())
                calculateString += Math.PI.toString()
            }

            buttonEqual.setOnClickListener {
                // Using Rhino script engine to calculate the string
                // Visit https://github.com/APISENSE/rhino-android to know more about this library
                val engine = ScriptEngineManager().getEngineByName("rhino")
                if (calculateString.isNotEmpty()) {
                    // Log the string which will be calculated to see if the string contains any error
                    Log.d("calculate_result", calculateString)
                    try {
                        if (primaryText.text.toString().contains("^")) {
                            calculateString = "$calculateString)"
                        }
                        val result = engine.eval(calculateString) as Double
                        if (result % 1 == 0.0) {
                            val a = String.format("%.0f", result)
                            secondaryText.text = primaryText.text
                            primaryText.text = a
                        } else {
                            secondaryText.text = primaryText.text
                            if (result.toString() == "Infinity") {
                                primaryText.text = getString(R.string.math_error)
                            } else {
                                primaryText.text = result.toString()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        secondaryText.text = primaryText.text
                        primaryText.text = getString(R.string.math_error)
                    }
                }
            }
        }
    }

    /* This function appends number to the string which will be calculated and shows the appended
    string on the screen
    */
    private fun appendNumber(number: String) {
        if (binding.primaryText.text.length > 32) {
            Toast.makeText(this@MainActivity, "Number is too big", Toast.LENGTH_SHORT).show()
        } else {
            binding.primaryText.text = binding.primaryText.text.toString().plus(number)
            calculateString += number
        }
    }

    /* This function appends mathematical functions such as sin, cos, tan, log, ln to the string
     which will be calculated and shows the appended string on the screen
     */
    private fun appendString(val1: String, val2: String) {
        if (binding.primaryText.text.length > 32) {
            Toast.makeText(this@MainActivity, "Number is too big", Toast.LENGTH_SHORT).show()
        } else {
            binding.primaryText.text = binding.primaryText.text.toString().plus(val1)
            calculateString += val2
        }
    }
}