package org.freebxml.omar.server.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import org.freebxml.omar.common.AbstractResourceBundle;
import org.freebxml.omar.common.CommonProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to load localized strings
 *
 * @author  Paul Sterk / Sun Microsystems
 */
public class ServerResourceBundle extends AbstractResourceBundle {

    public static final String BASE_NAME = "org.freebxml.omar.server.ResourceBundle";

    private static ServerResourceBundle instance = null;

    private static Locale locale = null;

    private ResourceBundle bundle = null;

    private static final Log log = LogFactory.getLog(ServerResourceBundle.class);

    protected ServerResourceBundle() {
        bundle = ResourceBundle.getBundle(BASE_NAME);
    }

    protected ServerResourceBundle(Locale locale) {
        bundle = ResourceBundle.getBundle(BASE_NAME, locale);
    }

    public static synchronized ServerResourceBundle getInstance() {
        if (instance == null) {
            instance = new ServerResourceBundle();
            String localeString = CommonProperties.getInstance().getProperty("omar.common.locale");
            locale = parseLocale(localeString);
        }
        return instance;
    }

    public static synchronized ServerResourceBundle getInstance(Locale locale) {
        if (instance == null) {
            instance = new ServerResourceBundle(locale);
        } else {
            if (ServerResourceBundle.locale != locale) {
                instance = new ServerResourceBundle(locale);
            }
        }
        return instance;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(BASE_NAME, locale);
    }
}
