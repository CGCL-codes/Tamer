package com.siberhus.commons.converter;

import java.io.File;
import java.util.Locale;

public class FileTypeConverter implements ITypeConverter<File> {

    public void setLocale(Locale locale) {
    }

    public File convert(String input) throws ConvertException {
        return convert(input, File.class);
    }

    @SuppressWarnings("unchecked")
    public File convert(String input, Class<? extends File> targetType) throws ConvertException {
        try {
            return new File(input);
        } catch (NullPointerException npe) {
            throw new ConvertException("nullFilePath", npe);
        }
    }
}
