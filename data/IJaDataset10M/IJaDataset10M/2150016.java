package javacode.cn.seed.util;

public class ByteUtil {

    public static void main(String[] args) {
        byte b = 0xB;
        System.out.println(byte2HexStr(b));
        System.out.println(swapByte(b));
        byte[] ll = new byte[4];
        ll[0] = (byte) 0x71;
        ll[1] = 0x01;
        ll[2] = 0x01;
        ll[3] = 0x00;
        String str = "FE234857E3";
        byte[] bbbbb = hexFullString2Bytes(str);
    }

    public static int byte2Int(byte bytes[]) {
        int num = bytes[0] & 0xFF;
        num |= ((bytes[1] << 8) & 0xFF00);
        num |= ((bytes[2] << 16) & 0xFF0000);
        num |= ((bytes[3] << 24) & 0xFF000000);
        return num;
    }

    public static int byte2Int_high(byte bytes[]) {
        int num = bytes[3] & 0xFF;
        num |= ((bytes[2] << 8) & 0xFF00);
        num |= ((bytes[1] << 16) & 0xFF0000);
        num |= ((bytes[0] << 24) & 0xFF000000);
        return num;
    }

    public static long unsigned4BytesToLong(byte[] buf) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = 0;
        firstByte = (0x000000FF & ((int) buf[index]));
        secondByte = (0x000000FF & ((int) buf[index + 1]));
        thirdByte = (0x000000FF & ((int) buf[index + 2]));
        fourthByte = (0x000000FF & ((int) buf[index + 3]));
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    public static long pcapBytesToLong(byte[] buf, int pos) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = pos + 3;
        firstByte = (0x000000FF & ((int) (buf[index])));
        secondByte = (0x000000FF & ((int) (buf[index - 1])));
        thirdByte = (0x000000FF & ((int) (buf[index - 2])));
        fourthByte = (0x000000FF & ((int) (buf[index - 3])));
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    public static int swapByte(byte b) {
        int high = (0x0000000F & ((int) b));
        high = high << 4;
        int low = (int) b >> 4;
        return high + low;
    }

    public static byte[] hexFullString2Bytes(String strs) {
        int len = strs.length() / 2;
        if (strs.length() % 2 == 1) len++;
        byte[] ret = new byte[len];
        int i = 0;
        while (i < strs.length()) {
            String sub = strs.substring(i, i + 2);
            byte[] temp = sub.getBytes();
            ret[i / 2] = uniteBytes(temp[0], temp[1]);
            i = i + 2;
        }
        return ret;
    }

    public static byte hexString2Bytes(String src) {
        byte[] tmp = src.getBytes();
        byte ret = uniteBytes(tmp[0], tmp[1]);
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 | _b1);
        return ret;
    }

    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) hs = hs + "0" + stmp; else hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static String byte2HexStr(byte b) {
        String hs = "";
        String stmp = "";
        stmp = (Integer.toHexString(b & 0XFF));
        if (stmp.length() == 1) hs = hs + "0" + stmp; else hs = hs + stmp;
        return hs.toUpperCase();
    }

    public static String byte2Ascii(byte[] bytes) {
        String s = "";
        for (byte b : bytes) s += (char) b;
        return s;
    }

    public static String getStringAddress(byte[] bytes) {
        String s = "";
        for (byte b : bytes) s += String.valueOf((int) b & 0xFF) + ".";
        return s;
    }
}
