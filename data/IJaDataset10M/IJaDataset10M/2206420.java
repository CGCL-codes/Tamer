package com.sun.server;

/**
 * This is a wrapper around the native code used by the Java Web Server
 * to set the effective user and group id's on Solaris.
 * It expects the "server.so" file used by the
 * <a href=http://www.sun.com/software/jwebserver>Java Webserver 2.0</a>.
 * Make sure you rename the file <code>server.so</code>
 * in the distribution to lib<code>com_sun_server_ServerProcess.so</code>, and
 * put it where it will be found by <b>System.loadLibrary</b>.
 *
 * @author		Colin Stevens
 * @version		2.2
 */
public class ServerProcess {

    static {
        System.loadLibrary("com_sun_server_ServerProcess");
    }

    public static native boolean setUser(String userName);

    public static native boolean setGroup(String groupName);
}
