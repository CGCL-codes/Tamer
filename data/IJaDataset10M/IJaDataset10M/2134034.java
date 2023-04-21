package acmsoft.util;

import java.util.*;
import java.io.*;

/*** 
 * This class accomodates various functions to deal
 * with locales, country codes, encodings etc.
 */
public final class NLSManager {

    private static Hashtable m_NameToEncodingMap = new Hashtable(100);

    private static Hashtable m_LangIdToEncodingVectorMap = new Hashtable(100);

    private static Hashtable m_LangIdToDefaultEncodingMap = new Hashtable(100);

    private static Vector m_OrderedByDescriptionEncodingList = new Vector(100);

    /** Here a list of unknow encodings will be stored */
    private static Vector m_UnknownEncodings = new Vector(100);

    public static String UNICODE_BIG_UNMARKED = "UnicodeBigUnmarked";

    public static String UNICODE_BIG = "UnicodeBig";

    public static String UNICODE_LITTLE_UNMARKED = "UnicodeLittleUnmarked";

    public static String UNICODE_LITTLE = "UnicodeLittle";

    public static String UTF_16 = "UTF-16";

    public static String UTF_8 = "UTF-8";

    /** This characters will be used to test if given encoding is a supreset of ASCII */
    private static String m_strCharactersToTest = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuv -:;,.";

    /** Predefined name of current system encoding (JVM default) */
    public static String SYSTEM_ENCODING_NAME;

    /** predefined Default Http Encoding */
    private static NLSEncodingData m_nlsdDefaultHttpEncoding;

    /** hashtable that maps encoding names into java canonical encoding names */
    private static Hashtable m_NameToCanonicalJavaEncodingName = new Hashtable(100);

    static {
        registerEncodings();
    }

    public static NLSEncodingData UTF8 = getEncodingData("UTF8");

    public static NLSEncodingData UTF16LE = getEncodingData("utf-16le");

