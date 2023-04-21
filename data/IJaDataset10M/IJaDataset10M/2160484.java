package org.xmlcml.cmlimpl.style;

import java.awt.Color;
import org.xmlcml.cml.style.BondStyle;

/** holds all style information for a bond or all bonds */
public class BondStyleImpl implements BondStyle {

    protected Color color = Color.black;

    protected double width = 2;

    /** default: new BondStyle() */
    public static final BondStyle DEFAULT = new BondStyleImpl();

    /** default: width 2.0; color: black; */
    public BondStyleImpl() {
        this(2.0f, Color.black);
    }

    public BondStyleImpl(float w, Color c) {
        width = w;
        color = c;
    }

    public BondStyleImpl(BondStyle _bondStyle) {
        BondStyleImpl bondStyle = (BondStyleImpl) _bondStyle;
        this.color = bondStyle.color;
        this.width = bondStyle.width;
    }

    /** width of actual stroke in bond (e.g. double is 3 times this value)
	* default is 2.0
	*/
    public void setWidth(float w) {
        if (w > 0) width = w;
    }

    public float getWidth() {
        return (float) width;
    }

    /** colour; default black */
    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }
}
