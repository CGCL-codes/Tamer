package com.hqsolution.hqserver.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IdGenerator {

    public static List<String> HEADER = new ArrayList<String>();

    public static int seq = 0;

    static {
        StringBuilder temp = new StringBuilder();
        for (char j = 'A'; j < 'Z'; j++) {
            temp.append(j);
        }
        for (int k = 0; k < 10; k++) {
            temp.append(k);
        }
        for (int i = 0; i < temp.length(); i++) {
            for (int j = 0; j < temp.length(); j++) {
                HEADER.add("" + temp.charAt(i) + temp.charAt(j));
            }
        }
        Random rd = new Random();
        Collections.shuffle(HEADER, rd);
    }

    public static String getId() {
        return new StringBuilder().append(getNextHeader()).append(Calendar.getInstance().getTimeInMillis()).toString();
    }

    private static synchronized String getNextHeader() {
        seq++;
        if (seq >= HEADER.size()) {
            seq = 0;
        }
        return HEADER.get(seq);
    }

    public static void main(String[] arg) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < 2000; i++) {
            String temp = IdGenerator.getId();
            System.out.println(temp);
            result.add(temp);
        }
        int len = result.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if (result.get(i).equals(result.get(j))) {
                    System.out.println("Duplicated");
                }
            }
        }
    }
}
