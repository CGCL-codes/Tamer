package org.encog.util.csv;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Specifies a CSV format. This allows you to determine if a decimal point or
 * decimal comma is uses. It also specifies the character that should be used to
 * separate numbers.
 * 
 */
public class CSVFormat {

    /**
	 * Use a decimal point, and a comma to separate numbers.
	 */
    public static final CSVFormat DECIMAL_POINT = new CSVFormat('.', ',');

    /**
	 * Use a decimal comma, and a semicolon to separate numbers.
	 */
    public static final CSVFormat DECIMAL_COMMA = new CSVFormat(',', ';');

    /**
	 * Decimal point is typically used in English speaking counties.
	 */
    public static final CSVFormat ENGLISH = CSVFormat.DECIMAL_POINT;

    /**
	 * EG files, internally use a decimal point and comma separator.
	 */
    public static final CSVFormat EG_FORMAT = CSVFormat.DECIMAL_POINT;

    /**
	 * Get the decimal character currently in use by the computer's default
	 * location.
	 * 
	 * @return The decimal character used.
	 */
    public static char getDecimalCharacter() {
        final NumberFormat nf = NumberFormat.getInstance();
        final String str = nf.format(0.5);
        for (int i = 0; i < str.length(); i++) {
            final char ch = str.charAt(i);
            if (!Character.isDigit(ch)) {
                return ch;
            }
        }
        return '.';
    }

    /**
	 * The decimal character.
	 */
    private final char decimal;

    /**
	 * The separator character.
	 */
    private final char separator;

    /**
	 * The number formatter to use for this format.
	 */
    private final NumberFormat numberFormatter;

    /**
	 * By default use USA conventions.
	 */
    public CSVFormat() {
        this('.', ',');
    }

    /**
	 * Construct a CSV format with he specified decimal and separator
	 * characters.
	 * 
	 * @param decimal
	 *            The decimal character.
	 * @param separator
	 *            The separator character.
	 */
    public CSVFormat(final char decimal, final char separator) {
        super();
        this.decimal = decimal;
        this.separator = separator;
        if (decimal == '.') {
            this.numberFormatter = NumberFormat.getInstance(Locale.US);
        } else if (decimal == ',') {
            this.numberFormatter = NumberFormat.getInstance(Locale.FRANCE);
        } else {
            this.numberFormatter = NumberFormat.getInstance();
        }
    }

    /**
	 * Format the specified number to a string with the specified number of
	 * fractional digits.
	 * 
	 * @param d
	 *            The number to format.
	 * @param digits
	 *            The number of fractional digits.
	 * @return The number formatted as a string.
	 */
    public synchronized String format(final double d, final int digits) {
        if (Double.isInfinite(d) || Double.isNaN(d)) return "0";
        this.numberFormatter.setGroupingUsed(false);
        this.numberFormatter.setMaximumFractionDigits(digits);
        return this.numberFormatter.format(d);
    }

    /**
	 * @return The decimal character.
	 */
    public char getDecimal() {
        return this.decimal;
    }

    /**
	 * @return The number formatter.
	 */
    public NumberFormat getNumberFormatter() {
        return this.numberFormatter;
    }

    /**
	 * @return The separator character.
	 */
    public char getSeparator() {
        return this.separator;
    }

    /**
	 * Parse the specified string to a double.
	 * 
	 * @param str
	 *            The string to parse.
	 * @return The parsed number.
	 */
    public synchronized double parse(final String str) {
        try {
            if (str.equals("?")) {
                return Double.NaN;
            } else {
                return this.numberFormatter.parse(str.trim()).doubleValue();
            }
        } catch (final Exception e) {
            throw new CSVError("Error:" + e.getMessage() + " on [" + str + "], decimal:" + this.decimal + ",sep: " + this.separator);
        }
    }
}
