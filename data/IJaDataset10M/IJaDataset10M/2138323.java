package mipt.gui.graph.impl;

import java.awt.Color;
import mipt.gui.chart.extra.Colors;

/**
 * Convenient to fill legend rows by maximally different colors
 * Colors can be adjusted by setType() ("spectrum" colors by default) 
 * TO DO: make LinesCollection (with this, int[] styles,
 *   int thickness (one for all))- to produce LineStyle object
 * Can be used simply to produce colors from double (without colors enumeration);
 *  then colorCount should be 0.
 * @author evdokimov
 */
public class ColorsCollection implements Colors {

    public static final int MONO_COLORS = 10;

    public static final int GRAY_COLORS = 11;

    public static final int BLUE_RED_COLORS = 21;

    public static final int CYAN_RED_COLORS = 22;

    public static final int GREEN_RED_COLORS = 23;

    public static final int SPECTRUM_COLORS = 30;

    public static final int BLUE_GREEN_RED_COLORS = 31;

    protected int type = SPECTRUM_COLORS;

    protected int colorCount;

    public ColorsCollection() {
        this(6);
    }

    public ColorsCollection(int colorCount) {
        setColorCount(colorCount);
    }

    public ColorsCollection(int colorCount, int collectionType) {
        setColorCount(colorCount);
        setCollectionType(collectionType);
    }

    /**
	 * @see mipt.gui.chart.extra.Colors#getColorCount()
	 */
    public final int getColorCount() {
        return colorCount;
    }

    /**
	 * 
	 */
    public void setColorCount(int colorCount) {
        if (colorCount < 1) colorCount = 1;
        this.colorCount = colorCount;
    }

    /**
	 * @see mipt.gui.chart.extra.Colors#getColor(int)
	 */
    public Color getColor(int index) {
        while (index >= colorCount) index -= colorCount;
        return doubleToColor(index / (colorCount - 1.));
    }

    /**
	 * V argument must be in [0;1]
	 * Override to use double2BGR(),double2BR(),double2GR(),double2CR(),double2KW()
	 *  instead of double2Spectrum()  
	 */
    public Color doubleToColor(double V) {
        Color c;
        switch(Math.abs(type)) {
            case GRAY_COLORS:
                V = colorCount == 0 ? V : V * colorCount / (colorCount + 1.);
            case MONO_COLORS:
                c = double2KW(V);
                break;
            case BLUE_RED_COLORS:
                c = double2BR(V);
                break;
            case CYAN_RED_COLORS:
                c = double2CR(V);
                break;
            case GREEN_RED_COLORS:
                c = double2GR(V);
                break;
            case BLUE_GREEN_RED_COLORS:
                c = double2BGR(V);
                break;
            case SPECTRUM_COLORS:
            default:
                c = double2Spectrum(V);
                break;
        }
        return type < 0 ? inverseColor(c) : c;
    }

    /**
	 * 
	 */
    public static Color inverseColor(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    /**
	 * Form "cold" to "warm" colors: margenta, blue, cyan, green, yellow, red
	 * 5 spectral segments from 6 are used:
	 * R: \__/^(^)   G: _/^^\(_)   B: ^^\__(/)
	 */
    public static Color double2Spectrum(double V) {
        float w = (float) (V * 5.);
        int part = (int) w;
        if (part < -1) part = -1; else if (part > 5) part = 5; else if (V == 1.) part = 4;
        w = w - part;
        float R, G, B;
        switch(part) {
            case -1:
                R = 0.1F;
                G = 0.1F;
                B = 0.1F;
                break;
            case 0:
                R = 1.F - w;
                G = 0.F;
                B = 1.F;
                break;
            case 1:
                R = 0.F;
                G = w;
                B = 1.F;
                break;
            case 2:
                R = 0.F;
                G = 1.F;
                B = 1.F - w;
                break;
            case 3:
                R = w;
                G = 1.F;
                B = 0.F;
                break;
            case 4:
                R = 1.F;
                G = 1.F - w;
                B = 0.F;
                break;
            case 5:
            default:
                R = 0.9F;
                G = 0.9F;
                B = 0.9F;
                break;
        }
        return new Color(R, G, B);
    }

    /**
	 * From blue through green to red
	 */
    public static Color double2BGR(double V) {
        float w = (float) V, g = w + w;
        if (g > 1.F) g = 2.F - g;
        return new Color(w, g, 1.F - w);
    }

    /**
	 * From blue to red (without green)
	 */
    public static Color double2BR(double V) {
        float w = (float) V;
        return new Color(w, 0.F, 1.F - w);
    }

    /**
	 * From green to red (without blue)
	 */
    public static Color double2GR(double V) {
        float w = (float) V;
        return new Color(w, 1.F - w, 0.F);
    }

    /**
	 * From cyan to red (blue=green)
	 */
    public static Color double2CR(double V) {
        float w = (float) V, bg = 1.F - w;
        return new Color(w, bg, bg);
    }

    /**
	 * From black to white (red=blue=green)
	 */
    public static Color double2KW(double V) {
        float w = (float) V;
        return new Color(w, w, w);
    }

    public final int getCollectionType() {
        return type;
    }

    /**
	 * type - one of the constants of this class
	 */
    public void setCollectionType(int type) {
        this.type = type;
    }
}
