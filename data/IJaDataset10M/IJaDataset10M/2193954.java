package net.sf.wmutils.eclipse.common.util;

public class Platform {

    private static final String osName = System.getProperty("os.name");

    private static final boolean isAIX = "AIX".equals(osName);

    private static final boolean isHpUX = "HP-UX".equals(osName);

    private static final boolean isSolaris = "SunOS".equals(osName);

    private static final boolean isWindows = osName.startsWith("Windows");

    /**
	 * Returns, whether the platform is AIX.
	 */
    public static boolean isAIX() {
        return isAIX;
    }

    /**
	 * Returns, whether the platform is HP/UX.
	 */
    public static boolean isHpUX() {
        return isHpUX;
    }

    /**
	 * Returns, whether the platform is Windows.
	 */
    public static boolean isWindows() {
        return isWindows;
    }

    /**
	 * Returns, whether the platform is Solaris.
	 */
    public static boolean isSolaris() {
        return isSolaris;
    }

    /**
	 * Returns, whether the JVM can turn on 64bit mode.
	 */
    public static boolean has64BitMode() {
        return isHpUX || isSolaris;
    }
}
