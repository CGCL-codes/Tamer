package org.owasp.esapi.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import org.owasp.esapi.ESAPI;

/**
 * @author Pawan Singh (pawan.singh@owasp.org)
 *
 */
public class DefaultMessageUtil {

    private final String DEFAULT_LOCALE_LANG = "en";

    private final String DEFAULT_LOCALE_LOC = "US";

    private ResourceBundle messages = null;

    public void initialize() {
        try {
            messages = ResourceBundle.getBundle("ESAPI", ESAPI.authenticator().getCurrentUser().getLocale());
        } catch (Exception e) {
            messages = ResourceBundle.getBundle("ESAPI", new Locale(DEFAULT_LOCALE_LANG, DEFAULT_LOCALE_LOC));
        }
    }

    public String getMessage(String msgKey, Object[] arguments) {
        initialize();
        return MessageFormat.format(messages.getString(msgKey), arguments);
    }
}
