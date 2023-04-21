package eu.cherrytree.paj.base;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.Serializable;
import javax.media.opengl.GL2;
import eu.cherrytree.paj.graphics.Graphics;

public class SystemProperties implements Serializable {

    private static final long serialVersionUID = -1444550330596851634L;

    public static class DisplayInfo {

        public int bitDepth;

        public int width;

        public int height;

        public int refreshRate;

        public DisplayInfo(int bitDepth, int width, int height, int refreshRate) {
            this.bitDepth = bitDepth;
            this.width = width;
            this.height = height;
            this.refreshRate = refreshRate;
        }
    }

    /**
	 * Type of the operating system.
	 */
    public enum OSName {

        LINUX, WINDOWS, MAC_OS, BSD, SOLARIS, UNKOWN
    }

    /**
	 * Hardware architecture.
	 */
    public enum Architecture {

        BIT_32, BIT_64, UNKOWN
    }

    /**
	 * User country.
	 */
    public enum Country {

        AFGHANISTAN("AFGHANISTAN", "AF"), ALAND_ISLANDS("ÅLAND ISLANDS", "AX"), ALBANIA("ALBANIA", "AL"), ALGERIA("ALGERIA", "DZ"), AMERICAN_SAMOA("AMERICAN SAMOA", "AS"), ANDORRA("ANDORRA", "AD"), ANGOLA("ANGOLA", "AO"), ANGUILLA("ANGUILLA", "AI"), ANTARCTICA("ANTARCTICA", "AQ"), ANTIGUA_AND_BARBUDA("ANTIGUA AND BARBUDA", "AG"), ARGENTINA("ARGENTINA", "AR"), ARMENIA("ARMENIA", "AM"), ARUBA("ARUBA", "AW"), AUSTRALIA("AUSTRALIA", "AU"), AUSTRIA("AUSTRIA", "AT"), AZERBAIJAN("AZERBAIJAN", "AZ"), BAHAMAS("BAHAMAS", "BS"), BAHRAIN("BAHRAIN", "BH"), BANGLADESH("BANGLADESH", "BD"), BARBADOS("BARBADOS", "BB"), BELARUS("BELARUS", "BY"), BELGIUM("BELGIUM", "BE"), BELIZE("BELIZE", "BZ"), BENIN("BENIN", "BJ"), BERMUDA("BERMUDA", "BM"), BHUTAN("BHUTAN", "BT"), BOLIVIA("PLURINATIONAL STATE OF BOLIVIA", "BO"), STATIUS("BONAIRE, SINT EUSTATIUS AND SABA", "BQ"), BOSNIA_AND_HERZEGOVINA("BOSNIA AND HERZEGOVINA", "BA"), BOTSWANA("BOTSWANA", "BW"), BOUVET_ISLAND("BOUVET ISLAND", "BV"), BRAZIL("BRAZIL", "BR"), BRITISH_INDIAN_OCEAN_TERRITORY("BRITISH INDIAN OCEAN TERRITORY", "IO"), BRUNEI("BRUNEI DARUSSALAM", "BN"), BULGARIA("BULGARIA", "BG"), BURKINA_FASO("BURKINA FASO", "BF"), BURUNDI("BURUNDI", "BI"), CAMBODIA("CAMBODIA", "KH"), CAMEROON("CAMEROON", "CM"), CANADA("CANADA", "CA"), CAPE_VERDE("CAPE VERDE", "CV"), CAYMAN_ISLANDS("CAYMAN ISLANDS", "KY"), CENTRAL_AFRICAN_REPUBLIC("CENTRAL AFRICAN REPUBLIC", "CF"), CHAD("CHAD", "TD"), CHILE("CHILE", "CL"), CHINA("PEOPLE'S REPUBLIC OF CHINA", "CN"), CHRISTMAS_ISLAND("CHRISTMAS ISLAND", "CX"), COCOS_ISLANDS("COCOS (KEELING) ISLANDS", "CC"), COLOMBIA("COLOMBIA", "CO"), COMOROS("COMOROS", "KM"), CONGO("REPUBLIC OF THE CONGO", "CG"), DEMOCRATIC_REPUBLIC_OF_THE_CONGO("DEMOCRATIC REPUBLIC OF THE CONGO", "CD"), COOK_ISLANDS("COOK ISLANDS", "CK"), COSTA_RICA("COSTA RICA", "CR"), IVORY_COAST("CÔTE D'IVOIRE", "CI"), CROATIA("CROATIA", "HR"), CUBA("CUBA", "CU"), CURAÇAO("CURAÇAO", "CW"), CYPRUS("CYPRUS", "CY"), CZECH_REPUBLIC("CZECH REPUBLIC", "CZ"), DENMARK("DENMARK", "DK"), DJIBOUTI("DJIBOUTI", "DJ"), DOMINICA("DOMINICA", "DM"), DOMINICAN_REPUBLIC("DOMINICAN REPUBLIC", "DO"), ECUADOR("ECUADOR", "EC"), EGYPT("EGYPT", "EG"), EL_SALVADOR("EL SALVADOR", "SV"), EQUATORIAL_GUINEA("EQUATORIAL GUINEA", "GQ"), ERITREA("ERITREA", "ER"), ESTONIA("ESTONIA", "EE"), ETHIOPIA("ETHIOPIA", "ET"), FALKLAND_ISLANDS("FALKLAND ISLANDS (MALVINAS)", "FK"), FAROE_ISLANDS("FAROE ISLANDS", "FO"), FIJI("FIJI", "FJ"), FINLAND("FINLAND", "FI"), FRANCE("FRANCE", "FR"), FRENCH_GUIANA("FRENCH GUIANA", "GF"), FRENCH_POLYNESIA("FRENCH POLYNESIA", "PF"), FRENCH_SOUTHERN_TERRITORIES("FRENCH SOUTHERN TERRITORIES", "TF"), GABON("GABON", "GA"), GAMBIA("GAMBIA", "GM"), GEORGIA("GEORGIA", "GE"), GERMANY("GERMANY", "DE"), GHANA("GHANA", "GH"), GIBRALTAR("GIBRALTAR", "GI"), GREECE("GREECE", "GR"), GREENLAND("GREENLAND", "GL"), GRENADA("GRENADA", "GD"), GUADELOUPE("GUADELOUPE", "GP"), GUAM("GUAM", "GU"), GUATEMALA("GUATEMALA", "GT"), GUERNSEY("GUERNSEY", "GG"), GUINEA("GUINEA", "GN"), GUINEA_BISSAU("GUINEA-BISSAU", "GW"), GUYANA("GUYANA", "GY"), HAITI("HAITI", "HT"), HEARD_ISLAND_AND_MCDONALD_ISLANDS("HEARD ISLAND AND MCDONALD ISLANDS", "HM"), HOLY_SEE("HOLY SEE", "VA"), HONDURAS("HONDURAS", "HN"), HONG_KONG("HONG KONG", "HK"), HUNGARY("HUNGARY", "HU"), ICELAND("ICELAND", "IS"), INDIA("INDIA", "IN"), INDONESIA("INDONESIA", "ID"), IRAN("ISLAMIC REPUBLIC OF IRAN", "IR"), IRAQ("IRAQ", "IQ"), IRELAND("IRELAND", "IE"), ISLE_OF_MAN("ISLE OF MAN", "IM"), ISRAEL("ISRAEL", "IL"), ITALY("ITALY", "IT"), JAMAICA("JAMAICA", "JM"), JAPAN("JAPAN", "JP"), JERSEY("JERSEY", "JE"), JORDAN("JORDAN", "JO"), KAZAKHSTAN("KAZAKHSTAN", "KZ"), KENYA("KENYA", "KE"), KIRIBATI("KIRIBATI", "KI"), NORTH_KOREA("DEMOCRATIC PEOPLE'S REPUBLIC OF KOREA", "KP"), SOUTH_KOREA("REPUBLIC OF KOREA", "KR"), KUWAIT("KUWAIT", "KW"), KYRGYZSTAN("KYRGYZSTAN", "KG"), LAOS("LAO PEOPLE'S DEMOCRATIC REPUBLIC", "LA"), LATVIA("LATVIA", "LV"), LEBANON("LEBANON", "LB"), LESOTHO("LESOTHO", "LS"), LIBERIA("LIBERIA", "LR"), LIBYA("LIBYA", "LY"), LIECHTENSTEIN("LIECHTENSTEIN", "LI"), LITHUANIA("LITHUANIA", "LT"), LUXEMBOURG("LUXEMBOURG", "LU"), MACAO("MACAO", "MO"), MACEDONIA("FORMER YUGOSLAV REPUBLIC OF MACEDONIA", "MK"), MADAGASCAR("MADAGASCAR", "MG"), MALAWI("MALAWI", "MW"), MALAYSIA("MALAYSIA", "MY"), MALDIVES("MALDIVES", "MV"), MALI("MALI", "ML"), MALTA("MALTA", "MT"), MARSHALL_ISLANDS("MARSHALL ISLANDS", "MH"), MARTINIQUE("MARTINIQUE", "MQ"), MAURITANIA("MAURITANIA", "MR"), MAURITIUS("MAURITIUS", "MU"), MAYOTTE("MAYOTTE", "YT"), MEXICO("MEXICO", "MX"), MICRONESIA("FEDERATED STATES OF MICRONESIA", "FM"), MOLDOVA("REPUBLIC OF MOLDOVA", "MD"), MONACO("MONACO", "MC"), MONGOLIA("MONGOLIA", "MN"), MONTENEGRO("MONTENEGRO", "ME"), MONTSERRAT("MONTSERRAT", "MS"), MOROCCO("MOROCCO", "MA"), MOZAMBIQUE("MOZAMBIQUE", "MZ"), MYANMAR("MYANMAR", "MM"), NAMIBIA("NAMIBIA", "NA"), NAURU("NAURU", "NR"), NEPAL("NEPAL", "NP"), NETHERLANDS("NETHERLANDS", "NL"), NEW_CALEDONIA("NEW CALEDONIA", "NC"), NEW_ZEALAND("NEW ZEALAND", "NZ"), NICARAGUA("NICARAGUA", "NI"), NIGER("NIGER", "NE"), NIGERIA("NIGERIA", "NG"), NIUE("NIUE", "NU"), NORFOLK_ISLAND("NORFOLK ISLAND", "NF"), NORTHERN_MARIANA_ISLANDS("NORTHERN MARIANA ISLANDS", "MP"), NORWAY("NORWAY", "NO"), OMAN("OMAN", "OM"), PAKISTAN("PAKISTAN", "PK"), PALAU("PALAU", "PW"), PALESTINE("OCCUPIED PALESTINIAN TERRITORY", "PS"), PANAMA("PANAMA", "PA"), PAPUA_NEW_GUINEA("PAPUA NEW GUINEA", "PG"), PARAGUAY("PARAGUAY", "PY"), PERU("PERU", "PE"), PHILIPPINES("PHILIPPINES", "PH"), PITCAIRN("PITCAIRN", "PN"), POLAND("POLAND", "PL"), PORTUGAL("PORTUGAL", "PT"), PUERTO_RICO("PUERTO RICO", "PR"), QATAR("QATAR", "QA"), RÉUNION("RÉUNION", "RE"), ROMANIA("ROMANIA", "RO"), RUSSIAN_FEDERATION("RUSSIAN FEDERATION", "RU"), RWANDA("RWANDA", "RW"), SAINT_BARTHELEMY("SAINT BARTHÉLEMY", "BL"), SAINT_HELENA("ASCENSION AND TRISTAN DA CUNHA SAINT HELENA", "SH"), SAINT_KITTS_AND_NEVIS("SAINT KITTS AND NEVIS", "KN"), SAINT_LUCIA("SAINT LUCIA", "LC"), FRENCH_SAINT_MARTIN("SAINT MARTIN (FRENCH PART)", "MF"), SAINT_PIERRE_AND_MIQUELON("SAINT PIERRE AND MIQUELON", "PM"), SAINT_VINCENT_AND_THE_GRENADINES("SAINT VINCENT AND THE GRENADINES", "VC"), SAMOA("SAMOA", "WS"), SAN_MARINO("SAN MARINO", "SM"), SAO_TOME_AND_PRINCIPE("SAO TOME AND PRINCIPE", "ST"), SAUDI_ARABIA("SAUDI ARABIA", "SA"), SENEGAL("SENEGAL", "SN"), SERBIA("SERBIA", "RS"), SEYCHELLES("SEYCHELLES", "SC"), SIERRA_LEONE("SIERRA LEONE", "SL"), SINGAPORE("SINGAPORE", "SG"), DUTCH_SAINT_MARTIN("SAINT MARTIN (DUTCH PART)", "SX"), SLOVAKIA("SLOVAKIA", "SK"), SLOVENIA("SLOVENIA", "SI"), SOLOMON_ISLANDS("SOLOMON ISLANDS", "SB"), SOMALIA("SOMALIA", "SO"), SOUTH_AFRICA("SOUTH AFRICA", "ZA"), SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "GS"), SPAIN("SPAIN", "ES"), SOUTH_SUDAN("SOUTH SUDAN", "SS"), SRI_LANKA("SRI LANKA", "LK"), SUDAN("SUDAN", "SD"), SURINAME("SURINAME", "SR"), SVALBARD_AND_JAN_MAYEN("SVALBARD AND JAN MAYEN", "SJ"), SWAZILAND("SWAZILAND", "SZ"), SWEDEN("SWEDEN", "SE"), SWITZERLAND("SWITZERLAND", "CH"), SYRIAN_ARAB_REPUBLIC("SYRIAN ARAB REPUBLIC", "SY"), TAIWAN("REPUBLIC OF CHINA", "TW"), TAJIKISTAN("TAJIKISTAN", "TJ"), TANZANIA("TANZANIA, UNITED REPUBLIC OF", "TZ"), THAILAND("THAILAND", "TH"), TIMOR_LESTE("TIMOR-LESTE", "TL"), TOGO("TOGO", "TG"), TOKELAU("TOKELAU", "TK"), TONGA("TONGA", "TO"), TRINIDAD_AND_TOBAGO("TRINIDAD AND TOBAGO", "TT"), TUNISIA("TUNISIA", "TN"), TURKEY("TURKEY", "TR"), TURKMENISTAN("TURKMENISTAN", "TM"), TURKS_AND_CAICOS_ISLANDS("TURKS AND CAICOS ISLANDS", "TC"), TUVALU("TUVALU", "TV"), UGANDA("UGANDA", "UG"), UKRAINE("UKRAINE", "UA"), UNITED_ARAB_EMIRATES("UNITED ARAB EMIRATES", "AE"), UNITED_KINGDOM("UNITED KINGDOM OF GREAT BRITTAIN AND NORTHERN IRELAND", "GB"), UNITED_STATES_OF_AMERICA("UNITED STATES OF AMERICA", "US"), UNITED_STATES_MINOR_OUTLYING_ISLANDS("UNITED STATES MINOR OUTLYING ISLANDS", "UM"), URUGUAY("URUGUAY", "UY"), UZBEKISTAN("UZBEKISTAN", "UZ"), VANUATU("VANUATU", "VU"), VENEZUELA("BOLIVARIAN REPUBLIC OF VENEZUELA", "VE"), VIET_NAM("VIET NAM", "VN"), BRITISH_VIRGIN_ISLANDS("BRITISH VIRGIN ISLANDS, ", "VG"), US_VIRGIN_ISLANDS("U.S. VIRGIN ISLANDS", "VI"), WALLIS_AND_FUTUNA("WALLIS AND FUTUNA", "WF"), WESTERN_SAHARA("WESTERN SAHARA", "EH"), YEMEN("YEMEN", "YE"), ZAMBIA("ZAMBIA", "ZM"), ZIMBABWE("ZIMBABWE", "ZW"), UNKOWN("UNKOWN", "??");

