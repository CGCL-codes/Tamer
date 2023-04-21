package net.sf.bioutils.proteomics.impl;

import java.util.Comparator;
import net.sf.bioutils.proteomics.Peak;
import net.sf.kerner.utils.collections.impl.ComparatorNull;

public class ComparatorPeakByIntensity<P extends Peak> extends ComparatorNull<P> implements Comparator<P> {

    public int compareNonNull(Peak o1, Peak o2) {
        return Double.compare(o1.getIntensity(), o2.getIntensity());
    }
}
