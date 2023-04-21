package org.systemsbiology.lib.commonobj;

import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.collections15.comparators.ComparableComparator;
import org.systemsbiology.aebersoldlab.core.time.Duration;

public class Trace {

    private Transition transition;

    private float[][] points;

    private int maxIntensityIndex;

    private SortedSet<Duration> rts = new TreeSet<Duration>(new ComparableComparator<Duration>());

    public Trace(Transition transition, float[][] points, int maxIntensityIndex) {
        this.transition = transition;
        this.points = points;
        this.maxIntensityIndex = maxIntensityIndex;
    }

    public Transition getTransition() {
        return this.transition;
    }

    public float[][] getPoints() {
        return this.points;
    }

    public int getMaxIntensityIndex() {
        return this.maxIntensityIndex;
    }

    public float getMaxIntensity() {
        return this.points[this.maxIntensityIndex][1];
    }

    public float getMaxIntensityRt() {
        return this.points[this.maxIntensityIndex][0];
    }

    public void addRt(Duration rt) {
        this.rts.add(rt);
    }

    public SortedSet<Duration> getRts() {
        return this.rts;
    }
}
