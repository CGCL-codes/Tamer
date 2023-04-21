package cn.chengdu.in.android.interpolator;

import cn.chengdu.in.android.interpolator.EasingType.Type;
import android.view.animation.Interpolator;

public class QuadInterpolator implements Interpolator {

    private Type type;

    public QuadInterpolator(Type type) {
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
        return t * t;
    }

    private float out(float t) {
        return -(t) * (t - 2);
    }

    private float inout(float t) {
        t *= 2;
        if (t < 1) {
            return 0.5f * t * t;
        } else {
            return -0.5f * ((--t) * (t - 2) - 1);
        }
    }
}
