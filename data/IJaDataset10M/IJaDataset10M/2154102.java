package SASLib;

/**
 * This class contains information on common error codes w/ in 
 * SASlib as well as the _DEBUG flag
 * 
 * @author Wil Cecil
 */
public abstract class Header {

    /**
     * Set this to true to display debug information.
     * <br><br>
     * Best set to false.
    */
    public static boolean _DEBUG = false;

    /**
     * Input file not specified
     */
    public static int NO_IN_FILE = -9101;

    /**
     * Output file not specified
     */
    public static int NO_OUT_FILE = -9102;
}