        public final String name;

        public final String code;

        private Country(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }

    /**
	 * User Language.
	 */
    public enum Language {

        OROMO("OM"), ABKHAZIAN("AB"), AFAR("AA"), AFRIKAANS("AF"), ALBANIAN("SQ"), AMHARIC("AM"), ARABIC("AR"), ARMENIAN("HY"), ASSAMESE("AS"), AYMARA("AY"), AZERBAIJANI("AZ"), BASHKIR("BA"), BASQUE("EU"), BENGALI("BN"), BHUTANI("DZ"), BIHARI("BH"), BISLAMA("BI"), BRETON("BR"), BULGARIAN("BG"), BURMESE("MY"), BYELORUSSIAN("BE"), CAMBODIAN("KM"), CATALAN("CA"), CHINESE("ZH"), CORSICAN("CO"), CROATIAN("HR"), CZECH("CS"), DANISH("DA"), DUTCH("NL"), ENGLISH("EN"), ESPERANTO("EO"), ESTONIAN("ET"), FAEROESE("FO"), FIJI("FJ"), FINNISH("FI"), FRENCH("FR"), FRISIAN("FY"), GALICIAN("GL"), GEORGIAN("KA"), GERMAN("DE"), GREEK("EL"), GREENLANDIC("KL"), GUARANI("GN"), GUJARATI("GU"), HAUSA("HA"), HEBREW("HE"), HINDI("HI"), HUNGARIAN("HU"), ICELANDIC("IS"), INDONESIAN("ID"), INTERLINGUA("IA"), INTERLINGUE("IE"), INUPIAK("IK"), INUKTITUT("IU"), IRISH("GA"), ITALIAN("IT"), JAPANESE("JA"), JAVANESE("JW"), KANNADA("KN"), KASHMIRI("KS"), KAZAKH("KK"), KINYARWANDA("RW"), KIRGHIZ("KY"), KIRUNDI("RN"), KOREAN("KO"), KURDISH("KU"), LAOTHIAN("LO"), LATIN("LA"), LATVIAN("LV"), LINGALA("LN"), LITHUANIAN("LT"), MACEDONIAN("MK"), MALAGASY("MG"), MALAY("MS"), MALAYALAM("ML"), MALTESE("MT"), MAORI("MI"), MARATHI("MR"), MOLDAVIAN("MO"), MONGOLIAN("MN"), NAURU("NA"), NEPALI("NE"), NORWEGIAN("NO"), OCCITAN("OC"), ORIYA("OR"), PASHTO("PS"), PERSIAN("FA"), POLISH("PL"), PORTUGUESE("PT"), PUNJABI("PA"), QUECHUA("QU"), RHAETO_ROMANCE("RM"), ROMANIAN("RO"), RUSSIAN("RU"), SAMOAN("SM"), SANGRO("SG"), SANSKRIT("SA"), SCOTS_GAELIC("GD"), SERBIAN("SR"), SERBO_CROATIAN("SH"), SESOTHO("ST"), SETSWANA("TN"), SHONA("SN"), SINDHI("SD"), SINGHALESE("SI"), SISWATI("SS"), SLOVAK("SK"), SLOVENIAN("SL"), SOMALI("SO"), SPANISH("ES"), SUDANESE("SU"), SWAHILI("SW"), SWEDISH("SV"), TAGALOG("TL"), TAJIK("TG"), TAMIL("TA"), TATAR("TT"), TEGULU("TE"), THAI("TH"), TIBETAN("BO"), TIGRINYA("TI"), TONGA("TO"), TSONGA("TS"), TURKISH("TR"), TURKMEN("TK"), TWI("TW"), UIGUR("UG"), UKRAINIAN("UK"), URDU("UR"), UZBEK("UZ"), VIETNAMESE("VI"), VOLAPUK("VO"), WELCH("CY"), WOLOF("WO"), XHOSA("XH"), YIDDISH("YI"), YORUBA("YO"), ZHUANG("ZA"), ZULU("ZU"), UNKOWN("??");