    private static void registerEncodings() {
        m_NameToEncodingMap.clear();
        m_LangIdToEncodingVectorMap.clear();
        m_LangIdToDefaultEncodingMap.clear();
        m_OrderedByDescriptionEncodingList.removeAllElements();
        m_UnknownEncodings.removeAllElements();
        String strLangIDs = "";
        strLangIDs = "de,de_CH,en,es,fi,fr,fr_CH,ge,it,it_CH,nl,sq,sv,sv_FI";
        registerEncoding("IBM437", "cp437,437,csPC8CodePage437,IBM-437", "Cp437", "IBM437", "Cp037", strLangIDs, "IBM-437", "NLSManager_Name_IBM-437");
        registerEncoding("Cp1047", "Cp1047,1047,IBM1047,IBM-1047", "Cp1047", "IBM1047", "Cp1047", strLangIDs, "Latin Open Systems EBCDIC", "NLSManager_Name_IBM-1047");
        strLangIDs = "de,en,fr_CA,ge,nl,pt,pt_BR,sq";
        registerEncoding("IBM037", "cp037,ebcdic-cp-us,ebcdic-cp-ca,ebcdic-cp-wt,ebcdic-cp-nl,csIBM037,IBM-037,IBM-37,IBM37,37", "Cp037", "IBM037", "Cp037", strLangIDs, "IBM-37", "NLSManager_Name_IBM-037");
        registerEncoding("IBM01140", "CCSID01140,CP01140,ebcdic-us-37+euro,IBM-1140,1140,Cp1140", "Cp1140", "IBM01140", "Cp1140", strLangIDs, "IBM-1140 (Cp037+Euro character)", "NLSManager_Name_IBM-1140");
        strLangIDs = "de";
        registerEncoding("IBM273", "CP273,csIBM273,IBM-273,273", "Cp273", "IBM273", "Cp273", strLangIDs, "IBM-273 (IBM Austria, Germany)", "NLSManager_Name_IBM-273");
        registerEncoding("IBM01141", "CCSID01141,CP01141,ebcdic-de-273+euro,IBM1141,IBM-1141,1141,Cp1141", "Cp1141", "IBM01141", "Cp1141", strLangIDs, "IBM-1141 (Cp273+Euro character)", "NLSManager_Name_IBM-1141");
        strLangIDs = "da,no";
        registerEncoding("IBM277", "EBCDIC-CP-DK,EBCDIC-CP-NO,csIBM277,IBM-277,277,Cp277", "Cp277", "IBM277", "Cp277", strLangIDs, "IBM-277 (IBM Denmark, Norway)", "NLSManager_Name_IBM-277");
        registerEncoding("IBM01142", "CCSID01142,CP01142,ebcdic-dk-277+euro,ebcdic-no-277+euro,IBM1142,IBM-1142,1142,Cp1142", "Cp1142", "IBM01142", "Cp1142", strLangIDs, "IBM-1142 (Cp277+Euro character)", "NLSManager_Name_IBM-1142");
        strLangIDs = "fi,sv,sv_FI";
        registerEncoding("IBM278", "CP278,ebcdic-cp-fi,ebcdic-cp-se,csIBM278,IBM-278,278", "Cp278", "IBM278", "Cp278", strLangIDs, "IBM-278 (IBM Finland, Sweden)", "NLSManager_Name_IBM-278");
        registerEncoding("IBM01143", "CCSID01143,CP01143,ebcdic-fi-278+euro,ebcdic-se-278+euro,IBM1143,IBM-1143,1143,Cp1143", "Cp1143", "IBM01143", "Cp1143", strLangIDs, "IBM-1143 (Cp278+Euro character)", "NLSManager_Name_IBM-1143");
        strLangIDs = "it";
        registerEncoding("IBM280", "CP280,ebcdic-cp-it,csIBM280,IBM-280,280", "Cp280", "IBM280", "Cp280", strLangIDs, "IBM-280 (IBM Italy)", "NLSManager_Name_IBM-280");
        registerEncoding("IBM01144", "CCSID01144,CP01144,ebcdic-it-280+euro,IBM1144,IBM-1144,1144,Cp1144", "Cp1144", "IBM01144", "Cp1144", strLangIDs, "IBM-1144 (Cp280+Euro character)", "NLSManager_Name_IBM-1144");
        strLangIDs = "es";
        registerEncoding("IBM284", "CP284,ebcdic-cp-es,csIBM284,IBM-284,284", "Cp284", "IBM284", "Cp284", strLangIDs, "IBM-284 (IBM Catalan/Spain, Spanish Latin America)", "NLSManager_Name_IBM-284");
        registerEncoding("IBM01145", "CCSID01145,CP01145,ebcdic-es-284+euro,IBM1145,IBM-1145,1145,Cp1145", "Cp1145", "IBM01145", "Cp1145", strLangIDs, "IBM-1145 (Cp284+Euro character)", "NLSManager_Name_IBM-1145");
        strLangIDs = "en_GB,en_UK,en_IE,en_ZA";
        registerEncoding("IBM285", "CP285,ebcdic-cp-gb,csIBM285,IBM-285,285", "Cp285", "IBM285", "Cp285", strLangIDs, "IBM-285 (IBM United Kingdom, Ireland)", "NLSManager_Name_IBM-285");
        registerEncoding("IBM01146", "CCSID01146,CP01146,ebcdic-gb-285+euro,IBM1146,IBM-1146,1146,Cp1146", "Cp1146", "IBM01146", "Cp1146", strLangIDs, "IBM-1146 (Cp285+Euro character)", "NLSManager_Name_IBM-1146");
        strLangIDs = "fr";
        registerEncoding("IBM297", "cp297,ebcdic-cp-fr,csIBM297,IBM-297,297", "Cp297", "IBM297", "Cp297", strLangIDs, "IBM-297 (IBM France)", "NLSManager_Name_IBM-297");
        registerEncoding("IBM01147", "CCSID01147,CP01147,ebcdic-fr-297+euro,IBM1147,IBM-1147,1147,Cp1147", "Cp1147", "IBM01147", "Cp1147", strLangIDs, "IBM-1147 (Cp297+Euro character)", "NLSManager_Name_IBM-1147");
        strLangIDs = "nl,de_CH,fr_CH,it_CH";
        registerEncoding("IBM500", "CP500,ebcdic-cp-be,ebcdic-cp-ch,csIBM500,IBM-500,500", "Cp500", "IBM500", "Cp500", strLangIDs, "IBM-500 (EBCDIC 500V1)", "NLSManager_Name_IBM-500");
        registerEncoding("IBM01148", "CCSID01148,CP01148,ebcdic-international-500+euro,IBM-1148,1148,Cp1148", "Cp1148", "IBM01148", "Cp1148", strLangIDs, "IBM-1148 (Cp500+Euro character)", "NLSManager_Name_IBM-1148");
        strLangIDs = "pt";
        registerEncoding("IBM860", "cp860,860,csIBM860,IBM-860", "Cp860", "IBM860", "Cp037", strLangIDs, "IBM-860 (MS-DOS Portuguese)", "NLSManager_Name_IBM-860");
        strLangIDs = "en_CA,fr_CA";
        registerEncoding("IBM863", "cp863,863,csIBM863,IBM-863", "Cp863", "IBM863", "Cp037", strLangIDs, "IBM-863 (MS-DOS Canadian French)", "NLSManager_Name_IBM-863");
        strLangIDs = "is";
        registerEncoding("IBM871", "CP871,ebcdic-cp-is,csIBM871,IBM-871,871", "Cp871", "IBM871", "Cp871", strLangIDs, "IBM-871 (IBM Iceland)", "NLSManager_Name_IBM-871");
        registerEncoding("IBM01149", "CCSID01149,CP01149,ebcdic-is-871+euro,IBM1149,IBM-1149,1149,Cp1149", "Cp1149", "IBM01149", "Cp1149", strLangIDs, "IBM-1149 (Cp871+Euro character)", "NLSManager_Name_IBM-1149");
        strLangIDs = "et";
        registerEncoding("IBM902", "IBM922,IBM-922,922,IBM-902,902,Cp922", "Cp922", "Cp922", "Cp037", strLangIDs, "IBM-922 (IBM Estonia - DOS, AIX)", "NLSManager_Name_IBM-902");
        registerEncoding("IBM01122", "CCSID01122,CP01122,IBM-1122,1122,Cp1122", "Cp1122", "IBM01122", "Cp1122", strLangIDs, "IBM-1122 (IBM Estonia)", "NLSManager_Name_IBM-1122");
        strLangIDs = "ca,da,de,de_CH,dk,en,es,et,fi,fo,fr,fr_CH,fr_CA,ga,ge,is,it,it_CH,nl,no,pt,pt_BR,sq,sv,sv_FI";
        registerEncoding("IBM850", "cp850,850,csPC850Multilingual,IBM-850", "Cp850", null, "Cp037", strLangIDs, "Western European (MS-DOS Latin-1)", "NLSManager_Name_MS-DOS_Latin-1");
        registerEncoding("windows-1252", "Cp1252,IBM1252,1252", "Cp1252", null, "Cp037", strLangIDs, "Western European (Windows-1252)", "NLSManager_Name_Windows-1252");
        registerEncoding("ISO_8859-1:1987", "8859_1,8859-1,iso-ir-100,ISO_8859-1,ISO-8859-1,latin1,l1,IBM819,CP819,csISOLatin1,819,iso88591,IBM-819,ISO8859_1", "ISO8859_1", "ISO-8859-1", "Cp037", strLangIDs, "Western European (ISO-8859-1)", "NLSManager_Name_ISO-8859-1");
        strLangIDs = "lt,lv";
        registerEncoding("IBM901", "IBM921,IBM-921,921,Cp921,IBM-901,901", "Cp921", "Cp921", "Cp037", strLangIDs, "IBM-921 (IBM Latvia, Lithuania - DOS, AIX)", "NLSManager_Name_IBM-901");
        registerEncoding("IBM01112", "CCSID01112,CP01112,IBM-1112,1112,Cp1112", "Cp1112", "IBM01112", "Cp1112", strLangIDs, "IBM-1112 (IBM Latvia, Lithuania)", "NLSManager_Name_IBM-1112");
        strLangIDs = "cz,hr,hu,lt,lv,pl,ro,sh,sl,sk";
        registerEncoding("IBM852", "IBM9044,IBM-9044,9044,cp852,852,csPCp852,IBM-852", "Cp852", "IBM852", "Cp500", strLangIDs, "MS-DOS Latin-2", "NLSManager_Name_IBM-852");
        registerEncoding("IBM870", "CP870,ebcdic-cp-roece,ebcdic-cp-yu,csIBM870,IBM-870,870", "Cp870", "IBM870", "Cp870", strLangIDs, "IBM-870 (IBM Multilingual Latin-2)", "NLSManager_Name_IBM-870");
        registerEncoding("windows-1250", "Cp1250, 1250", "Cp1250", null, "Cp870", strLangIDs, "Central European (Windows-1250)", "NLSManager_Name_Windows-1250");
        registerEncoding("ISO_8859-2:1987", "8859_2,8859-2,iso-ir-101,ISO_8859-2,ISO-8859-2,latin2,l2,csISOLatin2,IBM-912,IBM912,912,ISO8859-2,iso88592,ISO8859_2", "ISO8859_2", "ISO-8859-2", "Cp037", strLangIDs, "Central European (ISO-8859-2)", "NLSManager_Name_ISO-8859-2");
        strLangIDs = "cs,hu,pl,ro,sh,sk,sl";
        registerEncoding("ISO_8859-3:1988", "8859_3,8859-3,iso-ir-109,ISO_8859-3,ISO-8859-3,latin3,l3,csISOLatin3,ISO8859_3", "ISO8859_3", "ISO-8859-3", "Cp037", strLangIDs, "South European (ISO-8859-3)", "NLSManager_Name_ISO-8859-3");
        strLangIDs = "da,en,et,fi,de,gl,lv,lt,sv,no";
        registerEncoding("windows-1257", "1257,Cp1257", "Cp1257", null, "Cp1112", strLangIDs, "Baltic (Windows-1257)", "NLSManager_Name_Windows-1257");
        registerEncoding("ISO_8859-4:1988", "8859_4,8859-4,iso-ir-110,ISO_8859-4,ISO-8859-4,latin4,l4,csISOLatin4,ISO8859_4", "ISO8859_4", "ISO-8859-4", "Cp037", strLangIDs, "Baltic (ISO-8859-4)", "NLSManager_Name_ISO-8859-4");
        strLangIDs = "ru";
        registerEncoding("IBM866", "cp866,866,csIBM866,IBM-866", "Cp866", null, "Cp1025", strLangIDs, "Cyrillic (DOS-866)", "NLSManager_Name_DOS-866");
        registerEncoding("KOI8-R", "csKOI8R,KOI-8,KOI8,KOI8_R", "KOI8_R", null, "Cp1025", strLangIDs, "Cyrillic (KOI8-R)", "NLSManager_Name_KOI-8R");
        strLangIDs = "bg,mk,ru,sr";
        registerEncoding("IBM1025", "IBM-1025,1025,Cp1025", "Cp1025", "IBM1025", "Cp1025", strLangIDs, "IBM-1025 (IBM Multilingual Cyrillic)", "NLSManager_Name_IBM-1025");
        strLangIDs = "bg,mk,sr";
        registerEncoding("IBM872", "cp855,855,csIBM855,IBM855,IBM-855,IBM-872,872", "Cp855", null, "Cp037", strLangIDs, "IBM855 (IBM Cyrillic)", "NLSManager_Name_IBM-855");
        strLangIDs = "en,be,bg,mk,ru,sr,uk";
        registerEncoding("windows-1251", "Cp1251,1251", "Cp1251", null, "Cp1025", strLangIDs, "Cyrillic (Windows-1251)", "NLSManager_Name_Windows-1251");
        registerEncoding("ISO_8859-5:1988", "8859_5,8859-5,iso-ir-144,ISO_8859-5,ISO-8859-5,cyrillic,csISOLatinCyrillic,IBM-915,IBM915,915,iso88595,ISO8859_5", "ISO8859_5", "ISO-8859-5", "Cp1025", strLangIDs, "Cyrillic (ISO-8859-5)", "NLSManager_Name_ISO-8859-5");
        strLangIDs = "ar,aa";
        registerEncoding("IBM864", "cp864,csIBM864,IBM17248,IBM-17248,IBM-864,864", "Cp864", "cp864", "Cp420", strLangIDs, "IBM864 (PC Arabic)", "NLSManager_Name_IBM-864");
        registerEncoding("IBM420", "cp420,ebcdic-cp-ar1,csIBM420,IBM-420,420", "Cp420", "IBM420", "Cp420", strLangIDs, "IBM420 (IBM Arabic)", "NLSManager_Name_IBM-420");
        registerEncoding("windows-1256", "1256,Cp1256", "Cp1256", "windows-1256", "Cp420", strLangIDs, "Arabic (Windows-1256)", "NLSManager_Name_Windows-1256");
        registerEncoding("ISO_8859-6:1987", "8859_6,8859-6,iso-ir-127,ISO_8859-6,ISO-8859-6,ECMA-114,ASMO-708,arabic,csISOLatinArabic,IBM-1089,IBM1089,1089,iso88596,ISO8859_6", "ISO8859_6", "ISO-8859-6", "Cp420", strLangIDs, "Arabic (ISO-8859-6)", "NLSManager_Name_ISO-8859-6");
        strLangIDs = "el,gr_GR";
        registerEncoding("IBM869", "cp869,869,cp-gr,csIBM869,IBM-869,IBM9061,IBM-9061,9061", "Cp869", "IBM869", "Cp875", strLangIDs, "IBM-869 (Modern Greek)", "NLSManager_Name_IBM-869");
        registerEncoding("windows-1253", "1253,Cp1253", "Cp1253", "windows-1253", "Cp875", strLangIDs, "Greek (Windows-1253)", "NLSManager_Name_Windows-1253");
        registerEncoding("ISO_8859-7:1987", "8859_7,8859-7,iso-ir-126,ISO_8859-7,ISO-8859-7,ELOT_928,ECMA-118,greek,greek8,csISOLatinGreek,IBM-813,IBM813,813,iso88597,ISO8859-7,ISO8859_7", "ISO8859_7", "ISO-8859-7", "Cp875", strLangIDs, "Greek (ISO-8859-7)", "NLSManager_Name_ISO-8859-7");
        registerEncoding("IBM875", "IBM-875,cp875,875,ibm-875_P100-2000,ibm-875_STD", "Cp875", "IBM-875", "Cp875", strLangIDs, "EBCDIC Greek", "NLSManager_Name_IBM-875");
        strLangIDs = "he,iw";
        registerEncoding("IBM424", "cp424,ebcdic-cp-he,csIBM424,IBM-424,424", "Cp424", "IBM424", "Cp424", strLangIDs, "IBM-424 (IBM Hebrew)", "NLSManager_Name_IBM-424");
        registerEncoding("IBM862", "cp862,862,csPC862LatinHebrew,IBM-862", "Cp862", null, "Cp424", strLangIDs, "Hebrew (DOS-862)", "NLSManager_Name_DOS-862");
        registerEncoding("windows-1255", "1255,Cp1255", "Cp1255", null, "Cp424", strLangIDs, "Hebrew (Windows-1255)", "NLSManager_Name_Windows-1255");
        registerEncoding("ISO_8859-8-I", "csISO88598I,ISO-8859-8-I", "ISO8859_8", "ISO-8859-8-I", "Cp424", strLangIDs, "Hebrew (ISO Logical)", "NLSManager_Name_ISO-Herbew-Logical");
        registerEncoding("ISO_8859-8:1988", "8859_8,8859-8,iso-ir-138,ISO_8859-8,ISO-8859-8,hebrew,csISOLatinHebrew,IBM-916,IBM916,916,ISO8859_8", "ISO8859_8", "ISO-8859-8", "Cp424", strLangIDs, "Hebrew (ISO Visual, ISO-8859-8)", "NLSManager_Name_ISO-8859-8");
        strLangIDs = "tr";
        registerEncoding("IBM857", "cp857,csIBM857,IBM-857,857,IBM9049,IBM-9049,9049", "Cp857", "IBM857", "Cp1026", strLangIDs, "IBM-857 (IBM Turkish)", "NLSManager_Name_IBM-857");
        registerEncoding("IBM1026", "CP1026,csIBM1026,IBM-1026,1026", "Cp1026", "IBM1026", "Cp1026", strLangIDs, "IBM-1026 (BM Latin-5, Turkey)", "NLSManager_Name_IBM-1026");
        registerEncoding("windows-1254", "1254,Cp1254", "Cp1254", "windows-1254", "Cp1026", strLangIDs, "Turkish (Windows-1254)", "NLSManager_Name_Windows-1254");
        registerEncoding("ISO_8859-9:1989", "8859_9,8859-9,iso-ir-148,ISO_8859-9,ISO-8859-9,latin5,l5,csISOLatin5,IBM-920,IBM920,920,ISO8859_9", "ISO8859_9", "ISO-8859-9", "Cp1026", strLangIDs, "Turkish (ISO-8859-9)", "NLSManager_Name_ISO-8859-9");
        strLangIDs = "th";
        registerEncoding("windows-874", "IBM-874,IBM874,874,Cp874", "Cp874", "windows-874", "Cp037", strLangIDs, "Thailand (Windows)", "NLSManager_Name_Windows-874");
        registerEncoding("IBM-Thai", "csIBMThai,IBM-838,IBM838,838,Cp838", "Cp838", "IBM-Thai", "Cp037", strLangIDs, "IBM-838 (IBM Thai)", "NLSManager_Name_IBM-838");
        strLangIDs = "vi";
        registerEncoding("windows-1258", "1258,Cp1258", "Cp1258", "windows-1258", "Cp037", strLangIDs, "Vietnam (Windows-1258)", "NLSManager_Name_Windows-1258");
        strLangIDs = "ko";
        registerEncoding("KS_C_5601-1987", "iso-ir-149,KS_C_5601-1989,KSC_5601,korean,csKSC56011987,MS949,IBM-949,IBM949,949,IBM1363,1363", "MS949", "KS_C_5601-1987", "Cp933", strLangIDs, "Korean (KS_C_5601-1987)", "NLSManager_Name_Korean-KS");
        registerEncoding("EUC-KR", "csEUCKR,IBM-eucKR,eucKR,IBM-970,IBM970,970,5601", "EUC-KR", "EUC-KR", "Cp933", strLangIDs, "Korean (EUC)", "NLSManager_Name_Korean-EUC");
        registerEncoding("IBM933", "IBM-933,cp933,cpibm933,933", "Cp933", "IBM-933", "Cp933", strLangIDs, "Korea EBCDIC MIXED", "NLSManager_Name_IBM-933");
        strLangIDs = "cc,ch,zh";
        registerEncoding("GBK", "IBM1386,IBM-1386,1386,cp936,936", "GBK", "GBK", "Cp935", strLangIDs, "GBK, Simplified Chinese", "NLSManager_Name_IBM-1386");
        registerEncoding("Big5", "csBig5,MS950,950", "MS950", "Big5", "Cp935", strLangIDs, "Traditional Chinese (BIG5)", "NLSManager_Name_Chinese-BIG5");
        registerEncoding("GB2312", "csGB2312,IBM-1383,IBM1383,1383,eucCN,hp15CN,IBM-eucCN,EUC_CN,EUC-CN", "EUC_CN", "GB2312", "Cp935", strLangIDs, "Simplified Chinese (GB2312)", "NLSManager_Name_Chinese-GB2312");
        registerEncoding("IBM935", "IBM-935,cp935,cpibm935,935", "Cp935", "IBM-935", "Cp935", strLangIDs, "China EBCDIC MIXED", "NLSManager_Name_IBM-935");
        strLangIDs = "ja,jw";
        registerEncoding("MS932", "Cp943,943", "MS932", "MS932", "Cp930", strLangIDs, "Japanese (MS-932)", "NLSManager_Name_Japanese-MS932");
        registerEncoding("IBM-eucJP", "IBM-954,IBM954,954,eucJP,IBM33722,IBM-33722,33722,Cp33722", "Cp33722", "EUC-JP", "Cp930", strLangIDs, "EUC-JP - Japanese (superset of 5050)", "NLSManager_Name_IBM-33722");
        registerEncoding("Shift_JIS", "MS_Kanji,csShiftJIS,IBM-5039,IBM5039,5039,SJIS,Shift-JIS", "SJIS", "Shift_JIS", "Cp930", strLangIDs, "Japanese (Shift-JIS)", "NLSManager_Name_Japanese-Shift-JIS");
        registerEncoding("Extended_UNIX_Code_Packed_Format_for_Japanese", "csEUCPkdFmtJapanese,EUC-JP,EUC_JP", "EUC_JP", "EUC-JP", "Cp930", strLangIDs, "Japanese (EUC-JP)", "NLSManager_Name_Japanese-EUC-JP");
        registerEncoding("IBM930", "IBM-930,cp930,cpibm930,930", "Cp930", "IBM-930", "Cp930", strLangIDs, "Japan EBCDIC MIXED", "NLSManager_Name_IBM-930");
        registerEncoding("utf-16be", "UTF16_BigEndian,x-utf-16be,UnicodeBig", "UnicodeBig", "utf-16be", "Cp037", "", "Unicode (big endian)", "NLSManager_Name_Unicode-UCS2-BE");
        registerEncoding("utf-16le", "UCS2,UTF16_LittleEndian,x-utf-16le,UnicodeLittle", "UnicodeLittle", "utf-16le", "Cp037", "", "Unicode (little endian)", "NLSManager_Name_Unicode-UCS2-LE");
        registerEncoding("UTF-8", "UTF8,IBM-1208,IBM1208,1208", "UTF8", null, "Cp037", "", "Unicode (UTF-8)", "NLSManager_Name_Unicode-UTF-8");
        registerDefaultEncoding("en", "ISO-8859-1");
        registerDefaultEncoding("ru", "ISO-8859-5");
        registerDefaultEncoding("utf8", "UTF-8");
        registerSystemEncoding();
        registerDefaultHttpEncoding();
    }

