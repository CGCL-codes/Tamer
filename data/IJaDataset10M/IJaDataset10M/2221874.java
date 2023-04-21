package com.myres.util;

import java.sql.Timestamp;
import java.util.Random;

public class UIDTool {

    public static String generateUID() {
        return TimeTool.time2String("yyyyMMddHHmmss", new Timestamp(System.currentTimeMillis()));
    }

    public static String generateToken(int len) {
        String s = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int value = new Random().nextInt(422342434);
            value = value % 62;
            sb.append(s.charAt(value));
        }
        String token = sb.toString();
        return token;
    }

    public static void main(String[] ags) {
        String s = generateToken(10);
        System.out.print(s);
    }
}
