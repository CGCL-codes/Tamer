package net.woodstock.rockapi.util;

import java.text.Collator;
import java.util.Comparator;

public class StringComparator implements Comparator<String> {

    private static StringComparator comparator;

    private Collator collator;

    private StringComparator() {
        super();
        this.collator = Collator.getInstance();
    }

    public int compare(String o1, String o2) {
        return this.collator.compare(o1, o2);
    }

    public static StringComparator getInstance() {
        if (StringComparator.comparator == null) {
            synchronized (StringComparator.class) {
                if (StringComparator.comparator == null) {
                    StringComparator.comparator = new StringComparator();
                }
            }
        }
        return StringComparator.comparator;
    }
}
