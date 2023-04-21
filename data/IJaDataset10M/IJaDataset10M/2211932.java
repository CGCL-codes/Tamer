package org.illico.common.text.formatter;

import org.illico.common.lang.StringUtils;

public class BooleanFormatter extends AbstractFormatter<Boolean> {

    public static final String PATTERN_SEPARATOR = "|";

    private static final String DEFAULT_PATTERN = "true" + PATTERN_SEPARATOR + "false" + PATTERN_SEPARATOR + "null";

    private String getTrueString(String formatString) {
        int first = formatString.indexOf(PATTERN_SEPARATOR);
        return formatString.substring(0, first);
    }

    private String getFalseString(String formatString) {
        int first = formatString.indexOf(PATTERN_SEPARATOR);
        int last = formatString.lastIndexOf(PATTERN_SEPARATOR);
        return formatString.substring(first + 1, last);
    }

    private String getNullString(String formatString) {
        int last = formatString.lastIndexOf(PATTERN_SEPARATOR);
        return formatString.substring(last + 1);
    }

    public String getDefaultPattern() {
        return DEFAULT_PATTERN;
    }

    public String format(String pattern, Boolean b) throws FormatterException {
        String protectedPattern = getProtectedPattern(pattern);
        if (b == null) {
            return getNullString(protectedPattern);
        } else if (b) {
            return getTrueString(protectedPattern);
        } else {
            return getFalseString(protectedPattern);
        }
    }

    public Boolean parse(String pattern, String s) throws FormatterException {
        String protectedPattern = getProtectedPattern(pattern);
        if (StringUtils.isEmpty(s)) {
            return null;
        } else if (getTrueString(protectedPattern).equalsIgnoreCase(s)) {
            return Boolean.TRUE;
        } else if (getFalseString(protectedPattern).equalsIgnoreCase(s)) {
            return Boolean.FALSE;
        } else {
            throw new FormatterException("Can not parse '" + s + "' with pattern '" + protectedPattern + "'");
        }
    }
}