        public final String code;

        private Language(String code) {
            this.code = code;
        }
    }

    private static OSName operatingSystem;

    private static String operatingSystemString;

    private static String operatingSystemPatchLevel;

    private static Architecture architecture;

    private static int maxProcessors;

    private static Country country;

    private static Language language;

    private static String javaLauncher;

    private static String javaCompiler;

    private static String javaRunCommand;

    private static String javaVendor;

    private static String javaVersion;

    private static String javaSpecificationVendor;

    private static String javaSpecificationVersion;

    private static String javaVMVendor;

    private static String javaVMVersion;

    private static String javaVMName;

    private static String javaVMInfo;

    private static String javaVMSpecificationVendor;

    private static String javaVMSpecificationVersion;

    private static String javaVMSpecificationName;

    private static String javaRuntimeVersion;

    private static String javaRuntimeName;

    private static String javaClassVersion;

    private static Architecture javaArchicture;

    private static DisplayInfo[] displays;

    private static String userName;

    private static String userHome;

    private static String gpuVendor;

    private static String gpuName;

    private static String glVersion;

    private static String glslVersion;

    private static String glExtensions;

    static void initOS() {
        operatingSystem = parseOSName(System.getProperty("os.name"));
        operatingSystemString = System.getProperty("os.name") + " v" + System.getProperty("os.version");
        operatingSystemPatchLevel = System.getProperty("sun.os.patch.level");
        architecture = parseArchitecture(System.getProperty("sun.arch.data.model"));
        maxProcessors = Runtime.getRuntime().availableProcessors();
        country = parseCountry(System.getProperty("user.country"));
        language = parseLanguage(System.getProperty("user.language"));
        javaLauncher = System.getProperty("sun.java.launcher");
        javaCompiler = System.getProperty("sun.management.compiler");
        javaRunCommand = System.getProperty("sun.java.command");
        javaSpecificationVendor = System.getProperty("java.specification.vendor");
        javaSpecificationVersion = System.getProperty("java.specification.version");
        javaVMVendor = System.getProperty("java.vm.version");
        javaVMVersion = System.getProperty("java.vm.vendor");
        javaVMName = System.getProperty("java.vm.name");
        javaVMInfo = System.getProperty("java.vm.info");
        javaVMSpecificationVendor = System.getProperty("java.vm.specification.vendor");
        javaVMSpecificationVersion = System.getProperty("java.vm.specification.version");
        javaVMSpecificationName = System.getProperty("java.vm.specification.name");
        javaVendor = System.getProperty("java.vendor");
        javaVersion = System.getProperty("java.version");
        javaArchicture = parseArchitecture(System.getProperty("os.arch"));
        javaRuntimeVersion = System.getProperty("java.runtime.version");
        javaRuntimeName = System.getProperty("java.runtime.name");
        javaClassVersion = System.getProperty("java.class.version");
        userName = System.getProperty("user.name");
        userHome = System.getProperty("user.home");
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        displays = new DisplayInfo[devices.length];
        for (int i = 0; i < devices.length; i++) {
            displays[i] = new DisplayInfo(devices[i].getDisplayMode().getBitDepth(), devices[i].getDisplayMode().getWidth(), devices[i].getDisplayMode().getHeight(), devices[i].getDisplayMode().getRefreshRate());
        }
    }

