package br.protocount.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Jivago
 */
public class Internationalization {

    private static String contry = "";

    private static String lang = "";

    private static Locale locale;

    private static ResourceBundle resourceBundle;

    public static void init() {
        locale = new Locale(lang, contry);
        resourceBundle = ResourceBundle.getBundle("br.protocount.resources.language", locale);
    }

    public static String getMessage(String key) {
        if (resourceBundle == null) init();
        return resourceBundle.getString(key);
    }

    public static String getContry() {
        return contry;
    }

    public static void setContry(String contry) {
        Internationalization.contry = contry;
    }

    public static String getLang() {
        return lang;
    }

    public static void setLang(String lang) {
        Internationalization.lang = lang;
    }
}
