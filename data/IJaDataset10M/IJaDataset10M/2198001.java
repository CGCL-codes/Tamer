package calclipse.mcomp.trace;

/**
 * A location in a script specified with
 * one-based line and column numbers.
 * @author T. Sommerland
 */
public class Location {

    private final int line;

    private final int column;

    /**
     * Both parameters must be >= 1.
     */
    public Location(final int line, final int column) {
        if (line < 1 || column < 1) {
            throw new IllegalArgumentException("Line and column numbers are one-based.");
        }
        this.line = line;
        this.column = column;
    }

    /**
     * Creates the location (1, 1).
     */
    public Location() {
        this(1, 1);
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Creates a new location using loc as relative to this.
     */
    public Location add(final Location loc) {
        final int newLine = line + loc.line - 1;
        final int newCol;
        if (loc.line > 1) {
            newCol = loc.column;
        } else {
            newCol = column + loc.column - 1;
        }
        return new Location(newLine, newCol);
    }
}
