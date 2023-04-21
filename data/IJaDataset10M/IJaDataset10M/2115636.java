package org.apache.xerces.xinclude;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import org.apache.xerces.util.MessageFormatter;

/**
 * XIncludeMessageFormatter provides error messages for the XInclude 1.0 Candidate Recommendation
 * 
 * @author Peter McCracken, IBM
 * 
 * @version $Id: XIncludeMessageFormatter.java 447243 2006-09-18 05:15:27Z mrglavas $
 */
public class XIncludeMessageFormatter implements MessageFormatter {

    public static final String XINCLUDE_DOMAIN = "http://www.w3.org/TR/xinclude";

    private Locale fLocale = null;

    private ResourceBundle fResourceBundle = null;

    /**
     * Formats a message with the specified arguments using the given
     * locale information.
     * 
     * @param locale    The locale of the message.
     * @param key       The message key.
     * @param arguments The message replacement text arguments. The order
     *                  of the arguments must match that of the placeholders
     *                  in the actual message.
     * 
     * @return Returns the formatted message.
     *
     * @throws MissingResourceException Thrown if the message with the
     *                                  specified key cannot be found.
     */
    public String formatMessage(Locale locale, String key, Object[] arguments) throws MissingResourceException {
        if (fResourceBundle == null || locale != fLocale) {
            if (locale != null) {
                fResourceBundle = PropertyResourceBundle.getBundle("org.apache.xerces.impl.msg.XIncludeMessages", locale);
                fLocale = locale;
            }
            if (fResourceBundle == null) fResourceBundle = PropertyResourceBundle.getBundle("org.apache.xerces.impl.msg.XIncludeMessages");
        }
        String msg = fResourceBundle.getString(key);
        if (arguments != null) {
            try {
                msg = java.text.MessageFormat.format(msg, arguments);
            } catch (Exception e) {
                msg = fResourceBundle.getString("FormatFailed");
                msg += " " + fResourceBundle.getString(key);
            }
        }
        if (msg == null) {
            msg = fResourceBundle.getString("BadMessageKey");
            throw new MissingResourceException(msg, "org.apache.xerces.impl.msg.XIncludeMessages", key);
        }
        return msg;
    }
}
