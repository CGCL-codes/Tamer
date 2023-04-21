package com.siberhus.commons.converter;

import java.util.Locale;

public class ClassTypeConverter implements ITypeConverter<Class> {

    public void setLocale(Locale locale) {
    }

    public Class convert(String input) throws ConvertException {
        return convert(input, Class.class);
    }

    @SuppressWarnings("unchecked")
    public Class convert(String input, Class targetType) throws ConvertException {
        try {
            return targetType.forName(input);
        } catch (ClassNotFoundException cnfe) {
            throw new ConvertException("notAnClassTypeValue", cnfe);
        }
    }
}
