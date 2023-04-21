package nacaLib.mapSupport;

public class MapFieldAttrColor extends MapFieldBaseAttr {

    private MapFieldAttrColor(int nEncodedColorValue, String text) {
        super(nEncodedColorValue, text);
    }

    static int getNbBitsEncoding() {
        return 3;
    }

    static int getMask() {
        return 7;
    }

    public static MapFieldAttrColor BLUE = new MapFieldAttrColor(1, "blue");

    public static MapFieldAttrColor RED = new MapFieldAttrColor(2, "red");

    public static MapFieldAttrColor PINK = new MapFieldAttrColor(3, "pink");

    public static MapFieldAttrColor GREEN = new MapFieldAttrColor(4, "green");

    public static MapFieldAttrColor TURQUOISE = new MapFieldAttrColor(5, "turquoise");

    public static MapFieldAttrColor YELLOW = new MapFieldAttrColor(6, "yellow");

    public static MapFieldAttrColor NEUTRAL = new MapFieldAttrColor(7, "neutral");

    public static MapFieldAttrColor DEFAULT = NEUTRAL;

    /**
	 * @param nValue
	 * @return
	 */
    public static MapFieldAttrColor Select(int nValue) {
        if (nValue == BLUE.getInternalValue()) return BLUE; else if (nValue == RED.getInternalValue()) return RED; else if (nValue == PINK.getInternalValue()) return PINK; else if (nValue == GREEN.getInternalValue()) return GREEN; else if (nValue == TURQUOISE.getInternalValue()) return TURQUOISE; else if (nValue == YELLOW.getInternalValue()) return YELLOW; else if (nValue == NEUTRAL.getInternalValue()) return NEUTRAL;
        return null;
    }
}
