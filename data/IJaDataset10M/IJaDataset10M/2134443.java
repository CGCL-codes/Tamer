package com.techstar.dmis.util;

/**
 * 字符串处理类
 * @author yjb
 *
 */
public class StringUtil {

    public static String getNumberStrWithZero(String number, int length, String s) {
        String dd = "";
        for (int i = 0; i < length - number.length(); i++) dd = dd + s;
        return dd + number;
    }

    public static void main(String[] s) {
        String mm = StringUtil.getNumberStrWithZero("666", 10, "0");
        System.out.println(mm);
    }
}
