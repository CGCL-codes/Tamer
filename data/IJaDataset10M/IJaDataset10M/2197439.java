package com.iver.cit.gvsig.fmap.drivers.dbf;

import java.nio.charset.Charset;
import java.util.Hashtable;

public class DbfEncodings {

    private static DbfEncodings theInstance = null;

    private final int[] dbfIds = { 0x00, 0x02, 0x03, 0x04, 0x64, 0x65, 0x66, 0x67, 0x6A, 0x6B, 0x78, 0x79, 0x7A, 0x7B, 0x7C, 0x7D, 0x7E, 0x96, 0x97, 0x98, 0xC8, 0xC9, 0xCA, 0xCB, 0xF7, 0xF8, 0x99 };

    private Hashtable<Byte, String> dbfId2charset;

    DbfEncodings() {
    }

    public static DbfEncodings getInstance() {
        if (theInstance == null) theInstance = new DbfEncodings();
        return theInstance;
    }

    public String getCharsetForDbfId(int i) {
        String cCP = null;
        switch(i) {
            case (0x00):
                cCP = "UNKNOWN";
                Charset.defaultCharset().name();
                break;
            case (0x01):
                cCP = "US-ASCII";
                break;
            case (0x02):
                cCP = "Cp850";
                break;
            case (0x03):
                cCP = "Cp1252";
                break;
            case (0x04):
                cCP = "MacRoman";
                break;
            case (0x64):
                cCP = "Cp852";
                break;
            case (0x65):
                cCP = "Cp866";
                break;
            case (0x66):
                cCP = "Cp865";
                break;
            case (0x67):
                cCP = "Cp861";
                break;
            case (0x6A):
                cCP = "Cp737";
                break;
            case (0x6B):
                cCP = "Cp857";
                break;
            case (0x78):
                cCP = "Big5";
                break;
            case (0x79):
                cCP = "Cp949";
                break;
            case (0x7A):
                cCP = "GB2312";
                break;
            case (0x7B):
                cCP = "EUC-JP";
                break;
            case (0x7C):
                cCP = "Cp838";
                break;
            case (0x7D):
                cCP = "windows-1255";
                break;
            case (0x7E):
                cCP = "Cp1256";
                break;
            case (0x96):
                cCP = "cyrillic";
                break;
            case (0x97):
                cCP = "macintosh";
                break;
            case (0x98):
                cCP = "MacGreek";
                break;
            case (0xC8):
                cCP = "Cp1250";
                break;
            case (0xC9):
                cCP = "Cp1251";
                break;
            case (0xCA):
                cCP = "Cp1254";
                break;
            case (0xCB):
                cCP = "ISO-8859-7";
                break;
            case (0xF7):
                cCP = "ISO-8859-1";
                break;
            case (0xF8):
                cCP = "ISO-8859-15";
                break;
            case (0x99):
                cCP = "UTF-8";
                break;
        }
        return cCP;
    }

    public short getDbfIdForCharset(Charset charset) {
        short dbfId = 0x0;
        String s = charset.name();
        if (s.equalsIgnoreCase("US-ASCII")) dbfId = 0x01;
        if (s.equalsIgnoreCase("Cp850")) dbfId = 0x02;
        if (s.equalsIgnoreCase("Cp1252")) dbfId = 0x03;
        if (s.equalsIgnoreCase("MacRoman")) dbfId = 0x04;
        if (s.equalsIgnoreCase("Cp852")) dbfId = 0x64;
        if (s.equalsIgnoreCase("Cp866")) dbfId = 0x65;
        if (s.equalsIgnoreCase("Cp865")) dbfId = 0x66;
        if (s.equalsIgnoreCase("Cp861")) dbfId = 0x67;
        if (s.equalsIgnoreCase("Cp737")) dbfId = 0x6A;
        if (s.equalsIgnoreCase("Cp857")) dbfId = 0x6B;
        if (s.equalsIgnoreCase("Big5")) dbfId = 0x78;
        if (s.equalsIgnoreCase("Cp949")) dbfId = 0x79;
        if (s.equalsIgnoreCase("GB2312")) dbfId = 0x7A;
        if (s.equalsIgnoreCase("EUC-JP")) dbfId = 0x7B;
        if (s.equalsIgnoreCase("Cp838")) dbfId = 0x7C;
        if (s.equalsIgnoreCase("windows-1255")) dbfId = 0x7D;
        if (s.equalsIgnoreCase("Cp1256")) dbfId = 0x7E;
        if (s.equalsIgnoreCase("windows-1256")) dbfId = 0x7E;
        if (s.equalsIgnoreCase("cyrillic")) dbfId = 0x96;
        if (s.equalsIgnoreCase("macintosh")) dbfId = 0x97;
        if (s.equalsIgnoreCase("MacGreek")) dbfId = 0x98;
        if (s.equalsIgnoreCase("Cp1250")) dbfId = 0xC8;
        if (s.equalsIgnoreCase("windows-1250")) dbfId = 0xC8;
        if (s.equalsIgnoreCase("Cp1251")) dbfId = 0xC9;
        if (s.equalsIgnoreCase("windows-1251")) dbfId = 0xC9;
        if (s.equalsIgnoreCase("Cp1254")) dbfId = 0xCA;
        if (s.equalsIgnoreCase("windows-1254")) dbfId = 0xCA;
        if (s.equalsIgnoreCase("ISO-8859-7")) dbfId = 0xCB;
        if (s.equalsIgnoreCase("ISO-8859-1")) dbfId = 0xF7;
        if (s.equalsIgnoreCase("ISO-8859-15")) dbfId = 0xF8;
        if (s.equalsIgnoreCase("UTF-8")) dbfId = 0x99;
        System.out.println("getDbfIdForCharset " + s + " dbfId = " + dbfId);
        return dbfId;
    }

    public int[] getSupportedDbfLanguageIDs() {
        return dbfIds;
    }
}