    static void initGFX() {
        GL2 gl = Graphics.getGL();
        gpuVendor = gl.glGetString(GL2.GL_VENDOR);
        gpuName = gl.glGetString(GL2.GL_RENDERER);
        glVersion = gl.glGetString(GL2.GL_VERSION);
        glslVersion = gl.glGetString(GL2.GL_SHADING_LANGUAGE_VERSION);
        glExtensions = gl.glGetString(GL2.GL_EXTENSIONS);
    }

    private static OSName parseOSName(String s) {
        String check = s.toLowerCase();
        if (check.contains("linux")) return OSName.LINUX; else if (check.contains("windows")) return OSName.WINDOWS; else if (check.contains("mac")) return OSName.MAC_OS; else if (check.contains("bsd")) return OSName.BSD; else if (check.contains("sunos")) return OSName.SOLARIS; else return OSName.UNKOWN;
    }

    private static Architecture parseArchitecture(String s) {
        if (s.contains("64")) return Architecture.BIT_64; else return Architecture.BIT_32;
    }

    private static Country parseCountry(String s) {
        String check = s.toUpperCase();
        for (int i = 0; i < Country.values().length; i++) {
            Country ret = Country.values()[i];
            if (ret.code.equals(check)) return ret;
        }
        return Country.UNKOWN;
    }

