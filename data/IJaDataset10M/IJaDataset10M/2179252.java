package com.duodecimo.editors;

import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author duo
 */
public class MyFormattedDoubleConverter extends Converter {

    @Override
    public Object convertForward(Object value) {
        return value;
    }

    @Override
    public Object convertReverse(Object value) {
        if (value == null) {
            return new Double("0.0");
        }
        Double d = new Double(value.toString());
        return d;
    }
}
