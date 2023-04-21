package eulergui.util;

import java.io.File;

/**
 * @author Jean-Marc Vanel jeanmarc.vanel@gmail.com
 *
 */
public class OSHelper {

    private OSHelper() {
    }

    public static boolean runningOnWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    /** wrap File name If Necessary (necessary on windows) */
    public static String wrapFileIfNecessary(File file) {
        return runningOnWindows() ? "\"" + file + "\"" : file.toString();
    }

    /**
	 * @param location
	 * @return
	 */
    public static String wrapURLIfNecessary(String location) {
        return runningOnWindows() ? "\"" + location + "\"" : location.toString();
    }
}
