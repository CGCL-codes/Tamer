package org.monet.kernel.library;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.StringTokenizer;
import net.sf.json.JSONSerializer;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.exceptions.SystemException;
import com.twmacinta.util.MD5;

public class LibraryString {

    public static class DummyValue {

        private String sValue;

        public DummyValue(String sValue) {
            this.sValue = sValue;
        }

        public String getValue() {
            return this.sValue;
        }
    }

    public static String replaceSpecialChars(String sData, String sReplacement) {
        sData = sData.replaceAll("\n", sReplacement);
        sData = sData.replaceAll("\r", sReplacement);
        sData = sData.replaceAll("\t", sReplacement);
        return sData;
    }

    public static String cleanSpecialChars(String sData) {
        return LibraryString.replaceSpecialChars(sData, "");
    }

    public static String toJSON(String sData) {
        DummyValue oValue = new LibraryString.DummyValue(sData);
        String sResult = JSONSerializer.toJSON(oValue).toString();
        return sResult.substring(10, sResult.length() - 2);
    }

    public static String capitalize(String sContent) {
        char[] aChars;
        aChars = sContent.toCharArray();
        aChars[0] = Character.toUpperCase(aChars[0]);
        return new String(aChars);
    }

    public static String toMD5(String sContent) {
        MD5 md5 = new MD5();
        try {
            md5.Update(sContent, null);
        } catch (UnsupportedEncodingException oException) {
            throw new SystemException("Converting to MD5", Strings.EMPTY, oException);
        }
        return md5.asHex();
    }

    public static String cleanAccents(String sData) {
        sData = sData.replaceAll("á", "a");
        sData = sData.replaceAll("à", "a");
        sData = sData.replaceAll("â", "a");
        sData = sData.replaceAll("ã", "a");
        sData = sData.replaceAll("ª", "a");
        sData = sData.replaceAll("Á", "A");
        sData = sData.replaceAll("À", "A");
        sData = sData.replaceAll("Â", "A");
        sData = sData.replaceAll("Ã", "A");
        sData = sData.replaceAll("é", "e");
        sData = sData.replaceAll("è", "e");
        sData = sData.replaceAll("ê", "e");
        sData = sData.replaceAll("É", "E");
        sData = sData.replaceAll("È", "E");
        sData = sData.replaceAll("Ê", "E");
        sData = sData.replaceAll("ó", "o");
        sData = sData.replaceAll("ò", "o");
        sData = sData.replaceAll("ô", "o");
        sData = sData.replaceAll("õ", "o");
        sData = sData.replaceAll("º", "o");
        sData = sData.replaceAll("Ó", "O");
        sData = sData.replaceAll("Ò", "O");
        sData = sData.replaceAll("Ô", "O");
        sData = sData.replaceAll("Õ", "O");
        sData = sData.replaceAll("ú", "u");
        sData = sData.replaceAll("ù", "u");
        sData = sData.replaceAll("û", "u");
        sData = sData.replaceAll("Ú", "U");
        sData = sData.replaceAll("Ù", "U");
        sData = sData.replaceAll("Û", "U");
        sData = sData.replaceAll("í", "i");
        sData = sData.replaceAll("ì", "i");
        sData = sData.replaceAll("î", "i");
        sData = sData.replaceAll("Í", "I");
        sData = sData.replaceAll("Ì", "I");
        sData = sData.replaceAll("Î", "I");
        sData = sData.replaceAll("ç", "c");
        sData = sData.replaceAll("Ç", "C");
        sData = sData.replaceAll("ñ", "n");
        sData = sData.replaceAll("Ñ", "N");
        return sData;
    }

    public static String removeBrackets(String sData) {
        sData = sData.replaceAll("\\[", "");
        sData = sData.replaceAll("\\]", "");
        return sData;
    }

    public static String cleanString(String sContent, String sAlphabet) {
        String sAux = sContent;
        for (int iPos = 0; iPos < sContent.length(); iPos++) {
            String sChar = sContent.substring(iPos, iPos + 1);
            if (sAlphabet.indexOf(sChar.toLowerCase()) == -1) sAux = sAux.replace(sChar, Strings.EMPTY);
        }
        return sAux;
    }

