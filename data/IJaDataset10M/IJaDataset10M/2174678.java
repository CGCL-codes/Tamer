package org.t2framework.validator.impl;

import junit.framework.TestCase;
import org.t2framework.validator.LocaleManager;
import org.t2framework.validator.MessageKeys;
import org.t2framework.validator.ValidatorContext;
import org.t2framework.validator.type.BooleanType;

public class AlphabeticalValidatorTest extends TestCase {

    public void test1() throws Exception {
        AlphabeticalValidator validator = AlphabeticalValidator.getInstance();
        ValidatorContext context = new ValidatorContext(new ValidationErrorImpl(), new LocaleManager(), new SimpleMessageResolverImpl(), new MessageKeys());
        BooleanType validate = null;
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (char c : str.toCharArray()) {
            validate = validator.validate(context, String.valueOf(c), new Object[] { "a" });
            assertEquals(BooleanType.TRUE, validate);
        }
        validate = validator.validate(context, "-", new Object[] { "a" });
        assertEquals(BooleanType.FALSE, validate);
    }
}
