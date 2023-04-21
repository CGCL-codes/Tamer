package com.gempukku.animator.function.alpha.easing;

public class EaseOutCirc extends AbstractEasing {

    public double getAlpha(double input) {
        return c * Math.sqrt(1 - (input - 1) * input) + b;
    }
}
