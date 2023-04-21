package util;

public class Format {

    public static String LengthString(int i_m) {
        String str_m = String.valueOf(i_m);
        String str = "0000000";
        str_m = str.substring(0, 7 - str_m.length()) + str_m;
        return str_m;
    }

    public static String LengthString2(String str_m, int str_l) {
        int length = str_m.length();
        for (; length < str_l; length++) {
            str_m = "0" + str_m;
        }
        return str_m;
    }

    public static String IntToString(int str, int len) {
        String str_m = Integer.toString(str);
        int length = str_m.length();
        for (; length < len; length++) {
            str_m = "0" + str_m;
        }
        return str_m;
    }
}
