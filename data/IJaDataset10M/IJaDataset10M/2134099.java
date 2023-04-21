package org.torweg.pulse.site.content.suffices;

import java.util.Map;
import java.util.TreeMap;

/**
 * converts Turkish special characters {@code ç ğ ı İ ö ş ü} to URL safe
 * characters.
 * 
 * @author Thomas Weber
 * @version $Revision: 1402 $
 */
public final class TurkishConverter implements SuffixConverter {

    /**
	 * the list of substitutions for the special characters.
	 */
    private static Map<String, String> substitutions = new TreeMap<String, String>();

    static {
        substitutions.put("ç", "c");
        substitutions.put("ğ", "g");
        substitutions.put("ı", "i");
        substitutions.put("İ", "I");
        substitutions.put("ö", "o");
        substitutions.put("ş", "s");
        substitutions.put("ü", "u");
    }

    /**
	 * converts the given lower case string.
	 * 
	 * @param s
	 *            the string to convert
	 * @return the converted string
	 * @see org.torweg.pulse.site.content.suffices.SuffixConverter#convert(java.lang.String)
	 */
    public String convert(final String s) {
        StringBuilder converted = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i + 1);
            if (substitutions.containsKey(c)) {
                converted.append(substitutions.get(c));
            } else {
                converted.append(c);
            }
        }
        return converted.toString();
    }
}