    public static String cleanString(String sContent) {
        return LibraryString.cleanString(sContent, "abcdefghijklmnopqrstuvwxyz1234567890");
    }

    public static String[] getKeywords(String sData, Integer iMinLength) {
        HashMap<String, String> hmKeywords = new HashMap<String, String>();
        StringTokenizer tok = new StringTokenizer(sData);
        while (tok.hasMoreTokens()) {
            String sToken = tok.nextToken();
            sToken = LibraryString.cleanString(sToken, "abcdefghijklmnopqrstuvwxyz1234567890");
            if ((iMinLength != null) && (sToken.length() <= iMinLength)) continue;
            hmKeywords.put(sToken.toLowerCase(), sToken.toLowerCase());
        }
        return hmKeywords.keySet().toArray(new String[0]);
    }

    public static String[] getKeywordsWithEmpty(String data) {
        String[] keyWordsArray = LibraryString.getKeywords(data, null);
        if (keyWordsArray.length <= 0) {
            keyWordsArray = new String[1];
            keyWordsArray[0] = Strings.EMPTY;
        }
        return keyWordsArray;
    }

    public static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new DataException("Calculating leivenstein distance", s + Strings.SPACE + t);
        }
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }
        int p[] = new int[n + 1];
        int d[] = new int[n + 1];
        int _d[];
        int i, j, t_j, cost;
        for (i = 0; i <= n; i++) {
            p[i] = i;
        }
        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;
            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }
            _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }

    public static String replaceAll(String sContent, String sValue, String sReplacement) {
        Integer iPos = sContent.indexOf(sValue);
        String sLeftSide, sRightSide;
        while (iPos != -1) {
            sLeftSide = sContent.substring(0, iPos);
            sRightSide = sContent.substring(iPos + sValue.length());
            sContent = sLeftSide + sReplacement + sRightSide;
            sContent.substring(0, iPos);
            iPos = sContent.indexOf(sValue);
        }
        return sContent;
    }

    public static String generatePassword() {
        String sPassword = "";
        String[] aSyllables = { "er", "in", "tia", "wol", "fe", "pre", "vet", "jo", "nes", "al", "len", "son", "cha", "ir", "ler", "bo", "ok", "tio", "nar", "sim", "ple", "bla", "ten", "toe", "cho", "co", "lat", "spe", "ak", "er", "po", "co", "lor", "pen", "cil", "li", "ght", "wh", "at", "the", "he", "ck", "is", "mam", "bo", "no", "fi", "ve", "any", "way", "pol", "iti", "cs", "ra", "dio", "sou", "rce", "sea", "rch", "pa", "per", "com", "bo", "sp", "eak", "st", "fi", "rst", "gr", "oup", "boy", "ea", "gle", "tr", "ail", "bi", "ble", "brb", "pri", "dee", "kay", "en", "be", "se" };
        Integer iCount;
        for (iCount = 1; iCount <= 4; iCount++) {
            if (Math.random() % 10 == 1) sPassword += Integer.toString(Math.round(Math.round(Math.random() % 50) + 1)); else sPassword += aSyllables[Math.round(Math.round(Math.random() % 62))];
        }
        return sPassword;
    }

    public static String implodeAndWrap(String[] aObject, String sDelimiter, String sWrapper) {
        String sResult = "";
        for (int iPos = 0; iPos < aObject.length; iPos++) {
            if (iPos != 0) {
                sResult += sDelimiter;
            }
            sResult += (sWrapper != null) ? sWrapper : "";
            sResult += aObject[iPos];
            sResult += (sWrapper != null) ? sWrapper : "";
        }
        return sResult;
    }

    public static String toJavaIdentifier(String aString) {
        StringBuffer res = new StringBuffer();
        int idx = 0;
        char c = aString.charAt(idx);
        while (idx < aString.length()) {
            c = aString.charAt(idx++);
            if (Character.isJavaIdentifierPart(c)) res.append(c);
        }
        return res.toString();
    }
}
