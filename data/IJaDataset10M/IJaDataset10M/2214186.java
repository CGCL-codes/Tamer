package playground.thibautd.jointtrips.population;

/**
 * Defines different naming constants related to joint actings.
 * @author thibautd
 */
public interface JointActingTypes {

    public static final String PICK_UP = "pick_up";

    public static final String DROP_OFF = "drop_off";

    public static final String PASSENGER = "car_passenger";

    public static final String PICK_UP_SPLIT_EXPR = "_";

    public static final String PICK_UP_BEGIN = "pu";

    public static final String PICK_UP_REGEXP = PICK_UP_BEGIN + PICK_UP_SPLIT_EXPR + ".*";
}
