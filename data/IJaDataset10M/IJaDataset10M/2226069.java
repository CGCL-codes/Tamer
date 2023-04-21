package com.scbeta.helpers;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author aaa
 */
public class StringHelper {

    public static boolean arrayContainsString(String[] array, String key) {
        for (String tmpString : array) {
            if (tmpString.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static String GetLeftString(String source, String strTail) {
        return GetLeftString(source, strTail, false);
    }

    public static String GetLeftString(String source, String strTail, boolean KeepHeadAndTail) {
        return GetMiddleString(source, "", strTail, KeepHeadAndTail);
    }

    public static String GetRightString(String source, String strHead) {
        return GetRightString(source, strHead, false);
    }

    public static String GetRightString(String source, String strHead, boolean KeepHeadAndTail) {
        return GetMiddleString(source, strHead, "", KeepHeadAndTail);
    }

    public static String GetMiddleString(String source, String strHead, String strTail) {
        return GetMiddleString(source, strHead, strTail, false);
    }

    public static String GetMiddleString(String source, String strHead, String strTail, boolean KeepHeadAndTail) {
        try {
            int indexHead, indexTail;
            if (strHead == null || strHead.isEmpty()) {
                indexHead = 0;
            } else {
                indexHead = source.indexOf(strHead);
            }
            if (strTail == null || strTail.isEmpty()) {
                indexTail = source.length();
            } else {
                indexTail = source.indexOf(strTail, indexHead + strHead.length());
            }
            if (indexTail < 0) {
                indexTail = source.length();
            }
            String rtnStr = "";
            if ((indexHead >= 0) && (indexTail >= 0)) {
                if (KeepHeadAndTail) {
                    rtnStr = source.substring(indexHead, indexTail + strTail.length());
                } else {
                    rtnStr = source.substring(indexHead + strHead.length(), indexTail);
                }
            }
            return rtnStr;
        } catch (Exception ex) {
            return "";
        }
    }

    public static SinoDetect encodingDetecter = new SinoDetect();

    public static String GetStringFromHexString(String hexString) {
        String[] StringArray = hexString.split(":");
        byte[] buffer = new byte[StringArray.length];
        for (int i = 0; i <= StringArray.length - 1; i++) {
            String tmpString = StringArray[i];
            buffer[i] = Byte.valueOf(tmpString, 16);
        }
        return GetStringFromBytes(buffer).trim();
    }

    public static String GetStringFromBytes(byte[] buffer) {
        return GetStringFromBytes(buffer, 0, buffer.length);
    }

    public static String GetStringFromBytes(byte[] buffer, int startIndex, int length) {
        if (buffer == null || buffer.length == 0 || length <= 0) {
            return "";
        }
        byte[] tmpBuffer = new byte[length];
        System.arraycopy(buffer, startIndex, tmpBuffer, 0, length);
        int encodeIndex = encodingDetecter.detectEncoding(tmpBuffer);
        String encodingName;
        if (encodeIndex == SinoDetect.OTHER) {
            encodeIndex = SinoDetect.UTF8;
        }
        encodingName = SinoDetect.nicename[encodeIndex];
        try {
            return new String(buffer, 0, length, encodingName);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
