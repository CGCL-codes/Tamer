package com.sun.midp.io;

import javax.microedition.io.*;
import com.sun.cldc.io.*;
import com.sun.midp.Configuration;
import com.sun.midp.io.j2me.http.Protocol;
import java.io.IOException;

/**
 * This class provides access to the internal only connection
 * types needed by the implementations of the supported subsystems.
 * If bypasses the javax.microedition.
 */
public class InternalConnector {

    /**
     * The platform name
     */
    private static String platform;

    /**
     * True if we are running on a J2ME system (defaults to false)
     */
    private static boolean j2me;

    /** root for MIDP io packages */
    private static String midpRoot;

    /** root for CLDC io packagaes. */
    private static String cldcRoot;

    /**
     * If enableAllProtocols is true, then open(...)
     * will allow applications to use non-MIDP protocols.
     */
    private static boolean enableAllProtocols;

    /**
     * Class initializer
     */
    static {
        String prop;
        if (Configuration.getProperty("microedition.configuration") != null) {
            j2me = true;
        }
        platform = Configuration.getProperty("microedition.platform");
        if (platform == null) {
            platform = j2me ? "j2me" : "j2se";
        }
        midpRoot = "com.sun.midp.io." + platform;
        cldcRoot = "com.sun.cldc.io." + platform;
        prop = Configuration.getProperty("com.sun.midp.io.enable_extra_protocols");
        if (prop != null) {
            enableAllProtocols = prop.equals("true");
        }
    }

    /**
     * Prevent instantiation
     */
    private InternalConnector() {
    }

    /**
     * Create and open a Connection. This method is internal to
     * the MIPD implementation to allow
     * any of the MIDP and CLDC protocols can be instantiated.
     *
     * @param name             The URL for the connection
     * @param mode             The access mode
     * @param timeouts         A flag to indicate that the called wants
     *                         timeout exceptions
     * @return                 A new Connection object
     *
     * @exception IllegalArgumentException If a parameter is invalid.
     * @exception ConnectionNotFoundException if the requested connection
     * cannot be make, or the protocol type does not exist.
     * @exception IOException  If some other kind of I/O error occurs.
     */
    public static Connection openInternal(String name, int mode, boolean timeouts) throws IOException {
        try {
            return openPrim(name, mode, timeouts, midpRoot);
        } catch (ClassNotFoundException x) {
        }
        try {
            return openPrim(name, mode, timeouts, cldcRoot);
        } catch (ClassNotFoundException x) {
        }
        throw new ConnectionNotFoundException("The requested protocol does not exist " + name);
    }

    /**
     * Create and open a Connection.
     *
     * @param name             The URL for the connection
     * @param mode             The access mode
     * @param timeouts         A flag to indicate that the called
     *                         wants timeout exceptions
     * @return                 A new Connection object
     *
     * @exception IllegalArgumentException If a parameter is invalid.
     * @exception ConnectionNotFoundException if the requested connection
     * cannot be make, or the protocol type does not exist.
     * @exception IOException  If some other kind of I/O error occurs.
     */
    public static Connection open(String name, int mode, boolean timeouts) throws IOException {
        try {
            return openPrim(name, mode, timeouts, midpRoot);
        } catch (ClassNotFoundException x) {
        }
        if (enableAllProtocols) {
            try {
                return openPrim(name, mode, timeouts, cldcRoot);
            } catch (ClassNotFoundException x) {
            }
        }
        throw new ConnectionNotFoundException("The requested protocol does not exist " + name);
    }

    /**
     * Create and open a Connection
     *
     * @param name             The URL for the connection
     * @param mode             The access mode
     * @param timeouts         A flag to indicate that the called wants
     *                         timeout exceptions
     * @param root             MIDP or CLDC root path for I/O packages
     * @return                 A new Connection object
     *
     * @exception ClassNotFoundException  If the protocol cannot be found.
     * @exception IllegalArgumentException If a parameter is invalid.
     * @exception ConnectionNotFoundException If the connection cannot be found.
     * @exception IOException If some other kind of I/O error occurs.
     * @exception IOException  If an I/O error occurs
     * @exception IllegalArgumentException If a parameter is invalid
     */
    private static Connection openPrim(String name, int mode, boolean timeouts, String root) throws IOException, ClassNotFoundException {
        if (name == null) {
            throw new IllegalArgumentException("Null URL");
        }
        int colon = name.indexOf(':');
        if (colon < 1) {
            throw new IllegalArgumentException("no ':' in URL");
        }
        try {
            String protocol;
            protocol = name.substring(0, colon);
            name = name.substring(colon + 1);
            if (protocol.equals("file")) {
                protocol = "storage";
            }
            if (protocol.equals("http")) {
                ConnectionBaseInterface hc = (ConnectionBaseInterface) new com.sun.midp.io.j2me.http.Protocol();
                return hc.openPrim(name, mode, timeouts);
            }
            if (protocol.equals("https") && !enableAllProtocols) {
                throw new ClassNotFoundException("https is not enabled");
            }
            Class clazz = Class.forName(root + "." + protocol + ".Protocol");
            ConnectionBaseInterface uc = (ConnectionBaseInterface) clazz.newInstance();
            return uc.openPrim(name, mode, timeouts);
        } catch (InstantiationException x) {
            throw new IOException(x.toString());
        } catch (IllegalAccessException x) {
            throw new IOException(x.toString());
        } catch (ClassCastException x) {
            throw new IOException(x.toString());
        }
    }
}
