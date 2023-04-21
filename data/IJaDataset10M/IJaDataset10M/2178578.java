package com.kn.spring.customeditor;

import java.beans.PropertyEditorSupport;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.lang.StringUtils;

public class CustomByteEditor extends PropertyEditorSupport {

    /** Converter from String to Byte */
    private static Converter _converter = new ByteConverter();

    /**
     * @see java.beans.PropertyEditor#setAsText(java.lang.String)
     */
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isBlank(text)) {
            setValue(null);
        } else {
            try {
                setValue(_converter.convert(Byte.class, text));
            } catch (ConversionException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }
}