    private static void registerSystemEncoding() {
        SYSTEM_ENCODING_NAME = System.getProperty("file.encoding", "");
        NLSEncodingData systemEncoding = getEncodingData(SYSTEM_ENCODING_NAME);
        if (systemEncoding == null) {
            String strEBCDICAnalogueName = SYSTEM_ENCODING_NAME;
            byte[] btarrTest = m_strCharactersToTest.getBytes();
            boolean bIsSupersetOfASCII = (btarrTest.length == m_strCharactersToTest.length());
            for (int iCount = btarrTest.length - 1; (iCount >= 0) && bIsSupersetOfASCII; iCount--) {
                bIsSupersetOfASCII = ((byte) m_strCharactersToTest.charAt(iCount)) == btarrTest[iCount];
            }
            if (bIsSupersetOfASCII || (btarrTest.length != m_strCharactersToTest.length())) {
                strEBCDICAnalogueName = "Cp037";
            }
            registerEncoding(SYSTEM_ENCODING_NAME, "JVM_DEFAULT", SYSTEM_ENCODING_NAME, SYSTEM_ENCODING_NAME, strEBCDICAnalogueName, "", "", null);
            systemEncoding = getEncodingData(SYSTEM_ENCODING_NAME);
        } else {
            SYSTEM_ENCODING_NAME = systemEncoding.getJavaEncodingName();
            NLSEncodingData encoding = getEncodingData("JVM_DEFAULT");
            if (encoding == systemEncoding) {
                return;
            }
            if (encoding != null) {
            }
            systemEncoding.addJvmDefaultAlias("JVM_DEFAULT");
            m_NameToEncodingMap.put("JVM_DEFAULT", systemEncoding);
        }
    }

