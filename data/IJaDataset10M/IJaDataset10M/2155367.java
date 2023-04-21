package org.jdesktop.j3d.examples.morphing;

import java.util.Enumeration;
import javax.media.j3d.*;
import javax.vecmath.*;

public class MorphingBehavior extends Behavior {

    Alpha alpha;

    Morph morph;

    double weights[];

    WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);

    public void initialize() {
        alpha.setStartTime(System.currentTimeMillis());
        wakeupOn(w);
    }

    public void processStimulus(Enumeration criteria) {
        double val = alpha.value();
        if (val < 0.5) {
            double a = val * 2.0;
            weights[0] = 1.0 - a;
            weights[1] = a;
            weights[2] = 0.0;
        } else {
            double a = (val - 0.5) * 2.0;
            weights[0] = 0.0;
            weights[1] = 1.0f - a;
            weights[2] = a;
        }
        morph.setWeights(weights);
        wakeupOn(w);
    }

    public MorphingBehavior(Alpha a, Morph m) {
        alpha = a;
        morph = m;
        weights = morph.getWeights();
    }
}
