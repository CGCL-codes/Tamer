package jlib.misc;

public class AsciiEbcdicConverter {

    private static int gs_tEbcdic[] = null;

    private static int gs_tAscii[] = null;

    public static void create() {
        new AsciiEbcdicConverter();
    }

    private AsciiEbcdicConverter() {
        if (gs_tEbcdic == null && gs_tAscii == null) {
            gs_tEbcdic = new int[256];
            gs_tAscii = new int[256];
            init();
        }
    }

    static void init() {
        addChar("00", "00");
        addChar("01", "01");
        addChar("02", "02");
        addChar("03", "03");
        addChar("04", "37");
        addChar("05", "2D");
        addChar("06", "2E");
        addChar("07", "2F");
        addChar("08", "16");
        addChar("09", "05");
        addChar("0A", "15");
        addChar("0B", "0B");
        addChar("0C", "0C");
        addChar("0D", "0D");
        addChar("0E", "0E");
        addChar("0F", "0F");
        addChar("10", "10");
        addChar("11", "11");
        addChar("12", "12");
        addChar("13", "13");
        addChar("14", "3C");
        addChar("15", "3D");
        addChar("16", "32");
        addChar("17", "26");
        addChar("18", "18");
        addChar("19", "19");
        addChar("1A", "3F");
        addChar("1B", "27");
        addChar("1C", "1C");
        addChar("1D", "1D");
        addChar("1E", "1E");
        addChar("1F", "1F");
        addChar("20", "40");
        addChar("21", "4F");
        addChar("22", "7F");
        addChar("23", "7B");
        addChar("24", "5B");
        addChar("25", "6C");
        addChar("26", "50");
        addChar("27", "7D");
        addChar("28", "4D");
        addChar("29", "5D");
        addChar("2A", "5C");
        addChar("2B", "4E");
        addChar("2C", "6B");
        addChar("2D", "60");
        addChar("2E", "4B");
        addChar("2F", "61");
        addChar("30", "F0");
        addChar("31", "F1");
        addChar("32", "F2");
        addChar("33", "F3");
        addChar("34", "F4");
        addChar("35", "F5");
        addChar("36", "F6");
        addChar("37", "F7");
        addChar("38", "F8");
        addChar("39", "F9");
        addChar("3A", "7A");
        addChar("3B", "5E");
        addChar("3C", "4C");
        addChar("3D", "7E");
        addChar("3E", "6E");
        addChar("3F", "6F");
        addChar("40", "7C");
        addChar("41", "C1");
        addChar("42", "C2");
        addChar("43", "C3");
        addChar("44", "C4");
        addChar("45", "C5");
        addChar("46", "C6");
        addChar("47", "C7");
        addChar("48", "C8");
        addChar("49", "C9");
        addChar("4A", "D1");
        addChar("4B", "D2");
        addChar("4C", "D3");
        addChar("4D", "D4");
        addChar("4E", "D5");
        addChar("4F", "D6");
        addChar("50", "D7");
        addChar("51", "D8");
        addChar("52", "D9");
        addChar("53", "E2");
        addChar("54", "E3");
        addChar("55", "E4");
        addChar("56", "E5");
        addChar("57", "E6");
        addChar("58", "E7");
        addChar("59", "E8");
        addChar("5A", "E9");
        addChar("5B", "4A");
        addChar("5C", "E0");
        addChar("5D", "5A");
        addChar("5E", "5F");
        addChar("5F", "6D");
        addChar("60", "79");
        addChar("61", "81");
        addChar("62", "82");
        addChar("63", "83");
        addChar("64", "84");
        addChar("65", "85");
        addChar("66", "86");
        addChar("67", "87");
        addChar("68", "88");
        addChar("69", "89");
        addChar("6A", "91");
        addChar("6B", "92");
        addChar("6C", "93");
        addChar("6D", "94");
        addChar("6E", "95");
        addChar("6F", "96");
        addChar("70", "97");
        addChar("71", "98");
        addChar("72", "99");
        addChar("73", "A2");
        addChar("74", "A3");
        addChar("75", "A4");
        addChar("76", "A5");
        addChar("77", "A6");
        addChar("78", "A7");
        addChar("79", "A8");
        addChar("7A", "A9");
        addChar("7B", "C0");
        addChar("7C", "BB");
        addChar("7D", "D0");
        addChar("7E", "A1");
        addChar("7F", "07");
        addChar("80", "20");
        addChar("81", "21");
        addChar("82", "22");
        addChar("83", "23");
        addChar("84", "24");
        addChar("85", "25");
        addChar("86", "06");
        addChar("87", "17");
        addChar("88", "28");
        addChar("89", "29");
        addChar("8A", "2A");
        addChar("8B", "2B");
        addChar("8C", "2C");
        addChar("8D", "09");
        addChar("8E", "0A");
        addChar("8F", "1B");
        addChar("90", "30");
        addChar("91", "31");
        addChar("92", "1A");
        addChar("93", "33");
        addChar("94", "34");
        addChar("95", "35");
        addChar("96", "36");
        addChar("97", "08");
        addChar("98", "38");
        addChar("99", "39");
        addChar("9A", "3A");
        addChar("9B", "3B");
        addChar("9C", "04");
        addChar("9D", "14");
        addChar("9E", "3E");
        addChar("9F", "FF");
        addChar("A0", "41");
        addChar("A1", "AA");
        addChar("A2", "B0");
        addChar("A3", "B1");
        addChar("A4", "9F");
        addChar("A5", "B2");
        addChar("A6", "6A");
        addChar("A7", "B5");
        addChar("A8", "BD");
        addChar("A9", "B4");
        addChar("AA", "9A");
        addChar("AB", "8A");
        addChar("AC", "BA");
        addChar("AD", "CA");
        addChar("AE", "AF");
        addChar("AF", "BC");
        addChar("B0", "90");
        addChar("B1", "8F");
        addChar("B2", "EA");
        addChar("B3", "FA");
        addChar("B4", "BE");
        addChar("B5", "A0");
        addChar("B6", "B6");
        addChar("B7", "B3");
        addChar("B8", "9D");
        addChar("B9", "DA");
        addChar("BA", "9B");
        addChar("BB", "8B");
        addChar("BC", "B7");
        addChar("BD", "B8");
        addChar("BE", "B9");
        addChar("BF", "AB");
        addChar("C0", "64");
        addChar("C1", "65");
        addChar("C2", "62");
        addChar("C3", "66");
        addChar("C4", "63");
        addChar("C5", "67");
        addChar("C6", "9E");
        addChar("C7", "68");
        addChar("C8", "74");
        addChar("C9", "71");
        addChar("CA", "72");
        addChar("CB", "73");
        addChar("CC", "78");
        addChar("CD", "75");
        addChar("CE", "76");
        addChar("CF", "77");
        addChar("D0", "AC");
        addChar("D1", "69");
        addChar("D2", "ED");
        addChar("D3", "EE");
        addChar("D4", "EB");
        addChar("D5", "EF");
        addChar("D6", "EC");
        addChar("D7", "BF");
        addChar("D8", "80");
        addChar("D9", "FD");
        addChar("DA", "FE");
        addChar("DB", "FB");
        addChar("DC", "FC");
        addChar("DD", "AD");
        addChar("DE", "AE");
        addChar("DF", "59");
        addChar("E0", "44");
        addChar("E1", "45");
        addChar("E2", "42");
        addChar("E3", "46");
        addChar("E4", "43");
        addChar("E5", "47");
        addChar("E6", "9C");
        addChar("E7", "48");
        addChar("E8", "54");
        addChar("E9", "51");
        addChar("EA", "52");
        addChar("EB", "53");
        addChar("EC", "58");
        addChar("ED", "55");
        addChar("EE", "56");
        addChar("EF", "57");
        addChar("F0", "8C");
        addChar("F1", "49");
        addChar("F2", "CD");
        addChar("F3", "CE");
        addChar("F4", "CB");
        addChar("F5", "CF");
        addChar("F6", "CC");
        addChar("F7", "E1");
        addChar("F8", "70");
        addChar("F9", "DD");
        addChar("FA", "DE");
        addChar("FB", "DB");
        addChar("FC", "DC");
        addChar("FD", "8D");
        addChar("FE", "8E");
        addChar("FF", "FF");
    }

