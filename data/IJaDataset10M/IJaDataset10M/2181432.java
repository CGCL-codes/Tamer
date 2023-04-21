package com.leclercb.commons.gui.utils;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatter;

public final class FormatterUtils {

    private FormatterUtils() {
    }

    public static AbstractFormatter getIntegerFormatter() {
        AbstractFormatter formatter = new AbstractFormatter() {

            @Override
            public String valueToString(Object value) throws ParseException {
                if (value == null) {
                    return "";
                } else {
                    return value + "";
                }
            }

            @Override
            public Object stringToValue(String text) throws ParseException {
                if ("".equals(text)) {
                    return null;
                }
                try {
                    return Integer.parseInt(text);
                } catch (Exception exc) {
                    throw new ParseException(exc.getMessage(), 0);
                }
            }
        };
        return formatter;
    }

    public static DefaultFormatter getRegexFormatter(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        DefaultFormatter formatter = new DefaultFormatter() {

            @Override
            public Object stringToValue(String text) throws ParseException {
                Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) return text;
                throw new ParseException("Pattern did not match", 0);
            }
        };
        return formatter;
    }
}
