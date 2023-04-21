package com.sun.org.apache.xml.internal.security.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Internationalization (I18N) pack.
 *
 *
 *
 * @author Christian Geuer-Pollmann
 */
public class I18n {

    /** Field NOT_INITIALIZED_MSG */
    public static final String NOT_INITIALIZED_MSG = "You must initialize the xml-security library correctly before you use it. " + "Call the static method \"com.sun.org.apache.xml.internal.security.Init.init();\" to do that " + "before you use any functionality from that library.";

    /** Field defaultLanguageCode */
    private static String defaultLanguageCode;

    /** Field defaultCountryCode */
    private static String defaultCountryCode;

    /** Field resourceBundle */
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.exceptionMessagesResourceBundleBase, Locale.US);

    /** Field alreadyInitialized */
    private static boolean alreadyInitialized = false;

    /** Field _languageCode */
    private static String _languageCode = null;

    /** Field _countryCode */
    private static String _countryCode = null;

    /**
    * Constructor I18n
    *
    */
    private I18n() {
    }

    /**
    * Method translate
    *
    * translates a message ID into an internationalized String, see alse
    * <CODE>XMLSecurityException.getExceptionMEssage()</CODE>. The strings are
    * stored in the <CODE>ResourceBundle</CODE>, which is identified in
    * <CODE>exceptionMessagesResourceBundleBase</CODE>
    *
    * @param message
    * @param args is an <CODE>Object[]</CODE> array of strings which are inserted into the String which is retrieved from the <CODE>ResouceBundle</CODE>
    * @return message translated
    */
    public static String translate(String message, Object[] args) {
        return getExceptionMessage(message, args);
    }

    /**
    * Method translate
    *
    * translates a message ID into an internationalized String, see alse
    * <CODE>XMLSecurityException.getExceptionMEssage()</CODE>
    *
    * @param message
    * @return message translated
    */
    public static String translate(String message) {
        return getExceptionMessage(message);
    }

    /**
    * Method getExceptionMessage
    *
    * @param msgID
    * @return message translated
    *
    */
    public static String getExceptionMessage(String msgID) {
        try {
            String s = resourceBundle.getString(msgID);
            return s;
        } catch (Throwable t) {
            if (com.sun.org.apache.xml.internal.security.Init.isInitialized()) {
                return "No message with ID \"" + msgID + "\" found in resource bundle \"" + Constants.exceptionMessagesResourceBundleBase + "\"";
            }
            return I18n.NOT_INITIALIZED_MSG;
        }
    }

    /**
    * Method getExceptionMessage
    *
    * @param msgID
    * @param originalException
    * @return message translated
    */
    public static String getExceptionMessage(String msgID, Exception originalException) {
        try {
            Object exArgs[] = { originalException.getMessage() };
            String s = MessageFormat.format(resourceBundle.getString(msgID), exArgs);
            return s;
        } catch (Throwable t) {
            if (com.sun.org.apache.xml.internal.security.Init.isInitialized()) {
                return "No message with ID \"" + msgID + "\" found in resource bundle \"" + Constants.exceptionMessagesResourceBundleBase + "\". Original Exception was a " + originalException.getClass().getName() + " and message " + originalException.getMessage();
            }
            return I18n.NOT_INITIALIZED_MSG;
        }
    }

    /**
    * Method getExceptionMessage
    *
    * @param msgID
    * @param exArgs
    * @return message translated
    */
    public static String getExceptionMessage(String msgID, Object exArgs[]) {
        try {
            String s = MessageFormat.format(resourceBundle.getString(msgID), exArgs);
            return s;
        } catch (Throwable t) {
            if (com.sun.org.apache.xml.internal.security.Init.isInitialized()) {
                return "No message with ID \"" + msgID + "\" found in resource bundle \"" + Constants.exceptionMessagesResourceBundleBase + "\"";
            }
            return I18n.NOT_INITIALIZED_MSG;
        }
    }
}
