package org.owasp.esapi.codecs;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

/**
 * Implementation of the Codec interface for HTML entity encoding.
 * 
 * @author Jeff Williams (jeff.williams .at. aspectsecurity.com) <a
 *         href="http://www.aspectsecurity.com">Aspect Security</a>
 * @since June 1, 2007
 * @see org.owasp.esapi.Encoder
 */
public class HTMLEntityCodec extends Codec {

    private static final char REPLACEMENT_CHAR = '�';

    private static final String REPLACEMENT_HEX = "fffd";

    private static final String REPLACEMENT_STR = "" + REPLACEMENT_CHAR;

    private static final Map<Character, String> characterToEntityMap = mkCharacterToEntityMap();

    private static final Trie<Character> entityToCharacterTrie = mkEntityToCharacterTrie();

    /**
     *
     */
    public HTMLEntityCodec() {
    }

    /**
	 * {@inheritDoc}
	 * 
     * Encodes a Character for safe use in an HTML entity field.
     * @param immune
     */
    public String encodeCharacter(char[] immune, Character c) {
        if (containsCharacter(c, immune)) {
            return "" + c;
        }
        String hex = Codec.getHexForNonAlphanumeric(c);
        if (hex == null) {
            return "" + c;
        }
        if ((c <= 0x1f && c != '\t' && c != '\n' && c != '\r') || (c >= 0x7f && c <= 0x9f)) {
            hex = REPLACEMENT_HEX;
            c = REPLACEMENT_CHAR;
        }
        String entityName = (String) characterToEntityMap.get(c);
        if (entityName != null) {
            return "&" + entityName + ";";
        }
        return "&#x" + hex + ";";
    }

    /**
	 * {@inheritDoc}
	 * 
	 * Returns the decoded version of the character starting at index, or
	 * null if no decoding is possible.
	 * 
	 * Formats all are legal both with and without semi-colon, upper/lower case:
	 *   &#dddd;
	 *   &#xhhhh;
	 *   &name;
	 */
    public Character decodeCharacter(PushbackString input) {
        input.mark();
        Character first = input.next();
        if (first == null) {
            input.reset();
            return null;
        }
        if (first != '&') {
            input.reset();
            return null;
        }
        Character second = input.next();
        if (second == null) {
            input.reset();
            return null;
        }
        if (second == '#') {
            Character c = getNumericEntity(input);
            if (c != null) return c;
        } else if (Character.isLetter(second.charValue())) {
            input.pushback(second);
            Character c = getNamedEntity(input);
            if (c != null) return c;
        }
        input.reset();
        return null;
    }

    /**
	 * getNumericEntry checks input to see if it is a numeric entity
	 * 
	 * @param input
	 * 			The input to test for being a numeric entity
	 *  
	 * @return
	 * 			null if input is null, the character of input after decoding
	 */
    private Character getNumericEntity(PushbackString input) {
        Character first = input.peek();
        if (first == null) return null;
        if (first == 'x' || first == 'X') {
            input.next();
            return parseHex(input);
        }
        return parseNumber(input);
    }

