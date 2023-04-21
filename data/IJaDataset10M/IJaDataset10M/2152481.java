package com.psm.core.commons;

public class StringConverter {

    public static String convertStringToId(String stringa) {
        String nuova = "";
        if (stringa == null) return "-1";
        if (stringa.trim().length() == 0) return "-1";
        for (int i = 0; i < stringa.length(); i++) {
            nuova += ((int) stringa.charAt(i));
        }
        return nuova;
    }
}
