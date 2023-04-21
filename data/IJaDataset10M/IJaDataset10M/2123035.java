package org.anddev.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseCubicInOut implements IEaseFunction {

    private static EaseCubicInOut INSTANCE;

    private EaseCubicInOut() {
    }

    public static EaseCubicInOut getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EaseCubicInOut();
        }
        return INSTANCE;
    }

    @Override
    public float getPercentage(final float pSecondsElapsed, final float pDuration) {
        final float percentage = pSecondsElapsed / pDuration;
        if (percentage < 0.5f) {
            return 0.5f * EaseCubicIn.getValue(2 * percentage);
        } else {
            return 0.5f + 0.5f * EaseCubicOut.getValue(percentage * 2 - 1);
        }
    }
}
