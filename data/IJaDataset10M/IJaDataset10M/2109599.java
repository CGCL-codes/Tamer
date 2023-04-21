package joelib.ext;

import org.apache.log4j.Category;

/**
 * Some helper methods for calling external programs.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.11 $, $Date: 2004/07/25 20:43:16 $
 */
public class ExternalHelper {

    /** Obtain a suitable logger. */
    private static Category logger = Category.getInstance("joelib.ext.ExternalHelper");

    public static final String OS_WINDOWS = "windows";

    public static final String OS_LINUX = "linux";

    public static final String OS_SOLARIS = "solaris";

    /** Don't let anyone instantiate this class */
    private ExternalHelper() {
        logger.info("Operating system is: " + getOperationSystemName());
    }

    /**
     * Returns the name of the operation system.
     *
     *   @todo maybe move this method to a more common class */
    public static String getOperationSystemName() {
        String osName = System.getProperty("os.name");
        if (osName.indexOf("Windows") != -1) {
            osName = OS_WINDOWS;
        } else if (osName.indexOf("Linux") != -1) {
            osName = OS_LINUX;
        } else if (osName.indexOf("Solaris") != -1) {
            osName = OS_SOLARIS;
        }
        return osName;
    }
}
