package org.tracfoundation.trac2001.form;

import org.tracfoundation.trac2001.util.ByteBuffer;
import org.tracfoundation.trac2001.util.TRACUtil;

/**
 * Utilities for accessing TRAC2001 forms.
 * Note: Have to use char variables as opposed to short variables because
 * short variables are signed and the >> and >>> operators don't work correctly.
 * @author Edith Mooers, Trac Foundation http://tracfoundation.org
 * @version 1.0 (c) 2001
 * @see <CODE>trac2001.form.Form</CODE>
 * @see <CODE>trac2001.form.RootNode</CODE>
 */
public class FormUtil {

    /**
     * The error code is zero, because this is never a valid offset.
     */
    public static int ERROR = 0;

    /**
     * Creates a hash used to look up form names.
     *
     * @param <CODE>byte []</CODE> the name of a trac form
     *
     * @return <CODE>char</CODE> the hash code for name, as a char
     * because the bitwise operations need to be on unsigned values.
     */
    public static char hash(byte[] name) {
        char code = 0;
        int i = name.length;
        while (i > 0) {
            code ^= name[--i];
            if (i > 0) {
                int tmp = code;
                tmp = tmp >>> 13;
                code <<= 3;
                code |= tmp;
            }
        }
        return code;
    }

    /**
     * Takes a character (2 bytes) and returns it as a two byte array.
     * The lower eight bits are in slot 1 in the array, the upper eight
     * bits are in slot 0 in the array.
     *
     * @param <CODE>char</CODE> the character to translate
     *
     * @return <CODE>byte []</CODE> the two byte repersentation of the input
     */
    public static byte[] charToBytes(char code) {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) code;
        code >>>= 8;
        buffer[1] = (byte) code;
        return buffer;
    }

    /**
     * Takes an array of two bytes and turns it into a character.  Assumes
     * that the lower eight bits are in slot 1 in the arrray, the upper eight
     * bits in slot 0 in the array.
     *
     * @param <CODE>byte []</CODE> the array to translate
     *
     * @return <CODE>char</CODE> the character represented by the input
     */
    public static char bytesToChar(byte[] code) {
        char tmp = (char) code[1];
        tmp <<= 8;
        tmp |= ((char) code[0] & 0x00FF);
        return tmp;
    }

    /**
     * @param <CODE>byte []</CODE> the name of a form.
     *
     * @return <CODE>boolean</CODE> true if the byte string ends 
     * with a dot '.', indicating a node.
     */
    public static boolean isNode(byte[] name) {
        return (name.length > 0 && name[name.length - 1] == (byte) '.');
    }

    /**
     * Converts text twiddles "<n>", where n is an integer 
     * between 0 and 126, to binary twiddles "~b", where b 
     * is the binary representation of the digit n,
     * and doubles tilde "~" characters (ascii 127).
     * Removes any comments between "<*" and "*>" deliniators.
     * Checks for a 'p' suffix on the twiddle and, if it is present, 
     * sets the eigth bit to trigger protected interpretation.
     *
     * @param <CODE>byte []</CODE> the text to convert
     *
     * @return <CODE>byte []</CODE> the converted text
     */
    public static byte[] twiddleToBinary(byte[] text) {
        ByteBuffer result = new ByteBuffer();
        for (int i = 0; i < text.length; i++) {
            if (text[i] == (byte) '<' && i + 1 < text.length) {
                int start = i;
                if (text[i + 1] == (byte) '*') {
                    int j = i + 2;
                    while (j < text.length) if (text[j++] == (byte) '*' && text[j] == (byte) '>') {
                        i = j;
                        break;
                    }
                } else if (Character.isDigit((char) text[i + 1])) {
                    boolean prot = false;
                    String s = "";
                    int k = 1;
                    for (; k <= 4 && i + k + 1 < text.length; k++) {
                        byte b = text[i + k];
                        if (b >= TRACUtil.ZERO && b <= TRACUtil.NINE) {
                            s += Character.getNumericValue((char) b);
                        } else {
                            if (b == (byte) 'p') {
                                prot = true;
                                k++;
                            }
                            break;
                        }
                    }
                    if (!s.equals("") && text[i + k] == (byte) '>') {
                        int twid = Integer.parseInt(s);
                        if (twid >= 0 && twid <= 126) {
                            if (prot) twid |= 0x80;
                            result.add((byte) '~');
                            result.add((byte) twid);
                            i += k;
                        }
                    }
                }
                if (i == start) result.add(text[i]);
            } else if (text[i] == (byte) '~') result.add(new byte[] { (byte) '~', (byte) '~' }); else result.add(text[i]);
        }
        return result.get();
    }

    /**
     * The inverse of twiddleToBinary does not recreate comments.
     *
     * @param <CODE>byte []</CODE> the text to convert
     *
     * @return <CODE>byte []</CODE> the converted text
     */
    public static byte[] binaryToTwiddle(byte[] text) {
        ByteBuffer result = new ByteBuffer();
        for (int i = 0; i < text.length; i++) {
            if (text[i] != (byte) '~') result.add(text[i]); else if (i + 1 <= text.length) {
                boolean prot = false;
                byte b = text[++i];
                if (b == 126) result.add((byte) '~'); else if ((b & 0x80) == 0x80) {
                    prot = true;
                    b ^= 0x80;
                }
                if (b >= 0 && b < 126) {
                    byte[] bnum = Integer.toString((int) b).getBytes();
                    result.add((byte) '<');
                    result.add(bnum);
                    if (prot == true) result.add((byte) 'p');
                    result.add((byte) '>');
                }
            }
        }
        return result.get();
    }

    /**
     * Checks the text in a ByteBuffer for binary twiddles and fills them in,
     * returns the filled in text.
     *
     * @param <CODE>byte []</CODE> text the buffer to check
     *
     * @param <CODE>byte [] []</CODE> a double array containing 
     * the byte strings to use as filler
     *
     * @param <CODE>boolean</CODE> true if the 'i' flag is set on 
     * this primitive, so protected twid's need to get parenthesis added
     */
    public static byte[] fillMacro(byte[] text, byte[][] fill, boolean interpret) {
        ByteBuffer result = new ByteBuffer();
        for (int i = 0; i < text.length; i++) {
            if (text[i] == (byte) '~') {
                boolean prot = false;
                byte twid = text[++i];
                if (twid == (byte) '~') result.add((byte) '~'); else if ((twid & 0x80) == 0x80) {
                    twid ^= 0x80;
                    prot = true;
                }
                if (twid >= 0 && twid < fill.length) {
                    if (prot == true && interpret) result.add((byte) '(');
                    result.add(fill[twid]);
                    if (prot == true && interpret) result.add((byte) ')');
                }
            } else result.add(text[i]);
        }
        return result.get();
    }

    /**
     * Take a byte arrray representing a TRAC form name
     * and hand back the path (with the dot).
     *
     * @param <CODE>byte []</CODE> the form name
     *
     * @return <CODE>byte []</CODE> the path name
     */
    public static byte[] getPath(byte[] name) {
        int end = name.length;
        if (end > 0 && name[end - 1] == '.') end--;
        while (end > 0) if (name[--end] == '.') {
            end++;
            break;
        }
        return TRACUtil.getBytes(name, 0, end);
    }

    /**
     * Take a byte array representing a TRAC form name
     * and hand back the actual name of the node.
     *
     * @param <CODE>byte []</CODE> the form name
     *
     * @return <CODE>byte []</CODE> the node name
     */
    public static byte[] getName(byte[] name) {
        int end = name.length;
        if (end > 0 && name[end - 1] == (byte) '.') end--;
        int start = end;
        while (start > 0) if (name[--start] == (byte) '.') {
            start++;
            break;
        }
        return TRACUtil.getBytes(name, start, end - start);
    }

    public static void main(String[] args) {
        String test = "This <1>, is <130> <57> <* not *> ~ a <* test";
        byte[] out = twiddleToBinary(test.getBytes());
        System.out.println(test);
        System.out.println(new String(out));
        byte[] in = binaryToTwiddle(out);
        System.out.println(new String(in));
    }
}
