package eu.haslgruebler.darsens.conf;

/**
 * DEBUG LEVELs
 * @author Michael Haslgrübler, uni-michael@haslgruebler.eu
 *
 */
public interface Level {

    public static final byte NONE = 0;

    public static final byte SEVERE = 1;

    public static final byte WARNING = 2;

    public static final byte INFO = 3;

    public static final byte CONFIG = 4;

    public static final byte FINE = 5;

    public static final byte FINER = 6;

    public static final byte FINEST = 7;

    public static final byte ALL = 8;
}
