package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.AbstractValue;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

/**
 * This class represents an ICC color value.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: ICCColor.java,v 1.1 2005/11/21 09:51:37 dev Exp $
 */
public class ICCColor extends AbstractValue {

    /**
     * The color profile.
     */
    protected String colorProfile;

    /**
     * The color count.
     */
    protected int count;

    /**
     * The colors.
     */
    protected float[] colors = new float[5];

    /**
     * Creates a new ICCColor.
     */
    public ICCColor(String name) {
        colorProfile = name;
    }

    /**
     * Implements {@link
     * org.apache.batik.css.engine.value.Value#getCssValueType()}.
     */
    public short getCssValueType() {
        return CSSValue.CSS_CUSTOM;
    }

    /**
     * Returns the color name.
     */
    public String getColorProfile() throws DOMException {
        return colorProfile;
    }

    /**
     * Returns the number of colors.
     */
    public int getNumberOfColors() throws DOMException {
        return count;
    }

    /**
     * Returns the color at the given index.
     */
    public float getColor(int i) throws DOMException {
        return colors[i];
    }

    /**
     *  A string representation of the current value. 
     */
    public String getCssText() {
        StringBuffer sb = new StringBuffer();
        sb.append("icc-color(");
        sb.append(colorProfile);
        for (int i = 0; i < count; i++) {
            sb.append(", ");
            sb.append(colors[i]);
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Appends a color to the list.
     */
    public void append(float c) {
        if (count == colors.length) {
            float[] t = new float[count * 2];
            for (int i = 0; i < count; i++) {
                t[i] = colors[i];
            }
            colors = t;
        }
        colors[count++] = c;
    }
}
