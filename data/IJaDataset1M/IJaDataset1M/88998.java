package br.com.caelum.tubaina.util;

import java.util.HashMap;
import java.util.Map;

public class HtmlSanitizer {

    private Map<Character, String> map;

    public HtmlSanitizer() {
        map = new HashMap<Character, String>();
        map.put('À', "&Agrave;");
        map.put('Á', "&Aacute;");
        map.put('Â', "&Acirc;");
        map.put('Ã', "&Atilde;");
        map.put('Ä', "&Auml;");
        map.put('Å', "&Aring;");
        map.put('Æ', "&AElig;");
        map.put('Ç', "&Ccedil;");
        map.put('È', "&Egrave;");
        map.put('É', "&Eacute;");
        map.put('Ê', "&Ecirc;");
        map.put('Ë', "&Euml;");
        map.put('Ì', "&Igrave;");
        map.put('Í', "&Iacute;");
        map.put('Î', "&Icirc;");
        map.put('Ï', "&Iuml;");
        map.put('Ð', "&#272;");
        map.put('Ñ', "&Ntilde;");
        map.put('Ò', "&Ograve;");
        map.put('Ó', "&Oacute;");
        map.put('Ô', "&Ocirc;");
        map.put('Õ', "&Otilde;");
        map.put('Ö', "&Ouml;");
        map.put('Ø', "&Oslash;");
        map.put('Œ', "&OElig;");
        map.put('Þ', "&THORN;");
        map.put('Ù', "&Ugrave;");
        map.put('Ú', "&Uacute;");
        map.put('Û', "&Ucirc;");
        map.put('Ü', "&Uuml;");
        map.put('Ý', "&Yacute;");
        map.put('Ÿ', "&Ycirc;");
        map.put('à', "&agrave;");
        map.put('á', "&aacute;");
        map.put('â', "&acirc;");
        map.put('ã', "&atilde;");
        map.put('ä', "&auml;");
        map.put('å', "&aring;");
        map.put('æ', "&aelig;");
        map.put('ç', "&ccedil;");
        map.put('è', "&egrave;");
        map.put('é', "&eacute;");
        map.put('ê', "&ecirc;");
        map.put('ë', "&euml;");
        map.put('ì', "&igrave;");
        map.put('í', "&iacute;");
        map.put('î', "&icirc;");
        map.put('ï', "&iuml;");
        map.put('ð', "&eth;");
        map.put('ñ', "&ntilde;");
        map.put('ò', "&ograve;");
        map.put('ó', "&oacute;");
        map.put('ô', "&ocirc;");
        map.put('õ', "&otilde;");
        map.put('ö', "&ouml;");
        map.put('ø', "&oslash;");
        map.put('œ', "&oelig;");
        map.put('ß', "&szlig;");
        map.put('þ', "&thorn;");
        map.put('ù', "&ugrave;");
        map.put('ú', "&uacute;");
        map.put('û', "&ucirc;");
        map.put('ü', "&uuml;");
        map.put('ý', "&yacute;");
        map.put('ÿ', "&yuml;");
        map.put('<', "&lt;");
        map.put('>', "&gt;");
        map.put('&', "&amp;");
    }

    public String sanitize(String text) {
        final StringBuilder sane = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '<' && i < text.length() - 2) {
                String next = text.substring(i + 1, i + 3);
                if (next.equals("::")) {
                    continue;
                }
            }
            if (map.containsKey(current)) {
                sane.append(map.get(current));
            } else {
                sane.append(current);
            }
        }
        return sane.toString();
    }
}
