package com.mortennobel.imagescaling;

/**
 * A Bell resample filter.
 */
final class BellFilter implements ResampleFilter {

    public float getSamplingRadius() {
        return 1.5f;
    }

    public final float apply(float value) {
        if (value < 0.0f) {
            value = -value;
        }
        if (value < 0.5f) {
            return 0.75f - (value * value);
        } else if (value < 1.5f) {
            value = value - 1.5f;
            return 0.5f * (value * value);
        } else {
            return 0.0f;
        }
    }

    public String getName() {
        return "Bell";
    }
}
