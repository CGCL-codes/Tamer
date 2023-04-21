package com.bluebrim.paint.impl.shared;

/**
 * This type was created in VisualAge.
 */
public interface CoExtendedMultiInkColorIF extends CoMultiInkColorIF {

    public static final String FACTORY_KEY = "extended-multi-ink-color";

    public void addShadeColor(CoTrappableColorIF trappableColor, float shade);

    public float getShade(CoTrappableColorIF trapColor);

    public void removeShadeColor(CoTrappableColorIF trappableColor);

    public void setShade(CoTrappableColorIF trapColor, float value);
}
