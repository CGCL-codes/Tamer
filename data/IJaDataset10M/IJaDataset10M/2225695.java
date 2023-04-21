package org.gochacha.impl;

import org.gochacha.Converter;
import org.gochacha.array.ArrayTokenifier;

public abstract class ArrayConverter extends Converter {

    private Converter converter;

    public String[] convertStringToStringArray(String stringValue) {
        ArrayTokenifier tokenifier = new ArrayTokenifier(stringValue);
        return tokenifier.getStringArray();
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
}
