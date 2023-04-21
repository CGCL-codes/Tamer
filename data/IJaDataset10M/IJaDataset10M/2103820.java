package org.freehold.io.base64;

/**
 * This object represents a current state of the Base64 encoder and its
 * access methods.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1995-1998
 * @version $Id: Base64EncoderState.java,v 1.1 2000-12-18 23:04:46 vtt Exp $
 * @see Base64EncoderInputStream
 * @see Base64DecoderInputStream
 */
class Base64EncoderState extends Base64State implements Base64Constants {

    /**
     * Initialize the proper triplet and octet offsets here.
     */
    public Base64EncoderState() {
        offset3 = 0;
        offset4 = 4;
    }

    /**
     * Convert the triplet from the source stream to the octet for the
     * output stream.
     */
    public void encode() {
        writeOctetByte(alphabet[(triplet[0] & 0xFC) >> 2]);
        writeOctetByte(alphabet[((triplet[0] & 0x03) << 4) | ((triplet[1] & 0xF0) >> 4)]);
        writeOctetByte(alphabet[((triplet[1] & 0x0F) << 2) | ((triplet[2] & 0xC0) >> 6)]);
        writeOctetByte(alphabet[triplet[2] & 0x3F]);
        padOctet();
    }

    /**
     * If the octet is not fully loaded yet, pad it with '='.
     */
    public void padOctet() {
        for (int pad = offset3 + 1; pad < 4; pad++) {
            octet[pad] = (byte) '=';
        }
        clearOctet();
    }

    public void padTriplet() {
        for (int pad = offset3; pad < 3; pad++) {
            triplet[pad] = (byte) 0;
        }
    }
}
