package acmsoft.util.codec;

import java.io.IOException;
import acmsoft.util.ArrayUtils;
import acmsoft.util.ByteBuffer;

public class Base64Codec {

    private static final char PADDING_CHARACTER = '=';

    private static final char[] ENCODING_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private static final int MAX_ENCODED_STRING_LENGTH = 76;

    private Base64Codec() {
    }

    public static String encode(byte[] btArrSrc) {
        int iByteQuantum = 3;
        int iCurPos = 0;
        int iArrLength = btArrSrc.length;
        StringBuffer sbResult = new StringBuffer();
        int iBytesToEncode = 0;
        int iStringLengthCounter = 0;
        for (iCurPos = 0; (iCurPos + iByteQuantum) <= iArrLength; iCurPos += iByteQuantum) {
            iBytesToEncode = 0;
            for (int i = 0; i < iByteQuantum; i++) {
                iBytesToEncode = iBytesToEncode << 8;
                iBytesToEncode = iBytesToEncode | (((int) btArrSrc[iCurPos + i]) & 0xFF);
            }
            sbResult.append(encodePortion(iBytesToEncode, 0));
            iStringLengthCounter += 4;
            if (iStringLengthCounter + 4 > MAX_ENCODED_STRING_LENGTH - 1) {
                sbResult.append('\n');
                iStringLengthCounter = 0;
            }
        }
        int iRestBytesAmount = iArrLength - iCurPos;
        if (iRestBytesAmount > 0) {
            iBytesToEncode = 0;
            for (int i = 0; i < iRestBytesAmount; i++) {
                iBytesToEncode = iBytesToEncode << 8;
                iBytesToEncode = iBytesToEncode | (((int) btArrSrc[iCurPos + i]) & 0xFF);
            }
            int iZerosToAppend = iByteQuantum - iRestBytesAmount;
            for (int i = 0; i < iZerosToAppend; i++) {
                iBytesToEncode = iBytesToEncode << 8;
            }
            sbResult.append(encodePortion(iBytesToEncode, iZerosToAppend));
        }
        return sbResult.toString();
    }

    private static String encodePortion(int iBytesToEncode, int iNumZerosToAppend) {
        String strResult = "";
        int iMask = 63;
        byte bCharIdx = 0;
        char cArrChars[] = new char[4];
        for (int i = 0; i < 4; i++) {
            bCharIdx = (byte) (iBytesToEncode & iMask);
            iBytesToEncode = iBytesToEncode >> 6;
            cArrChars[i] = ENCODING_TABLE[bCharIdx];
        }
        if (iNumZerosToAppend > 0) {
            for (int i = 0; i < iNumZerosToAppend; i++) {
                cArrChars[i] = PADDING_CHARACTER;
            }
        }
        strResult += cArrChars[3] + "" + cArrChars[2] + "" + cArrChars[1] + "" + cArrChars[0];
        return strResult;
    }

    public static byte[] decode(String strSrc) throws Base64InvalidStringException {
        ByteBuffer bb = new ByteBuffer();
        int iSrcStrLength = strSrc.length();
        char chCurrent = 0;
        byte bCurrent = 0;
        int iBytesToDecode = 0;
        int iQuantumCounter = 0;
        int iChSequenceLength = 4;
        int iPadsCounter = 0;
        for (int i = 0; i < iSrcStrLength; i++) {
            chCurrent = strSrc.charAt(i);
            bCurrent = (byte) ArrayUtils.indexOf(ENCODING_TABLE, chCurrent);
            switch(chCurrent) {
                case PADDING_CHARACTER:
                    {
                        bCurrent = 0;
                        iPadsCounter++;
                        break;
                    }
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                    {
                        continue;
                    }
            }
            if (bCurrent < 0) {
                throw new Base64InvalidStringException();
            }
            iBytesToDecode = iBytesToDecode << 6;
            iBytesToDecode = iBytesToDecode | bCurrent;
            if (iQuantumCounter == (iChSequenceLength - 1)) {
                bb.append(decodePortion(iBytesToDecode, iPadsCounter));
                iQuantumCounter = 0;
                iBytesToDecode = 0;
            } else {
                iQuantumCounter++;
            }
        }
        return bb.getBytes();
    }

    private static byte[] decodePortion(int iBytesToDecode, int iPadsAmount) {
        byte[] btArrPortion = new byte[3 - iPadsAmount];
        int iMask = 255;
        if (iPadsAmount > 0) {
            for (int i = 0; i < iPadsAmount; i++) {
                iBytesToDecode = iBytesToDecode >> 8;
            }
        }
        for (int i = btArrPortion.length - 1; i >= 0; i--) {
            btArrPortion[i] = (byte) (iBytesToDecode & iMask);
            iBytesToDecode = iBytesToDecode >> 8;
        }
        return btArrPortion;
    }

    /**
 * Warning!
 * This class (due to resources limitation doesn't have String description.
 * This exception represent situation when input string in decoder is not
 * valid BASE64-encoded string
 * @author tsyganok
 */
    public static class Base64InvalidStringException extends IOException {

        Base64InvalidStringException() {
            super();
        }
    }
}
