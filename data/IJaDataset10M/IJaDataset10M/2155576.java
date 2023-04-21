package com.once.server.data.export;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import com.once.server.config.ConfigManager;

public class OnceReport {

    private static final String PATTERN_ESCAPED_AMPERSAND = "&amp;";

    private static final String PATTERN_ESCAPED_DOUBLE_QUOTE = "&quot;";

    private static final String PATTERN_ESCAPED_GREATER_THAN = "&gt;";

    private static final String PATTERN_ESCAPED_LESS_THAN = "&lt;";

    private static final String PATTERN_ESCAPED_SINGLE_QUOTE = "&apos;";

    private static final String PATTERN_NOTHING = "";

    private static final char SYMBOL_AMPERSAND = '&';

    private static final char SYMBOL_DOUBLE_QUOTE = '"';

    private static final char SYMBOL_GREATER_THAN = '>';

    private static final char SYMBOL_LESS_THAN = '<';

    private static final char SYMBOL_SINGLE_QUOTE = '\'';

    public static double _(Double encapsulated) {
        return (primitive(encapsulated));
    }

    public static double _(double target) {
        return (primitive(target));
    }

    public static float _(Float encapsulated) {
        return (primitive(encapsulated));
    }

    public static float _(float target) {
        return (primitive(target));
    }

    public static int _(Integer encapsulated) {
        return (primitive(encapsulated));
    }

    public static int _(int target) {
        return (primitive(target));
    }

    public static long _(Long encapsulated) {
        return (primitive(encapsulated));
    }

    public static long _(long target) {
        return (primitive(target));
    }

    public static Double $(Double target) {
        return (encapsulated(target));
    }

    public static Double $(double primitive) {
        return (encapsulated(primitive));
    }

    public static Float $(Float target) {
        return (encapsulated(target));
    }

    public static Float $(float primitive) {
        return (encapsulated(primitive));
    }

    public static Integer $(Integer target) {
        return (encapsulated(target));
    }

    public static Integer $(int primitive) {
        return (encapsulated(primitive));
    }

    public static Long $(Long target) {
        return (encapsulated(target));
    }

    public static Long $(long primitive) {
        return (encapsulated(primitive));
    }

    public static Double encapsulated(Double target) {
        return (new Double(primitive(target)));
    }

    public static Double encapsulated(double primitive) {
        return (new Double(primitive));
    }

    public static Float encapsulated(Float target) {
        return (new Float(primitive(target)));
    }

    public static Float encapsulated(float primitive) {
        return (new Float(primitive));
    }

    public static Integer encapsulated(Integer target) {
        return (new Integer(primitive(target)));
    }

    public static Integer encapsulated(int primitive) {
        return (new Integer(primitive));
    }

    public static Long encapsulated(Long target) {
        return (new Long(primitive(target)));
    }

    public static Long encapsulated(long primitive) {
        return (new Long(primitive));
    }

    public static String formattedDate(Object date) {
        return ((new SimpleDateFormat(ConfigManager.getInstance().getExportDateFormat())).format(date));
    }

    public static String formattedDate(Object date, String pattern) {
        return ((new SimpleDateFormat(pattern)).format(date));
    }

    public static String formattedNumber(Double number, String pattern) {
        return ((new DecimalFormat(pattern)).format(primitive(number)));
    }

    public static String formattedNumber(double number, String pattern) {
        return ((new DecimalFormat(pattern)).format(number));
    }

    public static String formattedNumber(Float number, String pattern) {
        return ((new DecimalFormat(pattern)).format(primitive(number)));
    }

    public static String formattedNumber(float number, String pattern) {
        return ((new DecimalFormat(pattern)).format(number));
    }

    public static String formattedNumber(Integer number, String pattern) {
        return ((new DecimalFormat(pattern)).format(primitive(number)));
    }

    public static String formattedNumber(int number, String pattern) {
        return ((new DecimalFormat(pattern)).format(number));
    }

    public static String formattedNumber(Long number, String pattern) {
        return ((new DecimalFormat(pattern)).format(primitive(number)));
    }

    public static String formattedNumber(long number, String pattern) {
        return ((new DecimalFormat(pattern)).format(number));
    }

    public static String formattedText(Object text) {
        return (text != null ? text.toString() : PATTERN_NOTHING);
    }

    public static double primitive(Double encapsulated) {
        return (encapsulated != null ? encapsulated.doubleValue() : 0);
    }

    public static double primitive(double target) {
        return (target);
    }

    public static float primitive(Float encapsulated) {
        return (encapsulated != null ? encapsulated.floatValue() : 0);
    }

    public static float primitive(float target) {
        return (target);
    }

    public static int primitive(Integer encapsulated) {
        return (encapsulated != null ? encapsulated.intValue() : 0);
    }

    public static int primitive(int target) {
        return (target);
    }

    public static long primitive(Long encapsulated) {
        return (encapsulated != null ? encapsulated.longValue() : 0);
    }

    public static long primitive(long target) {
        return (target);
    }

    public static String styled(Object unkempt) {
        String targetText;
        StringBuffer styledText;
        char eachCharacter;
        int index;
        int targetTextLength;
        styledText = new StringBuffer();
        if (unkempt != null) {
            targetText = unkempt.toString();
            targetTextLength = targetText.length();
            for (index = 0; index < targetTextLength; index++) switch(eachCharacter = targetText.charAt(index)) {
                case SYMBOL_DOUBLE_QUOTE:
                    styledText.append(PATTERN_ESCAPED_DOUBLE_QUOTE);
                    break;
                case SYMBOL_AMPERSAND:
                    styledText.append(PATTERN_ESCAPED_AMPERSAND);
                    break;
                case SYMBOL_SINGLE_QUOTE:
                    styledText.append(PATTERN_ESCAPED_SINGLE_QUOTE);
                    break;
                case SYMBOL_LESS_THAN:
                    styledText.append(PATTERN_ESCAPED_LESS_THAN);
                    break;
                case SYMBOL_GREATER_THAN:
                    styledText.append(PATTERN_ESCAPED_GREATER_THAN);
                    break;
                default:
                    styledText.append(eachCharacter);
                    break;
            }
        }
        return (styledText.toString());
    }
}
