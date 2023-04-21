package org.peaseplate.domain.util;

public class EncodeUtils {

    private interface EscapeSequenceProducer {

        /**
		 * Adds the character to the builder, encoding the character if necessary
		 * @param builder the builder
		 * @param c the character
		 */
        public void add(StringBuilder builder, char c);
    }

    protected static final String[] JAVASCRIPT_ESCAPE_SEQUENCES = new String[127];

    protected static final String[] JSON_ESCAPE_SEQUENCES = new String[127];

    protected static final String JSON_UNICODE_ESCAPE_SEQUENCE_PREFIX = "\\u";

    protected static final String[] XML_ESCAPE_SEQUENCES = new String[127];

    protected static final String XML_UNICODE_ESCAPE_SEQUENCE_PREFIX = "&#x";

    protected static final String XML_UNICODE_ESCAPE_SEQUENCE_SUFFIX = ";";

    protected static final String[] HTML_ESCAPE_SEQUENCES = new String[0x2667];

    private static final String EMPTY = "";

    private static final String HEX_4_DIGIT_PREFIX = "0000";

    private static final String HEX_3_DIGIT_PREFIX = "000";

    private static final String HEX_2_DIGIT_PREFIX = "00";

    private static final String HEX_1_DIGIT_PREFIX = "0";

