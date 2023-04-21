package com.hack23.cia.web.views.comparator;

import java.io.Serializable;
import java.util.Comparator;

/**
 * The Class FloatComparator.
 */
public class FloatComparator implements Comparator<Float>, Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2557127986172816812L;

    public final int compare(final Float o1, final Float o2) {
        return o1.compareTo(o2);
    }
}
