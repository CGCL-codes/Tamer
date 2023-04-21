package tgreiner.amy.go.engine;

/**
 * Typesafe enum for stones.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public final class Stone {

    /** A white stone. */
    public static final Stone WHITE = new Stone();

    /** A black stone. */
    public static final Stone BLACK = new Stone();

    /**
     * Create a stone.
     */
    private Stone() {
    }
}
