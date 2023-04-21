package org.jaxson.validation;

import java.util.regex.Pattern;
import org.jaxson.util.reflection.ReflectionUtil;

/**
 * @author Joe Maisel
 *
 */
@SuppressWarnings("serial")
public class EmailValidator extends AbstractValidator {

    private static final Pattern p = Pattern.compile("^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    /**
	 * @see org.jaxson.validation.FieldValidator#isValid(java.lang.Object)
	 */
    @Override
    public boolean isValid(Object obj) {
        String value = (String) ReflectionUtil.getProperty(obj, property);
        return value != null && p.matcher(value.toString()).matches();
    }
}
