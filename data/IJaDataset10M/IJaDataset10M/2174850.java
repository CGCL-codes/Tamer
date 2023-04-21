package net.sf.myway.gps.garmin.datatype;

import java.util.HashMap;
import java.util.Map;
import net.sf.myway.gps.datatypes.Display;

/**
 * @version $Revision: 1.1 $
 * @author andreas
 */
public class GarminDisplay extends Display {

    public static final Map<Integer, GarminDisplay> DISPLAYS_D103 = new HashMap<Integer, GarminDisplay>();

    public static final Map<Integer, GarminDisplay> DISPLAYS_D104 = new HashMap<Integer, GarminDisplay>();

    public static final Map<Integer, GarminDisplay> DISPLAYS_D110 = new HashMap<Integer, GarminDisplay>();

    public static final Map<String, Integer> CODES_D103 = new HashMap<String, Integer>();

    public static final Map<String, Integer> CODES_D104 = new HashMap<String, Integer>();

    public static final Map<String, Integer> CODES_D110 = new HashMap<String, Integer>();

    private final int _code;

    static {
        addGarminDisplay(DISPLAYS_D103, CODES_D103, 0, "Name");
        addGarminDisplay(DISPLAYS_D103, CODES_D103, 1, "None");
        addGarminDisplay(DISPLAYS_D103, CODES_D103, 2, "Comment");
        addGarminDisplay(DISPLAYS_D104, CODES_D104, 0, "None");
        addGarminDisplay(DISPLAYS_D104, CODES_D104, 1, "Only");
        addGarminDisplay(DISPLAYS_D104, CODES_D104, 3, "Name");
        addGarminDisplay(DISPLAYS_D104, CODES_D104, 5, "Comment");
        addGarminDisplay(DISPLAYS_D110, CODES_D110, 0, "Name");
        addGarminDisplay(DISPLAYS_D110, CODES_D110, 1, "Only");
        addGarminDisplay(DISPLAYS_D110, CODES_D110, 2, "Comment");
    }

    /**
	 * Method addGarminDisplay.
	 * 
	 * @param displays
	 * @param codes
	 * @param code
	 * @param name
	 */
    private static void addGarminDisplay(final Map<Integer, GarminDisplay> displays, final Map<String, Integer> codes, final int code, final String name) {
        displays.put(new Integer(code), new GarminDisplay(code, name));
        codes.put(name, new Integer(code));
    }

    /**
	 * Method getD103.
	 * 
	 * @param displayId
	 * @return GarminDisplay
	 */
    public static GarminDisplay getD103(final int displayId) {
        return DISPLAYS_D103.get(new Integer(displayId));
    }

    /**
	 * Method getD103Code.
	 * 
	 * @param disp
	 * @return int
	 */
    public static int getD103Code(final Display disp) {
        return CODES_D103.get(disp.getName()).intValue();
    }

    /**
	 * Method getD104.
	 * 
	 * @param displayId
	 * @return GarminDisplay
	 */
    public static GarminDisplay getD104(final int displayId) {
        return DISPLAYS_D104.get(new Integer(displayId));
    }

    public static int getD104Code(final Display disp) {
        return CODES_D104.get(disp.getName()).intValue();
    }

    public static GarminDisplay getD110(final int displayId) {
        return DISPLAYS_D110.get(new Integer(displayId));
    }

    /**
	 * @param data
	 * @return
	 */
    public static byte getD110Code(final Display data) {
        return 0;
    }

    /**
	 * Method GarminDisplay.
	 * 
	 * @param code
	 * @param name
	 */
    private GarminDisplay(final int code, final String name) {
        super(name);
        _code = code;
    }
}
