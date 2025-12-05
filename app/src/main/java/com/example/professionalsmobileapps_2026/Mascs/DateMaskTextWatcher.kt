package com.example.professionalsmobileapps_2026.Mascs

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DateMaskTextWatcher(private val editText: EditText) : TextWatcher {

    private var isUpdating = false
    private var oldText = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Не используется
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Не используется
    }

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) {
            isUpdating = false
            return
        }

        val text = s.toString()

        // Удаляем все символы кроме цифр
        val cleanText = text.replace(Regex("[^\\d]"), "")

        // Форматируем в dd.mm.yyyy
        val formatted = formatDate(cleanText)

        if (text != formatted) {
            isUpdating = true
            editText.setText(formatted)
            editText.setSelection(formatted.length)
        }
    }

    private fun formatDate(text: String): String {
        if (text.isEmpty()) return ""

        val length = text.length
        val builder = StringBuilder()

        for (i in 0 until length) {
            when (i) {
                0 -> {
                    // Первая цифра дня
                    val digit = text[i]
                    if (digit > '3') {
                        builder.append('0')
                        builder.append(digit)
                        if (length > 1) builder.append('.')
                    } else {
                        builder.append(digit)
                    }
                }
                1 -> {
                    // Вторая цифра дня
                    val digit = text[i]
                    val firstDigit = text[0]
                    if (firstDigit == '3' && digit > '1') {
                        builder.append('1')
                    }
                    builder.append(digit)
                    if (length > 2) builder.append('.')
                }
                2 -> {
                    // Точка после дня (уже добавлена)
                    // Первая цифра месяца
                    val digit = text[i]
                    if (digit > '1') {
                        builder.append('0')
                        builder.append(digit)
                        if (length > 3) builder.append('.')
                    } else {
                        builder.append(digit)
                    }
                }
                3 -> {
                    // Вторая цифра месяца
                    val digit = text[i]
                    val monthFirstDigit = text[2]
                    if (monthFirstDigit == '1' && digit > '2') {
                        builder.append('2')
                    } else {
                        builder.append(digit)
                    }
                    if (length > 4) builder.append('.')
                }
                4 -> {
                    // Точка после месяца (уже добавлена)
                    // Первая цифра года
                    builder.append(digit)
                }
                in 5..7 -> {
                    // Остальные цифры года
                    builder.append(text[i])
                }
            }

            // Ограничиваем длину 10 символами (dd.mm.yyyy)
            if (builder.length >= 10) break
        }

        return builder.toString()
    }

    companion object {
        fun isValidDate(date: String): Boolean {
            if (date.length != 10) return false

            val regex = Regex("""^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[0-2])\.(\d{4})$""")
            return regex.matches(date)
        }

        fun parseDate(date: String): Triple<Int, Int, Int>? {
            if (!isValidDate(date)) return null

            val parts = date.split(".")
            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()

            return Triple(day, month, year)
        }
    }
}