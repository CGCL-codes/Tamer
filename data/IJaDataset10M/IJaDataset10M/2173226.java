package org.util;

import java.util.ArrayList;
import java.util.List;

public class NumberUtils {

    public static List<Integer> list(int start, int end) {
        List l = new ArrayList(end - start);
        for (int i = start; i < end; i++) l.add(i);
        return l;
    }

    private NumberUtils() {
    }
}
