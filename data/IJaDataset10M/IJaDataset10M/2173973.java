package org.ncgr.cmtv.datamodel.impl;

import org.ncgr.cmtv.MapUtils;
import org.ncgr.isys.objectmodel.OffsetLinearObject;
import org.ncgr.isys.objectmodel.LinearObject;
import java.util.*;

public class AggregateLinearObjectImpl implements OffsetLinearObject {

    Collection maps;

    Number gmin = null;

    Number gmax = null;

    String units = null;

    public AggregateLinearObjectImpl(Collection maps) {
        this.maps = new HashSet(maps);
        for (Iterator itr = this.maps.iterator(); itr.hasNext(); ) {
            LinearObject m = (LinearObject) itr.next();
            units = m.getUnits();
            Number min = MapUtils.getMinimumMapCoordinate(m);
            Number max = MapUtils.getMaximumMapCoordinate(m);
            if (gmin == null || gmin.doubleValue() > min.doubleValue()) gmin = min;
            if (gmax == null || gmax.doubleValue() < max.doubleValue()) gmax = max;
        }
    }

    public Number getLength() {
        return gmax;
    }

    public String getUnits() {
        return units;
    }

    public Number getOffset() {
        return gmin;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (maps.contains(o)) return true;
        return false;
    }
}
