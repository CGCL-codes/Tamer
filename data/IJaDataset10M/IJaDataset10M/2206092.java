package com.bluebrim.paint.impl.shared;

/**
 * This type was created in VisualAge.
 */
public interface CoCMYKColorIF extends CoMultiInkColorIF {

    public static final String FACTORY_KEY = "cmyk-color";

    /**
 * This method was created in VisualAge.
 * @return float
 */
    public float getBlackPercentage();

    /**
 * This method was created in VisualAge.
 * @return float
 */
    public float getCyanPercentage();

    /**
 * This method was created in VisualAge.
 * @return float
 */
    public float getMagentaPercentage();

    /**
 * This method was created in VisualAge.
 * @return float
 */
    public float getYellowPercentage();

    /**
 * This method was created in VisualAge.
 * @param cyan float
 */
    public void setBlackPercentage(float black);

    /**
 * This method was created in VisualAge.
 * @param cyan float
 */
    public void setCyanPercentage(float cyan);

    /**
 * This method was created in VisualAge.
 * @param cyan float
 */
    public void setMagentaPercentage(float magenta);

    /**
 * This method was created in VisualAge.
 * @param cyan float
 */
    public void setYellowPercentage(float yellow);
}
