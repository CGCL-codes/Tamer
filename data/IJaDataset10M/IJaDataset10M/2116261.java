package hyperocha.freenet.fcp;

/**
 * @author saces
 *
 */
public class Verbosity {

    public static final Verbosity ALL = new Verbosity(Integer.MAX_VALUE);

    public static final Verbosity NONE = new Verbosity(0);

    public static final Verbosity SPLITFILE_PROGRESS = new Verbosity(1);

    public static final Verbosity PUT_FETCHABLE = new Verbosity(256);

    public static final Verbosity COMPRESSION_START_END = new Verbosity(512);

    private int verbosity;

    /**
	 * @param verbosity
	 */
    public Verbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Verbosity)) return false;
        return ((Verbosity) obj).verbosity == verbosity;
    }

    public String toString() {
        return "" + verbosity;
    }

    public int getVerbosity() {
        return verbosity;
    }
}
