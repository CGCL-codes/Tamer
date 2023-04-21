package com.novocode.naf.data;

import java.lang.reflect.Array;
import java.util.*;
import com.novocode.naf.app.NAFException;

/**
 * Contains utility methods to decode various data formats from
 * Strings and DOM Nodes.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 16, 2003
 * @version $Id: DataDecoder.java 379 2008-03-27 16:42:16Z szeiger $
 */
public class DataDecoder {

    private static final String[] NO_STRINGS = new String[0];

    private DataDecoder() {
    }

    public static int[] decodeNumberArray(String s) throws NAFException {
        String[] names = decodeStringArray(s, ",", true, false);
        if (names.length == 1 && names[0].length() == 0) return null;
        int[] ia = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            try {
                ia[i] = Integer.parseInt(names[i]);
            } catch (NumberFormatException ex) {
                throw new NAFException("Error parsing number \"" + names[i] + "\"", ex);
            }
        }
        return ia;
    }

    public static int decodeNumber(String s, final int def) throws NAFException {
        if (s == null) return def;
        s = s.trim();
        if (s.length() == 0) return def;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new NAFException("Error parsing number \"" + s + "\"", ex);
        }
    }

    public static boolean decodeBoolean(String s, final boolean def) throws NAFException {
        if (s == null) return def;
        s = s.trim().toLowerCase();
        if (s.length() == 0) return def;
        return s.equals("true") || s.equals("yes") || s.equals("1");
    }

    public static SizeMeasure decodeLength(final String str, final SizeMeasure def) throws NAFException {
        if (str == null) return def;
        String s = str.trim();
        if (s.length() == 0) return def;
        boolean em = false, percent = false;
        if (s.endsWith("px")) s = s.substring(0, s.length() - 2).trim(); else if (s.endsWith("em")) {
            s = s.substring(0, s.length() - 2).trim();
            em = true;
        } else if (s.endsWith("%")) {
            s = s.substring(0, s.length() - 1).trim();
            percent = true;
        }
        try {
            double d = Double.parseDouble(s);
            if (em) return new SizeMeasure(0, d, 0); else if (percent) return new SizeMeasure(0, 0, d / 100.0); else return new SizeMeasure(d);
        } catch (NumberFormatException ex) {
            throw new NAFException("Error parsing length \"" + str + "\"", ex);
        }
    }

    public static SizeMeasurePair decodeLengthPair(final String str, final SizeMeasure def) throws NAFException {
        if (str == null) return new SizeMeasurePair(def);
        int sep = str.indexOf(' ');
        if (sep == -1) return new SizeMeasurePair(decodeLength(str, def)); else {
            String s1 = str.substring(0, sep);
            String s2 = str.substring(sep + 1);
            return new SizeMeasurePair(decodeLength(s1, def), decodeLength(s2, def));
        }
    }

    public static SizeMeasureQuadruple decodeLengthQuadruple(final String str, final SizeMeasure def) throws NAFException {
        SizeMeasureQuadruple res = new SizeMeasureQuadruple(def);
        if (str == null) return res;
        StringTokenizer tok = new StringTokenizer(str, " ");
        if (!tok.hasMoreElements()) return res;
        res.setAll(decodeLength(tok.nextToken(), def));
        if (!tok.hasMoreElements()) return res;
        res.setVertical(decodeLength(tok.nextToken(), def));
        if (!tok.hasMoreElements()) return res;
        res.setLeft(res.getTop());
        res.setVertical(decodeLength(tok.nextToken(), def));
        if (!tok.hasMoreElements()) return res;
        res.setBottom(decodeLength(tok.nextToken(), def));
        return res;
    }

    public static String[] decodeStringArray(String s, String separators, boolean trim, boolean excludeEmptyEntries) throws NAFException {
        if (s == null) return NO_STRINGS;
        ArrayList<String> al = new ArrayList<String>();
        if (excludeEmptyEntries) {
            StringTokenizer tok = new StringTokenizer(s, separators);
            while (tok.hasMoreTokens()) {
                String t = tok.nextToken();
                if (trim) t = t.trim();
                if (excludeEmptyEntries && t.length() == 0) continue;
                al.add(t);
            }
        } else {
            StringTokenizer tok = new StringTokenizer(s, separators, true);
            boolean prevSep = true;
            while (tok.hasMoreTokens()) {
                String t = tok.nextToken();
                if (t.length() == 1 && separators.indexOf(t.charAt(0)) != -1) {
                    if (prevSep) al.add("");
                    prevSep = true;
                } else {
                    if (trim) t = t.trim();
                    al.add(t);
                    prevSep = false;
                }
            }
            if (prevSep) al.add("");
        }
        if (al.size() == 0) return NO_STRINGS; else return al.toArray(NO_STRINGS);
    }

    public static String decodeAccessKey(String s) {
        if (s == null) return null;
        int slen = s.length();
        if (slen == 0) return s;
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < slen; i++) {
            char c = s.charAt(i);
            if (c == '&') b.append("&&"); else if (c == '|') {
                if (i + 1 < slen) {
                    char c2 = s.charAt(++i);
                    if (c2 == '|') b.append(c); else b.append('&').append(c2);
                } else b.append(c);
            } else b.append(c);
        }
        return b.toString();
    }

    public static String decodeBackslashEscapes(String s) {
        if (s == null) return null;
        int slen = s.length();
        if (slen == 0) return s;
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < slen; i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                if (i < slen) {
                    char c2 = s.charAt(++i);
                    if (c2 == 'n') b.append('\n'); else if (c2 == 't') b.append('\t'); else if (c2 == '\\') b.append('\\');
                } else b.append(c);
            } else b.append(c);
        }
        return b.toString();
    }

    public static IntFraction decodeFraction(final String str, final IntFraction def) throws NAFException {
        if (str == null) return def;
        String s = str.trim();
        if (s.length() == 0) return def;
        try {
            if (s.endsWith("%")) {
                String s2 = s.substring(0, s.length() - 1).trim();
                if (s2.length() == 0) return def;
                return new IntFraction(Integer.parseInt(s2), 100);
            }
            int sep = str.indexOf('/');
            if (sep == -1) {
                double d = Double.parseDouble(s) * 32768;
                return new IntFraction((int) (d + 0.5), 32768);
            }
            String s1 = str.substring(0, sep).trim();
            String s2 = str.substring(sep + 1).trim();
            if (s1.length() == 0 || s2.length() == 0) return def;
            int i1 = Integer.parseInt(s1);
            int i2 = Integer.parseInt(s2);
            if (i2 == 0) throw new NAFException("Error parsing fraction \"" + str + "\": Denominator must not be 0");
            return new IntFraction(i1, i2);
        } catch (NumberFormatException ex) {
            throw new NAFException("Error parsing fraction \"" + str + "\"", ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T[] decodeEnumArray(String s, Class<T> enumClass) throws NAFException {
        String[] names = decodeStringArray(s, ",|", true, false);
        if (names.length == 1 && names[0].length() == 0) return null;
        T[] a = (T[]) Array.newInstance(enumClass, names.length);
        for (int i = 0; i < names.length; i++) {
            try {
                a[i] = (T) T.valueOf((Class) enumClass, names[i].replace('-', '_').toUpperCase());
            } catch (Exception ex) {
                throw new NAFException("Error parsing value \"" + names[i] + "\" of enum " + enumClass.getName(), ex);
            }
        }
        return a;
    }

    public static <T extends Enum<T>> T decodeEnum(String s, Class<T> enumClass) throws NAFException {
        try {
            return Enum.valueOf(enumClass, s.replace('-', '_').toUpperCase());
        } catch (Exception ex) {
            throw new NAFException("Error parsing value \"" + s + "\" of enum " + enumClass.getName(), ex);
        }
    }

    public static <T extends Enum<T>> T decodeEnum(String s, Class<T> enumClass, T def) throws NAFException {
        if (s == null || s.length() == 0) return def;
        return decodeEnum(s, enumClass);
    }
}
