package muse.common;

/**
 * Possible values of DataChannel's type and direction fields
 * @author Evdokimov
 */
public interface DataChannelConst {

    /**
     * Input direction.
     */
    public static final int DIRECTION_INPUT = 1;

    /**
     * Output direction.
     */
    public static final int DIRECTION_OUTPUT = 2;

    /**
     * For external module with command line.
     */
    public static final int TYPE_PROGRAM_COMMAND_LINE = 1;

    /**
     * For external module with text file.
     */
    public static final int TYPE_TEXT_FILE = 3;
}
