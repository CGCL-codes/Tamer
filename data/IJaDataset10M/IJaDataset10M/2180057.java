package org.eclipse.mylyn.internal.context.core;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

/**
 * @author Mik Kersten
 */
public class ScalingFactors {

    private ScalingFactor defaultDecay = new ScalingFactor("decay", .017f);

    private ScalingFactor defaultEdit = new ScalingFactor("edit", .7f);

    private ScalingFactor defaultPurge = new ScalingFactor("edit", -10f);

    private float landmark = 30f;

    private float interesting = 0f;

    private int degreeOfSeparation = 2;

    private int degreeOfSeparationScale = 3;

    private float errorInterest = .3f;

    private int maxNumInterestingErrors = 20;

    private Map<InteractionEvent.Kind, ScalingFactor> factors = new HashMap<InteractionEvent.Kind, ScalingFactor>();

    private static final ScalingFactor DEFAULT_SCALING_FACTOR = new ScalingFactor("<default>", 1);

    public ScalingFactors() {
        factors.put(InteractionEvent.Kind.EDIT, defaultEdit);
    }

    public ScalingFactor get(InteractionEvent.Kind kind) {
        ScalingFactor factor = factors.get(kind);
        if (factor != null) {
            return factor;
        } else {
            return DEFAULT_SCALING_FACTOR;
        }
    }

    public ScalingFactor getDecay() {
        return defaultDecay;
    }

    public void setDecay(ScalingFactor decay) {
        this.defaultDecay = decay;
    }

    public float getInteresting() {
        return interesting;
    }

    public void setInteresting(float interesting) {
        this.interesting = interesting;
    }

    public float getLandmark() {
        return landmark;
    }

    public void setLandmark(float landmark) {
        this.landmark = landmark;
    }

    public int getDegreeOfSeparation() {
        return degreeOfSeparation;
    }

    public void setDegreeOfSeparation(int degreeOfSeparation) {
        this.degreeOfSeparation = degreeOfSeparation;
    }

    public int getDegreeOfSeparationScale() {
        return degreeOfSeparationScale;
    }

    public void setDegreeOfSeparationScale(int degreeOfSeparationScale) {
        this.degreeOfSeparationScale = degreeOfSeparationScale;
    }

    @Deprecated
    public float getParentPropagationIncrement(int level) {
        return 1f / (level * level);
    }

    public float getErrorInterest() {
        return errorInterest;
    }

    public int getMaxNumInterestingErrors() {
        return maxNumInterestingErrors;
    }

    public ScalingFactor getPurge() {
        return defaultPurge;
    }
}