    private static void registerEncoding(String strEncodingName, String strEncodingAliases, String strJavaName, String strHttpName, String strEBCDICVariant, String strLangIDs, String strDescription, String strLocalizedDescription) {
        try {
            NLSEncodingData encoding = new NLSEncodingData(strEncodingName, strEncodingAliases, strJavaName, strHttpName, strEBCDICVariant, strLangIDs, strDescription, strLocalizedDescription);
            StringTokenizer strtok = new StringTokenizer(encoding.getNames(), ", ");
            while (strtok.hasMoreElements()) {
                String strEncodingAlias = strtok.nextToken().toUpperCase();
                NLSEncodingData enc = (NLSEncodingData) m_NameToEncodingMap.get(strEncodingAlias);
                if (enc != null) {
                }
                m_NameToEncodingMap.put(strEncodingAlias, encoding);
            }
            strtok = new StringTokenizer(encoding.getLangIDs(), ", ");
            while (strtok.hasMoreElements()) {
                String strLangID = strtok.nextToken();
                Vector vEncodings = (Vector) m_LangIdToEncodingVectorMap.get(strLangID);
                if (vEncodings != null) {
                    vEncodings.addElement(encoding);
                } else {
                    vEncodings = new Vector();
                    vEncodings.addElement(encoding);
                    m_LangIdToEncodingVectorMap.put(strLangID, vEncodings);
                }
                registerDefaultEncoding(strLangID, encoding);
            }
            if ((strLocalizedDescription != null) && (strLocalizedDescription.length() > 0)) {
                int iIdx = 0;
                while ((iIdx < m_OrderedByDescriptionEncodingList.size()) && (((NLSEncodingData) m_OrderedByDescriptionEncodingList.elementAt(iIdx)).getDescription().compareTo(encoding.getDescription()) < 0)) {
                    iIdx++;
                }
                m_OrderedByDescriptionEncodingList.insertElementAt(encoding, iIdx);
            }
        } catch (UnsupportedEncodingException ex) {
            m_UnknownEncodings.addElement(strDescription + " (" + strEncodingName + ")");
        } catch (NoSuchFieldError error) {
            m_UnknownEncodings.addElement(strDescription + " (" + strEncodingName + ")");
        }
    }

