package net.sf.aft.test;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Hashtable;
import net.sf.aft.util.Queue;
import org.apache.tomcat.core.BaseInterceptor;
import org.apache.tomcat.core.Context;
import org.apache.tomcat.core.ContextManager;
import org.apache.tomcat.core.TomcatException;
import org.apache.tomcat.startup.EmbededTomcat;
import org.apache.tomcat.startup.Main;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tools.ant.BuildException;

/**
 * Start an embedded Tomcat servlet container.
 *
 * <p>Tomcat will start executing in a parallel thread with the main
 * Ant thread. Incoming HTTP requests are received by Tomcat, which
 * will create a new thread for serving the request. The servlet which
 * gets executed will always be <code>{@link
 * net.sf.aft.servlet.ListenerProxyServlet}</code>.
 *
 * <p>When a <code>listener</code> element is executed by Ant, the
 * execution will stop until an HTTP request is received by
 * Tomcat. When this happens, <code>{@link
 * net.sf.aft.servlet.ListenerProxyServlet}</code> will pass to
 * the <code>Listener</code> instance the received HTTP message. This
 * in turn will pass the object to the embedded {@link MatcherSet},
 * for the matching process to start.
 *
 * <p>Responses are serialized from the Ant thread into the {@link
 * net.sf.aft.servlet.ListenerProxyServlet} thread, using a
 * <code>{@link java.io.PipedWriter}</code> object. The servlet thread
 * will in turn write the data back to the HTTP client.
 *
 * @author <a href="mailto:ovidiu@cup.hp.com">Ovidiu Predescu</a>
 * @author <a href="mailto:cmanolache@yahoo.com">Costin Manolache</a>
 * @version $Revision: 1.2 $ $Date: 2002/01/01 12:51:56 $
 * @since September 25, 2001
 * @see net.sf.aft.servlet.ListenerProxyServlet
 */
public class ServletContainer {

    static ServletContainer container = null;

    static int DEFAULT_PORT = 8080;

    Vector portNumbers = new Vector();

    int maxThreads = 100;

    int maxSpareThreads = 50;

    int minSpareThreads = 10;

    Queue requestsQueue = new Queue();

    Hashtable registeredListeners = new Hashtable();

    EmbededTomcat tomcat;

    boolean alreadyStarted = false;

    public static synchronized ServletContainer getServletContainer() {
        if (container == null) container = new ServletContainer();
        return container;
    }

    public ServletContainer() {
    }

    public Queue getRequestsQueue() {
        return requestsQueue;
    }

    public void registerListener(Listener listener) {
        registeredListeners.put(listener, listener);
    }

    public void removeListener(Listener listener) {
        registeredListeners.remove(listener);
    }

    public Hashtable getRegisteredListeners() {
        return registeredListeners;
    }

    public void setPort(String ports) throws BuildException {
        StringTokenizer st = new StringTokenizer(ports, " ,;");
        while (st.hasMoreTokens()) {
            String port = st.nextToken();
            try {
                Integer portInt = Integer.valueOf(port);
                portNumbers.add(portInt);
            } catch (NumberFormatException ex) {
                throw new BuildException("Port specification is not an integer: " + port);
            }
        }
    }

    /**
   * Returns the first port declared with {@link #setPort} or DEFAULT_PORT.
   */
    public int getDefaultPort() {
        if (portNumbers.size() > 0) return ((Integer) portNumbers.get(0)).intValue(); else return DEFAULT_PORT;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void setMaxSpareThreads(int maxSpareThreads) {
        this.maxSpareThreads = maxSpareThreads;
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
    }

    public void execute() {
        if (alreadyStarted) return;
        if (portNumbers.size() == 0) portNumbers.add(new Integer(DEFAULT_PORT));
        String cwd = System.getProperty("ant.home") + File.separator + "tomcat";
        System.setProperty("tomcat.home", cwd);
        System.out.println("tomcat.home set to " + System.getProperty("tomcat.home"));
        try {
            String installDir = IntrospectionUtils.guessInstall("tomcat.install", "tomcat.home", "tomcat.jar");
            System.out.println("Guessed installDir to be " + installDir);
            installDir = cwd;
            System.out.println("Forcing installDir to be " + cwd);
            String libDir = installDir + File.separator + "lib" + File.separator + "common";
            ClassLoader parentL = this.getClass().getClassLoader();
            URL commonCP[] = IntrospectionUtils.getClassPath(libDir, null, Main.PROPERTY_COMMON_LOADER, false);
            System.out.println("commonCP contains ");
            for (int i = 0; i < commonCP.length; i++) {
                System.out.println("\t" + commonCP[i]);
            }
            System.out.println();
            IntrospectionUtils.displayClassPath("Main classpath: ", commonCP);
            ClassLoader commonCL = URLClassLoader.newInstance(commonCP, parentL);
            Class tomcatClass = commonCL.loadClass("org.apache.tomcat.startup.EmbededTomcat");
            tomcat = (EmbededTomcat) tomcatClass.newInstance();
            tomcat.setInstall(cwd);
            tomcat.setEstart(true);
            tomcat.initClassLoaders();
            tomcat.setParentClassLoader(parentL);
            tomcat.setCommonClassPath(commonCP);
            tomcat.setCommonClassLoader(commonCL);
            tomcat.addDefaultModules();
            for (int i = 0; i < portNumbers.size(); i++) {
                Integer port = (Integer) portNumbers.get(i);
                int mid = tomcat.addEndpoint(port.intValue(), null, null);
                tomcat.setModuleProperty(mid, "maxThreads", Integer.toString(maxThreads));
                System.out.println("Adding interceptor on port " + port);
            }
            tomcat.initContextManager();
            tomcat.execute();
            alreadyStarted = true;
        } catch (Exception ex) {
            tomcat.debug("main", ex);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {
        ServletContainer container = new ServletContainer();
        container.setPort("8100, 8101");
        container.execute();
    }
}
