package com.gempukku.animator.function.alpha.easing;

public class EaseInExpo extends AbstractEasing {

    public double getAlpha(double input) {
        return (input == 0) ? b : c * Math.pow(2, 10 * (input - 1)) + b;
    }
}