    private static void addChar(String csAscii, String csEbcdic) {
        int nAscii = getCode(csAscii);
        int nEbcdic = getCode(csEbcdic);
        gs_tEbcdic[nAscii] = nEbcdic;
        gs_tAscii[nEbcdic] = nAscii;
    }

    public static int getEbcdicCorrepondingCode(int nAscii) {
        if (nAscii < 256) return gs_tEbcdic[nAscii];
        return 0;
    }

    private static int getCode(String cs) {
        char cHigh = cs.charAt(0);
        int nHigh = getHexValue(cHigh);
        char cLow = cs.charAt(1);
        int nLow = getHexValue(cLow);
        int n = (nHigh * 16) + nLow;
        return n;
    }

    public static String getEbcdicHexaValue(int nAscii) {
        if (nAscii >= 0 && nAscii <= 255) {
            int nEbcdic = gs_tEbcdic[nAscii];
            return getHexaValue(nEbcdic);
        }
        return "Invalid char code";
    }

    public static String getHexaValue(int nAscii) {
        if (nAscii >= 0 && nAscii <= 255) {
            int nHigh = nAscii / 16;
            char cHigh = getHexChar(nHigh);
            int nLow = nAscii % 16;
            char cLow = getHexChar(nLow);
            String cs = new String();
            cs += cHigh;
            cs += cLow;
            return cs;
        }
        return "Invalid char code";
    }

