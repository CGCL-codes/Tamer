package com.mrkuchipudi.requestmaps.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 
 * @author Maruthi R. Kuchipudi
 *
 */
public class NumberUtils {

    public static final String CURRENCY = "CURRENCY";

    public static final String PERCENT = "PERCENT";

    public static final String DECIMAL2 = "DECIMAL2";

    public static final String DECIMAL4 = "DECIMAL4";

    public static final String NUMEBR = "NUMBER";

    public static final String DECIMAL = "DECIMAL";

    /**
     * Rounding Methods
     */
    public static String roundDecimal(double value) {
        return roundDecimal(value, BigDecimal.ROUND_HALF_UP, 2);
    }

    public static String roundDecimal(double value, int scale) {
        return roundDecimal(value, BigDecimal.ROUND_HALF_UP, scale);
    }

    public static String roundDecimal(double value, int roundingMode, int scale) {
        return (new BigDecimal(value).setScale(scale, roundingMode).toString());
    }

    public static Double roundDecimalDouble(Double value) {
        if (value != null) return new Double(roundDecimal(value.doubleValue(), BigDecimal.ROUND_HALF_UP, 2)); else return null;
    }

    public static Double roundDecimalDouble(Double value, int scale) {
        if (value != null) return new Double(roundDecimal(value.doubleValue(), BigDecimal.ROUND_HALF_UP, scale)); else return null;
    }

    public static Double roundDecimalDouble(Double value, int roundingMode, int scale) {
        if (value != null) return new Double(roundDecimal(value.doubleValue(), roundingMode, 2)); else return null;
    }

    public static String format(String value, String format) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(Double.parseDouble(value));
    }

    public static String currencyFormat(String value) {
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            return nf.format(Double.parseDouble(value));
        } catch (Exception ex) {
            return value;
        }
    }

    public static String percentFormat(String value) {
        NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
        return nf.format(Double.parseDouble(value));
    }

    public static String numberFormat(String value) {
        String formattedValue = "";
        DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(false);
        try {
            formattedValue = df.format(Double.parseDouble(value));
        } catch (Exception ex) {
            formattedValue = value;
        }
        return formattedValue;
    }

    public static String decimal2Format(String value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(2);
        return nf.format(Double.parseDouble(value));
    }

    public static String decimal4Format(String value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(4);
        return nf.format(Double.parseDouble(value));
    }

    public static void main(String args[]) {
        String number = "27788AG";
        System.out.println(numberFormat(number));
    }
}
