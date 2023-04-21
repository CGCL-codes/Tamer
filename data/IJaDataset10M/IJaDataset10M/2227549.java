package com.art.anette.common;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    private StringUtils() {
    }

    /**
     * Filtert HTML-Tags aus einem String.
     *
     * @param input Eingabe-String.
     * @return String ohne HTML-Tags.
     */
    public static String filterHTML(final String input) {
        StringBuilder result = new StringBuilder(input);
        int pos1, pos2;
        do {
            pos1 = result.indexOf("<");
            pos2 = result.indexOf(">", pos1);
            if (pos2 > pos1 && pos1 >= 0 && pos2 > 0) {
                result.delete(pos1, pos2 + 1);
            }
        } while (pos1 >= 0 && pos2 > 0 && pos2 > pos1);
        return result.toString();
    }

    public static String join(List<?> list, String delimeter, String lastDelimeter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            Object o = list.get(i);
            sb.append(o);
            if (i != list.size() - 2) {
                sb.append(delimeter);
            }
        }
        if (list.size() > 1) {
            sb.append(lastDelimeter);
        }
        if (!list.isEmpty()) {
            sb.append(list.get(list.size() - 1));
        }
        return sb.toString();
    }

    public static String join(Object[] list, String delimeter) {
        return join(Arrays.asList(list), delimeter);
    }

    public static String join(List<?> objects, String delimeter) {
        return join(objects, delimeter, delimeter);
    }
}
