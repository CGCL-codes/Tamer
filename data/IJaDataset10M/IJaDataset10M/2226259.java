package org.skeegenin.skuttil;

import java.util.Comparator;

public class CharSequenceComparator implements Comparator<CharSequence> {

    @Override
    public int compare(CharSequence o1, CharSequence o2) {
        return comp(o1, o2);
    }

    public static int comp(CharSequence o1, CharSequence o2) {
        if (o1 == o2) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        for (int i = 0; true; i++) {
            if (o1.length() <= i) {
                if (o2.length() <= i) break;
                return -1;
            }
            if (o2.length() <= i) return 1;
            if (o1.charAt(i) < o2.charAt(i)) return -1;
            if (o1.charAt(i) > o2.charAt(i)) return 1;
        }
        return 0;
    }
}
