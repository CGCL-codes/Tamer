package org.processmining.analysis.performance.dottedchart.ui;

import java.awt.Color;
import java.util.HashMap;

public class ShapeReference {

    public static String ITEM_HANDLE_DOT = "Dot";

    public static String ITEM_HANDLE_CIRCLE = "Circle";

    public static String ITEM_HANDLE_TRIANGLE = "Triangle";

    public static String ITEM_HANDLE_BOX = "Box";

    public static String ITEM_HANDLE_RHOMBUS = "Rhombus";

    public static String ITEM_HANDLE_ROUND_BOX = "RoundBox";

    public static String ITEM_HANDLE_DRAW_BOX = "DrawBox";

    public static String ITEM_HANDLE_DRAW_CIRCLE = "Circle";

    public static String ITEM_HANDLE_DRAW_TRIANGLE = "Triangle";

    public static String ITEM_HANDLE_DRAW_RHOMBUS = "DrawRhombusBox";

    public static String ITEM_HANDLE_DRAW_ROUND_BOX = "DrawRoundBox";

    public static String[] shapeList = new String[] { ITEM_HANDLE_CIRCLE, ITEM_HANDLE_TRIANGLE, ITEM_HANDLE_BOX, ITEM_HANDLE_RHOMBUS, ITEM_HANDLE_ROUND_BOX, ITEM_HANDLE_DRAW_CIRCLE, ITEM_HANDLE_DRAW_TRIANGLE, ITEM_HANDLE_DRAW_BOX, ITEM_HANDLE_DRAW_ROUND_BOX, ITEM_HANDLE_DRAW_RHOMBUS };

    public static int index = 0;

    protected HashMap mappings = null;

    protected Color[] colors = null;

    protected boolean[] assigned = null;

    /**
	 * constructor
	 */
    public ShapeReference() {
        mappings = new HashMap();
    }

    /**
	 * Retrieves the color mapped to the given key (identity preserved). If no
	 * color was previously mapped, a new one is taken from the standard
	 * repository and, if all taken, a random color is assigned.
	 * 
	 * @param key
	 *            the key to map a color to
	 * @return mapped color instance
	 */
    public String getShape(String key) {
        if (mappings.containsKey(key)) {
            return (String) mappings.get(key);
        } else {
            String str = shapeList[index % 8];
            index++;
            mappings.put(key, str);
            return str;
        }
    }

    /**
	 * Frees a keyed color object for new assignment, i.e. it is not used
	 * anymore.
	 * 
	 * @param key
	 *            previously assigned key
	 */
    public void freeColor(String key) {
    }
}