    static {
        JAVASCRIPT_ESCAPE_SEQUENCES['\\'] = "\\\\";
        JAVASCRIPT_ESCAPE_SEQUENCES['\''] = "\\\'";
        JAVASCRIPT_ESCAPE_SEQUENCES['\"'] = "\\\"";
        JAVASCRIPT_ESCAPE_SEQUENCES['\b'] = "\\b";
        JAVASCRIPT_ESCAPE_SEQUENCES['\f'] = "\\f";
        JAVASCRIPT_ESCAPE_SEQUENCES['\n'] = "\\n";
        JAVASCRIPT_ESCAPE_SEQUENCES['\r'] = "\\r";
        JAVASCRIPT_ESCAPE_SEQUENCES['\t'] = "\\t";
        JSON_ESCAPE_SEQUENCES['\\'] = "\\\\";
        JSON_ESCAPE_SEQUENCES['\''] = "\\\'";
        JSON_ESCAPE_SEQUENCES['\"'] = "\\\"";
        JSON_ESCAPE_SEQUENCES['/'] = "\\/";
        JSON_ESCAPE_SEQUENCES['\b'] = "\\b";
        JSON_ESCAPE_SEQUENCES['\f'] = "\\f";
        JSON_ESCAPE_SEQUENCES['\n'] = "\\n";
        JSON_ESCAPE_SEQUENCES['\r'] = "\\r";
        JSON_ESCAPE_SEQUENCES['\t'] = "\\t";
        XML_ESCAPE_SEQUENCES['\''] = "&#39;";
        XML_ESCAPE_SEQUENCES['\"'] = "&quot;";
        XML_ESCAPE_SEQUENCES['&'] = "&amp;";
        XML_ESCAPE_SEQUENCES['<'] = "&lt;";
        XML_ESCAPE_SEQUENCES['>'] = "&gt;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0022] = "&quot;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0026] = "&amp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x003c] = "&lt;";
        HTML_ESCAPE_SEQUENCES[(char) 0x003e] = "&gt;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a0] = "&nbsp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a1] = "&iexcl;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a2] = "&cent;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a3] = "&pound;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a4] = "&curren;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a5] = "&yen;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a6] = "&brvbar;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a7] = "&sect;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a8] = "&uml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00a9] = "&copy;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00aa] = "&ordf;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ab] = "&laquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ac] = "&not;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ad] = "&shy;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ae] = "&reg;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00af] = "&macr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b0] = "&deg;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b1] = "&plusmn;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b2] = "&sup2;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b3] = "&sup3;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b4] = "&acute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b5] = "&micro;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b6] = "&para;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b7] = "&middot;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b8] = "&cedil;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00b9] = "&sup1;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ba] = "&ordm;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00bb] = "&raquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00bc] = "&frac14;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00bd] = "&frac12;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00be] = "&frac34;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00bf] = "&iquest;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c0] = "&Agrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c1] = "&Aacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c2] = "&Acirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c3] = "&Atilde;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c4] = "&Auml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c5] = "&Aring;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c6] = "&AElig;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c7] = "&Ccedil;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c8] = "&Egrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00c9] = "&Eacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ca] = "&Ecirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00cb] = "&Euml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00cc] = "&Igrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00cd] = "&Iacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ce] = "&Icirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00cf] = "&Iuml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d0] = "&ETH;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d1] = "&Ntilde;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d2] = "&Ograve;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d3] = "&Oacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d4] = "&Ocirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d5] = "&Otilde;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d6] = "&Ouml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d7] = "&times;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d8] = "&Oslash;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00d9] = "&Ugrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00da] = "&Uacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00db] = "&Ucirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00dc] = "&Uuml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00dd] = "&Yacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00de] = "&THORN;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00df] = "&szlig;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e0] = "&agrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e1] = "&aacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e2] = "&acirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e3] = "&atilde;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e4] = "&auml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e5] = "&aring;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e6] = "&aelig;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e7] = "&ccedil;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e8] = "&egrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00e9] = "&eacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ea] = "&ecirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00eb] = "&euml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ec] = "&igrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ed] = "&iacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ee] = "&icirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ef] = "&iuml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f0] = "&eth;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f1] = "&ntilde;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f2] = "&ograve;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f3] = "&oacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f4] = "&ocirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f5] = "&otilde;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f6] = "&ouml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f7] = "&divide;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f8] = "&oslash;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00f9] = "&ugrave;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00fa] = "&uacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00fb] = "&ucirc;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00fc] = "&uuml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00fd] = "&yacute;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00fe] = "&thorn;";
        HTML_ESCAPE_SEQUENCES[(char) 0x00ff] = "&yuml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0152] = "&OElig;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0153] = "&oelig;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0160] = "&Scaron;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0161] = "&scaron;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0178] = "&Yuml;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0192] = "&fnof;";
        HTML_ESCAPE_SEQUENCES[(char) 0x02c6] = "&circ;";
        HTML_ESCAPE_SEQUENCES[(char) 0x02dc] = "&tilde;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0391] = "&Alpha;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0392] = "&Beta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0393] = "&Gamma;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0394] = "&Delta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0395] = "&Epsilon;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0396] = "&Zeta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0397] = "&Eta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0398] = "&Theta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x0399] = "&Iota;";
        HTML_ESCAPE_SEQUENCES[(char) 0x039a] = "&Kappa;";
        HTML_ESCAPE_SEQUENCES[(char) 0x039b] = "&Lambda;";
        HTML_ESCAPE_SEQUENCES[(char) 0x039c] = "&Mu;";
        HTML_ESCAPE_SEQUENCES[(char) 0x039d] = "&Nu;";
        HTML_ESCAPE_SEQUENCES[(char) 0x039e] = "&Xi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x039f] = "&Omicron;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a0] = "&Pi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a1] = "&Rho;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a3] = "&Sigma;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a4] = "&Tau;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a5] = "&Upsilon;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a6] = "&Phi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a7] = "&Chi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a8] = "&Psi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03a9] = "&Omega;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b1] = "&alpha;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b2] = "&beta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b3] = "&gamma;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b4] = "&delta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b5] = "&epsilon;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b6] = "&zeta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b7] = "&eta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b8] = "&theta;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03b9] = "&iota;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03ba] = "&kappa;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03bb] = "&lambda;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03bc] = "&mu;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03bd] = "&nu;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03be] = "&xi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03bf] = "&omicron;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c0] = "&pi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c1] = "&rho;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c2] = "&sigmaf;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c3] = "&sigma;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c4] = "&tau;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c5] = "&upsilon;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c6] = "&phi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c7] = "&chi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c8] = "&psi;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03c9] = "&omega;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03d1] = "&thetasym;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03d2] = "&upsih;";
        HTML_ESCAPE_SEQUENCES[(char) 0x03d6] = "&piv;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2002] = "&ensp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2003] = "&emsp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2009] = "&thinsp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x200c] = "&zwnj;";
        HTML_ESCAPE_SEQUENCES[(char) 0x200d] = "&zwj;";
        HTML_ESCAPE_SEQUENCES[(char) 0x200e] = "&lrm;";
        HTML_ESCAPE_SEQUENCES[(char) 0x200f] = "&rlm;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2013] = "&ndash;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2014] = "&mdash;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2018] = "&lsquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2019] = "&rsquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x201a] = "&sbquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x201c] = "&ldquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x201d] = "&rdquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x201e] = "&bdquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2020] = "&dagger;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2021] = "&Dagger;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2022] = "&bull;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2026] = "&hellip;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2030] = "&permil;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2032] = "&prime;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2033] = "&Prime;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2039] = "&lsaquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x203a] = "&rsaquo;";
        HTML_ESCAPE_SEQUENCES[(char) 0x203e] = "&oline;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2044] = "&frasl;";
        HTML_ESCAPE_SEQUENCES[(char) 0x20ac] = "&euro;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2111] = "&image;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2118] = "&weierp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x211c] = "&real;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2122] = "&trade;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2135] = "&alefsym;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2190] = "&larr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2191] = "&uarr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2192] = "&rarr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2193] = "&darr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2194] = "&harr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x21b5] = "&crarr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x21d0] = "&lArr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x21d1] = "&uArr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x21d2] = "&rArr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x21d3] = "&dArr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x21d4] = "&hArr;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2200] = "&forall;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2202] = "&part;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2203] = "&exist;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2205] = "&empty;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2207] = "&nabla;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2208] = "&isin;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2209] = "&notin;";
        HTML_ESCAPE_SEQUENCES[(char) 0x220b] = "&ni;";
        HTML_ESCAPE_SEQUENCES[(char) 0x220f] = "&prod;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2211] = "&sum;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2212] = "&minus;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2217] = "&lowast;";
        HTML_ESCAPE_SEQUENCES[(char) 0x221a] = "&radic;";
        HTML_ESCAPE_SEQUENCES[(char) 0x221d] = "&prop;";
        HTML_ESCAPE_SEQUENCES[(char) 0x221e] = "&infin;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2220] = "&ang;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2227] = "&and;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2228] = "&or;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2229] = "&cap;";
        HTML_ESCAPE_SEQUENCES[(char) 0x222a] = "&cup;";
        HTML_ESCAPE_SEQUENCES[(char) 0x222b] = "&int;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2234] = "&there4;";
        HTML_ESCAPE_SEQUENCES[(char) 0x223c] = "&sim;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2245] = "&cong;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2248] = "&asymp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2260] = "&ne;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2261] = "&equiv;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2264] = "&le;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2265] = "&ge;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2282] = "&sub;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2283] = "&sup;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2284] = "&nsub;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2286] = "&sube;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2287] = "&supe;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2295] = "&oplus;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2297] = "&otimes;";
        HTML_ESCAPE_SEQUENCES[(char) 0x22a5] = "&perp;";
        HTML_ESCAPE_SEQUENCES[(char) 0x22c5] = "&sdot;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2308] = "&lceil;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2309] = "&rceil;";
        HTML_ESCAPE_SEQUENCES[(char) 0x230a] = "&lfloor;";
        HTML_ESCAPE_SEQUENCES[(char) 0x230b] = "&rfloor;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2329] = "&lang;";
        HTML_ESCAPE_SEQUENCES[(char) 0x232a] = "&rang;";
        HTML_ESCAPE_SEQUENCES[(char) 0x25ca] = "&loz;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2660] = "&spades;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2663] = "&clubs;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2665] = "&hearts;";
        HTML_ESCAPE_SEQUENCES[(char) 0x2666] = "&diams;";
    }

    private static final EscapeSequenceProducer JAVASCRIPT_ESCAPE_SEQUENCE_PRODUCER = new EscapeSequenceProducer() {

        public void add(StringBuilder builder, char c) {
            if (c < JAVASCRIPT_ESCAPE_SEQUENCES.length) {
                String escapeSequence = JAVASCRIPT_ESCAPE_SEQUENCES[c];
                if (escapeSequence != null) builder.append(escapeSequence); else builder.append(c);
            } else builder.append(c);
        }
    };

    private static final EscapeSequenceProducer JSON_ESCAPE_SEQUENCE_PRODUCER = new EscapeSequenceProducer() {

        public void add(StringBuilder builder, char c) {
            if (c < JSON_ESCAPE_SEQUENCES.length) {
                String escapeSequence = JSON_ESCAPE_SEQUENCES[c];
                if (escapeSequence != null) builder.append(escapeSequence); else builder.append(c);
            } else builder.append(JSON_UNICODE_ESCAPE_SEQUENCE_PREFIX).append(get4DigitHexValue(c));
        }
    };

    private static final EscapeSequenceProducer XML_ESCAPE_SEQUENCE_PRODUCER = new EscapeSequenceProducer() {

        public void add(StringBuilder builder, char c) {
            if (c < XML_ESCAPE_SEQUENCES.length) {
                String escapeSequence = XML_ESCAPE_SEQUENCES[c];
                if (escapeSequence != null) builder.append(escapeSequence); else builder.append(c);
            } else builder.append(XML_UNICODE_ESCAPE_SEQUENCE_PREFIX).append(get4DigitHexValue(c)).append(XML_UNICODE_ESCAPE_SEQUENCE_SUFFIX);
        }
    };

    private static final EscapeSequenceProducer HTML_ESCAPE_SEQUENCE_PRODUCER = new EscapeSequenceProducer() {

        public void add(StringBuilder builder, char c) {
            if (c < HTML_ESCAPE_SEQUENCES.length) {
                String escapeSequence = HTML_ESCAPE_SEQUENCES[c];
                if (escapeSequence != null) builder.append(escapeSequence); else builder.append(c);
            } else builder.append(c);
        }
    };

    /**
	 * Encodes the value to be used in Javascript strings.
	 *  
	 * @param context the context
	 * @param value the value, may be null
	 * @return the encoded value
	 */
    public static String toJavaScript(Object value) {
        return encode(JAVASCRIPT_ESCAPE_SEQUENCE_PRODUCER, value);
    }

    /**
	 * Encodes the value to be used in JSON strings.
	 *  
	 * @param value the value, may be null
	 * @return the encoded value
	 */
    public static String toJSON(Object value) {
        return encode(JSON_ESCAPE_SEQUENCE_PRODUCER, value);
    }

    /**
	 * Encodes the value using XML entities 
	 *  
	 * @param value the value, may be null
	 * @return the encoded (or empty) string
	 */
    public static String toXML(Object value) {
        return encode(XML_ESCAPE_SEQUENCE_PRODUCER, value);
    }

    /**
	 * Encodes the value using HTML entities.
	 * Encodes encodes CR or LF or CRLF to &lt;br /&gt;, 
	 * does not touch spaces or tabs.
	 *  
	 * @param value the value, may be null
	 * @return the encoded (or empty) string
	 */
    public static String toHTML(Object value) {
        return toHTML(value, true, false, -1);
    }

    /**
	 * Encodes the value using HTML entities.
	 * Does not touch spaces or tabs.
	 *  
	 * @param value the value, may be null
	 * @param encodeCRLF if set to true, this method encodes CR or LF or CRLF to &lt;br /&gt;
	 * @return the encoded (or empty) string
	 */
    public static String toHTML(Object value, boolean encodeCRLF) {
        return toHTML(value, encodeCRLF, false, -1);
    }

    /**
	 * Encodes the value using HTML entities
	 *  
	 * @param value the value, may be null
	 * @param encodeCRLF if set to true, this method encodes \r or \n or \r\n to &lt;br /&gt;
	 * @param encodeSpace if set to true, this method encodes spaces (0x20) to &amp;nbsp;
	 * @param encodeTabBy if set to >= 0, this method encodes tabs to the specified amount of &amp;nbsp;
	 * @return the encoded (or empty) string
	 */
    public static String toHTML(Object value, boolean encodeCRLF, boolean encodeSpace, int encodeTabBy) {
        if (value == null) return EMPTY;
        String s = String.valueOf(value);
        StringBuilder builder = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i += 1) {
            char c = s.charAt(i);
            if ((encodeCRLF) && (c == '\r')) {
                if ((i < s.length() - 1) && (s.charAt(i + 1) == '\n')) i += 1;
                builder.append("<br />");
            } else if ((encodeCRLF) && (c == '\n')) builder.append("<br />"); else if ((encodeSpace) && (c == ' ')) builder.append("&nbsp;"); else if ((encodeTabBy >= 0) && (c == '\t')) for (int j = 0; j < encodeTabBy; j += 1) builder.append("&nbsp;"); else HTML_ESCAPE_SEQUENCE_PRODUCER.add(builder, c);
        }
        return builder.toString();
    }

    /**
	 * Encodes the value by using the specified escape sequences.
	 * 
	 * @param producer the producer for the escape sequenced
	 * @param value the value, may be null
	 * @return the encoded value or empty if null
	 */
    protected static String encode(EscapeSequenceProducer producer, Object value) {
        if (value == null) return EMPTY;
        String s = String.valueOf(value);
        StringBuilder builder = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i += 1) producer.add(builder, s.charAt(i));
        return builder.toString();
    }

    /**
	 * Returns the 4-digit hex value of the character.
	 * This method is performance optimized, even if it looks wierd.
	 * @param value the value
	 * @return the 4-digit hex value
	 */
    protected static String get4DigitHexValue(char value) {
        String s = Integer.toHexString(value);
        int length = s.length();
        switch(length) {
            case 0:
                return HEX_4_DIGIT_PREFIX;
            case 1:
                return HEX_3_DIGIT_PREFIX + s;
            case 2:
                return HEX_2_DIGIT_PREFIX + s;
            case 3:
                return HEX_1_DIGIT_PREFIX + s;
            default:
                return s;
        }
    }
}
