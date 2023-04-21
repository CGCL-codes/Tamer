package org.openXpertya.print.pdf.text.pdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.openXpertya.print.pdf.text.ExceptionConverter;

/** Supports fast encodings for winansi and PDFDocEncoding.
 * Supports conversions from CJK encodings to CID.
 * Supports custom encodings.
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfEncodings {

    protected static final int CIDNONE = 0;

    protected static final int CIDRANGE = 1;

    protected static final int CIDCHAR = 2;

    static final char winansiByteToChar[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 8364, 65533, 8218, 402, 8222, 8230, 8224, 8225, 710, 8240, 352, 8249, 338, 65533, 381, 65533, 65533, 8216, 8217, 8220, 8221, 8226, 8211, 8212, 732, 8482, 353, 8250, 339, 65533, 382, 376, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255 };

    static final char pdfEncodingByteToChar[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 0x2022, 0x2020, 0x2021, 0x2026, 0x2014, 0x2013, 0x0192, 0x2044, 0x2039, 0x203a, 0x2212, 0x2030, 0x201e, 0x201c, 0x201d, 0x2018, 0x2019, 0x201a, 0x2122, 0xfb01, 0xfb02, 0x0141, 0x0152, 0x0160, 0x0178, 0x017d, 0x0131, 0x0142, 0x0153, 0x0161, 0x017e, 65533, 0x20ac, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255 };

    static final IntHashtable winansi = new IntHashtable();

    static final IntHashtable pdfEncoding = new IntHashtable();

    static final HashMap extraEncodings = new HashMap();

    static {
        for (int k = 128; k < 160; ++k) {
            char c = winansiByteToChar[k];
            if (c != 65533) winansi.put(c, k);
        }
        for (int k = 128; k < 161; ++k) {
            char c = pdfEncodingByteToChar[k];
            if (c != 65533) pdfEncoding.put(c, k);
        }
        addExtraEncoding("Wingdings", new WingdingsConversion());
        addExtraEncoding("Symbol", new SymbolConversion(true));
        addExtraEncoding("ZapfDingbats", new SymbolConversion(false));
        addExtraEncoding("SymbolTT", new SymbolTTConversion());
        addExtraEncoding("Cp437", new Cp437Conversion());
    }

    /** Converts a <CODE>String</CODE> to a </CODE>byte</CODE> array according
     * to the font's encoding.
     * @return an array of <CODE>byte</CODE> representing the conversion according to the font's encoding
     * @param encoding the encoding
     * @param text the <CODE>String</CODE> to be converted
     */
    public static final byte[] convertToBytes(String text, String encoding) {
        if (text == null) return new byte[0];
        if (encoding == null || encoding.length() == 0) {
            int len = text.length();
            byte b[] = new byte[len];
            for (int k = 0; k < len; ++k) b[k] = (byte) text.charAt(k);
            return b;
        }
        ExtraEncoding extra = null;
        synchronized (extraEncodings) {
            extra = (ExtraEncoding) extraEncodings.get(encoding.toLowerCase());
        }
        if (extra != null) {
            byte b[] = extra.charToByte(text, encoding);
            if (b != null) return b;
        }
        IntHashtable hash = null;
        if (encoding.equals(BaseFont.WINANSI)) hash = winansi; else if (encoding.equals(PdfObject.TEXT_PDFDOCENCODING)) hash = pdfEncoding;
        if (hash != null) {
            char cc[] = text.toCharArray();
            int len = cc.length;
            int ptr = 0;
            byte b[] = new byte[len];
            int c = 0;
            for (int k = 0; k < len; ++k) {
                char char1 = cc[k];
                if (char1 < 128 || (char1 >= 160 && char1 <= 255)) c = char1; else c = hash.get(char1);
                if (c != 0) b[ptr++] = (byte) c;
            }
            if (ptr == len) return b;
            byte b2[] = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }
        if (encoding.equals(PdfObject.TEXT_UNICODE)) {
            char cc[] = text.toCharArray();
            int len = cc.length;
            byte b[] = new byte[cc.length * 2 + 2];
            b[0] = -2;
            b[1] = -1;
            int bptr = 2;
            for (int k = 0; k < len; ++k) {
                char c = cc[k];
                b[bptr++] = (byte) (c >> 8);
                b[bptr++] = (byte) (c & 0xff);
            }
            return b;
        }
        try {
            return text.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionConverter(e);
        }
    }

    /** Converts a </CODE>byte</CODE> array to a <CODE>String</CODE> according
     * to the some encoding.
     * @param bytes the bytes to convert
     * @param encoding the encoding
     * @return the converted <CODE>String</CODE>
     */
    public static final String convertToString(byte bytes[], String encoding) {
        if (bytes == null) return PdfObject.NOTHING;
        if (encoding == null || encoding.length() == 0) {
            char c[] = new char[bytes.length];
            for (int k = 0; k < bytes.length; ++k) c[k] = (char) (bytes[k] & 0xff);
            return new String(c);
        }
        ExtraEncoding extra = null;
        synchronized (extraEncodings) {
            extra = (ExtraEncoding) extraEncodings.get(encoding.toLowerCase());
        }
        if (extra != null) {
            String text = extra.byteToChar(bytes, encoding);
            if (text != null) return text;
        }
        char ch[] = null;
        if (encoding.equals(BaseFont.WINANSI)) ch = winansiByteToChar; else if (encoding.equals(PdfObject.TEXT_PDFDOCENCODING)) ch = pdfEncodingByteToChar;
        if (ch != null) {
            int len = bytes.length;
            char c[] = new char[len];
            for (int k = 0; k < len; ++k) {
                c[k] = ch[bytes[k] & 0xff];
            }
            return new String(c);
        }
        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionConverter(e);
        }
    }

    /** Checks is <CODE>text</CODE> only has PdfDocEncoding characters.
     * @param text the <CODE>String</CODE> to test
     * @return <CODE>true</CODE> if only PdfDocEncoding characters are present
     */
    public static boolean isPdfDocEncoding(String text) {
        if (text == null) return true;
        int len = text.length();
        for (int k = 0; k < len; ++k) {
            char char1 = text.charAt(k);
            if (char1 < 128 || (char1 >= 160 && char1 <= 255)) continue;
            if (!pdfEncoding.containsKey(char1)) return false;
        }
        return true;
    }

    static final HashMap cmaps = new HashMap();

    /** Assumes that '\\n' and '\\r\\n' are the newline sequences. It may not work for
     * all CJK encodings. To be used with loadCmap().
     */
    public static final byte CRLF_CID_NEWLINE[][] = new byte[][] { { (byte) '\n' }, { (byte) '\r', (byte) '\n' } };

    /** Clears the CJK cmaps from the cache. If <CODE>name</CODE> is the
     * empty string then all the cache is cleared. Calling this method
     * has no consequences other than the need to reload the cmap
     * if needed.
     * @param name the name of the cmap to clear or all the cmaps if the empty string
     */
    public static void clearCmap(String name) {
        synchronized (cmaps) {
            if (name.length() == 0) cmaps.clear(); else cmaps.remove(name);
        }
    }

    /** Loads a CJK cmap to the cache with the option of associating
     * sequences to the newline.
     * @param name the CJK cmap name
     * @param newline the sequences to be replaced bi a newline in the resulting CID. See <CODE>CRLF_CID_NEWLINE</CODE>
     */
    public static void loadCmap(String name, byte newline[][]) {
        try {
            char planes[][] = null;
            synchronized (cmaps) {
                planes = (char[][]) cmaps.get(name);
            }
            if (planes == null) {
                planes = readCmap(name, (byte[][]) newline);
                synchronized (cmaps) {
                    cmaps.put(name, planes);
                }
            }
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    /** Converts a <CODE>byte</CODE> array encoded as <CODE>name</CODE>
     * to a CID string. This is needed to reach some CJK characters
     * that don't exist in 16 bit Unicode.</p>
     * The font to use this result must use the encoding "Identity-H"
     * or "Identity-V".</p>
     * See ftp://ftp.oreilly.com/pub/examples/nutshell/cjkv/adobe/.
     * @param name the CJK encoding name
     * @param seq the <CODE>byte</CODE> array to be decoded
     * @return the CID string
     */
    public static String convertCmap(String name, byte seq[]) {
        return convertCmap(name, seq, 0, seq.length);
    }

    /** Converts a <CODE>byte</CODE> array encoded as <CODE>name</CODE>
     * to a CID string. This is needed to reach some CJK characters
     * that don't exist in 16 bit Unicode.</p>
     * The font to use this result must use the encoding "Identity-H"
     * or "Identity-V".</p>
     * See ftp://ftp.oreilly.com/pub/examples/nutshell/cjkv/adobe/.
     * @param name the CJK encoding name
     * @param start the start offset in the data
     * @param length the number of bytes to convert
     * @param seq the <CODE>byte</CODE> array to be decoded
     * @return the CID string
     */
    public static String convertCmap(String name, byte seq[], int start, int length) {
        try {
            char planes[][] = null;
            synchronized (cmaps) {
                planes = (char[][]) cmaps.get(name);
            }
            if (planes == null) {
                planes = readCmap(name, (byte[][]) null);
                synchronized (cmaps) {
                    cmaps.put(name, planes);
                }
            }
            return decodeSequence(seq, start, length, planes);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    static String decodeSequence(byte seq[], int start, int length, char planes[][]) {
        StringBuffer buf = new StringBuffer();
        int end = start + length;
        int currentPlane = 0;
        for (int k = start; k < end; ++k) {
            int one = (int) seq[k] & 0xff;
            char plane[] = planes[currentPlane];
            int cid = plane[one];
            if ((cid & 0x8000) == 0) {
                buf.append((char) cid);
                currentPlane = 0;
            } else currentPlane = cid & 0x7fff;
        }
        return buf.toString();
    }

    static char[][] readCmap(String name, byte newline[][]) throws IOException {
        ArrayList planes = new ArrayList();
        planes.add(new char[256]);
        readCmap(name, planes);
        if (newline != null) {
            for (int k = 0; k < newline.length; ++k) encodeSequence(newline[k].length, newline[k], BaseFont.CID_NEWLINE, planes);
        }
        char ret[][] = new char[planes.size()][];
        return (char[][]) planes.toArray(ret);
    }

    static void readCmap(String name, ArrayList planes) throws IOException {
        String fullName = BaseFont.RESOURCE_PATH + "cmaps/" + name;
        InputStream in = BaseFont.getResourceStream(fullName);
        if (in == null) throw new IOException("The Cmap " + name + " was not found.");
        encodeStream(in, planes);
        in.close();
    }

    static void encodeStream(InputStream in, ArrayList planes) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
        String line = null;
        int state = CIDNONE;
        byte seqs[] = new byte[7];
        while ((line = rd.readLine()) != null) {
            if (line.length() < 6) continue;
            switch(state) {
                case CIDNONE:
                    {
                        if (line.indexOf("begincidrange") >= 0) state = CIDRANGE; else if (line.indexOf("begincidchar") >= 0) state = CIDCHAR; else if (line.indexOf("usecmap") >= 0) {
                            StringTokenizer tk = new StringTokenizer(line);
                            String t = tk.nextToken();
                            readCmap(t.substring(1), planes);
                        }
                        break;
                    }
                case CIDRANGE:
                    {
                        if (line.indexOf("endcidrange") >= 0) {
                            state = CIDNONE;
                            break;
                        }
                        StringTokenizer tk = new StringTokenizer(line);
                        String t = tk.nextToken();
                        int size = t.length() / 2 - 1;
                        long start = Long.parseLong(t.substring(1, t.length() - 1), 16);
                        t = tk.nextToken();
                        long end = Long.parseLong(t.substring(1, t.length() - 1), 16);
                        t = tk.nextToken();
                        int cid = Integer.parseInt(t);
                        for (long k = start; k <= end; ++k) {
                            breakLong(k, size, seqs);
                            encodeSequence(size, seqs, (char) cid, planes);
                            ++cid;
                        }
                        break;
                    }
                case CIDCHAR:
                    {
                        if (line.indexOf("endcidchar") >= 0) {
                            state = CIDNONE;
                            break;
                        }
                        StringTokenizer tk = new StringTokenizer(line);
                        String t = tk.nextToken();
                        int size = t.length() / 2 - 1;
                        long start = Long.parseLong(t.substring(1, t.length() - 1), 16);
                        t = tk.nextToken();
                        int cid = Integer.parseInt(t);
                        breakLong(start, size, seqs);
                        encodeSequence(size, seqs, (char) cid, planes);
                        break;
                    }
            }
        }
    }

    static void breakLong(long n, int size, byte seqs[]) {
        for (int k = 0; k < size; ++k) {
            seqs[k] = (byte) (n >> ((size - 1 - k) * 8));
        }
    }

    static void encodeSequence(int size, byte seqs[], char cid, ArrayList planes) {
        --size;
        int nextPlane = 0;
        for (int idx = 0; idx < size; ++idx) {
            char plane[] = (char[]) planes.get(nextPlane);
            int one = (int) seqs[idx] & 0xff;
            char c = plane[one];
            if (c != 0 && (c & 0x8000) == 0) throw new RuntimeException("Inconsistent mapping.");
            if (c == 0) {
                planes.add(new char[256]);
                c = (char) ((planes.size() - 1) | 0x8000);
                plane[one] = c;
            }
            nextPlane = c & 0x7fff;
        }
        char plane[] = (char[]) planes.get(nextPlane);
        int one = (int) seqs[size] & 0xff;
        char c = plane[one];
        if ((c & 0x8000) != 0) throw new RuntimeException("Inconsistent mapping.");
        plane[one] = cid;
    }

    /** Adds an extra encoding.
     * @param name the name of the encoding. The encoding recognition is case insensitive
     * @param enc the conversion class
     */
    public static void addExtraEncoding(String name, ExtraEncoding enc) {
        synchronized (extraEncodings) {
            extraEncodings.put(name.toLowerCase(), enc);
        }
    }

    private static class WingdingsConversion implements ExtraEncoding {

        public byte[] charToByte(String text, String encoding) {
            char cc[] = text.toCharArray();
            byte b[] = new byte[cc.length];
            int ptr = 0;
            int len = cc.length;
            for (int k = 0; k < len; ++k) {
                char c = cc[k];
                if (c == ' ') b[ptr++] = (byte) c; else if (c >= '✁' && c <= '➾') {
                    byte v = table[c - 0x2700];
                    if (v != 0) b[ptr++] = v;
                }
            }
            if (ptr == len) return b;
            byte b2[] = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public String byteToChar(byte[] b, String encoding) {
            return null;
        }

        private static final byte table[] = { 0, 35, 34, 0, 0, 0, 41, 62, 81, 42, 0, 0, 65, 63, 0, 0, 0, 0, 0, -4, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, 86, 0, 88, 89, 0, 0, 0, 0, 0, 0, 0, 0, -75, 0, 0, 0, 0, 0, -74, 0, 0, 0, -83, -81, -84, 0, 0, 0, 0, 0, 0, 0, 0, 124, 123, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 0, -90, 0, 0, 0, 113, 114, 0, 0, 0, 117, 0, 0, 0, 0, 0, 0, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -24, -40, 0, 0, -60, -58, 0, 0, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, -36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }

    private static class Cp437Conversion implements ExtraEncoding {

        private static IntHashtable c2b = new IntHashtable();

        public byte[] charToByte(String text, String encoding) {
            char cc[] = text.toCharArray();
            byte b[] = new byte[cc.length];
            int ptr = 0;
            int len = cc.length;
            for (int k = 0; k < len; ++k) {
                char c = cc[k];
                if (c < ' ') continue;
                if (c < 128) b[ptr++] = (byte) c; else {
                    byte v = (byte) c2b.get(c);
                    if (v != 0) b[ptr++] = v;
                }
            }
            if (ptr == len) return b;
            byte b2[] = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public String byteToChar(byte[] b, String encoding) {
            int len = b.length;
            char cc[] = new char[len];
            int ptr = 0;
            for (int k = 0; k < len; ++k) {
                int c = b[k] & 0xff;
                if (c < ' ') continue;
                if (c < 128) cc[ptr++] = (char) c; else {
                    char v = table[c - 128];
                    cc[ptr++] = v;
                }
            }
            return new String(cc, 0, ptr);
        }

        private static final char table[] = { 'Ç', 'ü', 'é', 'â', 'ä', 'à', 'å', 'ç', 'ê', 'ë', 'è', 'ï', 'î', 'ì', 'Ä', 'Å', 'É', 'æ', 'Æ', 'ô', 'ö', 'ò', 'û', 'ù', 'ÿ', 'Ö', 'Ü', '¢', '£', '¥', '₧', 'ƒ', 'á', 'í', 'ó', 'ú', 'ñ', 'Ñ', 'ª', 'º', '¿', '⌐', '¬', '½', '¼', '¡', '«', '»', '░', '▒', '▓', '│', '┤', '╡', '╢', '╖', '╕', '╣', '║', '╗', '╝', '╜', '╛', '┐', '└', '┴', '┬', '├', '─', '┼', '╞', '╟', '╚', '╔', '╩', '╦', '╠', '═', '╬', '╧', '╨', '╤', '╥', '╙', '╘', '╒', '╓', '╫', '╪', '┘', '┌', '█', '▄', '▌', '▐', '▀', 'α', 'ß', 'Γ', 'π', 'Σ', 'σ', 'µ', 'τ', 'Φ', 'Θ', 'Ω', 'δ', '∞', 'φ', 'ε', '∩', '≡', '±', '≥', '≤', '⌠', '⌡', '÷', '≈', '°', '∙', '·', '√', 'ⁿ', '²', '■', ' ' };

        static {
            for (int k = 0; k < table.length; ++k) c2b.put(table[k], k + 128);
        }
    }

    private static class SymbolConversion implements ExtraEncoding {

        private static final IntHashtable t1 = new IntHashtable();

        private static final IntHashtable t2 = new IntHashtable();

        private IntHashtable translation;

        SymbolConversion(boolean symbol) {
            if (symbol) translation = t1; else translation = t2;
        }

        public byte[] charToByte(String text, String encoding) {
            char cc[] = text.toCharArray();
            byte b[] = new byte[cc.length];
            int ptr = 0;
            int len = cc.length;
            for (int k = 0; k < len; ++k) {
                char c = cc[k];
                byte v = (byte) translation.get((int) c);
                if (v != 0) b[ptr++] = v;
            }
            if (ptr == len) return b;
            byte b2[] = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public String byteToChar(byte[] b, String encoding) {
            return null;
        }

        private static final char table1[] = { ' ', '!', '∀', '#', '∃', '%', '&', '∋', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '≅', 'Α', 'Β', 'Χ', 'Δ', 'Ε', 'Φ', 'Γ', 'Η', 'Ι', 'ϑ', 'Κ', 'Λ', 'Μ', 'Ν', 'Ο', 'Π', 'Θ', 'Ρ', 'Σ', 'Τ', 'Υ', 'ς', 'Ω', 'Ξ', 'Ψ', 'Ζ', '[', '∴', ']', '⊥', '_', '̅', 'α', 'β', 'χ', 'δ', 'ε', 'ϕ', 'γ', 'η', 'ι', 'φ', 'κ', 'λ', 'μ', 'ν', 'ο', 'π', 'θ', 'ρ', 'σ', 'τ', 'υ', 'ϖ', 'ω', 'ξ', 'ψ', 'ζ', '{', '|', '}', '~', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '€', 'ϒ', '′', '≤', '⁄', '∞', 'ƒ', '♣', '♦', '♥', '♠', '↔', '←', '↑', '→', '↓', '°', '±', '″', '≥', '×', '∝', '∂', '•', '÷', '≠', '≡', '≈', '…', '│', '─', '↵', 'ℵ', 'ℑ', 'ℜ', '℘', '⊗', '⊕', '∅', '∩', '∪', '⊃', '⊇', '⊄', '⊂', '⊆', '∈', '∉', '∠', '∇', '®', '©', '™', '∏', '√', '•', '¬', '∧', '∨', '⇔', '⇐', '⇑', '⇒', '⇓', '◊', '〈', '\0', '\0', '\0', '∑', '⎛', '⎜', '⎝', '⎡', '⎢', '⎣', '⎧', '⎨', '⎩', '⎪', '\0', '〉', '∫', '⌠', '⎮', '⌡', '⎞', '⎟', '⎠', '⎤', '⎥', '⎦', '⎫', '⎬', '⎭', '\0' };

        private static final char table2[] = { ' ', '✁', '✂', '✃', '✄', '☎', '✆', '✇', '✈', '✉', '☛', '☞', '✌', '✍', '✎', '✏', '✐', '✑', '✒', '✓', '✔', '✕', '✖', '✗', '✘', '✙', '✚', '✛', '✜', '✝', '✞', '✟', '✠', '✡', '✢', '✣', '✤', '✥', '✦', '✧', '★', '✩', '✪', '✫', '✬', '✭', '✮', '✯', '✰', '✱', '✲', '✳', '✴', '✵', '✶', '✷', '✸', '✹', '✺', '✻', '✼', '✽', '✾', '✿', '❀', '❁', '❂', '❃', '❄', '❅', '❆', '❇', '❈', '❉', '❊', '❋', '●', '❍', '■', '❏', '❐', '❑', '❒', '▲', '▼', '◆', '❖', '◗', '❘', '❙', '❚', '❛', '❜', '❝', '❞', ' ', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', ' ', '❡', '❢', '❣', '❤', '❥', '❦', '❧', '♣', '♦', '♥', '♠', '①', '②', '③', '④', '⑤', '⑥', '⑦', '⑧', '⑨', '⑩', '❶', '❷', '❸', '❹', '❺', '❻', '❼', '❽', '❾', '❿', '➀', '➁', '➂', '➃', '➄', '➅', '➆', '➇', '➈', '➉', '➊', '➋', '➌', '➍', '➎', '➏', '➐', '➑', '➒', '➓', '➔', '→', '↔', '↕', '➘', '➙', '➚', '➛', '➜', '➝', '➞', '➟', '➠', '➡', '➢', '➣', '➤', '➥', '➦', '➧', '➨', '➩', '➪', '➫', '➬', '➭', '➮', '➯', ' ', '➱', '➲', '➳', '➴', '➵', '➶', '➷', '➸', '➹', '➺', '➻', '➼', '➽', '➾', ' ' };

        static {
            for (int k = 0; k < table1.length; ++k) {
                int v = (int) table1[k];
                if (v != 0) t1.put(v, k + 32);
            }
            for (int k = 0; k < table2.length; ++k) {
                int v = (int) table2[k];
                if (v != 0) t2.put(v, k + 32);
            }
        }
    }

    private static class SymbolTTConversion implements ExtraEncoding {

        public byte[] charToByte(String text, String encoding) {
            char ch[] = text.toCharArray();
            byte b[] = new byte[ch.length];
            int ptr = 0;
            int len = ch.length;
            for (int k = 0; k < len; ++k) {
                char c = ch[k];
                if ((c & 0xff00) == 0 || (c & 0xff00) == 0xf000) b[ptr++] = (byte) c;
            }
            if (ptr == len) return b;
            byte b2[] = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public String byteToChar(byte[] b, String encoding) {
            return null;
        }
    }
}
