package com.jecelyin.util;

public class ArrayUtil {

    public static boolean inArray(String value, String[] strArray) {
        for (String v : strArray) {
            if (v == value) return true;
        }
        return false;
    }

    public static boolean inArray(int value, int[] intArray) {
        for (int v : intArray) {
            if (v == value) return true;
        }
        return false;
    }

    public static int idealCharArraySize(int need) {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) if (need <= (1 << i) - 12) return (1 << i) - 12;
        return need;
    }
}
