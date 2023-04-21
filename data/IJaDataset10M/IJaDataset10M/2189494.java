package com.jedi;

import java.text.DecimalFormat;
import java.text.*;
import java.util.*;

/**
 * 
 * @author button
 */
public class FormatNum {

    /** Creates a new instance of Format */
    public static String formatCurrency(double d, String pattern, Locale l) {
        String s = "";
        try {
            DecimalFormat nf = (DecimalFormat) NumberFormat.getCurrencyInstance(l);
            nf.applyPattern(pattern);
            s = nf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String formatCurrency(double d) {
        return formatCurrency(d, "#,##0.00", Locale.CHINA);
    }

    public static String formatNumber(double d) {
        return formatCurrency(d, "#0.00", Locale.CHINA);
    }

    public static String formatNumber4(double d) {
        return formatCurrency(d, "#.0000", Locale.CHINA);
    }

    public static String formatInt(double d) {
        return formatCurrency(d, "#", Locale.CHINA);
    }

    public static String formatCurrencyus(double d) {
        return formatCurrency(d, "$#,##0.00", Locale.US);
    }

    public static String formatCurrencycn(double d) {
        return formatCurrency(d, "¤#,##0.00", Locale.CHINA);
    }

    public FormatNum() {
    }
}
