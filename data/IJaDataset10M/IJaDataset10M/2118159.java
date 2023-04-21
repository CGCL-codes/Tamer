package agentswarm.simulation;

/**
 * A globally unique identifier.
 */
public class Id {

    private static long next = 1;

    public static synchronized Id nextId() {
        return new Id(next++);
    }

    private long value;

    private Id(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
