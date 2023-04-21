package it.uniroma3.plasm.editors.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * This class provide to all PLaSM environment colors for syntax coloring. Here
 * you can specify your color preferences.
 * 
 * @author EcT(o)PLaSM Group
 */
public class PlasmColorProvider {

    /** The color used for PLaSM comments */
    public static final RGB COMMENT = new RGB(150, 150, 150);

    /** The color used for PLaSM keywords */
    public static final RGB KEYWORD = new RGB(63, 95, 191);

    /** The color used for PLaSM single character operators */
    public static final RGB OPERATOR = new RGB(127, 0, 85);

    /** The color used for PLaSM strings */
    public static final RGB STRING = new RGB(0, 128, 100);

    /** The color used for PLaSM default text */
    public static final RGB DEFAULT = new RGB(0, 0, 0);

    /** The color used for PLaSM digits  */
    public static final RGB DIGIT = new RGB(200, 0, 0);

    protected Map fColorTable = new HashMap(10);

    /**
	 * Release all of the color resources held onto by the receiver.
	 */
    public void dispose() {
        Iterator e = fColorTable.values().iterator();
        while (e.hasNext()) ((Color) e.next()).dispose();
    }

    /**
	 * Return the Color that is stored in the Color table a RGB.
	 * 
	 * @param rgb the RGB object associated with one PLaSM environment 
	 * @return the Color object related to the required RGB
	 */
    public Color getColor(RGB rgb) {
        Color color = (Color) fColorTable.get(rgb);
        if (color == null) {
            color = new Color(Display.getCurrent(), rgb);
            fColorTable.put(rgb, color);
        }
        return color;
    }
}
