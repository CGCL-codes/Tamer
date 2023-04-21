package org.wikipediacleaner.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.wikipediacleaner.api.constants.EnumLanguage;

/**
 * Internationalisation management.
 */
public class GT {

    private static GT getTextWrapper;

    private final ResourceBundle defaultResource;

    private EnumLanguage language;

    /**
   * Constructor.
   */
    private GT() {
        defaultResource = new Messages();
        Locale locale = Locale.getDefault();
        if ((locale != null) && (locale.getLanguage() != null)) {
            language = EnumLanguage.getLanguage(locale.getLanguage());
        } else {
            language = EnumLanguage.EN;
        }
    }

    /**
   * @param msg Original text.
   * @return Translated text.
   */
    public static String _(String msg) {
        return getTextWrapper().getString(msg);
    }

    /**
   * @param msg Original text (with variables placeholders).
   * @param variable Variable value.
   * @return Translated text.
   */
    public static String _(String msg, String variable) {
        return getTextWrapper().getString(msg, new Object[] { variable });
    }

    /**
   * @param msg Original text (with variables placeholders).
   * @param objects Variables values.
   * @return Translated text.
   */
    public static String _(String msg, Object[] objects) {
        return getTextWrapper().getString(msg, objects);
    }

    /**
   * @return Wrapper.
   */
    private static GT getTextWrapper() {
        if (getTextWrapper == null) {
            getTextWrapper = new GT();
        }
        return getTextWrapper;
    }

    /**
   * @param msg Original text.
   * @return Translated text.
   */
    private String getString(String msg) {
        try {
            if ((language != null) && (language.getResourceBundle() != null)) {
                String txt = language.getResourceBundle().getString(msg);
                if (txt != null) {
                    return txt;
                }
            }
        } catch (NullPointerException e) {
        } catch (MissingResourceException e) {
        } catch (ClassCastException e) {
        }
        return defaultResource.getString(msg);
    }

    /**
   * @param msg Original text (with variables placeholders).
   * @param objects Variables values.
   * @return Translated text.
   */
    private String getString(String msg, Object[] objects) {
        if (msg == null) {
            return null;
        }
        String txt = getString(msg);
        return MessageFormat.format(txt, objects);
    }

    /**
   * Change the current language.
   * 
   * @param language Current language.
   */
    public static void setCurrentLanguage(EnumLanguage language) {
        getTextWrapper().language = language;
    }
}
