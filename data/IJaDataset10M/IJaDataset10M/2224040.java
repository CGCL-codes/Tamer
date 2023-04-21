package org.ncgr.cmtv.rendering.impl;

import org.ncgr.cmtv.rendering.*;
import org.ncgr.cmtv.datamodel.impl.PositionMaskingLinearlyLocatedObject;
import org.ncgr.cmtv.datamodel.impl.GlobalMapModel;
import org.ncgr.cmtv.services.ThresholdingStrategy;
import org.ncgr.cmtv.PropertyConstants;
import org.ncgr.isys.objectmodel.LinearlyLocatedObject;
import org.ncgr.isys.objectmodel.LinearObjectPosition;
import org.ncgr.isys.objectmodel.LinearPointPosition;
import org.ncgr.isys.objectmodel.LinearIntervalPosition;
import org.ncgr.isys.objectmodel.LinearObjectDistribution;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Creates zero-to-many <code>BarWidgets</code> for <code>LinearObjectDistributions</code> 
 * whose bounds are determined by one of various strategies to find the
 * "significant" regions. The resultant widgets, though representing the same
 * <code>LinearlyLocatedObject</code> can be tiled independently, allowing one
 * to take a large set of distributions, and visually compress them into the
 * peak regions.
 */
public class ThresholdingDistributionBarWidgetFactory implements LinearMapWidgetFactory {

    private ThresholdingStrategy strategy = null;

    ThresholdingDistributionBarWidgetFactory(ThresholdingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("ThresholdingStrategy must not be null");
        }
        this.strategy = strategy;
    }

    public String getDescription() {
        return "Create colored bars using " + strategy.getDescription();
    }

    public String toString() {
        return getDescription();
    }

    public LinearMapWidget[] createWidgets(LinearlyLocatedObject[] mappedObjects, LinearMapWidgetContainer target) {
        ArrayList retval = new ArrayList(mappedObjects.length);
        for (int i = 0; i < mappedObjects.length; i++) {
            if (!(mappedObjects[i] instanceof LinearObjectDistribution)) {
                continue;
            }
            LinearObjectDistribution lod = (LinearObjectDistribution) mappedObjects[i];
            double threshold = Double.NaN;
            Object tVal = org.ncgr.cmtv.CompMapViewer.getPropertyForObject(PropertyConstants.MAX_THRESHOLD_PROP, lod);
            if (tVal == null) {
                tVal = org.ncgr.cmtv.CompMapViewer.getMaxDistributionValue();
            }
            if (tVal == null) {
                threshold = Double.NEGATIVE_INFINITY;
            } else if (tVal instanceof Number) {
                threshold = ((Number) tVal).doubleValue();
            } else if (tVal instanceof String) {
                try {
                    threshold = Double.parseDouble((String) tVal);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                throw new IllegalArgumentException("instance of " + tVal + "could not be converted to numeric value");
            }
            LinearObjectPosition[] regions = strategy.getSignificantRegions(lod, threshold);
            for (int j = 0; j < regions.length; j++) {
                retval.add(new BarWidget(new PositionMaskingLinearlyLocatedObject(lod, regions[j]), GlobalMapModel.getInstance().isSelected(lod)));
            }
        }
        return (LinearMapWidget[]) retval.toArray(new LinearMapWidget[0]);
    }
}
