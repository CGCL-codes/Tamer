package com.siberhus.commons.converter;

import java.util.Locale;
import com.siberhus.commons.validator.regex.EmailValidator;

public class EmailTypeConverter implements ITypeConverter<Object> {

    /** Accepts the Locale provide, but does nothing with it since emails are Locale-less. */
    public void setLocale(Locale locale) {
    }

    public String convert(String input) throws ConvertException {
        return convert(input, String.class);
    }

    public String convert(String input, Class<? extends Object> targetType) throws ConvertException {
        if (!isValidEmail(input)) {
            throw new ConvertException("invalidEmail");
        }
        return input;
    }

    public boolean isValidEmail(String email) {
        return EmailValidator.isValidEmail(email);
    }
}
