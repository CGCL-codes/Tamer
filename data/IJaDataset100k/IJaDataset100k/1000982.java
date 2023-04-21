package de.xirp.chart.fixedutils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * A resource provider implementation which creates a new resource for
 * each call.
 * 
 * @author Rabea Gransberger
 */
public class DefaultRessourceProvider implements IRessourceProvider {

    /**
	 * @return a new color
	 * @see de.xirp.chart.fixedutils.IRessourceProvider#getColor(int,
	 *      int, int)
	 */
    @Override
    public Color getColor(int r, int g, int b) {
        return new Color(Display.getDefault(), r, g, b);
    }

    /**
	 * @return a new color
	 * @see de.xirp.chart.fixedutils.IRessourceProvider#getColor(org.eclipse.swt.graphics.RGB)
	 */
    @Override
    public Color getColor(RGB rgb) {
        return new Color(Display.getDefault(), rgb);
    }

    /**
	 * @return a new font
	 * @see de.xirp.chart.fixedutils.IRessourceProvider#getFont(org.eclipse.swt.graphics.FontData)
	 */
    @Override
    public Font getFont(FontData data) {
        return new Font(Display.getDefault(), data);
    }

    /**
	 * @return a new font
	 * @see de.xirp.chart.fixedutils.IRessourceProvider#getFont(java.lang.String,
	 *      int, int)
	 */
    @Override
    public Font getFont(String name, int height, int style) {
        return new Font(Display.getDefault(), name, height, style);
    }
}
