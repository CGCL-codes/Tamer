package jcontrol.util;

public class Helper {

    public static String toHex(byte[] b) {
        String s = "";
        String d = "";
        if (b == null) {
            return " ";
        } else {
            for (int i = 0; i < b.length; i++) {
                d = Integer.toHexString((int) b[i]).toUpperCase();
                d = ("00".concat(d)).substring(d.length());
                s = s.concat(d);
                s = s.concat(" ");
            }
        }
        return s;
    }

    public static String toHex(long i) {
        String d = "0000000000000000".concat(Long.toHexString(i).toUpperCase());
        return d.substring(d.length() - 16);
    }

    public static String toHex(int i) {
        String d = "00000000".concat(Integer.toHexString(i).toUpperCase());
        return d.substring(d.length() - 8);
    }

    public static String toHex(short s) {
        String d = "0000".concat(Integer.toHexString((int) s).toUpperCase());
        return d.substring(d.length() - 4);
    }

    public static String toHex(byte b) {
        String d = "00".concat(Integer.toHexString((int) b).toUpperCase());
        return d.substring(d.length() - 2);
    }

    public static byte[] hexStringtoArray(String txt) {
        int i = 0;
        int v = 0;
        byte[] value = null;
        txt = txt.trim();
        if (txt.equals("") || (txt == null)) {
            value = new byte[0];
        } else {
            while ((i = txt.indexOf(" ")) >= 0) {
                txt = txt.substring(0, i) + txt.substring(i + 1, txt.length());
            }
            value = new byte[txt.length() / 2];
            for (i = 0; i < txt.length() / 2; i++) {
                v = Integer.parseInt(txt.substring(i * 2, i * 2 + 2), 16);
                value[i] = (byte) (v % 0x100);
            }
        }
        return value;
    }

    public static byte[] hexStringtoArray(String txt, int len) {
        byte[] data = new byte[len];
        byte[] fulldata = hexStringtoArray(txt);
        for (int i = 0; (i < len) && (i < fulldata.length); i++) {
            data[i] = fulldata[i];
        }
        return data;
    }
}
