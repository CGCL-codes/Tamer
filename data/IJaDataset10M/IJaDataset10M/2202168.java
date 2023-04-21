package org.compiere.util;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import javax.print.attribute.standard.*;

/**
 *  Language Management.
 *
 *  @author     Jorg Janke
 *  @version    $Id: Language.java,v 1.2 2006/07/30 00:52:23 jjanke Exp $
 */
public class Language implements Serializable {

    /** Base Language               */
    public static final String AD_Language_en_US = "en_US";

    /** Additional Languages         */
    private static final String AD_Language_en_GB = "en_GB";

    private static final String AD_Language_en_AU = "en_AU";

    private static final String AD_Language_ca_ES = "ca_ES";

    private static final String AD_Language_hr_HR = "hr_HR";

    private static final String AD_Language_de_DE = "de_DE";

    private static final String AD_Language_it_IT = "it_IT";

    private static final String AD_Language_es_ES = "es_ES";

    private static final String AD_Language_fr_FR = "fr_FR";

    private static final String AD_Language_bg_BG = "bg_BG";

    private static final String AD_Language_th_TH = "th_TH";

    private static final String AD_Language_pl_PL = "pl_PL";

    private static final String AD_Language_zh_TW = "zh_TW";

    private static final String AD_Language_nl_NL = "nl_NL";

    private static final String AD_Language_no_NO = "no_NO";

    private static final String AD_Language_pt_BR = "pt_BR";

    private static final String AD_Language_ru_RU = "ru_RU";

    private static final String AD_Language_sl_SI = "sl_SI";

    private static final String AD_Language_sv_SE = "sv_SE";

    private static final String AD_Language_vi_VN = "vi_VN";

    private static final String AD_Language_zh_CN = "zh_CN";

    private static final String AD_Language_da_DK = "da_DK";

    private static final String AD_Language_ms_MY = "ml_ML";

    private static final String AD_Language_fa_IR = "fa_IR";

    private static final String AD_Language_fi_FI = "fi_FI";

    private static final String AD_Language_ro_RO = "ro_RO";

    private static final String AD_Language_ja_JP = "ja_JP";

    private static final String AD_Language_in_ID = "in_ID";

    private static final String AD_Language_ar_TN = "ar_TN";

