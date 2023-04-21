package de.fhg.igd.semoa.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import de.fhg.igd.logging.LogLevel;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.fhg.igd.semoa.net.AbstractServer;
import de.fhg.igd.semoa.net.SSLMaster;
import de.fhg.igd.semoa.server.Environment;
import de.fhg.igd.util.WhatIs;

/**
 * This class provides an Https server.
 * <p>
 * It is usually run within a JShell using
 * <code>de.fhg.igd.semoa.bin.Publish<code>.
 * A command line run was removed because it
 * is not possible (for now) to register
 * servlets in the same VM after the server
 * is run.
 * <p>
 * Servlets should not get a direct reference to
 * this server because it inherits some public
 * methods that can be used by malicious servlets.
 * Since servlets need a reference to their
 * <codE>ServletContext</code> you can use the
 * <codE>ServletContextWrapper</code> class.
 * <p>
 * A typical command line could look like this:
 * <br><code>
 * java de.fhg.igd.semoa.bin.Publish -detach
 * -spawn -proxy none -key ${WhatIs:HTTP_SERVER}
 * -class de.fhg.igd.semoa.web.HttpsServer ;
 * -Port 8080 -MimeTypes ${user.home}/semoa/etc/mime.types
 * -run
 * </code><br>
 * where <code>-run</code> should be the last
 * parameter.<br>
 * important parameters:<br>
 * <code>-Port</code> followed by an integet that specifies
 * the port where the server will run on.
 * <br>
 * <code>-MimeTypes</code> followed by a file name where
 * the mime types are saved (this is a property file).
 * <br>
 * <code>-InitParameters</code>followed by a file name where
 * the server will read its init parameters from
 * (this is a property file).
 * <br>
 * <code>-DebugOut</code>followed by a file name where
 * the server will write additional debug information
 * like the headers that are sent by a browser.
 * <br>
 * <code>-run</code> runs the server. If not called
 * the server does not listen to requests. A call to
 * run blocks so it must be the last method called.
 * <br>
 * All these parameters except <code>run</code>
 * (which should be the last one) are not mandatory.
 *
 * <p>
 * FIX ME:
 * Right now servlets can just be registered on a
 * path directly under the root path. That means
 * you can register a servlet on <code>/foo</code>
 * but not one on <code>/foo/bar</code>.
 * Solution:
 * must be done in the <code>Request</code> class
 * that it returns the correct servlet path.
 *
 * @see AbstractServer
 *
 * @author Roger Hartmann
 * @author Jan Peters
 * @version "$Id: HttpsServer.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class HttpsServer extends HttpServer {

    /**
     * The <code>Logger</code> instance for this class 
     */
    private static Logger log_ = LoggerFactory.getLogger("network/web");

    /**
     * The {@link WhatIs} entry key for this service.
     */
    public static final String WHATIS = "HTTPS_SERVER";

    /**
     * The dependencies to other objects in the global
     * <code>Environment</code>.
     */
    private static final String[] DEPEND_ = { "WHATIS:SSL_MASTER" };

    /**
     * The protocol names.
     */
    private static final String PROTOCOL = "https";

    /**
     * Flag that enables/disables client authentication.
     */
    private static final boolean CLIENT_AUTHENTICATION = false;

    /**
     * The internal lock object.
     */
    private Object lock_ = new Object();

    /**
     * The <code>ServerSocketFactory</code> for SSL sockets.
     */
    private SSLServerSocketFactory factory_;

    /**
     * If no port is given, the server will use this one.
     */
    private static final int DEF_PORT = 8443;

    public HttpsServer() {
        super();
        initServerSocketFactory();
    }

    public String info() {
        return "This Service provides an HTTPS Server.";
    }

    public String revision() {
        return "$Id: HttpsServer.java 1913 2007-08-08 02:41:53Z jpeters $";
    }

    public String[] dependencies() {
        return (String[]) DEPEND_.clone();
    }

    public String protocol() {
        return PROTOCOL;
    }

    /**
     * When using SSL, this method initialises the
     * server socket factory.
     */
    protected void initServerSocketFactory() {
        SSLContext ctx;
        SSLMaster sm;
        String key;
        synchronized (lock_) {
            if (factory_ != null) {
                return;
            }
            key = WhatIs.stringValue(SSLMaster.WHATIS);
            sm = (SSLMaster) Environment.getEnvironment().lookup(key);
            if (sm == null) {
                throw new IllegalStateException("SSLMaster not found at \"" + key + "\"");
            }
            ctx = sm.getSSLContext(PROTOCOL + "-server");
            factory_ = ctx.getServerSocketFactory();
        }
    }

    /**
     * When using SSL, the old method has to be replaced.
     */
    protected ServerSocket createServerSocket() throws IOException {
        SSLServerSocket socket;
        synchronized (lock_) {
            try {
                socket = (SSLServerSocket) factory_.createServerSocket(getPort());
                socket.setNeedClientAuth(CLIENT_AUTHENTICATION);
                return socket;
            } catch (Exception e) {
                log_.caught(LogLevel.ERROR, "Could not create SSL socket: " + e, e);
                throw new IOException(e.getMessage());
            }
        }
    }

    /**
     * Original method overidden that an
     * <code>HttpJob</code> is created.
     *
     * @param socket The <code>Socket</code> of the
     *   connection.
     * @return A <code>Runnable</code> cast of the
     *   <code>HttpJob</code>.
     */
    protected Runnable createJob(Socket socket) {
        HttpJob job;
        if (socket == null) {
            throw new NullPointerException("Parameter may not be null.");
        }
        job = new HttpJob(this, socket, getEnvironment(), WhatIs.stringValue(HttpsServer.WHATIS));
        return job;
    }

    /**
     * Returns the name and version of the servlet engine on which
     * the servlet is running. 
     *
     * <p>The form of the returned string is
     * <i>servername</i>/<i>versionnumber</i>.
     * For example, the Java Web Server can return the string
     * <code>Java Web Server/1.1.3</code>.
     *
     * <p>You can design the servlet engine to have this method return 
     * other optional information in parentheses after the primary string, 
     * for example,
     * <code>Java Web Server/1.1.3 (JDK 1.1.6; Windows NT 4.0 x86)</code>.
     *
     * @return a <code>String</code> containing at least the 
     *   servlet engine name and version number
     */
    public String getServerInfo() {
        return "Semoa Https Server/$Id: HttpsServer.java 1913 2007-08-08 02:41:53Z jpeters $";
    }

    /**
     * Returns a boolean indicating whether this
     * request was made using a secure channel, such as
     * HTTPS.
     *
     * @return <code>true</code>, since the request was made
     *   using a secure channel
     */
    public boolean isSecure() {
        return true;
    }

    /**
     * Returns the name of the authentication scheme the
     * server uses, for example, "BASIC" or "SSL," or <code>null</code>
     * if the server does not have an authentication scheme. 
     *
     * <p>The authentication scheme provides a challenge-response
     * model in which the server challenges the client,
     * and the client provides authentication information.
     * Same as the value of the CGI variable AUTH_TYPE.
     *
     * @return a <code>String</code> specifying the name of
     *   the authentication scheme, or
     *   <code>null</code> if the server
     *   does not have an authentication
     *   scheme
     */
    public String getAuthType() {
        if (CLIENT_AUTHENTICATION) {
            return "SSL/TLS with client authentication";
        } else {
            return "SSL/TLS without client authentication";
        }
    }

    public String toString() {
        StringBuffer buf;
        String[] suites;
        int n;
        buf = new StringBuffer();
        buf.append(super.toString());
        buf.append("\nEnabled cipher suites");
        buf.append("\n---------------------");
        synchronized (lock_) {
            suites = factory_.getSupportedCipherSuites();
        }
        for (n = 0; n < suites.length; n++) {
            buf.append("\n" + (n + 1) + ". " + suites[n]);
        }
        return buf.toString();
    }
}
