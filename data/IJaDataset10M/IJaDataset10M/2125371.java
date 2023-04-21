package org.apache.xmlbeans.impl.piccolo.xml;

import org.apache.xmlbeans.impl.piccolo.io.CharsetDecoder;
import org.apache.xmlbeans.impl.piccolo.io.IllegalCharException;
import java.io.CharConversionException;

/**
 * Converts bytes to ISO8859-1 characters while converting
 * carriage returns into linefeeds and CRLF into LF.
 * Checks for invalid XML characters.
 *
 * @author Yuval Oren, yuval@bluecast.com
 * @version $Revision: 1.5 $
 */
public final class ISO8859_1XMLDecoder implements XMLDecoder {

    private boolean sawCR = false;

    public CharsetDecoder newCharsetDecoder() {
        return newXMLDecoder();
    }

    public XMLDecoder newXMLDecoder() {
        return new ISO8859_1XMLDecoder();
    }

    public int minBytesPerChar() {
        return 1;
    }

    public int maxBytesPerChar() {
        return 1;
    }

    public void reset() {
        sawCR = false;
    }

    public void decode(byte[] in_buf, int in_off, int in_len, char[] out_buf, int out_off, int out_len, int[] result) throws CharConversionException {
        internalDecode(in_buf, in_off, in_len, out_buf, out_off, out_len, result, false);
    }

    public void decodeXMLDecl(byte[] in_buf, int in_off, int in_len, char[] out_buf, int out_off, int out_len, int[] result) throws CharConversionException {
        internalDecode(in_buf, in_off, in_len, out_buf, out_off, out_len, result, true);
    }

    private void internalDecode(byte[] in_buf, int in_off, int in_len, char[] out_buf, int out_off, int out_len, int[] result, boolean decodeDecl) throws CharConversionException {
        int i, o;
        inputLoop: for (i = o = 0; i < in_len && o < out_len; i++) {
            char c = (char) (0xFF & in_buf[in_off + i]);
            if (c >= 0x20) {
                sawCR = false;
                out_buf[out_off + o++] = (char) c;
            } else {
                switch(c) {
                    case '\n':
                        if (sawCR) {
                            sawCR = false;
                        } else out_buf[out_off + o++] = '\n';
                        break;
                    case '\r':
                        sawCR = true;
                        out_buf[out_off + o++] = '\n';
                        break;
                    case '\t':
                        out_buf[out_off + o++] = '\t';
                        break;
                    default:
                        if (decodeDecl) break inputLoop; else throw new IllegalCharException("Illegal XML character: 0x" + Integer.toHexString(c));
                }
            }
        }
        result[0] = i;
        result[1] = o;
    }
}