    private static void registerDefaultEncoding(String strLangID, String strDefaultEncodingName) {
        registerDefaultEncoding(strLangID, getEncodingData(strDefaultEncodingName));
    }

    private static void registerDefaultEncoding(String strLangID, NLSEncodingData defaultEncoding) {
        if (defaultEncoding != null) {
            m_LangIdToDefaultEncodingMap.put(strLangID, defaultEncoding);
        }
    }

    private static void registerDefaultHttpEncoding() {
        NLSEncodingData nls_data = getEncodingData("ISO-8859-1");
        if (nls_data == null) {
            nls_data = getEncodingData("1252");
        }
        m_nlsdDefaultHttpEncoding = nls_data;
    }

    public static NLSEncodingData getDefaultEncoding(String strLangID) {
        return (NLSEncodingData) m_LangIdToDefaultEncodingMap.get(strLangID);
    }

    /**
 * Returns an encoding that is a superset of ASCII
 * @return NLSEncodingData
 */
    public static NLSEncodingData getDefaultHttpEncoding() {
        return m_nlsdDefaultHttpEncoding;
    }

    /**
 * Returns encoding data based on one of registered encoding names, if null is
 * passed the System-default encoding will be returned.
 * @param strEncodingName is one of known name of encoding or null for default.
 * @return NLSEncodingData is the found encoding or null if not found.
 */
    public static NLSEncodingData getEncodingData(String strEncodingName) {
        if (strEncodingName == null) {
            return getSystemJVMEncoding();
        }
        strEncodingName = strEncodingName.trim().toUpperCase();
        return (NLSEncodingData) m_NameToEncodingMap.get(strEncodingName);
    }

