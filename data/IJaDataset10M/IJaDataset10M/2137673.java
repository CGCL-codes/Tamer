package gpl.scotlandyard.utils;

/**
 * @author Norbert
 *
 */
public interface Constants {

    /**
	   * Use to exit program on normal mode.
	   */
    public static final int STD_EXIT = 0;

    /**
	   * Use to exit program on fatal error concerning configuration.
	   */
    public static final int FATAL_CONFIG = 1;

    /**
	   * Use to exit program on fatal error concerning bundle.
	   */
    public static final int FATAL_I18N = 2;

    /**
	   * Use to exit program on fatal error concerning IO.
	   */
    public static final int FATAL_IO = 3;
}
