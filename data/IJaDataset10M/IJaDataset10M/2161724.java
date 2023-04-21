package net.sf.myway.gps.garmin.datatype;

import java.util.HashMap;
import java.util.Map;
import net.sf.myway.gps.datatypes.Color;

/**
 * @version $Revision: 1.1 $
 * @author andreas
 */
public class GarminColor extends Color {

    public static final Map<Integer, GarminColor> COLORS_D107 = new HashMap<Integer, GarminColor>();

    public static final Map<Integer, GarminColor> COLORS_D108 = new HashMap<Integer, GarminColor>();

    public static final Map<Integer, GarminColor> COLORS_D110 = new HashMap<Integer, GarminColor>();

    public static final Map<String, Integer> CODE_D107 = new HashMap<String, Integer>();

    public static final Map<String, Integer> CODE_D108 = new HashMap<String, Integer>();

    public static final Map<String, Integer> CODE_D110 = new HashMap<String, Integer>();

    private final int _code;

    static {
        addGarminColor(COLORS_D107, CODE_D107, 0, "Default");
        addGarminColor(COLORS_D107, CODE_D107, 1, "Red");
        addGarminColor(COLORS_D107, CODE_D107, 2, "Green");
        addGarminColor(COLORS_D107, CODE_D107, 3, "Blue");
        addGarminColor(COLORS_D108, CODE_D108, 0, "Black");
        addGarminColor(COLORS_D108, CODE_D108, 1, "Dark Red");
        addGarminColor(COLORS_D108, CODE_D108, 2, "Dark_Green");
        addGarminColor(COLORS_D108, CODE_D108, 3, "Dark Yellow");
        addGarminColor(COLORS_D108, CODE_D108, 4, "Dark Blue");
        addGarminColor(COLORS_D108, CODE_D108, 5, "Dark Magenta");
        addGarminColor(COLORS_D108, CODE_D108, 6, "Dark Cyan");
        addGarminColor(COLORS_D108, CODE_D108, 7, "Light Gray");
        addGarminColor(COLORS_D108, CODE_D108, 8, "Dark Gray");
        addGarminColor(COLORS_D108, CODE_D108, 9, "Red");
        addGarminColor(COLORS_D108, CODE_D108, 10, "Green");
        addGarminColor(COLORS_D108, CODE_D108, 11, "Yellow");
        addGarminColor(COLORS_D108, CODE_D108, 12, "Blue");
        addGarminColor(COLORS_D108, CODE_D108, 13, "Magenta");
        addGarminColor(COLORS_D108, CODE_D108, 14, "Cyan");
        addGarminColor(COLORS_D108, CODE_D108, 15, "White");
        addGarminColor(COLORS_D108, CODE_D108, 255, "Default Color");
        addGarminColor(COLORS_D110, CODE_D110, 0, "Black");
        addGarminColor(COLORS_D110, CODE_D110, 1, "Dark Red");
        addGarminColor(COLORS_D110, CODE_D110, 2, "Dark_Green");
        addGarminColor(COLORS_D110, CODE_D110, 3, "Dark Yellow");
        addGarminColor(COLORS_D110, CODE_D110, 4, "Dark Blue");
        addGarminColor(COLORS_D110, CODE_D110, 5, "Dark Magenta");
        addGarminColor(COLORS_D110, CODE_D110, 6, "Dark Cyan");
        addGarminColor(COLORS_D110, CODE_D110, 7, "Light Gray");
        addGarminColor(COLORS_D110, CODE_D110, 8, "Dark Gray");
        addGarminColor(COLORS_D110, CODE_D110, 9, "Red");
        addGarminColor(COLORS_D110, CODE_D110, 10, "Green");
        addGarminColor(COLORS_D110, CODE_D110, 11, "Yellow");
        addGarminColor(COLORS_D110, CODE_D110, 12, "Blue");
        addGarminColor(COLORS_D110, CODE_D110, 13, "Magenta");
        addGarminColor(COLORS_D110, CODE_D110, 14, "Cyan");
        addGarminColor(COLORS_D110, CODE_D110, 15, "White");
        addGarminColor(COLORS_D110, CODE_D110, 16, "Transparent");
    }

    /**
	 * Method addGarminColor.
	 * 
	 * @param colors
	 * @param codes
	 * @param code
	 * @param name
	 */
    private static void addGarminColor(final Map<Integer, GarminColor> colors, final Map<String, Integer> codes, final int code, final String name) {
        colors.put(new Integer(code), new GarminColor(code, name));
        codes.put(name, new Integer(code));
    }

    /**
	 * Method getD107.
	 * 
	 * @param colorId
	 * @return GarminColor
	 */
    public static GarminColor getD107(final int colorId) {
        return COLORS_D107.get(new Integer(colorId));
    }

    public static Color getD107(final String name) {
        return COLORS_D107.get(CODE_D107.get(name));
    }

    public static int getD107Code(final Color col) {
        return CODE_D107.get(col.getName()).intValue();
    }

    /**
	 * Method getD108.
	 * 
	 * @param colorId
	 * @return GarminColor
	 */
    public static GarminColor getD108(final int colorId) {
        return COLORS_D108.get(new Integer(colorId));
    }

    /**
	 * @param string
	 * @return
	 */
    public static GarminColor getD108(final String name) {
        return COLORS_D108.get(CODE_D108.get(name));
    }

    /**
	 * Method getD108Code.
	 * 
	 * @param col
	 * @return int
	 */
    public static int getD108Code(final Color col) {
        return CODE_D108.get(col.getName()).intValue();
    }

    /**
	 * @param byte1
	 * @return
	 */
    public static Color getD110(final int colorId) {
        return COLORS_D110.get(new Integer(colorId));
    }

    public static Color getD110(final String name) {
        return COLORS_D110.get(CODE_D110.get(name));
    }

    /**
	 * @param col
	 * @return
	 */
    public static int getD110Code(final Color col) {
        return CODE_D110.get(col.getName()).intValue();
    }

    /**
	 * Method GarminColor.
	 * 
	 * @param code
	 * @param name
	 */
    private GarminColor(final int code, final String name) {
        super(name);
        _code = code;
    }
}
