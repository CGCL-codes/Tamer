package org.usajusaj.jtex.util;

import java.util.ArrayList;

/**
 * @author Matej Usaj
 */
public class Tools {

    public static String[] arrayListToStringArray(ArrayList<String> l) {
        return l.toArray(new String[l.size()]);
    }

    public static StringBuilder commatize(String... s) {
        return elseize(",", s);
    }

    public static StringBuilder spaceize(String... s) {
        return elseize(" ", s);
    }

    public static StringBuilder elseize(String delim, String... s) {
        if (s == null || s.length < 1 || delim == null) return new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (String x : s) sb.append(x).append(delim);
        return sb.replace(sb.length() - delim.length(), sb.length(), "");
    }
}
