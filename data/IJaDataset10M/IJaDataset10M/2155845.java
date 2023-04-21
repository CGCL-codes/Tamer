package org.gwtoolbox.bean.client.validation.validator;

import org.gwtoolbox.bean.client.validation.constraint.HttpUrl;
import org.gwtoolbox.bean.client.validation.validator.jsr.AbstractConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Uri Boness
 */
public class HttpUrlConstraintValidator extends AbstractConstraintValidator<HttpUrl, String> {

    private static final String URL_PATTERN = "(http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?";

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches(URL_PATTERN);
    }
}
