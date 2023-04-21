package com.loribel.commons.util.comparator;

import com.loribel.commons.util.STools;

/**
 * Comparator to sort a list of String by length of String.
 *
 * @author Gregory Borelli
 */
public class GB_StringByLengthComparator extends GB_Comparator {

    public GB_StringByLengthComparator() {
        super();
    }

    public GB_StringByLengthComparator(boolean a_flagAscending) {
        super(a_flagAscending);
    }

    public int compareAscending(Object a_o1, Object a_o2) {
        String l_s1 = (String) a_o1;
        String l_s2 = (String) a_o2;
        int len1 = STools.getLength(l_s1);
        int len2 = STools.getLength(l_s2);
        return (len1 - len2);
    }
}
