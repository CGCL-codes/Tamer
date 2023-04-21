package org.t2framework.validator.impl;

import java.util.Locale;
import org.t2framework.validator.Validator;
import org.t2framework.validator.ValidatorContext;
import org.t2framework.validator.type.BooleanType;

public class AlphabeticalValidator implements Validator<String, BooleanType> {

    protected static final AlphabeticalValidator INSTANCE = new AlphabeticalValidator();

    public static AlphabeticalValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public BooleanType validate(ValidatorContext context, String s, Object[] args) {
        final String key = context.messageKeys.getAlphabeticalKey();
        final Locale locale = context.localeManager.getCurrentLocale();
        if (s == null || "".equals(s)) {
            context.validationError.addSimpleError(key, locale, args);
            return BooleanType.FALSE;
        }
        for (char c : s.toCharArray()) {
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                continue;
            } else {
                context.validationError.addSimpleError(key, locale, args);
                return BooleanType.FALSE;
            }
        }
        return BooleanType.TRUE;
    }
}