    private static char getHexChar(int n) {
        if (n <= 9) return (char) (n + '0');
        return (char) ((n - 10) + 'A');
    }

    private static int getHexValue(char c) {
        if (c <= '9') return (c - '0');
        return (c - 'A') + 10;
    }

    public static int compareEbcdic(int n1, int n2) {
        int e1 = AsciiEbcdicConverter.getEbcdicCorrepondingCode(n1);
        int e2 = AsciiEbcdicConverter.getEbcdicCorrepondingCode(n2);
        if (e1 < e2) return -1;
        if (e1 > e2) return 1;
        return 0;
    }

    public static char getEbcdicChar(char cAscii) {
        int nEbcdic = gs_tEbcdic[cAscii];
        char cEbcdic = (char) nEbcdic;
        return cEbcdic;
    }

    public static char getAsciiChar(char cEbcdic) {
        int nAscii = gs_tAscii[cEbcdic];
        char cAscii = (char) nAscii;
        return cAscii;
    }

    public static char getAsciiChar(byte byEbcdic) {
        int nEbcdic = byEbcdic;
        if (nEbcdic < 0) nEbcdic += 256;
        int nAscii = gs_tAscii[nEbcdic];
        char cAscii = (char) nAscii;
        return cAscii;
    }

    public static byte getAsciiByte(byte byEbcdic) {
        int nEbcdic = byEbcdic;
        if (nEbcdic < 0) nEbcdic += 256;
        int nAscii = gs_tAscii[nEbcdic];
        byte byteAscii = (byte) nAscii;
        return byteAscii;
    }

    public static int getAsAscii(int nEbcdic) {
        int nAscii = gs_tAscii[nEbcdic];
        return nAscii;
    }

    public static byte getEbcdicByte(byte byAscii) {
        int nAscii = byAscii;
        if (nAscii < 0) nAscii += 256;
        int nEbcdic = gs_tEbcdic[nAscii];
        byte byteEbcdic = (byte) nEbcdic;
        return byteEbcdic;
    }

    public static String getEbcdicString(String csIn) {
        String csOut = new String();
        int nLg = csIn.length();
        for (int n = 0; n < nLg; n++) {
            char cIn = csIn.charAt(n);
            int nIn = cIn;
            int nEbcdic = gs_tEbcdic[nIn];
            char cEbcdic = (char) nEbcdic;
            csOut += cEbcdic;
        }
        return csOut;
    }

    public static byte[] convertUnicodeToEbcdic(char[] tChars) {
        int nLength = tChars.length;
        byte[] tOut = new byte[nLength];
        for (int n = 0; n < nLength; n++) {
            char c = tChars[n];
            byte b = (byte) c;
            int nIndex = b;
            if (nIndex < 0) nIndex += 256;
            int nOut = gs_tEbcdic[nIndex];
            tOut[n] = (byte) nOut;
        }
        return tOut;
    }

    public static char[] convertEbcdicToUnicode(byte[] tBytes) {
        int nLength = tBytes.length;
        char[] tOut = new char[nLength];
        for (int n = 0; n < nLength; n++) {
            tOut[n] = getAsciiChar(tBytes[n]);
        }
        return tOut;
    }

    public static char[] noConvertEbcdicToUnicode(byte[] tBytes) {
        return noConvertEbcdicToUnicode(tBytes, tBytes.length);
    }

    public static char[] noConvertEbcdicToUnicode(byte[] tBytes, int nLength) {
        char[] tChars = new char[nLength];
        for (int n = 0; n < nLength; n++) {
            tChars[n] = (char) tBytes[n];
        }
        return tChars;
    }

    public static byte[] noConvertUnicodeToEbcdic(char[] tChars) {
        byte[] tBytes = new byte[tChars.length];
        for (int n = 0; n < tChars.length; n++) {
            tBytes[n] = (byte) tChars[n];
        }
        return tBytes;
    }

    public static byte[] noConvertUnicodeToEbcdic(char[] tChars, int nSourceOffset, int nLength) {
        byte[] tBytes = new byte[nLength];
        for (int n = 0; n < nLength; n++) {
            tBytes[n] = (byte) tChars[nSourceOffset + n];
        }
        return tBytes;
    }

