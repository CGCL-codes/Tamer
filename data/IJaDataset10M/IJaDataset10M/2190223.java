package org.gruposp2p.dnie.client.util;

public class ValidationUtils {

    /**
   * RFC 2822 compliant
   * http://www.regular-expressions.info/email.html
   */
    private static final String EMAIL_VALIDATION_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    public static boolean validateEmail(String email) {
        if (email.matches(EMAIL_VALIDATION_REGEX)) {
            return true;
        } else {
            return false;
        }
    }
}