    /***
	 *  System Languages.
	 *  If you want to add a language, extend the array
	 *  - or use the addLanguage() method.
	 **/
    private static Language[] s_languages = { new Language("English", AD_Language_en_US, Locale.US, null, null, MediaSize.NA.LETTER), new Language("ﺔﻴﺑﺮﻌﻟﺍ (AR)", AD_Language_ar_TN, new Locale("ar", "TN"), new Boolean(true), "dd.MM.yyyy", MediaSize.ISO.A4), new Language("Български (BG)", AD_Language_bg_BG, new Locale("bg", "BG"), new Boolean(false), "dd/MM/yyyy", MediaSize.ISO.A4), new Language("Català", AD_Language_ca_ES, new Locale("ca", "ES"), null, "dd/MM/yyyy", MediaSize.ISO.A4), new Language("Deutsch", AD_Language_de_DE, Locale.GERMANY, null, null, MediaSize.ISO.A4), new Language("Dansk", AD_Language_da_DK, new Locale("da", "DK"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("English (AU)", AD_Language_en_AU, new Locale("en", "AU"), null, "dd/MM/yyyy", MediaSize.ISO.A4), new Language("English (UK)", AD_Language_en_GB, Locale.UK, null, null, MediaSize.ISO.A4), new Language("Español", AD_Language_es_ES, new Locale("es", "ES"), new Boolean(false), "dd/MM/yyyy", MediaSize.ISO.A4), new Language("Farsi", AD_Language_fa_IR, new Locale("fa", "IR"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("Finnish", AD_Language_fi_FI, new Locale("fi", "FI"), new Boolean(true), "dd.MM.yyyy", MediaSize.ISO.A4), new Language("Français", AD_Language_fr_FR, Locale.FRANCE, null, null, MediaSize.ISO.A4), new Language("Hrvatski", AD_Language_hr_HR, new Locale("hr", "HR"), null, "dd.MM.yyyy", MediaSize.ISO.A4), new Language("Indonesia Bahasa", AD_Language_in_ID, new Locale("in", "ID"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("Italiano", AD_Language_it_IT, Locale.ITALY, null, null, MediaSize.ISO.A4), new Language("日本語 (JP)", AD_Language_ja_JP, Locale.JAPAN, null, null, MediaSize.ISO.A4), new Language("Malay", AD_Language_ms_MY, new Locale("ms", "MY"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("Nederlands", AD_Language_nl_NL, new Locale("nl", "NL"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("Norsk", AD_Language_no_NO, new Locale("no", "NO"), new Boolean(false), "dd/MM/yyyy", MediaSize.ISO.A4), new Language("Polski", AD_Language_pl_PL, new Locale("pl", "PL"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("Portuguese (BR)", AD_Language_pt_BR, new Locale("pt", "BR"), new Boolean(false), "dd/MM/yyyy", MediaSize.ISO.A4), new Language("Română", AD_Language_ro_RO, new Locale("ro", "RO"), new Boolean(false), "dd.MM.yyyy", MediaSize.ISO.A4), new Language("Русский (Russian)", AD_Language_ru_RU, new Locale("ru", "RU"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("Slovenski", AD_Language_sl_SI, new Locale("sl", "SI"), null, "dd.MM.yyyy", MediaSize.ISO.A4), new Language("Svenska", AD_Language_sv_SE, new Locale("sv", "SE"), new Boolean(false), "yyyy-MM-dd", MediaSize.ISO.A4), new Language("ไทย (TH)", AD_Language_th_TH, new Locale("th", "TH"), new Boolean(false), "dd/MM/yyyy", MediaSize.ISO.A4), new Language("Việt Nam", AD_Language_vi_VN, new Locale("vi", "VN"), new Boolean(false), "dd-MM-yyyy", MediaSize.ISO.A4), new Language("简体中文 (CN)", AD_Language_zh_CN, Locale.CHINA, null, "yyyy-MM-dd", MediaSize.ISO.A4), new Language("繁體中文 (TW)", AD_Language_zh_TW, Locale.TAIWAN, null, null, MediaSize.ISO.A4) };

    /** Default Language            */
    private static Language s_loginLanguage = s_languages[0];

    /**	Logger			*/
    private static Logger log = Logger.getLogger(Language.class.getName());

    /**
	 *  Get Number of Languages
	 *  @return Language count
	 */
    public static int getLanguageCount() {
        return s_languages.length;
    }

    /**
	 *  Get Language
	 *  @param index index
	 *  @return Language
	 */
    public static Language getLanguage(int index) {
        if (index < 0 || index >= s_languages.length) return s_loginLanguage;
        return s_languages[index];
    }

    /**
	 *  Add Language to supported Languages
	 *  @param language new language
	 */
    public static void addLanguage(Language language) {
        if (language == null) return;
        ArrayList<Language> list = new ArrayList<Language>(Arrays.asList(s_languages));
        list.add(language);
        s_languages = new Language[list.size()];
        list.toArray(s_languages);
    }

    /**************************************************************************
	 *  Get Language.
	 * 	If language does not exist, create it on the fly assuming taht it is valid
	 *  @param langInfo either language (en) or locale (en-US) or display name
	 *  @return Name (e.g. Deutsch)
	 */
    public static Language getLanguage(String langInfo) {
        String lang = langInfo;
        if (lang == null || lang.length() == 0) lang = System.getProperty("user.language", "");
        for (int i = 0; i < s_languages.length; i++) {
            if (lang.equals(s_languages[i].getName()) || lang.equals(s_languages[i].getLanguageCode()) || lang.equals(s_languages[i].getAD_Language())) return s_languages[i];
        }
        if (lang.length() == 5) {
            String language = lang.substring(0, 2);
            String country = lang.substring(3);
            Locale locale = new Locale(language, country);
            log.info("Adding Language=" + language + ", Country=" + country + ", Locale=" + locale);
            Language ll = new Language(lang, lang, locale);
            ArrayList<Language> list = new ArrayList<Language>(Arrays.asList(s_languages));
            list.add(ll);
            s_languages = new Language[list.size()];
            list.toArray(s_languages);
            return ll;
        }
        return s_loginLanguage;
    }

    /**
	 *  Is it the base language
	 *  @param langInfo either language (en) or locale (en-US) or display name
	 *  @return true if base language
	 */
    public static boolean isBaseLanguage(String langInfo) {
        if (langInfo == null || langInfo.length() == 0 || langInfo.equals(s_languages[0].getName()) || langInfo.equals(s_languages[0].getLanguageCode()) || langInfo.equals(s_languages[0].getAD_Language())) return true;
        return false;
    }

    /**
	 *  Get Base Language
	 *  @return Base Language
	 */
    public static Language getBaseLanguage() {
        return s_languages[0];
    }

    /**
	 *  Get Base Language code. (e.g. en-US)
	 *  @return Base Language
	 */
    public static String getBaseAD_Language() {
        return s_languages[0].getAD_Language();
    }

    /**
	 *  Get Supported Locale
	 *  @param langInfo either language (en) or locale (en-US) or display name
	 *  @return Supported Locale
	 */
    public static Locale getLocale(String langInfo) {
        return getLanguage(langInfo).getLocale();
    }

    /**
	 *  Get Supported Language
	 *  @param langInfo either language (en) or locale (en-US) or display name
	 *  @return AD_Language (e.g. en-US)
	 */
    public static String getAD_Language(String langInfo) {
        return getLanguage(langInfo).getAD_Language();
    }

    /**
	 *  Get Supported Language
	 *  @param locale Locale
	 *  @return AD_Language (e.g. en-US)
	 */
    public static String getAD_Language(Locale locale) {
        if (locale != null) {
            for (int i = 0; i < s_languages.length; i++) {
                if (locale.equals(s_languages[i].getLocale())) return s_languages[i].getAD_Language();
            }
        }
        return s_loginLanguage.getAD_Language();
    }

    /**
	 *  Get Language Name
	 *  @param langInfo either language (en) or locale (en-US) or display name
	 *  @return Langauge Name (e.g. English)
	 */
    public static String getName(String langInfo) {
        return getLanguage(langInfo).getName();
    }

    /**
	 *  Returns true if Decimal Point (not comma)
	 *  @param langInfo either language (en) or locale (en-US) or display name
	 *  @return use of decimal point
	 */
    public static boolean isDecimalPoint(String langInfo) {
        return getLanguage(langInfo).isDecimalPoint();
    }

    /**
	 *  Get Display names of supported languages
	 *  @return Array of Language names
	 */
    public static String[] getNames() {
        String[] retValue = new String[s_languages.length];
        for (int i = 0; i < s_languages.length; i++) retValue[i] = s_languages[i].getName();
        return retValue;
    }

    /**************************************************************************
	 *  Get Default Login Language
	 *  @return default Language
	 */
    public static Language getLoginLanguage() {
        return s_loginLanguage;
    }

    /**
	 *  Set Default Login Language
	 *  @param language language
	 */
    public static void setLoginLanguage(Language language) {
        if (language != null) {
            s_loginLanguage = language;
            log.config(s_loginLanguage.toString());
        }
    }

    /**************************************************************************
	 *  Define Language
	 *  @param name - displayed value, e.g. English
	 *  @param AD_Language - the code of system supported langauge, e.g. en_US
	 *  (might be different than Locale - i.e. if the system does not support the language)
	 *  @param locale - the Locale, e.g. Locale.US
	 *  @param decimalPoint true if Decimal Point - if null, derived from Locale
	 *  @param javaDatePattern Java date pattern as not all locales are defined - if null, derived from Locale
	 *  @param mediaSize default media size
	 */
    public Language(String name, String AD_Language, Locale locale, Boolean decimalPoint, String javaDatePattern, MediaSize mediaSize) {
        if (name == null || AD_Language == null || locale == null) throw new IllegalArgumentException("Language - parameter is null");
        m_name = name;
        m_AD_Language = AD_Language;
        m_locale = locale;
        m_decimalPoint = decimalPoint;
        setDateFormat(javaDatePattern);
        setMediaSize(mediaSize);
    }

    /**
	 *  Define Language with A4 and default decimal point and date format
	 *  @param name - displayed value, e.g. English
	 *  @param AD_Language - the code of system supported langauge, e.g. en_US
	 *  (might be different than Locale - i.e. if the system does not support the language)
	 *  @param locale - the Locale, e.g. Locale.US
	 */
    public Language(String name, String AD_Language, Locale locale) {
        this(name, AD_Language, locale, null, null, null);
    }

    /**	Name					*/
    private String m_name;

    /**	Language (key)			*/
    private String m_AD_Language;

    /** Locale					*/
    private Locale m_locale;

    private Boolean m_decimalPoint;

    private Boolean m_leftToRight;

    private SimpleDateFormat m_dateFormat;

    private MediaSize m_mediaSize = MediaSize.ISO.A4;

    /**
	 *  Get Language Name.
	 *  e.g. English
	 *  @return name
	 */
    public String getName() {
        return m_name;
    }

    /**
	 *  Get Application Dictionary Language (system supported).
	 *  e.g. en-US
	 *  @return AD_Language
	 */
    public String getAD_Language() {
        return m_AD_Language;
    }

    /**
	 *  Set Application Dictionary Language (system supported).
	 *  @param AD_Language e.g. en-US
	 */
    public void setAD_Language(String AD_Language) {
        if (AD_Language != null) {
            m_AD_Language = AD_Language;
            log.config(toString());
        }
    }

    /**
	 *  Get Locale
	 *  @return locale
	 */
    public Locale getLocale() {
        return m_locale;
    }

    /**
	 *  Overwrite Locale
	 *  @param locale locale
	 */
    public void setLocale(Locale locale) {
        if (locale == null) return;
        m_locale = locale;
        m_decimalPoint = null;
    }

    /**
	 *  Get Language Code.
	 *  e.g. en - derived from Locale
	 *  @return language code
	 */
    public String getLanguageCode() {
        return m_locale.getLanguage();
    }

    /**
	 *  Component orientation is Left To Right
	 *  @return true if left-to-right
	 */
    public boolean isLeftToRight() {
        if (m_leftToRight == null) m_leftToRight = new Boolean(ComponentOrientation.getOrientation(m_locale).isLeftToRight());
        return m_leftToRight.booleanValue();
    }

    /**
	 *  Returns true if Decimal Point (not comma)
	 *  @return use of decimal point
	 */
    public boolean isDecimalPoint() {
        if (m_decimalPoint == null) {
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(m_locale);
            m_decimalPoint = new Boolean(dfs.getDecimalSeparator() == '.');
        }
        return m_decimalPoint.booleanValue();
    }

    /**
	 * 	Is This the Base Language
	 * 	@return true if base Language
	 */
    public boolean isBaseLanguage() {
        return this.equals(getBaseLanguage());
    }

    /**
	 *  Set Date Pattern.
	 *  The date format is not checked for correctness
	 *  @param javaDatePattern for details see java.text.SimpleDateFormat,
	 *  format must be able to be converted to database date format by
	 *  using the upper case function.
	 *  It also must have leading zero for day and month.
	 */
    public void setDateFormat(String javaDatePattern) {
        if (javaDatePattern == null) return;
        m_dateFormat = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, m_locale);
        try {
            m_dateFormat.applyPattern(javaDatePattern);
        } catch (Exception e) {
            log.severe(javaDatePattern + " - " + e);
            m_dateFormat = null;
        }
    }

    /**
	 *  Get (Short) Date Format.
	 *  The date format must parseable by org.compiere.grid.ed.MDocDate
	 *  i.e. leading zero for date and month
	 *  @return date format MM/dd/yyyy - dd.MM.yyyy
	 */
    public SimpleDateFormat getDateFormat() {
        if (m_dateFormat == null) {
            m_dateFormat = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, m_locale);
            String sFormat = m_dateFormat.toPattern();
            if (sFormat.indexOf("MM") == -1 && sFormat.indexOf("dd") == -1) {
                String nFormat = "";
                for (int i = 0; i < sFormat.length(); i++) {
                    if (sFormat.charAt(i) == 'M') nFormat += "MM"; else if (sFormat.charAt(i) == 'd') nFormat += "dd"; else nFormat += sFormat.charAt(i);
                }
                m_dateFormat.applyPattern(nFormat);
            }
            if (m_dateFormat.toPattern().length() != 8) m_dateFormat.applyPattern("yyyy-MM-dd");
            if (m_dateFormat.toPattern().indexOf("yyyy") == -1) {
                sFormat = m_dateFormat.toPattern();
                String nFormat = "";
                for (int i = 0; i < sFormat.length(); i++) {
                    if (sFormat.charAt(i) == 'y') nFormat += "yy"; else nFormat += sFormat.charAt(i);
                }
                m_dateFormat.applyPattern(nFormat);
            }
            m_dateFormat.setLenient(true);
        }
        return m_dateFormat;
    }

    /**
	 * 	Get Date Time Format.
	 * 	Used for Display only
	 *  @return Date Time format MMM d, yyyy h:mm:ss a z -or- dd.MM.yyyy HH:mm:ss z
	 *  -or- j nnn aaaa, H' ?????? 'm' ????'
	 */
    public SimpleDateFormat getDateTimeFormat() {
        SimpleDateFormat retValue = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, m_locale);
        return retValue;
    }

    /**
	 * 	Get Time Format.
	 * 	Used for Display only
	 *  @return Time format h:mm:ss z or HH:mm:ss z
	 */
    public SimpleDateFormat getTimeFormat() {
        return (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.LONG, m_locale);
    }

    /**
	 *  Get Database Date Pattern.
	 *  Derive from date pattern (make upper case)
	 *  @return date pattern
	 */
    public String getDBdatePattern() {
        return getDateFormat().toPattern().toUpperCase(m_locale);
    }

    /**
	 * 	Get default MediaSize
	 * 	@return media size
	 */
    public MediaSize getMediaSize() {
        return m_mediaSize;
    }

    /**
	 * 	Set default MediaSize
	 * 	@param size media size
	 */
    public void setMediaSize(MediaSize size) {
        if (size != null) m_mediaSize = size;
    }

    /**
	 *  String Representation
	 *  @return string representation
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("Language=[");
        sb.append(m_name).append(",Locale=").append(m_locale.toString()).append(",AD_Language=").append(m_AD_Language).append(",DatePattern=").append(getDBdatePattern()).append(",DecimalPoint=").append(isDecimalPoint()).append("]");
        return sb.toString();
    }

    /**
	 * 	Hash Code
	 * 	@return hashcode
	 */
    public int hashCode() {
        return m_AD_Language.hashCode();
    }

    /**
	 * 	Equals.
	 *  Two languages are equal, if they have the same AD_Language
	 * 	@param obj compare
	 * 	@return true if AD_Language is the same
	 */
    public boolean equals(Object obj) {
        if (obj instanceof Language) {
            Language cmp = (Language) obj;
            if (cmp.getAD_Language().equals(m_AD_Language)) return true;
        }
        return false;
    }

    /**************************************************************************
	 * 	Test
	 * 	@param args ignored
	 */
    public static void main(String[] args) {
        System.out.println(Locale.TRADITIONAL_CHINESE);
        System.out.println(Locale.TAIWAN);
        System.out.println(Locale.SIMPLIFIED_CHINESE);
        System.out.println(Locale.CHINESE);
        System.out.println(Locale.PRC);
    }
}