    public static void swapByteAsciiToEbcdic(byte tBytesData[], int nOffset, int nLength) {
        for (int n = 0; n < nLength; n++) {
            int nAscii = tBytesData[n + nOffset];
            if (nAscii < 0) nAscii += 256;
            int nEbcdic = gs_tEbcdic[nAscii];
            tBytesData[n + nOffset] = (byte) nEbcdic;
        }
    }

    public static void swapByteEbcdicToAscii(byte tBytesData[], int nOffset, int nLength) {
        for (int n = 0; n < nLength; n++) {
            int nEbcdic = tBytesData[n + nOffset];
            if (nEbcdic < 0) nEbcdic += 256;
            int nAscii = gs_tAscii[nEbcdic];
            tBytesData[n + nOffset] = (byte) nAscii;
        }
    }

    private static final byte AFP_ASCII_5A = (byte) 0x5D;

    private static final byte[] AFP_ASCII_PAGEFORMAT = { (byte) 0x4C, (byte) 0xBF, (byte) 0xAD };

    private static final byte[] AFP_ASCII_COPYGROUP = { (byte) 0x4C, (byte) 0xBF, (byte) 0xF6 };

    private static final byte[] AFP_ASCII_SEGMENT = { (byte) 0x4C, (byte) 0xAE, (byte) 0x5E };

    private static final byte[] AFP_ASCII_SFI = { (byte) 0x4C, (byte) 0xD3, (byte) 0xBA };

    public static void swapByteAsciiToEbcdicPrintAFP(byte tBytesData[], int nOffset, int nLength) {
        if (nLength > 6 && tBytesData[nOffset] == AFP_ASCII_5A) {
            if (isSpecialPrintAfp(tBytesData, nOffset, AFP_ASCII_COPYGROUP) || isSpecialPrintAfp(tBytesData, nOffset, AFP_ASCII_PAGEFORMAT)) {
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset, 1);
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 3, 3);
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 9, 8);
                if (nLength > 17) {
                    AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 17, nLength - 17);
                }
            } else if (isSpecialPrintAfp(tBytesData, nOffset, AFP_ASCII_SEGMENT)) {
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset, 1);
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 3, 3);
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 9, 8);
                if (nLength > 23) {
                    AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 23, nLength - 23);
                }
            } else if (isSpecialPrintAfp(tBytesData, nOffset, AFP_ASCII_SFI)) {
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset, 1);
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 3, 3);
                AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset + 9, nLength - 9);
            }
        } else {
            AsciiEbcdicConverter.swapByteAsciiToEbcdic(tBytesData, nOffset, nLength);
        }
    }

    private static final byte AFP_EBCDIC_5A = (byte) 0x5A;

    private static final byte[] AFP_EBCDIC_PAGEFORMAT = { (byte) 0xD3, (byte) 0xAB, (byte) 0xCA };

    private static final byte[] AFP_EBCDIC_COPYGROUP = { (byte) 0xD3, (byte) 0xAB, (byte) 0xCC };

    private static final byte[] AFP_EBCDIC_SEGMENT = { (byte) 0xD3, (byte) 0xAF, (byte) 0x5F };

    public static void swapByteEbcdicToAsciiPrintAFP(byte tBytesData[], int nOffset, int nLength) {
        if (nLength > 6 && tBytesData[nOffset] == AFP_EBCDIC_5A) {
            if (isSpecialPrintAfp(tBytesData, nOffset, AFP_EBCDIC_PAGEFORMAT) || isSpecialPrintAfp(tBytesData, nOffset, AFP_EBCDIC_COPYGROUP)) {
                AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset, 1);
                AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset + 3, 3);
                AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset + 9, 8);
                if (nLength > 17) {
                    AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset + 17, nLength - 17);
                }
            } else if (isSpecialPrintAfp(tBytesData, nOffset, AFP_EBCDIC_SEGMENT)) {
                AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset, 1);
                AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset + 3, 3);
                AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset + 9, 8);
                if (nLength > 23) {
                    AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset + 23, nLength - 23);
                }
            }
        } else {
            AsciiEbcdicConverter.swapByteEbcdicToAscii(tBytesData, nOffset, nLength);
        }
    }

    private static boolean isSpecialPrintAfp(byte tBytesData[], int nOffset, byte[] arrToCheck) {
        if (tBytesData[nOffset + 3] == arrToCheck[0] && tBytesData[nOffset + 4] == arrToCheck[1] && tBytesData[nOffset + 5] == arrToCheck[2]) return true; else return false;
    }
}
