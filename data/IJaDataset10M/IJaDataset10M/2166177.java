package net.openchrom.chromatogram.msd.model.core.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.internal.core.support.BaselineSegment;
import net.openchrom.chromatogram.msd.model.internal.core.support.IBaselineSegment;
import net.openchrom.numeric.core.Point;
import net.openchrom.numeric.equations.Equations;
import net.openchrom.numeric.equations.LinearEquation;

/**
 * This class represents the baseline model of the current chromatogram.
 * 
 * @author eselmeister
 */
public class BaselineModel implements IBaselineModel {

    private IChromatogram chromatogram;

    private NavigableMap<Integer, IBaselineSegment> baselineSegments = null;

    public BaselineModel(IChromatogram chromatogram) {
        this.chromatogram = chromatogram;
        clearBaseline();
    }

    @Override
    public void addBaseline(int startRetentionTime, int stopRetentionTime, float startBackgroundAbundance, float stopBackgroundAbundance) {
        if (startRetentionTime >= stopRetentionTime) {
            return;
        }
        IBaselineSegment segment;
        List<IBaselineSegment> addSegments = new ArrayList<IBaselineSegment>();
        List<Integer> removeSegments = new ArrayList<Integer>();
        int start = startRetentionTime;
        int stop = stopRetentionTime;
        segment = new BaselineSegment(start, stop);
        segment.setStartBackgroundAbundance(startBackgroundAbundance);
        segment.setStopBackgroundAbundance(stopBackgroundAbundance);
        addSegments.add(segment);
        for (Integer key : baselineSegments.keySet()) {
            segment = baselineSegments.get(key);
            int x0 = segment.getStartRetentionTime();
            int x1 = segment.getStopRetentionTime();
            if ((start < x0 && stop < x0) || (start > x1 && stop > x1)) {
                continue;
            }
            if (start < x0 && stop > x1) {
                removeSegments.add(key);
                continue;
            }
            if (stop > x0 && stop < x1 && start < x0) {
                cutSegmentsBeginningPart(stop, segment, removeSegments, addSegments, key);
            }
            if (start < x1 && stop >= x1) {
                cutSegmentsEndingPart(start, segment, removeSegments, key);
            }
            if ((start > x0 && start < x1) && (stop > x0 && stop < x1)) {
                cutExistingSegmentInTwoParts(start, stop, segment, removeSegments, addSegments, key);
            }
        }
        for (Integer key : removeSegments) {
            baselineSegments.remove(key);
        }
        for (IBaselineSegment addSegment : addSegments) {
            baselineSegments.put(addSegment.getStartRetentionTime(), addSegment);
        }
    }

    @Override
    public void removeBaseline(int startRetentionTime, int stopRetentionTime) {
        if (startRetentionTime >= stopRetentionTime) {
            return;
        }
        addBaseline(startRetentionTime, stopRetentionTime, 0.0f, 0.0f);
    }

    @Override
    public void removeBaseline() {
        clearBaseline();
    }

    @Override
    public float getBackgroundAbundance(int retentionTime) {
        if (retentionTime < chromatogram.getStartRetentionTime() || retentionTime > chromatogram.getStopRetentionTime()) {
            return 0.0f;
        }
        Map.Entry<Integer, IBaselineSegment> entry = baselineSegments.floorEntry(retentionTime);
        if (entry == null) {
            return 0.0f;
        } else {
            IBaselineSegment segment = entry.getValue();
            Point p1 = new Point(segment.getStartRetentionTime(), segment.getStartBackgroundAbundance());
            Point p2 = new Point(segment.getStopRetentionTime(), segment.getStopBackgroundAbundance());
            LinearEquation eq = Equations.createLinearEquation(p1, p2);
            return (float) eq.calculateY(retentionTime);
        }
    }

    @Override
    public IBaselineModel makeDeepCopy() {
        IBaselineModel baselineModelCopy = new BaselineModel(chromatogram);
        int startRT;
        int stopRT;
        float startAB;
        float stopAB;
        for (IBaselineSegment segment : baselineSegments.values()) {
            startRT = segment.getStartRetentionTime();
            stopRT = segment.getStopRetentionTime();
            startAB = segment.getStartBackgroundAbundance();
            stopAB = segment.getStopBackgroundAbundance();
            baselineModelCopy.addBaseline(startRT, stopRT, startAB, stopAB);
        }
        return baselineModelCopy;
    }

    private void cutSegmentsBeginningPart(int stop, IBaselineSegment segment, List<Integer> removeSegments, List<IBaselineSegment> addSegments, int key) {
        int startRT = stop + 1;
        if (startRT == 0) {
            removeSegments.add(key);
        } else {
            removeSegments.add(key);
            segment.setStartBackgroundAbundance(getBackgroundAbundance(startRT));
            segment.setStartRetentionTime(startRT);
            addSegments.add(segment);
        }
    }

    private void cutSegmentsEndingPart(int start, IBaselineSegment segment, List<Integer> removeSegments, int key) {
        int stopRT = start - 1;
        if (stopRT == 0) {
            removeSegments.add(key);
        } else {
            segment.setStopBackgroundAbundance(getBackgroundAbundance(stopRT));
            segment.setStopRetentionTime(stopRT);
        }
    }

    private void cutExistingSegmentInTwoParts(int start, int stop, IBaselineSegment segment, List<Integer> removeSegments, List<IBaselineSegment> addSegments, int key) {
        int startRT = stop + 1;
        if (startRT == chromatogram.getStopRetentionTime()) {
            removeSegments.add(key);
        } else {
            IBaselineSegment segmentII = new BaselineSegment(startRT, segment.getStopRetentionTime());
            segmentII.setStartBackgroundAbundance(getBackgroundAbundance(startRT));
            segmentII.setStopBackgroundAbundance(segment.getStopBackgroundAbundance());
            addSegments.add(segmentII);
        }
        int stopRT = start - 1;
        if (stopRT == 0) {
            removeSegments.add(key);
        } else {
            removeSegments.add(key);
            segment.setStopBackgroundAbundance(getBackgroundAbundance(stopRT));
            segment.setStopRetentionTime(stopRT);
            addSegments.add(segment);
        }
    }

    /**
	 * Clear the baseline segments and create a new tree map to store them.
	 */
    private void clearBaseline() {
        if (baselineSegments != null && baselineSegments.size() > 0) {
            baselineSegments.clear();
        }
        baselineSegments = new TreeMap<Integer, IBaselineSegment>();
    }
}
