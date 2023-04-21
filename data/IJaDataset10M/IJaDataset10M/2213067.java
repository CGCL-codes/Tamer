package it.infocostitre.bean;

import java.util.Collection;

public class Util {

    public static boolean isEmpty(String str) {
        boolean result = true;
        if (str == null) ; else {
            str = str.trim();
            if (str.length() > 0) result = false;
        }
        return result;
    }

    public static boolean isEmpty(Collection<?> collection) {
        boolean result = true;
        if (collection == null) result = true; else if (collection.isEmpty()) result = true; else result = false;
        return result;
    }
}
