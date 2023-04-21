package org.gbif.ipt.validation;

import java.util.Date;
import java.util.regex.Pattern;
import com.opensymphony.xwork2.validator.validators.EmailValidator;

public abstract class BaseValidator {

    protected static Pattern emailPattern = Pattern.compile(EmailValidator.emailAddressPattern);

    protected boolean exists(String x) {
        return exists(x, 2);
    }

    protected boolean exists(Integer i) {
        return i != null;
    }

    protected boolean exists(Date d) {
        return d != null;
    }

    protected boolean exists(String x, int minLength) {
        return x != null && x.trim().length() >= minLength;
    }

    protected boolean isValidEmail(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }
}
