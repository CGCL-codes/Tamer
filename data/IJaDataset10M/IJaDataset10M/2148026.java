package sonia.settings;

public class ApplySettings extends PropertySettings {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3577622237041196839L;

    /**
	 * property key for setting the starting coordinates to be used in the
	 * layout
	 */
    public static final String STARTING_COORDS = "STARTING_COORDS";

    /**
	 * property key for specifying how isolates should be positioned
	 */
    public static final String ISOLATE_POSITION = "ISOLATE_POSITION";

    /**
	 * property key for specifying how coordinates should be recenterd
	 */
    public static final String RECENTER_TRANSFORM = "RECENTER_TRANSFORM";

    /**
	 * property key for specifying if layout should be rescaled to fit
	 */
    public static final String RESCALE_LAYOUT = "RESCALE_LAYOUT";

    /**
	 * property key for specifying if isolates should be excluded on layout
	 * transform value should be true or false
	 */
    public static final String TRANSFORM_ISOLATE_EXCLUDE = "TRANSFORM_ISOLATE_EXCLUDE";

    /**
	 * property key for specifying if layout should continue if errors are
	 * encounterd value should be true or false
	 */
    public static final String STOP_ON_ERROR = "STOP_ON_ERROR";

    /**
	 * property key whose value is represents a set of keys for other algorithm
	 * properties
	 */
    public static final String ALG_PROP_KEYS = "ALG_PROP_KEYS";

    /**
	 * property key whose value is tells if and howoften the layout should be
	 * redrawn during the algorithm's run, value should be an int, 0 means don't
	 * reaint
	 */
    public static final String LAYOUT_REPAINT_N = "LAYOUT_REPAINT_N";

    /**
	 * property key indicating if these settings should be repeatedly applied to
	 * all layots value should be true or false
	 */
    public static final String APPLY_REMAINING = "APPLY_REMAINING";

    /**
	 * property key with value that should give the name of the layout algorithm
	 * being applied NOTE: AT THIS POINT IN TIME CHANGING THIS WILL NOT CHANGE
	 * THE LAYOUT!!
	 */
    public static final String ALG_NAME = "Algorithm_Name";

    /**
	 * value for starting coords indicates to use the coordinates from the file,
	 * if included
	 */
    public static final String COORDS_FROM_FILE = "from orginal file";

    /**
	 * value for starting coords indicates to use the coords from the previous
	 * slice
	 */
    public static final String COORDS_FROM_PREV = "from previous slice";

    /**
	 * value for starting coords indicating to use the coords from the next 
	 * slice in time
	 */
    public static final String COORDS_FROM_NEXT = "from next slice";

    /**
	 * value for starting coords indicates to position in a circle
	 */
    public static final String COORDS_CIRCLE = "use circular";

    /**
	 * value for starting coords indicates to position randomly
	 */
    public static final String COORDS_RANDOM = "use random positions";

    /**
	 * value for starting coords indicates use current positions
	 */
    public static final String COORDS_CURRENT = "use current positions";

    /**
	 * value for isolate positioning to leave isolates wherever they are
	 */
    public static final String ISLOLATE_IGNORE = "ignore isolates";

    /**
	 * value for isolate positioning to position along bottom edge
	 */
    public static final String ISLOLATE_EDGE = "pin to bottom edge";

    /**
	 * value for isolate positioning to position in a circle
	 */
    public static final String ISLOLATE_CIRCLE = "pin to circle";

    /**
	 * value for isolate positioning to use file coordinates
	 */
    public static final String ISLOLATE_FILE = "pin to file coords";

    /**
	 * value for isolate positioning to use position in previous slice
	 */
    public static final String ISLOLATE_PREVIOUS = "pin to previous position";

    /**
	 * value for recenter transform indicating the a recentering should be done
	 * during the layout, after each iteration
	 */
    public static final String RECENTER_DURING = "recenter during layout";

    /**
	 * value for recenter transform indicating the a recentering should be done
	 * after layout is complete
	 */
    public static final String RECENTER_AFTER = "recenter after layout";

    public static final String NONE = "none";

    /**
	 * value for recenter transform indicating that layout should be recenterd
	 * to its center of gravity
	 */
    public static final String BARYCENTER = "barycenter after layout";

    /**
	 * value for transformation indicating that layout should be rescaled
	 */
    public static final String RESCALE_TO_FIT = "rescale layout to fit window";

    public void setLayoutSpecificProperty(String key, String value) {
        if (getProperty(key) == null) {
            String keyString = getProperty(ALG_PROP_KEYS);
            if (keyString == null) {
                setProperty(ALG_PROP_KEYS, key);
            } else {
                keyString = keyString + "," + key;
                setProperty(ALG_PROP_KEYS, keyString);
            }
        }
        setProperty(key, value);
    }
}
