package org.miscwidgets.interpolator;

import org.miscwidgets.interpolator.EasingType.Type;
import android.view.animation.Interpolator;

public class QuintInterpolator implements Interpolator {

    private Type type;

    public QuintInterpolator(Type type) {
        this.type = type;
    }

    public float getInterpolation(float t) {
        if (type == Type.IN) {
            return in(t);
        } else if (type == Type.OUT) {
            return out(t);
        } else if (type == Type.INOUT) {
            return inout(t);
        }
        return 0;
    }

    private float in(float t) {
        return t * t * t * t * t;
    }

    private float out(float t) {
        return ((t -= 1) * t * t * t * t + 1);
    }

    private float inout(float t) {
        t *= 2;
        if (t < 1) {
            return 0.5f * t * t * t * t * t;
        } else {
            return 0.5f * ((t -= 2) * t * t * t * t + 2);
        }
    }
}
