package net.asfun.jvalog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueChecker {

    public static boolean isEmail(String str) {
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