    private static Language parseLanguage(String s) {
        String check = s.toUpperCase();
        for (int i = 0; i < Language.values().length; i++) {
            Language ret = Language.values()[i];
            if (ret.code.equals(check)) return ret;
        }
        return Language.UNKOWN;
    }

    public static String getAnynomousString() {
        String ret = "";
        ret += "Operating System: " + operatingSystem + " (" + operatingSystemString + ")\n";
        ret += "Operating System Patch Level: " + operatingSystemPatchLevel + "\n";
        ret += "Architecture: " + architecture + "\n";
        ret += "Number of processors: " + maxProcessors + "\n";
        ret += "\n";
        ret += "Country: " + country.name + " (" + country.code + ")\n";
        ret += "Language: " + language + " (" + language.code + ")\n";
        ret += "\n";
        ret += "Java Vendor: " + javaVendor + "\n";
        ret += "Java Version: " + javaVersion + "\n";
        ret += "Java Architecture: " + javaArchicture + "\n";
        ret += "\n";
        ret += "Java Launcher: " + javaLauncher + "\n";
        ret += "Java Compiler: " + javaCompiler + "\n";
        ret += "\n";
        ret += "Java Specification Vendor: " + javaSpecificationVendor + "\n";
        ret += "Java Specification Version: " + javaSpecificationVersion + "\n";
        ret += "\n";
        ret += "Java Runtime Name: " + javaRuntimeName + "\n";
        ret += "Java Runtime Version: " + javaRuntimeVersion + "\n";
        ret += "\n";
        ret += "Java Class Version: " + javaClassVersion + "\n";
        ret += "\n";
        ret += "Java Run Command: " + javaRunCommand + "\n";
        ret += "\n";
        ret += "Java Virtual Machine Vendor: " + javaVMVendor + "\n";
        ret += "Java Virtual Machine Version: " + javaVMVersion + "\n";
        ret += "Java Virtual Machine Name: " + javaVMName + "\n";
        ret += "Java Virtual Machine Info: " + javaVMInfo + "\n";
        ret += "\n";
        ret += "Java Virtual Machine Specification Vendor: " + javaVMSpecificationVendor + "\n";
        ret += "Java Virtual Machine Specification Version: " + javaVMSpecificationVersion + "\n";
        ret += "Java Virtual Machine Specification Name: " + javaVMSpecificationName + "\n";
        ret += "\n";
        for (int i = 0; i < displays.length; i++) {
            ret += "Display [" + i + "] Width: " + displays[i].width + "\n";
            ret += "Display [" + i + "] Height: " + displays[i].height + "\n";
            ret += "Display [" + i + "] Bit Depth: " + displays[i].bitDepth + "\n";
            ret += "Display [" + i + "] Refresh Rate: " + displays[i].refreshRate + "\n";
            ret += "\n";
        }
        ret += "GPU Vendor: " + gpuVendor + "\n";
        ret += "GPU Name: " + gpuName + "\n";
        ret += "\n";
        ret += "OpenGL Version: " + glVersion + "\n";
        ret += "GLSL Version: " + glslVersion + "\n";
        String ext = " " + glExtensions.trim();
        ext = ext.replaceAll(" ", "\n\t");
        ret += "OpenGL Extensions: " + ext + "\n";
        return ret;
    }

