package com.sun.pdfview.decrypt;

import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFParseException;
import java.nio.ByteBuffer;

/**
 * Performs identity decryption; that is, inputs aren't encrypted and
 * are returned right back.
 *
 * @Author Luke Kirby
 */
public class IdentityDecrypter implements PDFDecrypter {

    private static IdentityDecrypter INSTANCE = new IdentityDecrypter();

    public ByteBuffer decryptBuffer(String cryptFilterName, PDFObject streamObj, ByteBuffer streamBuf) throws PDFParseException {
        if (cryptFilterName != null) {
            throw new PDFParseException("This Encryption version does not support Crypt filters");
        }
        return streamBuf;
    }

    public String decryptString(int objNum, int objGen, String inputBasicString) {
        return inputBasicString;
    }

    public static IdentityDecrypter getInstance() {
        return INSTANCE;
    }

    public boolean isEncryptionPresent() {
        return false;
    }

    public boolean isOwnerAuthorised() {
        return false;
    }
}
