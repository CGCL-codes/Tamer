package rvsnoop;

/**
 * A typesafe enumeration representing a simple set of states.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @version $Revision: 393 $, $Date: 2008-06-02 10:22:38 -0400 (Mon, 02 Jun 2008) $
 * @since 1.5
 */
public final class State {

    /**
     * A key to use when this enumeration is used as a property on a JavaBean.
     */
    public static final String PROP_STATE = "state";

    public static final State PAUSED = new State(1, "PAUSED", "Paused");

    public static final State STARTED = new State(2, "STARTED", "Started");

    public static final State STOPPED = new State(0, "STOPPED", "Stopped");

    private static final State[] VALUES = { STOPPED, PAUSED, STARTED };

    public static State[] getStates() {
        final State[] states = new State[VALUES.length];
        System.arraycopy(VALUES, 0, states, 0, VALUES.length);
        return states;
    }

    public static State valueOf(String state) {
        for (int i = 0; i < VALUES.length; ++i) if (VALUES[i].name.equals(state)) return VALUES[i];
        return null;
    }

    private final String displayName;

    private final String name;

    private final int ordinal;

    private State(int ordinal, String name, String displayName) {
        super();
        this.ordinal = ordinal;
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof State && ((State) obj).ordinal == ordinal;
    }

    /**
     * @return Returns the displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the ordinal.
     */
    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public int hashCode() {
        return 17 * ordinal;
    }

    @Override
    public String toString() {
        return name;
    }
}
