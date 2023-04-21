package org.geometerplus.zlibrary.core.language;

import java.util.*;
import org.geometerplus.zlibrary.core.filesystem.*;
import org.geometerplus.zlibrary.core.resources.ZLResource;

public abstract class ZLLanguageUtil {

    public static final String OTHER_LANGUAGE_CODE = "other";

    public static final String MULTI_LANGUAGE_CODE = "multi";

    private static ArrayList<String> ourLanguageCodes = new ArrayList<String>();

    private ZLLanguageUtil() {
    }

    public static class CodeComparator implements Comparator<String> {

        public int compare(String code0, String code1) {
            if (code0 == null) {
                return code1 == null ? 0 : -1;
            }
            if (code1 == null) {
                return 1;
            }
            if (code0.equals(code1)) {
                return 0;
            }
            if (MULTI_LANGUAGE_CODE.equals(code0)) {
                return 1;
            }
            if (MULTI_LANGUAGE_CODE.equals(code1)) {
                return -1;
            }
            if (OTHER_LANGUAGE_CODE.equals(code0)) {
                return 1;
            }
            if (OTHER_LANGUAGE_CODE.equals(code1)) {
                return -1;
            }
            return languageName(code0).compareTo(languageName(code1));
        }
    }

    public static List<String> languageCodes() {
        if (ourLanguageCodes.isEmpty()) {
            TreeSet<String> codes = new TreeSet<String>();
            for (ZLFile file : patternsFile().children()) {
                String name = file.getShortName();
                final int index = name.indexOf("_");
                if (index != -1) {
                    String str = name.substring(0, index);
                    if (!codes.contains(str)) {
                        codes.add(str);
                    }
                }
            }
            ourLanguageCodes.addAll(codes);
        }
        return Collections.unmodifiableList(ourLanguageCodes);
    }

    public static String languageName(String code) {
        return ZLResource.resource("language").getResource(code).getValue();
    }

    public static ZLFile patternsFile() {
        return ZLResourceFile.createResourceFile("languagePatterns");
    }

    public static String languageByIntCode(int languageCode, int subLanguageCode) {
        switch(languageCode) {
            default:
                return null;
            case 0x01:
                return "ar";
            case 0x02:
                return "bg";
            case 0x03:
                return "ca";
            case 0x04:
                return "zh";
            case 0x05:
                return "cs";
            case 0x06:
                return "da";
            case 0x07:
                return "de";
            case 0x08:
                return "el";
            case 0x09:
                return "en";
            case 0x0A:
                return "es";
            case 0x0B:
                return "fi";
            case 0x0C:
                return "fr";
            case 0x0D:
                return "he";
            case 0x0E:
                return "hu";
            case 0x0F:
                return "is";
            case 0x10:
                return "it";
            case 0x11:
                return "ja";
            case 0x12:
                return "ko";
            case 0x13:
                return "nl";
            case 0x14:
                return "no";
            case 0x15:
                return "pl";
            case 0x16:
                return "pt";
            case 0x17:
                return "rm";
            case 0x18:
                return "ro";
            case 0x19:
                return "ru";
            case 0x1A:
                switch(subLanguageCode) {
                    default:
                        return "sr";
                    case 0x04:
                    case 0x10:
                        return "hr";
                    case 0x14:
                    case 0x20:
                    case 0x78:
                        return "bs";
                }
            case 0x1B:
                return "sk";
            case 0x1C:
                return "sq";
            case 0x1D:
                return "sv";
            case 0x1E:
                return "th";
            case 0x1F:
                return "tr";
            case 0x20:
                return "ur";
            case 0x21:
                return "id";
            case 0x22:
                return "uk";
            case 0x23:
                return "be";
            case 0x24:
                return "sl";
            case 0x25:
                return "et";
            case 0x26:
                return "lv";
            case 0x27:
                return "lt";
            case 0x28:
                return "tg";
            case 0x29:
                return "fa";
            case 0x2A:
                return "vi";
            case 0x2B:
                return "hy";
            case 0x2C:
                return "az";
            case 0x2D:
                return "eu";
            case 0x2E:
                return (subLanguageCode == 0x08) ? "dsb" : "wen";
            case 0x2F:
                return "mk";
            case 0x32:
                return "tn";
            case 0x34:
                return "xh";
            case 0x35:
                return "zu";
            case 0x36:
                return "af";
            case 0x37:
                return "ka";
            case 0x38:
                return "fo";
            case 0x39:
                return "hi";
            case 0x3A:
                return "mt";
            case 0x3B:
                return "se";
            case 0x3C:
                return "ga";
            case 0x3E:
                return "ms";
            case 0x3F:
                return "kk";
            case 0x40:
                return "ky";
            case 0x41:
                return "sw";
            case 0x42:
                return "tk";
            case 0x43:
                return "uz";
            case 0x44:
                return "tt";
            case 0x45:
                return "bn";
            case 0x46:
                return "pa";
            case 0x47:
                return "gu";
            case 0x48:
                return "or";
            case 0x49:
                return "ta";
            case 0x4A:
                return "te";
            case 0x4B:
                return "kn";
            case 0x4C:
                return "ml";
            case 0x4D:
                return "as";
            case 0x4E:
                return "mr";
            case 0x4F:
                return "sa";
            case 0x50:
                return "mn";
            case 0x51:
                return "bo";
            case 0x52:
                return "cy";
            case 0x53:
                return "kh";
            case 0x54:
                return "lo";
            case 0x56:
                return "gl";
            case 0x57:
                return "kok";
            case 0x58:
                return "mni";
            case 0x59:
                return "sd";
            case 0x5A:
                return "syr";
            case 0x5B:
                return "si";
            case 0x5D:
                return "iu";
            case 0x5E:
                return "am";
            case 0x5F:
                return "tzm";
            case 0x60:
                return "ks";
            case 0x61:
                return "ne";
            case 0x62:
                return "fy";
            case 0x63:
                return "ps";
            case 0x64:
                return "fil";
            case 0x65:
                return "dv";
            case 0x68:
                return "ha";
            case 0x6A:
                return "yo";
            case 0x6B:
                return "quz";
            case 0x6C:
                return "ns";
            case 0x6D:
                return "ba";
            case 0x6E:
                return "lb";
            case 0x6F:
                return "kl";
            case 0x70:
                return "ig";
            case 0x73:
                return "ti";
            case 0x78:
                return "yi";
            case 0x7A:
                return "arn";
            case 0x7C:
                return "moh";
            case 0x7E:
                return "be";
            case 0x80:
                return "ug";
            case 0x81:
                return "mi";
            case 0x82:
                return "oc";
            case 0x83:
                return "co";
            case 0x84:
                return "gsw";
            case 0x85:
                return "sah";
            case 0x86:
                return "qut";
            case 0x87:
                return "rw";
            case 0x88:
                return "wo";
            case 0x8C:
                return "prs";
            case 0x8D:
                return "mg";
        }
    }
}
