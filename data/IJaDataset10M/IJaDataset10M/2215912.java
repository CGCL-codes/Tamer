package com.oscwave.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Some string glue, helper methods etc.
 * @author dan
 */
public final class StringUtilities {

    private StringUtilities() {
    }

    /**
	 * Hex symbol table 0-9A-F
	 */
    private static final byte[] TABLE_HEXCHAR = { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f' };

    private static Map<String, String> dec;

    static {
        dec = new HashMap<String, String>();
        dec.put("%9", "\t");
        dec.put("%09", "\t");
        dec.put("%20", " ");
        dec.put("%21", "!");
        dec.put("%22", "\"");
        dec.put("%23", "#");
        dec.put("%24", "$");
        dec.put("%25", "%");
        dec.put("%26", "&");
        dec.put("%27", "'");
        dec.put("%28", "(");
        dec.put("%29", ")");
        dec.put("%2A", "*");
        dec.put("%2B", "+");
        dec.put("%2C", ",");
        dec.put("%2D", "-");
        dec.put("%2E", ".");
        dec.put("%2F", "/");
        dec.put("%30", "0");
        dec.put("%31", "1");
        dec.put("%32", "2");
        dec.put("%33", "3");
        dec.put("%34", "4");
        dec.put("%35", "5");
        dec.put("%36", "6");
        dec.put("%37", "7");
        dec.put("%38", "8");
        dec.put("%39", "9");
        dec.put("%3A", ":");
        dec.put("%3B", ";");
        dec.put("%3C", "<");
        dec.put("%3D", "=");
        dec.put("%3E", ">");
        dec.put("%3F", "?");
        dec.put("%40", "@");
        dec.put("%41", "A");
        dec.put("%42", "B");
        dec.put("%43", "C");
        dec.put("%44", "D");
        dec.put("%45", "E");
        dec.put("%46", "F");
        dec.put("%47", "G");
        dec.put("%48", "H");
        dec.put("%49", "I");
        dec.put("%4A", "J");
        dec.put("%4B", "K");
        dec.put("%4C", "L");
        dec.put("%4D", "M");
        dec.put("%4E", "N");
        dec.put("%4F", "O");
        dec.put("%50", "P");
        dec.put("%51", "Q");
        dec.put("%52", "R");
        dec.put("%53", "S");
        dec.put("%54", "T");
        dec.put("%55", "U");
        dec.put("%56", "V");
        dec.put("%57", "W");
        dec.put("%58", "X");
        dec.put("%59", "Y");
        dec.put("%5A", "Z");
        dec.put("%5B", "[");
        dec.put("%5C", "\\");
        dec.put("%5D", "]");
        dec.put("%5E", "^");
        dec.put("%5F", "_");
        dec.put("%60", "`");
        dec.put("%61", "a");
        dec.put("%62", "b");
        dec.put("%63", "c");
        dec.put("%64", "d");
        dec.put("%65", "e");
        dec.put("%66", "f");
        dec.put("%67", "g");
        dec.put("%68", "h");
        dec.put("%69", "i");
        dec.put("%6A", "j");
        dec.put("%6B", "k");
        dec.put("%6C", "l");
        dec.put("%6D", "m");
        dec.put("%6E", "n");
        dec.put("%6F", "o");
        dec.put("%70", "p");
        dec.put("%71", "q");
        dec.put("%72", "r");
        dec.put("%73", "s");
        dec.put("%74", "t");
        dec.put("%75", "u");
        dec.put("%76", "v");
        dec.put("%77", "w");
        dec.put("%78", "x");
        dec.put("%79", "y");
        dec.put("%7A", "z");
        dec.put("%7B", "{");
        dec.put("%7C", "|");
        dec.put("%7D", "}");
        dec.put("%7E", "~");
        dec.put("%7F", "");
        dec.put("%80", "�");
        dec.put("%81", "");
        dec.put("%82", "�");
        dec.put("%83", "�");
        dec.put("%84", "�");
        dec.put("%85", "�");
        dec.put("%86", "�");
        dec.put("%87", "�");
        dec.put("%88", "�");
        dec.put("%89", "�");
        dec.put("%8A", "�");
        dec.put("%8B", "�");
        dec.put("%8C", "�");
        dec.put("%8D", "");
        dec.put("%8E", "�");
        dec.put("%8F", "");
        dec.put("%90", " ");
        dec.put("%91", "�");
        dec.put("%92", "�");
        dec.put("%93", "�");
        dec.put("%94", "�");
        dec.put("%95", "�");
        dec.put("%96", "�");
        dec.put("%97", "�");
        dec.put("%98", "� ");
        dec.put("%99", "�");
        dec.put("%9A", "�");
        dec.put("%9B", "�");
        dec.put("%9C", "�");
        dec.put("%9D", " ");
        dec.put("%9E", "�");
        dec.put("%9F", "�");
        dec.put("%A0", " ");
        dec.put("%A1", "�");
        dec.put("%A2", "�");
        dec.put("%A3", "�");
        dec.put("%A4", " ");
        dec.put("%A5", "�");
        dec.put("%A6", "|");
        dec.put("%A7", "�");
        dec.put("%A8", "��");
        dec.put("%A9", "�");
        dec.put("%AA", "�");
        dec.put("%AB", "�");
        dec.put("%AC", "�");
        dec.put("%AD", "�");
        dec.put("%AE", "�");
        dec.put("%AF", "�");
        dec.put("%B0", "�");
        dec.put("%B1", "�");
        dec.put("%B2", "�");
        dec.put("%B3", "�");
        dec.put("%B4", "�");
        dec.put("%B5", "�");
        dec.put("%B6", "�");
        dec.put("%B7", "�");
        dec.put("%B8", "��");
        dec.put("%B9", "�");
        dec.put("%BA", "�");
        dec.put("%BB", "�");
        dec.put("%BC", "�");
        dec.put("%BD", "�");
        dec.put("%BE", "�");
        dec.put("%BF", "�");
        dec.put("%C0", "�");
        dec.put("%C1", "�");
        dec.put("%C2", "�");
        dec.put("%C3", "�");
        dec.put("%C4", "�");
        dec.put("%C5", "�");
        dec.put("%C6", "�");
        dec.put("%C7", "�");
        dec.put("%C8", "��");
        dec.put("%C9", "�");
        dec.put("%CA", "�");
        dec.put("%CB", "�");
        dec.put("%CC", "�");
        dec.put("%CD", "�");
        dec.put("%CE", "�");
        dec.put("%CF", "�");
        dec.put("%D0", "�");
        dec.put("%D1", "�");
        dec.put("%D2", "�");
        dec.put("%D3", "�");
        dec.put("%D4", "�");
        dec.put("%D5", "�");
        dec.put("%D6", "�");
        dec.put("%D7", " ");
        dec.put("%D8", "�");
        dec.put("%D9", "�");
        dec.put("%DA", "�");
        dec.put("%DB", "�");
        dec.put("%DC", "�");
        dec.put("%DD", "�");
        dec.put("%DE", "�");
        dec.put("%DF", "�");
        dec.put("%E0", "�");
        dec.put("%E1", "�");
        dec.put("%E2", "�");
        dec.put("%E3", "�");
        dec.put("%E4", "�");
        dec.put("%E5", "�");
        dec.put("%E6", "�");
        dec.put("%E7", "�");
        dec.put("%E8", "�");
        dec.put("%E9", "�");
        dec.put("%EA", "�");
        dec.put("%EB", "�");
        dec.put("%EC", "�");
        dec.put("%ED", "�");
        dec.put("%EE", "�");
        dec.put("%EF", "�");
        dec.put("%F0", "�");
        dec.put("%F1", "�");
        dec.put("%F2", "�");
        dec.put("%F3", "�");
        dec.put("%F4", "�");
        dec.put("%F5", "�");
        dec.put("%F6", "�");
        dec.put("%F7", "�");
        dec.put("%F8", "�");
        dec.put("%F9", "�");
        dec.put("%FA", "�");
        dec.put("%FB", "�");
        dec.put("%FC", "�");
        dec.put("%FD", "�");
        dec.put("%FE", "�");
        dec.put("%FF", "�");
    }

    /**
	 * Converts a given byte-array into the hex-string representation.
	 * @param raw Bytes to get hex-string representation from.
	 * @return Bytes as hex-string
	 * @throws UnsupportedEncodingException Not to expect, internally encodes result string in ASCII
	 */
    public static String toHexString(byte[] raw) throws UnsupportedEncodingException {
        int i = 0, t = 0;
        byte[] hex = new byte[2 * raw.length];
        for (byte b : raw) {
            t = b & 0xFF;
            hex[i++] = TABLE_HEXCHAR[t >>> 4];
            hex[i++] = TABLE_HEXCHAR[t & 0xF];
        }
        return new String(hex, "ASCII");
    }

    public static String decodeUrl(String s) {
        if (s.contains("%")) {
            for (String t : dec.keySet()) {
                s = s.replace(t, dec.get(t));
                s = s.replace(t.toLowerCase(), dec.get(t));
            }
        }
        return s;
    }
}
