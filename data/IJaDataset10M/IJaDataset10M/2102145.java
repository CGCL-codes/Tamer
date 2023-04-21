package net.sf.openforge.util;

import java.io.*;

/**
 * Nice Format class, really a Hex formatting class.
 * 
 * @author C.R.S. Schanck
 * @version $Id: HF.java 2 2005-06-09 20:00:48Z imiller $
 */
public class HF {

    private static final String rcs_id = "RCS_REVISION: $Rev: 2 $";

    private static String zzzs = "0000000000";

    private static String sps = "                                  ";

    private static String hextbl[] = { "00 ", "01 ", "02 ", "03 ", "04 ", "05 ", "06 ", "07 ", "08 ", "09 ", "0a ", "0b ", "0c ", "0d ", "0e ", "0f ", "10 ", "11 ", "12 ", "13 ", "14 ", "15 ", "16 ", "17 ", "18 ", "19 ", "1a ", "1b ", "1c ", "1d ", "1e ", "1f ", "20 ", "21 ", "22 ", "23 ", "24 ", "25 ", "26 ", "27 ", "28 ", "29 ", "2a ", "2b ", "2c ", "2d ", "2e ", "2f ", "30 ", "31 ", "32 ", "33 ", "34 ", "35 ", "36 ", "37 ", "38 ", "39 ", "3a ", "3b ", "3c ", "3d ", "3e ", "3f ", "40 ", "41 ", "42 ", "43 ", "44 ", "45 ", "46 ", "47 ", "48 ", "49 ", "4a ", "4b ", "4c ", "4d ", "4e ", "4f ", "50 ", "51 ", "52 ", "53 ", "54 ", "55 ", "56 ", "57 ", "58 ", "59 ", "5a ", "5b ", "5c ", "5d ", "5e ", "5f ", "60 ", "61 ", "62 ", "63 ", "64 ", "65 ", "66 ", "67 ", "68 ", "69 ", "6a ", "6b ", "6c ", "6d ", "6e ", "6f ", "70 ", "71 ", "72 ", "73 ", "74 ", "75 ", "76 ", "77 ", "78 ", "79 ", "7a ", "7b ", "7c ", "7d ", "7e ", "7f ", "80 ", "81 ", "82 ", "83 ", "84 ", "85 ", "86 ", "87 ", "88 ", "89 ", "8a ", "8b ", "8c ", "8d ", "8e ", "8f ", "90 ", "91 ", "92 ", "93 ", "94 ", "95 ", "96 ", "97 ", "98 ", "99 ", "9a ", "9b ", "9c ", "9d ", "9e ", "9f ", "a0 ", "a1 ", "a2 ", "a3 ", "a4 ", "a5 ", "a6 ", "a7 ", "a8 ", "a9 ", "aa ", "ab ", "ac ", "ad ", "ae ", "af ", "b0 ", "b1 ", "b2 ", "b3 ", "b4 ", "b5 ", "b6 ", "b7 ", "b8 ", "b9 ", "ba ", "bb ", "bc ", "bd ", "be ", "bf ", "c0 ", "c1 ", "c2 ", "c3 ", "c4 ", "c5 ", "c6 ", "c7 ", "c8 ", "c9 ", "ca ", "cb ", "cc ", "cd ", "ce ", "cf ", "d0 ", "d1 ", "d2 ", "d3 ", "d4 ", "d5 ", "d6 ", "d7 ", "d8 ", "d9 ", "da ", "db ", "dc ", "dd ", "de ", "df ", "e0 ", "e1 ", "e2 ", "e3 ", "e4 ", "e5 ", "e6 ", "e7 ", "e8 ", "e9 ", "ea ", "eb ", "ec ", "ed ", "ee ", "ef ", "f0 ", "f1 ", "f2 ", "f3 ", "f4 ", "f5 ", "f6 ", "f7 ", "f8 ", "f9 ", "fa ", "fb ", "fc ", "fd ", "fe ", "ff " };

    /**
     * Method declaration
     *
     *
     * @param s
     * @param w
     *
     * @return
     *
     * @see
     */
    private static final String z(String s, int w) {
        int missing = w - s.length();
        if (missing > 0) {
            for (; missing > zzzs.length(); missing = missing - zzzs.length()) {
                s = zzzs + s;
            }
            s = zzzs.substring(0, missing) + s;
        }
        return s;
    }

    /**
     * Hex representation of a long
     * @return	String
     * 
     * @param	i long
     */
    public static final String hex(long i) {
        return Long.toHexString(i);
    }

    /**
     * Hex representation of a int
     * @return	String
     * 
     * @param	i int
     */
    public static final String hex(int i) {
        return Integer.toHexString(i);
    }

    /**
     * Hex representation of a short
     * @return	String
     * 
     * @param	i short
     */
    public static final String hex(short i) {
        return Integer.toHexString((int) i);
    }

    /**
     * Hex representation of a byte
     * @return	String
     * 
     * @param	i byte
     */
    public static final String hex(byte i) {
        return Integer.toHexString(((int) i) & 0xff);
    }

    /**
     * Hex representation of a long, padded.
     * @return	String
     * 
     * @param	i long
     * @param	w width to pad to
     */
    public static final String hex(long i, int w) {
        return z(hex(i), w);
    }

    /**
     * Hex representation of an int, padded.
     * @return	String
     * 
     * @param	i int
     * @param	w width to pad to
     */
    public static final String hex(int i, int w) {
        return z(hex(i), w);
    }

    /**
     * Hex representation of a short, padded.
     * @return	String
     * 
     * @param	i short
     * @param	w width to pad to
     */
    public static final String hex(short i, int w) {
        return z(hex(i), w);
    }

    /**
     * Hex representation of a byte, padded.
     * @return	String
     * 
     * @param	i byte
     * @param	w width to pad to
     */
    public static final String hex(byte i, int w) {
        return z(hex(i), w);
    }

    /**
     * Return String of hexDump of a byte buffer, nicely formatted
     * with offsets displayed.
     * @return	String
     * 
     * @param	[]buf byte buffer to dump
     * @param	off offset to start at
     * @param	len number of bytes to dump
     */
    public static String hexDump(byte[] buf, int off, int len) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        HF.hexDump(baos, buf, off, len);
        return baos.toString();
    }

    /**
     * hexDump to a specific OutputStream
     * @return	void
     * 
     * @param	os OutputStream to dump hexDump to
     * @param	[]buf byte buffer to dump
     * @param	off offset to start at
     * @param	len number of bytes to dump
     */
    public static void hexDump(OutputStream os, byte[] buf, int off, int len) {
        HF.hexDump(new PrintWriter(os), buf, off, len);
    }

    /**
     * hexDump to a specific PrintWriter
     * @return	void
     * 
     * @param	pw PrintWriter to dump hexDump to
     * @param	[]buf byte buffer to dump
     * @param	off offset to start at
     * @param	len number of bytes to dump
     */
    public static void hexDump(PrintWriter pw, byte[] buf, int off, int len) {
        for (int i = 0; i < len; i += 16) {
            pw.print(HF.hex(i, 6) + " ");
            for (int j = 0; j < 16; j++) {
                if ((i + j) >= len) {
                    break;
                }
                pw.print(hextbl[((int) buf[off + i + j]) & 0xff]);
            }
            pw.println();
        }
        pw.flush();
    }
}