    /**
 * Resolves alias of encoding into java name of that encoding.
 * @param strEncodingName - name or alias of the encoding
 * @return String
 */
    public static String getJavaEncodingNameForEncoding(String strEncodingName) {
        NLSEncodingData encoding = getEncodingData(strEncodingName);
        if (encoding != null) {
            return encoding.getJavaEncodingName();
        } else {
            return null;
        }
    }

    /**
 * Returns enumeration of encodings ordered by descriptions
 */
    public static Enumeration getEncodingEnumerationByDescription() {
        return m_OrderedByDescriptionEncodingList.elements();
    }

    /**
 * Returns JVM system encoding (accordingly to "file.encoding" property)
 */
    public static NLSEncodingData getSystemJVMEncoding() {
        return getEncodingData(SYSTEM_ENCODING_NAME);
    }

    /**
 * Returns the name of the default JVM system encoding
 */
    public static String getSystemEncodingName() {
        return SYSTEM_ENCODING_NAME;
    }

    /** Returns an enumeration of names of encodings that 
 * failed to be registered */
    public static Enumeration getUnknownEncodingsEnumeration() {
        return m_UnknownEncodings.elements();
    }

    /**
 * Method resolves name of encoding (known to java) 
 * into java canonical name of the encoding 
 * (for example the only way to determine 
 * whether UTF16 and UTF16be are the same encodings
 * is to compare their canonical names);
 * Some encoding might be unknown to java, thus 
 * this is a good idea to pass encoding name 
 * through getJavaEncodingNameForEncoding() method first  
 */
    public static String getCanonicalJavaEncodingName(String strEncoding) {
        String strResult = (String) m_NameToCanonicalJavaEncodingName.get(strEncoding);
        if (strResult == null) {
            try {
                OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream(), strEncoding);
                strResult = writer.getEncoding();
            } catch (UnsupportedEncodingException ignored) {
                strResult = strEncoding;
            }
            m_NameToCanonicalJavaEncodingName.put(strEncoding, strResult);
        }
        return strResult;
    }

    /**
 * Determines by given encoding name the marked version of this encoding;
 * returns marked version name or source name if no marked version exists
 * (in fact this method is actual only for Unicode encodings)
 */
    public static String getMarkedUnicodeEncoding(String strEncoding) throws UnsupportedEncodingException {
        String strCanonicalEncodingName = NLSManager.getCanonicalJavaEncodingName(strEncoding);
        String strResult = strCanonicalEncodingName;
        if (strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UNICODE_BIG_UNMARKED))) {
            strResult = UNICODE_BIG;
        } else if (strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UNICODE_LITTLE_UNMARKED))) {
            strResult = UNICODE_LITTLE;
        } else if (strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UTF_16))) {
            strResult = UNICODE_BIG;
        }
        return strResult;
    }

    /**
 * Determines by given encoding name the un-marked version of this encoding;
 * returns marked version name or source name if no marked version exists
 * (in fact this method is actual only for Unicode encodings)
 */
    public static String getUnmarkedUnicodeEncoding(String strEncoding) throws UnsupportedEncodingException {
        String strCanonicalEncodingName = NLSManager.getCanonicalJavaEncodingName(strEncoding);
        String strResult = strCanonicalEncodingName;
        if (strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UNICODE_BIG))) {
            strResult = UNICODE_BIG_UNMARKED;
        } else if (strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UNICODE_LITTLE))) {
            strResult = UNICODE_LITTLE_UNMARKED;
        } else if (strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UTF_16))) {
            strResult = UNICODE_BIG_UNMARKED;
        }
        return strResult;
    }

    /**
 * Returns unicode marker for given encoding or null if not applicable 
 * (marker is applicable for two-bytes unicode encodings only
 */
    public static byte[] getUnicodeMarker(String strEncoding) throws UnsupportedEncodingException {
        byte[] btarrResult = null;
        String strCanonicalEncodingName = NLSManager.getCanonicalJavaEncodingName(strEncoding);
        if (strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UNICODE_BIG)) || strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UNICODE_LITTLE)) || strCanonicalEncodingName.equals(NLSManager.getCanonicalJavaEncodingName(UTF_16))) {
            btarrResult = " ".getBytes(strCanonicalEncodingName);
            btarrResult = ArrayUtils.subarray(btarrResult, 0, 2);
        }
        return btarrResult;
    }

    /**
 * Checks whether given encoding is present in JRE by creating output writer in
 * given encoding.
 * @param strEncodingName The name of an encoding
 */
    public static boolean isEncodingSupported(String strEncodingName) {
        try {
            checkEncodingSupported(strEncodingName);
            return true;
        } catch (UnsupportedEncodingException ex) {
            return false;
        }
    }

    /**
 * Checks whether given encoding is present in JRE by creating output writer in
 * given encoding. If encoding is not supported exception is thrown
 * @param strEncodingName The name of an encoding
 * @throws UnsupportedEncodingException
 */
    public static void checkEncodingSupported(String strEncodingName) throws UnsupportedEncodingException {
        String strResolvedName = getJavaEncodingNameForEncoding(strEncodingName);
        if (strResolvedName == null) {
            strResolvedName = strEncodingName;
        }
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new ByteArrayOutputStream(), strEncodingName);
        } catch (NullPointerException e) {
            DebugTracer.errPrintln(strEncodingName);
            throw new UnsupportedEncodingException(strEncodingName);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
