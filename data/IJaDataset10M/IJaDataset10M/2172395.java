package br.ufmg.catustec.arangi.commons;

import java.util.List;

/**
 * 
 * @author Cesar Correia
 *
 */
public class StringHelper {

    public static final String[] EMPTY_STRING_VECTOR = new String[] {};

    public static final Object[] EMPTY_OBJECT_VECTOR = new Object[] {};

    public static final String NO = "N";

    public static final String YES = "Y";

    public static final String SPACE = " ";

    public static final String BREAK_LINE = "\n";

    public static String removeDuplicatedSpaces(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        while (str.indexOf("  ") >= 0) {
            str = str.replaceAll("  ", " ");
        }
        return str;
    }

    public static String removeSpaces(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        while (str.indexOf(" ") >= 0) {
            str = str.replaceAll(" ", "");
        }
        return str;
    }

    public static String removeLineBreaks(String str) {
        str = str.replaceAll("\r", " ");
        str = str.replaceAll("\n", " ");
        return str;
    }

    public static String preprocessQuotesToHTML(String str) {
        str = str.replaceAll("'", "\\\\'");
        str = str.replaceAll("\"", "\\\\\"");
        return str;
    }

    public static String splitArrayInStringObjectsSeparetedByComma(List list) {
        if (list == null) {
            return null;
        } else if (list.size() == 0) {
            return "";
        }
        StringBuffer stringList = new StringBuffer();
        for (Object object : list) {
            if (object instanceof String) {
                object = "'" + object + "'";
                stringList.append("'");
                stringList.append(object);
                stringList.append("'");
            } else {
                stringList.append(object.toString());
            }
            stringList.append(",");
        }
        String aux = stringList.substring(0, stringList.length() - 1);
        return aux;
    }

    public static String removeTagsHTML(String str) {
        if (str == null) {
            return "";
        }
        str = str.trim().replaceAll("<ul>", "").replaceAll("<li>", "").replaceAll("</li>", "").replaceAll("</ul>", "").replaceAll("<br>", "");
        return str;
    }

    /**
	 * This method replaces all accents in a word by their respective non-accent
	 * character.
	 * @param text Input text
	 * @return Text without accents
	 */
    public static String convertToAscii(String text) {
        return text.replaceAll("[àáâãä]", "a").replaceAll("[èéêë]", "e").replaceAll("[ìíîï]", "i").replaceAll("[òóôõö]", "o").replaceAll("[ùúûü]", "u").replaceAll("[ÀÁÂÃÄ]", "A").replaceAll("[ÈÉÊË]", "E").replaceAll("[ÌÍÎÏ]", "I").replaceAll("[ÒÓÔÕÖ]", "O").replaceAll("[ÙÚÛÜ]", "U").replaceAll("ç", "c").replaceAll("Ç", "C").replaceAll("ñ", "n").replaceAll("Ñ", "N");
    }
}
