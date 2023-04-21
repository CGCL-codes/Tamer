package sdljava.event;

/**
 *
 *
 */
public final class SDLEventState {

    public static final SDLEventState QUERY = new SDLEventState("QUERY", -1);

    public static final SDLEventState IGNORE = new SDLEventState("IGNORE", 0);

    public static final SDLEventState DISABLE = new SDLEventState("DISABLE", 0);

    public static final SDLEventState ENABLE = new SDLEventState("ENABLE", 1);

    public final int swigValue() {
        return swigValue;
    }

    public String toString() {
        return swigName;
    }

    public static SDLEventState swigToEnum(int swigValue) {
        if (swigValue < swigValues.length && swigValues[swigValue].swigValue == swigValue) return swigValues[swigValue];
        for (int i = 0; i < swigValues.length; i++) if (swigValues[i].swigValue == swigValue) return swigValues[i];
        throw new IllegalArgumentException("No enum " + SDLEventState.class + " with value " + swigValue);
    }

    private SDLEventState(String swigName) {
        this.swigName = swigName;
        this.swigValue = swigNext++;
    }

    private SDLEventState(String swigName, int swigValue) {
        this.swigName = swigName;
        this.swigValue = swigValue;
        swigNext = swigValue + 1;
    }

    private static SDLEventState[] swigValues = { QUERY, IGNORE, DISABLE, ENABLE };

    private static int swigNext = 0;

    private final int swigValue;

    private final String swigName;
}
