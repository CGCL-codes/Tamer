package org.openxava.util;

import java.util.Comparator;
import org.openxava.mapping.CmpField;

/**
 * Create on 26/02/2010 (10:33:06)
 * @author Ana Andr�s
 */
public class CMPFieldComparator implements Comparator {

    private static final CMPFieldComparator instance = new CMPFieldComparator();

    private CMPFieldComparator() {
    }

    public int compare(Object f1, Object f2) {
        if (f1 == f2) return 0;
        if (f1 == null) return -1;
        if (f2 == null) return 1;
        String name1 = ((CmpField) f1).getCmpPropertyName().toLowerCase();
        if (name1.startsWith("_")) name1 = name1.substring(1);
        String name2 = ((CmpField) f2).getCmpPropertyName().toLowerCase();
        if (name2.startsWith("_")) name2 = name2.substring(1);
        return name1.compareTo(name2);
    }

    public static CMPFieldComparator getInstance() {
        return instance;
    }
}
