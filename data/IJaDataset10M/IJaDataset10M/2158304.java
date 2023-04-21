package org.collada.xml_walker;

import com.jme.renderer.ColorRGBA;

/**
 *
 * @author Chris Nagle
 */
public class PColladaColor {

    public float red = 1.0f;

    public float green = 1.0f;

    public float blue = 1.0f;

    public float alpha = 1.0f;

    /**
     * Default constructor.
     */
    public PColladaColor() {
    }

    /**
     * Constructor.
     * @param fRed
     * @param fGreen
     * @param fBlue
     * @param fAlpha
     */
    public PColladaColor(float fRed, float fGreen, float fBlue, float fAlpha) {
        red = fRed;
        green = fGreen;
        blue = fBlue;
        alpha = fAlpha;
    }

    /**
     * Sets the fields of the PColladaColor.  Alpha is set to 1.0f.
     * @param fRed
     * @param fGreen
     * @param fBlue
     */
    public void set(float fRed, float fGreen, float fBlue) {
        red = fRed;
        green = fGreen;
        blue = fBlue;
        alpha = 1.0f;
    }

    /**
     * Sets the fields of the PColladaColor.
     * @param fRed
     * @param fGreen
     * @param fBlue
     * @param fAlpha
     */
    public void set(float fRed, float fGreen, float fBlue, float fAlpha) {
        red = fRed;
        green = fGreen;
        blue = fBlue;
        alpha = fAlpha;
    }

    ColorRGBA toColorRGBA() {
        return new ColorRGBA(red, green, blue, alpha);
    }
}
