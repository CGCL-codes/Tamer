package org.lwjgl;

/**
 *
 * System class platform specific method interface
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3426 $
 * $Id: SysImplementation.java 3426 2010-10-01 22:20:14Z spasi $
 */
interface SysImplementation {

    /**
	 * Return the required version of the native library
	 */
    int getRequiredJNIVersion();

    /**
	 * Return the version of the native library
	 */
    int getJNIVersion();

    /**
	 * Returns the platform's pointer size in bytes
	 */
    int getPointerSize();

    void setDebug(boolean debug);

    /**
	 * Obtains the number of ticks that the hires timer does in a second.
	 *
	 * @return timer resolution in ticks per second or 0 if no timer is present.
	 */
    long getTimerResolution();

    long getTime();

    void alert(String title, String message);

    boolean openURL(String url);

    String getClipboard();

    /**
	 * Returns true there exists a separate 64 bit library
	 * on the platform
	 */
    boolean has64Bit();
}
