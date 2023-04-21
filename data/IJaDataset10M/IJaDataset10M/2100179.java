package de.bea.domingo.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Country names and their locale strings.
 *
 * TODO test this class
 * TODO use this class
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoLocaleCode {

    /** Map from country names to locale strings. */
    private static final Map LOCALE_CODES_MAP = new HashMap();

    private final String fCountry;

    private final String fLocaleString;

    /**
     * Constructor.
     *
     * @param country the country name
     * @param localeString the locale string
     */
    public DominoLocaleCode(final String country, final String localeString) {
        fCountry = country;
        fLocaleString = localeString;
    }

    /**
     * For a given country name returns the corresponsing locale string.
     *
     * @param country country name
     * @return locale string
     */
    public static DominoLocaleCode get(final String country) {
        return (DominoLocaleCode) LOCALE_CODES_MAP.get(country);
    }

    /**
     * Returns the first found locale code for a given search string.
     *
     * <p>The search string is simply a partial string of a time zone name.</p>
     *
     * @param searchString search string of a locale code
     * @return the locale code or <code>null</code> if not found
     */
    public static DominoLocaleCode searchLocaleCode(final String searchString) {
        if (searchString == null) {
            return null;
        }
        Iterator iterator = LOCALE_CODES_MAP.values().iterator();
        while (iterator.hasNext()) {
            DominoLocaleCode localeCode = (DominoLocaleCode) iterator.next();
            if (localeCode.getCountry().indexOf(searchString) >= 0) {
                return localeCode;
            }
        }
        return null;
    }

    /**
     * @return Returns the country.
     */
    public String getCountry() {
        return fCountry;
    }

    /**
     * @return Returns the localeString.
     */
    public String getLocaleString() {
        return fLocaleString;
    }

    static {
        addLocaleCode(new DominoLocaleCode("Afrikaans", "af"));
        addLocaleCode(new DominoLocaleCode("Afrikaans(South Africa)", "af-ZA"));
        addLocaleCode(new DominoLocaleCode("Albanian", "sq"));
        addLocaleCode(new DominoLocaleCode("Arabic", "ar"));
        addLocaleCode(new DominoLocaleCode("Arabic(Algeria)", "ar-DZ"));
        addLocaleCode(new DominoLocaleCode("Arabic(Bahrain)", "ar-BH"));
        addLocaleCode(new DominoLocaleCode("Arabic(Egypt)", "ar-EG"));
        addLocaleCode(new DominoLocaleCode("Arabic(Jordan)", "ar-JO"));
        addLocaleCode(new DominoLocaleCode("Arabic(Kuwait)", "ar-KW"));
        addLocaleCode(new DominoLocaleCode("Arabic(Lebanon)", "ar-LB"));
        addLocaleCode(new DominoLocaleCode("Arabic(Morocco)", "ar-MA"));
        addLocaleCode(new DominoLocaleCode("Arabic(Oman)", "ar-OM"));
        addLocaleCode(new DominoLocaleCode("Arabic(Qatar)", "ar-QA"));
        addLocaleCode(new DominoLocaleCode("Arabic(Saudi Arabia)", "ar-SA"));
        addLocaleCode(new DominoLocaleCode("Arabic(Tunisia)", "ar-TN"));
        addLocaleCode(new DominoLocaleCode("Arabic(United Arab Emirates)", "ar-AE"));
        addLocaleCode(new DominoLocaleCode("Arabic(Yemen)", "ar-YE"));
        addLocaleCode(new DominoLocaleCode("Bulgarian", "bg"));
        addLocaleCode(new DominoLocaleCode("Byelorussian", "be"));
        addLocaleCode(new DominoLocaleCode("Catalan", "ca"));
        addLocaleCode(new DominoLocaleCode("Chinese", "zh"));
        addLocaleCode(new DominoLocaleCode("Chinese(China)", "zh-CN"));
        addLocaleCode(new DominoLocaleCode("Chinese(Hong Kong)", "zh-HK"));
        addLocaleCode(new DominoLocaleCode("Chinese(Macau)", "zh-MO"));
        addLocaleCode(new DominoLocaleCode("Chinese(Taiwan)", "zh-TW"));
        addLocaleCode(new DominoLocaleCode("Chinese(Singapore)", "zh-SG"));
        addLocaleCode(new DominoLocaleCode("Croatian", "hr"));
        addLocaleCode(new DominoLocaleCode("Czech", "cs"));
        addLocaleCode(new DominoLocaleCode("Danish", "da"));
        addLocaleCode(new DominoLocaleCode("Dutch", "nl"));
        addLocaleCode(new DominoLocaleCode("Dutch(Belgium)", "nl-BE"));
        addLocaleCode(new DominoLocaleCode("Dutch(Netherlands)", "nl-NL"));
        addLocaleCode(new DominoLocaleCode("English", "en"));
        addLocaleCode(new DominoLocaleCode("English(Australia)", "en-AU"));
        addLocaleCode(new DominoLocaleCode("English(Canada)", "en-CA"));
        addLocaleCode(new DominoLocaleCode("English(Hong Kong)", "en-HK"));
        addLocaleCode(new DominoLocaleCode("English(Ireland)", "en-IE"));
        addLocaleCode(new DominoLocaleCode("English(India)", "en-IN"));
        addLocaleCode(new DominoLocaleCode("English(Jamaica)", "en-JM"));
        addLocaleCode(new DominoLocaleCode("English(New Zealand)", "en-NZ"));
        addLocaleCode(new DominoLocaleCode("English(Philippines)", "en-PH"));
        addLocaleCode(new DominoLocaleCode("English(Singapore)", "en-SG"));
        addLocaleCode(new DominoLocaleCode("English(South Africa)", "en-ZA"));
        addLocaleCode(new DominoLocaleCode("English(United Kingdom)", "en-GB"));
        addLocaleCode(new DominoLocaleCode("English(United States)", "en-US"));
        addLocaleCode(new DominoLocaleCode("Estonian", "et"));
        addLocaleCode(new DominoLocaleCode("Finnish", "fi"));
        addLocaleCode(new DominoLocaleCode("French", "fr"));
        addLocaleCode(new DominoLocaleCode("French(Belgium)", "fr-BE"));
        addLocaleCode(new DominoLocaleCode("French(Canada)", "fr-CA"));
        addLocaleCode(new DominoLocaleCode("French(France)", "fr-FR"));
        addLocaleCode(new DominoLocaleCode("French(Luxembourg)", "fr-LU"));
        addLocaleCode(new DominoLocaleCode("French(Switzerland)", "fr-CH"));
        addLocaleCode(new DominoLocaleCode("German", "de"));
        addLocaleCode(new DominoLocaleCode("German(Austria)", "de-AT"));
        addLocaleCode(new DominoLocaleCode("German(Germany)", "de-DE"));
        addLocaleCode(new DominoLocaleCode("German(Liechtenstein)", "de-LI"));
        addLocaleCode(new DominoLocaleCode("German(Luxembourg)", "de-LU"));
        addLocaleCode(new DominoLocaleCode("German(Switzerland)", "de-CH"));
        addLocaleCode(new DominoLocaleCode("Greek", "el"));
        addLocaleCode(new DominoLocaleCode("Gujarati", "gu"));
        addLocaleCode(new DominoLocaleCode("Hebrew", "he"));
        addLocaleCode(new DominoLocaleCode("Hindi", "hi"));
        addLocaleCode(new DominoLocaleCode("Hungarian", "hu"));
        addLocaleCode(new DominoLocaleCode("Icelandic", "is"));
        addLocaleCode(new DominoLocaleCode("Indonesian", "id"));
        addLocaleCode(new DominoLocaleCode("Italian", "it"));
        addLocaleCode(new DominoLocaleCode("Italian(Italy)", "it-IT"));
        addLocaleCode(new DominoLocaleCode("Italian(Switzerland)", "it-CH"));
        addLocaleCode(new DominoLocaleCode("Japanese", "ja"));
        addLocaleCode(new DominoLocaleCode("Konkani", "x-KOK"));
        addLocaleCode(new DominoLocaleCode("Korean", "ko"));
        addLocaleCode(new DominoLocaleCode("Latvian", "lv"));
        addLocaleCode(new DominoLocaleCode("Lithuanian", "lt"));
        addLocaleCode(new DominoLocaleCode("Macedonian(FYROM)", "mk"));
        addLocaleCode(new DominoLocaleCode("Malay", "ms"));
        addLocaleCode(new DominoLocaleCode("Marathi", "mr"));
        addLocaleCode(new DominoLocaleCode("Norwegian", "no"));
        addLocaleCode(new DominoLocaleCode("Norwegian(Norway)", "no-NO"));
        addLocaleCode(new DominoLocaleCode("Norwegian(Nynorsk)", "ny-NO"));
        addLocaleCode(new DominoLocaleCode("Polish", "pl"));
        addLocaleCode(new DominoLocaleCode("Portuguese", "pt"));
        addLocaleCode(new DominoLocaleCode("Portuguese(Brazil)", "pt-BR"));
        addLocaleCode(new DominoLocaleCode("Portuguese(Portugal)", "pt-PT"));
        addLocaleCode(new DominoLocaleCode("Romanian", "ro"));
        addLocaleCode(new DominoLocaleCode("Romanian(Moldavia)", "ro-MD"));
        addLocaleCode(new DominoLocaleCode("Romanian(Romania)", "ro-RO"));
        addLocaleCode(new DominoLocaleCode("Russian", "ru"));
        addLocaleCode(new DominoLocaleCode("Serbian", "sr"));
        addLocaleCode(new DominoLocaleCode("Slovak", "sk"));
        addLocaleCode(new DominoLocaleCode("Slovenian", "sl"));
        addLocaleCode(new DominoLocaleCode("Spanish", "es"));
        addLocaleCode(new DominoLocaleCode("Spanish(Argentina)", "es-AR"));
        addLocaleCode(new DominoLocaleCode("Spanish(Bolivia)", "es-BO"));
        addLocaleCode(new DominoLocaleCode("Spanish(Chile)", "es-CL"));
        addLocaleCode(new DominoLocaleCode("Spanish(Colombia)", "es-CO"));
        addLocaleCode(new DominoLocaleCode("Spanish(Costa Rica)", "es-CR"));
        addLocaleCode(new DominoLocaleCode("Spanish(Dominican Republic)", "es-DO"));
        addLocaleCode(new DominoLocaleCode("Spanish(Ecuador)", "es-EC"));
        addLocaleCode(new DominoLocaleCode("Spanish(El Salvador)", "es-SV"));
        addLocaleCode(new DominoLocaleCode("Spanish(Guatemala)", "es-GT"));
        addLocaleCode(new DominoLocaleCode("Spanish(Honduras)", "es-HN"));
        addLocaleCode(new DominoLocaleCode("Spanish(Mexico)", "es-MX"));
        addLocaleCode(new DominoLocaleCode("Spanish(Nicaragua)", "es-NI"));
        addLocaleCode(new DominoLocaleCode("Spanish(Panama)", "es-PA"));
        addLocaleCode(new DominoLocaleCode("Spanish(Paraguay)", "es-PY"));
        addLocaleCode(new DominoLocaleCode("Spanish(Peru)", "es-PE"));
        addLocaleCode(new DominoLocaleCode("Spanish(Puerto Rico)", "es-PR"));
        addLocaleCode(new DominoLocaleCode("Spanish(Spain)", "es-ES"));
        addLocaleCode(new DominoLocaleCode("Spanish(United States)", "es-US"));
        addLocaleCode(new DominoLocaleCode("Spanish(Uruguay)", "es-UY"));
        addLocaleCode(new DominoLocaleCode("Spanish(Venezuela)", "es-VE"));
        addLocaleCode(new DominoLocaleCode("Swedish", "sv"));
        addLocaleCode(new DominoLocaleCode("Tamil", "ta"));
        addLocaleCode(new DominoLocaleCode("Telugu", "te"));
        addLocaleCode(new DominoLocaleCode("Thai", "th"));
        addLocaleCode(new DominoLocaleCode("Turkish", "tr"));
        addLocaleCode(new DominoLocaleCode("Ukrainian", "uk"));
        addLocaleCode(new DominoLocaleCode("Vietnamese", "vi"));
        addLocaleCode(new DominoLocaleCode("Arabic(Syria)", "ar-SY"));
        addLocaleCode(new DominoLocaleCode("Kazakh", "kk"));
        addLocaleCode(new DominoLocaleCode("Norwegian Bokmal", "nb"));
        addLocaleCode(new DominoLocaleCode("Norwegian Nynorsk", "nn"));
    }

    private static void addLocaleCode(final DominoLocaleCode code) {
        LOCALE_CODES_MAP.put(code.getCountry(), code.getLocaleString());
    }
}
