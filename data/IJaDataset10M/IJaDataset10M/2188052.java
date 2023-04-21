package com.sync.extractor.mysql.conversion;

/**
 * This class contains general conversion methods, that are not related either
 * to little endian nor big endian encoding
 * 
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @version 1.0
 */
public class GeneralConversion {

    public static int unsignedByteToInt(byte b) {
        return 0xFF & b;
    }
}
