package jgraphics.shading;

import jgraphics.Constants;
import jgraphics.Light;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>License: General Public License (GPL)</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * @author Tony & JP
 * @version 1.0
 */
public class Phong implements Node {

    /**
     * Coefficient
     */
    private double k_r;

    private double k_g;

    private double k_b;

    /**
     * n = 1, a dull surface;
     * n = 50, a quite shiny surface.
     */
    private double k_n;

    private double tempRed = 1.0;

    private double tempGreen = 1.0;

    private double tempBlue = 1.0;

    public Phong(double r, double g, double b, double n) {
        k_r = r;
        k_g = g;
        k_b = b;
        k_n = n;
        if (k_n < 1) k_n = 1;
    }

    /**
     *
     * @param light Light intensity.
     * @param cos Cosine value of the angle between the ray from the camera and
     * the direction of mirror reflection of the light.
     */
    public void init(double shadowness, Light light, double cos) {
        if (Double.isNaN(cos) || Double.isInfinite(cos) || cos < 0) cos = 0;
        tempRed = shadowness * light.getRed() * Math.pow(cos, k_n);
        tempGreen = shadowness * light.getGreen() * Math.pow(cos, k_n);
        tempBlue = shadowness * light.getBlue() * Math.pow(cos, k_n);
    }

    public double getRed() {
        return k_r * tempRed;
    }

    public double getGreen() {
        return k_g * tempGreen;
    }

    public double getBlue() {
        return k_b * tempBlue;
    }

    public String getName() {
        return Constants.SHADING_PHONG;
    }
}
