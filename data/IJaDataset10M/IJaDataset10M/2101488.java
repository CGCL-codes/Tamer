package com.tll.util;

import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.DateValidator;
import org.apache.oro.text.perl.Perl5Util;

/**
 * Validation utility methods mostly RegExp definitions for common property
 * types.
 * @author jpk
 */
public final class ValidationUtil {

    public static final String US_PHONE_REGEXP = "/^\\D?(\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4})$/";

    public static final String INTNL_PHONE_REGEXP = "/^[01]?[- .]?\\(?[2-9]\\d{2}\\)?[- .]?\\d{3}[- .]?\\d{4}$/";

    public static final String US_ZIPCODE_REGEXP = "/^\\d{5}(-\\d{4})?$/";

    public static final String US_STATE_REGEXP = "/^(AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY)$/";

    public static final String PASSWORD_REGEXP = "/^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$/";

    public static final String SSN_REGEXP = "/^\\d{3}-\\d{2}-\\d{4}$/";

    public static boolean isValidUsaStateAbbr(String s) {
        return (s == null) ? false : ("us".equals(s) || "usa".equals(s));
    }

    public static boolean isValidDate(String s, Locale locale) {
        return DateValidator.getInstance().isValid(s, locale);
    }

    public static boolean isValidPassword(String s) {
        return (s == null || s.length() < 8) ? false : (new Perl5Util()).match(PASSWORD_REGEXP, StringUtils.strip(s));
    }

    private ValidationUtil() {
    }
}
