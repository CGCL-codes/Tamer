package com.wm.pkg.mktcon.util;

import com.wm.app.b2b.client.*;
import com.wm.app.b2b.server.Service;
import com.wm.data.*;
import com.wm.pkg.wic1.*;
import com.wm.pkg.wic1.uuid.*;
import com.wm.util.Values;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.activation.*;
import javax.mail.internet.*;
import junit.framework.TestCase;

/**
 * A very minimal client facade to webMethods Integration server.
 */
public class ImportTest {

    public static final String PORT_KEY = "port";

    public static final String HOST_KEY = "host";

    public static final String USER_KEY = "user";

    public static final String PASSWORD_KEY = "password";

    public static final String PROXY_HOST_KEY = "proxy";

    public static final String PROXY_USER_KEY = "proxyUser";

    public static final String PROXY_PASSWORD_KEY = "proxyPass";

    public static final String SERVICE_KEY = "service";

    public static final String IFACE_KEY = "iface";

    private Context _context;

    public ImportTest(String host, String port, String user, String password, String dir) {
        try {
            _context = new Context();
            if (dir != null) {
                System.out.println("setting home directory to " + dir);
                System.getProperties().put("watt.server.homeDir", dir);
            }
            System.out.println("Connecting to " + host + ":" + port);
            _context.connect(host + ":" + port, user, password);
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }

    /**
     * Invokes the given service and returns the results..
     */
    public Values invoke(String iface, String service, Values in) {
        try {
            return _context.invoke(iface, service, in);
        } catch (ServiceException se) {
            se.printStackTrace(System.out);
            return null;
        }
    }
}
