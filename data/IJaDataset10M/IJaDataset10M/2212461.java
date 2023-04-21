package org.fao.waicent.util;

public class ParamSubst {

    public static String replace(String str, String find, String replace) {
        int i = str.indexOf(find);
        if (i == -1) {
            return str;
        }
        return str.substring(0, i) + replace + str.substring(i + find.length());
    }
}
