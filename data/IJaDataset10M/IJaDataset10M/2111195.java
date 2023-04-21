package net.jsrb.util;

import net.jsrb.rtl.RtlConstants;
import net.jsrb.rtl.RtlPlatform;

/**
 * Get OS platform info
 */
public class PlatformUtil {

    /**
     * Return the unique string for current os platform and 64bit mode
     */
    public static String getPlatform() {
        return RtlConstants.PLATFORM.name();
    }

    public static String getPlatformLibraryEnv() {
        RtlPlatform.Os os = RtlConstants.PLATFORM.getOs();
        if (os == RtlPlatform.Os.linux || os == RtlPlatform.Os.solaris) {
            return "LD_LIBRARY_PATH";
        } else if (os == RtlPlatform.Os.hpux) {
            return "SHLIB_PATH";
        } else if (os == RtlPlatform.Os.aix) {
            return "LIBPATH";
        }
        return null;
    }
}
