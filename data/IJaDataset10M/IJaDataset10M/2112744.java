package com.elibera.ccs.res;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import com.elibera.ccs.app.ApplicationParams;
import com.elibera.ccs.util.HelperStd;

/**
 * @author meisi
 *
 */
public class Msg {

    public static String MSG_BUNDLE = "com.elibera.ccs.res.msg";

    public static String HELP_MSG_BUNDLE = "com.elibera.ccs.res.help_msg";

    public static String CONF_BUNDLE = "com.elibera.ccs.res.conf";

    private static HashMap BUNDLES = new HashMap();

    public static Locale stdLoc = Locale.getDefault();

    /**
	     * gibt eine Übersetzte Msg zurĂźck
	     * @param loc
	     * @param key
	     * @return
	     */
    public static String getMsg(Locale loc, String key) {
        return getMsgFromBundle(loc, MSG_BUNDLE, key, null, true);
    }

    public static String getHelpMsg(Locale loc, String key) {
        return getMsgFromBundle(loc, HELP_MSG_BUNDLE, key, null, true);
    }

    public static String getMsg(String key) {
        return getMsgFromBundle(stdLoc, MSG_BUNDLE, key, null, true);
    }

    public static String getString(String key) {
        return getMsgFromBundle(stdLoc, MSG_BUNDLE, key, null, true);
    }

    public static String getHelpMsg(String key) {
        return getMsgFromBundle(stdLoc, HELP_MSG_BUNDLE, key, null, true);
    }

    public static String getMsg(String key, String[] replace) {
        return getMsgFromBundle(stdLoc, MSG_BUNDLE, key, replace, true);
    }

    public static String getMsg(String key, String replace) {
        String[] r = { replace };
        return getMsgFromBundle(stdLoc, MSG_BUNDLE, key, r, true);
    }

    public static String getMsg(String key, String replace1, String replace2) {
        String[] r = { replace1, replace2 };
        return getMsgFromBundle(stdLoc, MSG_BUNDLE, key, r, true);
    }

    /**
	     * gibt eine Einstellung / Setting zurĂźck
	     * @param key
	     * @return
	     */
    public static String getConfSetting(String key) {
        return getConfSetting(key, true);
    }

    public static int getConfSettingIntValue(String key) {
        return HelperStd.parseInt(getConfSetting(key), -1);
    }

    public static String getConfSetting(String key, String defaultreturn) {
        String ret = getConfSetting(key, false);
        if (ret == null || ret.compareTo(key) == 0) return defaultreturn;
        return ret;
    }

    /**
	     * gibt eine Einstellung / Setting zurĂźck
	     * @param key
	     * @return
	     */
    public static String getConfSetting(String key, boolean displayError) {
        if (ApplicationParams.params != null) {
            String ret = ApplicationParams.params.getParameter(key, null);
            if (ret != null) return ret;
        }
        return getMsgFromBundle(null, CONF_BUNDLE, key, null, displayError);
    }

    /**
	     * gibt aus dem Bundle den Msg-Text zurĂźck und ersetzt gegebenfalls die params
	     * @param locale --> Locale, kann auch null sein
	     * @param bundleName --> der Bundle name
	     * @param key --> key nachdem gesucht werden soll
	     * @param params --> {0}, {1} --> wird durch das zeugs ersetzt
	     * @return
	     */
    public static String getMsgFromBundle(Locale locale, String bundleName, String key, String[] params, boolean displayError) {
        String msg = key;
        try {
            ResourceBundle bundle = (ResourceBundle) BUNDLES.get(bundleName + locale);
            if (bundle == null) {
                if (locale == null) bundle = ResourceBundle.getBundle(bundleName); else bundle = ResourceBundle.getBundle(bundleName, locale);
                BUNDLES.put(bundleName + locale, bundle);
            }
            msg = bundle.getString(key);
            if (params == null) return msg;
            MessageFormat format = new MessageFormat(msg, locale);
            return format.format(params);
        } catch (Exception e) {
            if (displayError) e.printStackTrace();
        }
        return msg;
    }
}