    /**
	 * Parse a decimal number, such as those from JavaScript's String.fromCharCode(value)
	 * 
	 * @param input
	 * 			decimal encoded string, such as 65
	 * @return
	 * 			character representation of this decimal value, e.g. A 
	 * @throws NumberFormatException
	 */
    private Character parseNumber(PushbackString input) {
        StringBuilder sb = new StringBuilder();
        while (input.hasNext()) {
            Character c = input.peek();
            if (Character.isDigit(c.charValue())) {
                sb.append(c);
                input.next();
            } else if (c == ';') {
                input.next();
                break;
            } else {
                break;
            }
        }
        try {
            int i = Integer.parseInt(sb.toString());
            if (Character.isValidCodePoint(i)) {
                return (char) i;
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
	 * Parse a hex encoded entity
	 * 
	 * @param input
	 * 			Hex encoded input (such as 437ae;)
	 * @return
	 * 			A single character from the string
	 * @throws NumberFormatException
	 */
    private Character parseHex(PushbackString input) {
        StringBuilder sb = new StringBuilder();
        while (input.hasNext()) {
            Character c = input.peek();
            if ("0123456789ABCDEFabcdef".indexOf(c) != -1) {
                sb.append(c);
                input.next();
            } else if (c == ';') {
                input.next();
                break;
            } else {
                break;
            }
        }
        try {
            int i = Integer.parseInt(sb.toString(), 16);
            if (Character.isValidCodePoint(i)) {
                return (char) i;
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
	 * 
	 * Returns the decoded version of the character starting at index, or
	 * null if no decoding is possible.
	 * 
	 * Formats all are legal both with and without semi-colon, upper/lower case:
	 *   &aa;
	 *   &aaa;
	 *   &aaaa;
	 *   &aaaaa;
	 *   &aaaaaa;
	 *   &aaaaaaa;
	 *
	 * @param input
	 * 		A string containing a named entity like &quot;
	 * @return
	 * 		Returns the decoded version of the character starting at index, or null if no decoding is possible.
	 */
    private Character getNamedEntity(PushbackString input) {
        StringBuilder possible = new StringBuilder();
        Map.Entry<CharSequence, Character> entry;
        int len;
        len = Math.min(input.remainder().length(), entityToCharacterTrie.getMaxKeyLength());
        for (int i = 0; i < len; i++) possible.append(Character.toLowerCase(input.next()));
        entry = entityToCharacterTrie.getLongestMatch(possible);
        if (entry == null) return null;
        input.reset();
        input.next();
        len = entry.getKey().length();
        for (int i = 0; i < len; i++) input.next();
        if (input.peek(';')) input.next();
        return entry.getValue();
    }

    /**
	 * Build a unmodifiable Map from entity Character to Name.
	 * @return Unmodifiable map.
	 */
    private static synchronized Map<Character, String> mkCharacterToEntityMap() {
        Map<Character, String> map = new HashMap<Character, String>(252);
        map.put((char) 34, "quot");
        map.put((char) 38, "amp");
        map.put((char) 60, "lt");
        map.put((char) 62, "gt");
        map.put((char) 160, "nbsp");
        map.put((char) 161, "iexcl");
        map.put((char) 162, "cent");
        map.put((char) 163, "pound");
        map.put((char) 164, "curren");
        map.put((char) 165, "yen");
        map.put((char) 166, "brvbar");
        map.put((char) 167, "sect");
        map.put((char) 168, "uml");
        map.put((char) 169, "copy");
        map.put((char) 170, "ordf");
        map.put((char) 171, "laquo");
        map.put((char) 172, "not");
        map.put((char) 173, "shy");
        map.put((char) 174, "reg");
        map.put((char) 175, "macr");
        map.put((char) 176, "deg");
        map.put((char) 177, "plusmn");
        map.put((char) 178, "sup2");
        map.put((char) 179, "sup3");
        map.put((char) 180, "acute");
        map.put((char) 181, "micro");
        map.put((char) 182, "para");
        map.put((char) 183, "middot");
        map.put((char) 184, "cedil");
        map.put((char) 185, "sup1");
        map.put((char) 186, "ordm");
        map.put((char) 187, "raquo");
        map.put((char) 188, "frac14");
        map.put((char) 189, "frac12");
        map.put((char) 190, "frac34");
        map.put((char) 191, "iquest");
        map.put((char) 192, "Agrave");
        map.put((char) 193, "Aacute");
        map.put((char) 194, "Acirc");
        map.put((char) 195, "Atilde");
        map.put((char) 196, "Auml");
        map.put((char) 197, "Aring");
        map.put((char) 198, "AElig");
        map.put((char) 199, "Ccedil");
        map.put((char) 200, "Egrave");
        map.put((char) 201, "Eacute");
        map.put((char) 202, "Ecirc");
        map.put((char) 203, "Euml");
        map.put((char) 204, "Igrave");
        map.put((char) 205, "Iacute");
        map.put((char) 206, "Icirc");
        map.put((char) 207, "Iuml");
        map.put((char) 208, "ETH");
        map.put((char) 209, "Ntilde");
        map.put((char) 210, "Ograve");
        map.put((char) 211, "Oacute");
        map.put((char) 212, "Ocirc");
        map.put((char) 213, "Otilde");
        map.put((char) 214, "Ouml");
        map.put((char) 215, "times");
        map.put((char) 216, "Oslash");
        map.put((char) 217, "Ugrave");
        map.put((char) 218, "Uacute");
        map.put((char) 219, "Ucirc");
        map.put((char) 220, "Uuml");
        map.put((char) 221, "Yacute");
        map.put((char) 222, "THORN");
        map.put((char) 223, "szlig");
        map.put((char) 224, "agrave");
        map.put((char) 225, "aacute");
        map.put((char) 226, "acirc");
        map.put((char) 227, "atilde");
        map.put((char) 228, "auml");
        map.put((char) 229, "aring");
        map.put((char) 230, "aelig");
        map.put((char) 231, "ccedil");
        map.put((char) 232, "egrave");
        map.put((char) 233, "eacute");
        map.put((char) 234, "ecirc");
        map.put((char) 235, "euml");
        map.put((char) 236, "igrave");
        map.put((char) 237, "iacute");
        map.put((char) 238, "icirc");
        map.put((char) 239, "iuml");
        map.put((char) 240, "eth");
        map.put((char) 241, "ntilde");
        map.put((char) 242, "ograve");
        map.put((char) 243, "oacute");
        map.put((char) 244, "ocirc");
        map.put((char) 245, "otilde");
        map.put((char) 246, "ouml");
        map.put((char) 247, "divide");
        map.put((char) 248, "oslash");
        map.put((char) 249, "ugrave");
        map.put((char) 250, "uacute");
        map.put((char) 251, "ucirc");
        map.put((char) 252, "uuml");
        map.put((char) 253, "yacute");
        map.put((char) 254, "thorn");
        map.put((char) 255, "yuml");
        map.put((char) 338, "OElig");
        map.put((char) 339, "oelig");
        map.put((char) 352, "Scaron");
        map.put((char) 353, "scaron");
        map.put((char) 376, "Yuml");
        map.put((char) 402, "fnof");
        map.put((char) 710, "circ");
        map.put((char) 732, "tilde");
        map.put((char) 913, "Alpha");
        map.put((char) 914, "Beta");
        map.put((char) 915, "Gamma");
        map.put((char) 916, "Delta");
        map.put((char) 917, "Epsilon");
        map.put((char) 918, "Zeta");
        map.put((char) 919, "Eta");
        map.put((char) 920, "Theta");
        map.put((char) 921, "Iota");
        map.put((char) 922, "Kappa");
        map.put((char) 923, "Lambda");
        map.put((char) 924, "Mu");
        map.put((char) 925, "Nu");
        map.put((char) 926, "Xi");
        map.put((char) 927, "Omicron");
        map.put((char) 928, "Pi");
        map.put((char) 929, "Rho");
        map.put((char) 931, "Sigma");
        map.put((char) 932, "Tau");
        map.put((char) 933, "Upsilon");
        map.put((char) 934, "Phi");
        map.put((char) 935, "Chi");
        map.put((char) 936, "Psi");
        map.put((char) 937, "Omega");
        map.put((char) 945, "alpha");
        map.put((char) 946, "beta");
        map.put((char) 947, "gamma");
        map.put((char) 948, "delta");
        map.put((char) 949, "epsilon");
        map.put((char) 950, "zeta");
        map.put((char) 951, "eta");
        map.put((char) 952, "theta");
        map.put((char) 953, "iota");
        map.put((char) 954, "kappa");
        map.put((char) 955, "lambda");
        map.put((char) 956, "mu");
        map.put((char) 957, "nu");
        map.put((char) 958, "xi");
        map.put((char) 959, "omicron");
        map.put((char) 960, "pi");
        map.put((char) 961, "rho");
        map.put((char) 962, "sigmaf");
        map.put((char) 963, "sigma");
        map.put((char) 964, "tau");
        map.put((char) 965, "upsilon");
        map.put((char) 966, "phi");
        map.put((char) 967, "chi");
        map.put((char) 968, "psi");
        map.put((char) 969, "omega");
        map.put((char) 977, "thetasym");
        map.put((char) 978, "upsih");
        map.put((char) 982, "piv");
        map.put((char) 8194, "ensp");
        map.put((char) 8195, "emsp");
        map.put((char) 8201, "thinsp");
        map.put((char) 8204, "zwnj");
        map.put((char) 8205, "zwj");
        map.put((char) 8206, "lrm");
        map.put((char) 8207, "rlm");
        map.put((char) 8211, "ndash");
        map.put((char) 8212, "mdash");
        map.put((char) 8216, "lsquo");
        map.put((char) 8217, "rsquo");
        map.put((char) 8218, "sbquo");
        map.put((char) 8220, "ldquo");
        map.put((char) 8221, "rdquo");
        map.put((char) 8222, "bdquo");
        map.put((char) 8224, "dagger");
        map.put((char) 8225, "Dagger");
        map.put((char) 8226, "bull");
        map.put((char) 8230, "hellip");
        map.put((char) 8240, "permil");
        map.put((char) 8242, "prime");
        map.put((char) 8243, "Prime");
        map.put((char) 8249, "lsaquo");
        map.put((char) 8250, "rsaquo");
        map.put((char) 8254, "oline");
        map.put((char) 8260, "frasl");
        map.put((char) 8364, "euro");
        map.put((char) 8465, "image");
        map.put((char) 8472, "weierp");
        map.put((char) 8476, "real");
        map.put((char) 8482, "trade");
        map.put((char) 8501, "alefsym");
        map.put((char) 8592, "larr");
        map.put((char) 8593, "uarr");
        map.put((char) 8594, "rarr");
        map.put((char) 8595, "darr");
        map.put((char) 8596, "harr");
        map.put((char) 8629, "crarr");
        map.put((char) 8656, "lArr");
        map.put((char) 8657, "uArr");
        map.put((char) 8658, "rArr");
        map.put((char) 8659, "dArr");
        map.put((char) 8660, "hArr");
        map.put((char) 8704, "forall");
        map.put((char) 8706, "part");
        map.put((char) 8707, "exist");
        map.put((char) 8709, "empty");
        map.put((char) 8711, "nabla");
        map.put((char) 8712, "isin");
        map.put((char) 8713, "notin");
        map.put((char) 8715, "ni");
        map.put((char) 8719, "prod");
        map.put((char) 8721, "sum");
        map.put((char) 8722, "minus");
        map.put((char) 8727, "lowast");
        map.put((char) 8730, "radic");
        map.put((char) 8733, "prop");
        map.put((char) 8734, "infin");
        map.put((char) 8736, "ang");
        map.put((char) 8743, "and");
        map.put((char) 8744, "or");
        map.put((char) 8745, "cap");
        map.put((char) 8746, "cup");
        map.put((char) 8747, "int");
        map.put((char) 8756, "there4");
        map.put((char) 8764, "sim");
        map.put((char) 8773, "cong");
        map.put((char) 8776, "asymp");
        map.put((char) 8800, "ne");
        map.put((char) 8801, "equiv");
        map.put((char) 8804, "le");
        map.put((char) 8805, "ge");
        map.put((char) 8834, "sub");
        map.put((char) 8835, "sup");
        map.put((char) 8836, "nsub");
        map.put((char) 8838, "sube");
        map.put((char) 8839, "supe");
        map.put((char) 8853, "oplus");
        map.put((char) 8855, "otimes");
        map.put((char) 8869, "perp");
        map.put((char) 8901, "sdot");
        map.put((char) 8968, "lceil");
        map.put((char) 8969, "rceil");
        map.put((char) 8970, "lfloor");
        map.put((char) 8971, "rfloor");
        map.put((char) 9001, "lang");
        map.put((char) 9002, "rang");
        map.put((char) 9674, "loz");
        map.put((char) 9824, "spades");
        map.put((char) 9827, "clubs");
        map.put((char) 9829, "hearts");
        map.put((char) 9830, "diams");
        return Collections.unmodifiableMap(map);
    }

    /**
	 * Build a unmodifiable Trie from entitiy Name to Character
	 * @return Unmodifiable trie.
	 */
    private static synchronized Trie<Character> mkEntityToCharacterTrie() {
        Trie<Character> trie = new HashTrie<Character>();
        for (Map.Entry<Character, String> entry : characterToEntityMap.entrySet()) trie.put(entry.getValue(), entry.getKey());
        return Trie.Util.unmodifiable(trie);
    }
}
