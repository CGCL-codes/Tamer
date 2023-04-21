package org.ncgr.cmtv;

import org.ncgr.cmtv.rendering.LinearMapWidget;
import java.util.Comparator;

class ScalingFactorComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        if (o1.equals(o2)) return 0;
        if (o1 instanceof LinearMapWidget && o2 instanceof LinearMapWidget) {
            Object sf1 = ((LinearMapWidget) o1).getProperty(PropertyConstants.SCALING_FACTOR_PROP);
            Object sf2 = ((LinearMapWidget) o2).getProperty(PropertyConstants.SCALING_FACTOR_PROP);
            if (sf1 != null && sf2 != null) {
                double diff = ((Number) sf1).doubleValue() - ((Number) sf2).doubleValue();
                if (diff >= 0.0) return 1;
                if (diff < 0.0) return -1;
            }
        }
        return o1.hashCode() - o2.hashCode();
    }
}
