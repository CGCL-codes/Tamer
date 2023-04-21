package de.pitkley.minejar;

/**
 *
 * @author pit
 */
public class OSInfo {

    public enum OSFamily {

        OS_MAC, OS_WINDOWS, OS_LINUX
    }

    public static OSFamily getOSFamily() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            return OSFamily.OS_MAC;
        } else if (osName.contains("Windows")) {
            return OSFamily.OS_WINDOWS;
        } else {
            return OSFamily.OS_LINUX;
        }
    }
}
