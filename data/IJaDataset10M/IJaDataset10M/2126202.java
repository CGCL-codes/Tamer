package org.anddev.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseCubicOut implements IEaseFunction {

    private static EaseCubicOut INSTANCE;

    private EaseCubicOut() {
    }

    public static EaseCubicOut getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EaseCubicOut();
        }
        return INSTANCE;
    }

    @Override
    public float getPercentage(final float pSecondsElapsed, final float pDuration) {
        return EaseCubicOut.getValue(pSecondsElapsed / pDuration);
    }

    public static float getValue(final float pPercentage) {
        final float t = pPercentage - 1;
        return 1 + (t * t * t);
    }
}