    public static String getFullString() {
        String ret = getAnynomousString();
        ret += "\n";
        ret += "User Name: " + userName + "\n";
        ret += "User Home: " + userHome + "\n";
        return ret;
    }

    public static OSName getOperatingSystem() {
        return operatingSystem;
    }

    public static String getOperatingSystemString() {
        return operatingSystemString;
    }

    public static Architecture getArchitecture() {
        return architecture;
    }

    public static int getMaxProcessors() {
        return maxProcessors;
    }

    public static Country getCountry() {
        return country;
    }

    public static Language getLanguage() {
        return language;
    }

    public static String getJavaVendor() {
        return javaVendor;
    }

    public static String getJavaVersion() {
        return javaVersion;
    }

    public static Architecture getJavaArchicture() {
        return javaArchicture;
    }

    public static DisplayInfo[] getDisplays() {
        return displays;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserHome() {
        return userHome;
    }

    public static String getGpuVendor() {
        return gpuVendor;
    }

    public static String getGpuName() {
        return gpuName;
    }

    public static String getGlVersion() {
        return glVersion;
    }

    public static String getGLSLVersion() {
        return glslVersion;
    }

    public static String getGlExtensions() {
        return glExtensions;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static String getOperatingSystemPatchLevel() {
        return operatingSystemPatchLevel;
    }

    public static String getJavaLauncher() {
        return javaLauncher;
    }

    public static String getJavaCompiler() {
        return javaCompiler;
    }

    public static String getJavaRunCommand() {
        return javaRunCommand;
    }

    public static String getJavaSpecificationVendor() {
        return javaSpecificationVendor;
    }

    public static String getJavaSpecificationVersion() {
        return javaSpecificationVersion;
    }

    public static String getJavaVMVendor() {
        return javaVMVendor;
    }

    public static String getJavaVMVersion() {
        return javaVMVersion;
    }

    public static String getJavaVMName() {
        return javaVMName;
    }

    public static String getJavaVMInfo() {
        return javaVMInfo;
    }

    public static String getJavaVMSpecificationVendor() {
        return javaVMSpecificationVendor;
    }

    public static String getJavaVMSpecificationVersion() {
        return javaVMSpecificationVersion;
    }

    public static String getJavaVMSpecificationName() {
        return javaVMSpecificationName;
    }

    public static String getJavaRuntimeVersion() {
        return javaRuntimeVersion;
    }

    public static String getJavaRuntimeName() {
        return javaRuntimeName;
    }

    public static String getJavaClassVersion() {
        return javaClassVersion;
    }
}
