package com.gempukku.animator.function.alpha.easing;

public class EaseInBack extends AbstractEasing {

    protected final double s;

    public EaseInBack() {
        this(1.70158);
    }

    public EaseInBack(double s) {
        this.s = s;
    }

    public double getAlpha(double input) {
        return c * (input) * input * ((s + 1) * input - s) + b;
    }
}
