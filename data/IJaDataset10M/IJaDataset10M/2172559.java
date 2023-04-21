package net.sf.bitext2tmx.ui;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Localization: localization functionality
 * @author Raymond Martin
 * @version 0.91
 */
public class Localization {

    private static String m_strPropertyPrefix = "l10n-rb";

    private static ResourceBundle m_rbDefault = getResourceBundle(Locale.getDefault());

    public static ResourceBundle getResourceBundle(Locale loc) {
        return (ResourceBundle.getBundle(m_strPropertyPrefix, loc));
    }

    public static void refreshLocalization() {
        m_rbDefault = getResourceBundle(Locale.getDefault());
    }

    public static void setLocale(Locale loc) {
        Locale.setDefault(loc);
    }

    public static void printLocale() {
        System.out.println(m_rbDefault.getLocale().getDisplayName());
    }

    /**
   *  Language ID, the current one - for later use
   */
    protected static String m_strLanguageId = null;

    /**
   * Private constructor.
   * @noinspection UNUSED_SYMBOL
   */
    private Localization() {
    }

    /**
   * getCurrentLanguageId: Current language ID accessor
   * @param  void
   * @return String
   */
    public static String getCurrentLanguageId() {
        return (m_strLanguageId);
    }

    /** 
  * setCurrentLanguageId: Current language ID mutator 
  *
  * @param  String
  * @return void 
  */
    public static void setCurrentLanguageId(String strLanguageId) throws LocalizationException {
        if (strLanguageId != null) {
            m_strLanguageId = strLanguageId;
        } else throw new LocalizationException("Language ID cannot be null");
    }

    /** 
  * get: Return localized string for given key 
  *
  * @param  String
  * @return String 
  */
    public static String get(String strKey) {
        return (m_rbDefault.getString(strKey));
    }
}
