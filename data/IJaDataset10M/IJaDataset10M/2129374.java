package org.dbe.servent.coreapps;

import java.lang.reflect.Method;
import org.dbe.servent.ServentContext;
import org.dbe.servent.ServentInfo;
import org.dbe.servent.ServiceContext;
import org.dbe.servent.coreapps.WebappService;
import org.dbe.servent.http.ServentServer;
import org.dbe.servent.tools.CoreAdapter;

/**
 * Webapp service implementation.
 * 
 * This is the implementation of the WebappService interface.
 * 
 * All webapplication creation work is done in the
 * <code>init (ServentContext)</code> method hieraced from CoreAdapter. No
 * other methods are needed at this moment.
 * 
 * @author bob
 */
public class WebappServiceImpl implements WebappService, CoreAdapter {

    /** service context */
    ServiceContext serviceContext;

    /** Webapp specific server */
    Object jettyServer;

    /** hostname */
    private String hostname;

    /** deployment directory */
    private String webappsDirectory;

    /** listening port */
    private int port;

    /**
     * @see org.dbe.servent.tools.CoreAdapter#init(org.dbe.servent.ServentContext)
     */
    public void init(ServentContext context) {
        try {
            ServentServer httpServer = context.getHttpServer();
            readParameters(context);
            if (httpServer.getClass().getName().equals(JETTY_HTTP_SERVER)) {
                Method getServerMethod = httpServer.getClass().getMethod("getServer", new Class[] { Integer.TYPE });
                jettyServer = getServerMethod.invoke(httpServer, new Object[] { new Integer(port) });
                Method addWebApplicationsMehod = jettyServer.getClass().getMethod("addWebApplications", new Class[] { String.class });
                addWebApplicationsMehod.invoke(jettyServer, new Object[] { this.webappsDirectory });
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                Method startMethod = jettyServer.getClass().getMethod("start", (Class[]) null);
                startMethod.invoke(jettyServer, (Object[]) null);
            } else {
                serviceContext.getLogger().error("HttpServer is not a JettyHttpServer instance. Webapps are not allowed");
            }
        } catch (Throwable e) {
            serviceContext.getLogger().error("Service was not deployed!", e);
        }
    }

    /**
     * Read parameters from the service deployment file or set the default
     * parameters if they are not specified in the deploiment file.
     * 
     * @param context
     *            servent context
     */
    private void readParameters(ServentContext context) {
        this.port = ServentInfo.getInstance().getPrivatePort();
        try {
            this.port = Integer.parseInt(serviceContext.getParameter("port"));
        } catch (NumberFormatException e) {
        }
        this.hostname = ServentInfo.getInstance().getPrivateURL().getHost();
        try {
            String readHostName = serviceContext.getParameter("hostname");
            if (readHostName != null) {
                if (!"".equals(readHostName)) {
                    this.hostname = readHostName;
                }
            }
        } catch (Exception ex) {
        }
        this.webappsDirectory = (serviceContext.getParameter("webappsDirectory") == null) ? serviceContext.getHome().getAbsolutePath() + "/webapps" : serviceContext.getHome().getAbsolutePath() + "/" + serviceContext.getParameter("webappsDirectory");
        serviceContext.getLogger().debug("WEBAPPS http://" + hostname + ":" + port + "/  running on " + webappsDirectory);
    }

    /**
     * @see org.dbe.servent.Adapter#init(org.dbe.servent.ServiceContext)
     */
    public void init(ServiceContext context) {
        this.serviceContext = context;
    }

    /**
     * @see org.dbe.servent.Adapter#destroy()
     */
    public void destroy() {
        try {
            Method startMethod = jettyServer.getClass().getMethod("stop", (Class[]) null);
            startMethod.invoke(jettyServer, (Object[]) null);
        } catch (Throwable e) {
            System.out.println("ERROR. " + serviceContext.getName() + " was not correctly stoped");
            e.printStackTrace();
        }
    }
}
