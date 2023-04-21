package net.sf.portletunit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import net.sf.portletunit.service.PortletUnitPortletDefinitionRegistryServiceImpl;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.io.IOUtils;
import org.apache.pluto.PortletContainerServices;
import com.meterware.httpunit.FrameSelector;
import com.meterware.httpunit.PortletUnitResponse;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.InvocationContextFactory;
import com.meterware.servletunit.PortletUnitClient;
import com.meterware.servletunit.PortletUnitInvocationContextImpl;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.meterware.servletunit.ServletUnitHelper;

/**
 *
 * @author <a href="mailto:lpmsmith@ihc.com">Matthew O. Smith</a>
 * @version $Revision: 1.4 $
 *
 **/
public class PortletRunner {

    private static final PortletUnitWebXmlStream WEB_XML_STREAM_FACTORY = new PortletUnitWebXmlStream();

    private static String fileSeparator = System.getProperty("file.separator");

    private static String xmlFile = "web.xml";

    private ServletRunner servletRunner;

    private PortletUser user = null;

    private List properties = new ArrayList();

    private Map sessionAttributes = new HashMap();

    private Map requestAttributes = new HashMap();

    private String applicationName;

    private String portletName;

    private List initListeners = new ArrayList();

    private List destroyListeners = new ArrayList();

    private List renderListeners = new ArrayList();

    private List processActionListeners = new ArrayList();

    private Set roles = new HashSet();

    private PortletRunner(String portletApplicationName) throws Exception {
        this.applicationName = portletApplicationName;
        initializePluto(portletApplicationName);
    }

    /**
	 * Turn on the container logging by providing a PrintStream
	 *
	 * @param stream a <code>PrintStream</code> VALUE
	 */
    public static void setLogging(PrintStream stream) {
    }

    public static PortletRunner createPortletRunner(File portletDir, String portletApplicationName, String portletName) throws Exception {
        PortletRunner rtnval = createPortletRunner(portletDir, portletApplicationName);
        rtnval.setPortletName(portletName);
        return rtnval;
    }

    private void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    /**
	 * Define a portlet based on the <code>portletDir</code>.
	 * @param portletDir the directory that contatains the web.xml and portlet.xml
	 * @return a <code>PortletRunner</code> VALUE
	 * @exception Exception if an error occurs
	 */
    public static PortletRunner createPortletRunner(File portletDir, String portletApplicationName) throws Exception {
        PortletUnitPortletDefinitionRegistryServiceImpl.addPortletApplicationDir(portletDir, portletApplicationName);
        PortletRunner rtnval = new PortletRunner(portletApplicationName);
        String contentPath = "/" + portletApplicationName;
        File webXml = new File(portletDir, fileSeparator + "WEB-INF" + fileSeparator + xmlFile);
        rtnval.setServletRunner(new ServletRunner(webXml, contentPath));
        return rtnval;
    }

    /**
     * Returns the session to be used by the next request. DO NOT USE!!! USE setSessionAttribute instead
     * @param create if true, will create a new session if no valid session is defined.
     */
    public HttpSession getSession(boolean create) {
        return servletRunner.getSession(create);
    }

    public ServletRunner getServletRunner() {
        return servletRunner;
    }

    /**
     * Define the user who is logged in.  Pass <code>null</code> if
     * for no logged in user.
     **/
    public void setUser(PortletUser user) {
        this.user = user;
        _factory = createFactory(user);
        servletRunner.getSession(true).invalidate();
    }

    public void setProperty(String name, String value) {
        properties.add(new MultiKey(name, value));
    }

    /**
	 * Add an attribute to the session.
	 *
	 * @param name a <code>String</code> VALUE
	 * @param VALUE an <code>Object</code> VALUE
	 */
    public void setSessionAttribute(String name, Object value) {
        sessionAttributes.put(name, value);
    }

    /**
	 * Add an attribute to the request.
	 *
	 * @param name a <code>String</code> VALUE
	 * @param VALUE an <code>Object</code> VALUE
	 */
    public void setRequestAttribute(String name, Object value) {
        requestAttributes.put(name, value);
    }

    public WebResponse getResponseForPortlet(String localportletName) throws Exception {
        return getResponse("http://portletunit.sf.net/" + applicationName + "/" + localportletName);
    }

    public WebResponse getResponse() throws Exception {
        String url = "http://portletunit.sf.net/" + applicationName + "/" + portletName;
        return getResponse(url);
    }

    /**
     * Call the servlet at <code>url</code> and return the response.  The host part of the <code>url</code>
     * does not matter.  The rest of the <code>url</code> should match the <code>url-pattern</code> from the
     * web.xml file for the portlet.
     * <pre>
     *    &lt;servlet-mapping>
     *        &lt;servlet-name>welcome&lt;/servlet-name>
     *        &lt;url-pattern>/welcome&lt;/url-pattern>
     *   &lt;/servlet-mapping>
     * </pre>
     *
     * @param url The of the portlet.
     * @return a <code>WebResponse</code> VALUE
     * @exception Exception if an error occurs
     */
    public WebResponse getResponse(String url) throws Exception {
        ServletUnitClient client = ServletUnitClient.newClient(_factory);
        WebRequest request = new PostMethodWebRequest(url);
        InvocationContext invocationContext = PortletUnitClient.initializeInvocationContext(client, request, requestAttributes, sessionAttributes, roles, user);
        for (Iterator iter = properties.iterator(); iter.hasNext(); ) {
            MultiKey entry = (MultiKey) iter.next();
            request.setHeaderField((String) entry.getKey(0), (String) entry.getKey(1));
        }
        HttpSession session = client.getSession(true);
        for (Iterator iter = sessionAttributes.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            session.setAttribute((String) entry.getKey(), entry.getValue());
        }
        WebResponse response = client.getResponse(invocationContext);
        return response;
    }

    static boolean isPlutoInitialized = false;

    private ServletContext servletContext;

    /**
	 * Do all the initialization of the portlet container (pluto)
	 *
	 * @param portletApplicationName a <code>String</code> VALUE
	 * @exception Exception if an error occurs
	 */
    private static void initializePluto(String portletApplicationName) throws Exception {
        InputStream is = PortletRunner.class.getClassLoader().getResourceAsStream("web.servletunit.xml");
        InputStream is2 = PortletRunner.class.getClassLoader().getResourceAsStream("web.servletunit.xml");
        if (is == null) {
            throw new FileNotFoundException("The file web.servletunit.xml.  Make sure that either the portletunit.jar or the directory containing the file is in the classpath");
        }
        List lines = IOUtils.readLines(is2);
        ServletRunner plutoRunner = new ServletRunner(is, "/pluto");
        ServletUnitClient plutoClient = plutoRunner.newClient();
        InvocationContext plutoContext = plutoClient.newInvocation("http://portletunit.sf.net/pluto/portal");
        plutoContext.getServlet();
        PortletContainerServices.prepare("pluto");
        isPlutoInitialized = true;
    }

    private InvocationContextFactory _factory = createFactory(user);

    private InvocationContextFactory createFactory(final PortletUser user) {
        return new InvocationContextFactory() {

            public InvocationContext newInvocation(ServletUnitClient client, FrameSelector targetFrame, WebRequest request, Dictionary clientHeaders, byte[] messageBody) throws IOException, MalformedURLException {
                PortletUnitInvocationContextImpl rtnval = new com.meterware.servletunit.PortletUnitInvocationContextImpl(ServletUnitClient.newClient(this), PortletRunner.this, targetFrame, request, clientHeaders, messageBody);
                try {
                    return PortletUnitClient.initializeInvocationContext(rtnval, request, requestAttributes, sessionAttributes, roles, user);
                } catch (ServletException e) {
                    e.printStackTrace();
                }
                return rtnval;
            }

            public HttpSession getSession(String sessionId, boolean create) {
                return PortletRunner.this.getSession(create);
            }
        };
    }

    public static PortletRunner createPortletRunner(Class portletClass, File portletDir, String portletName) throws Exception {
        String appName = "pu_app";
        File tempWebXml = createTempWebXml(portletClass, portletDir, appName, portletName);
        PortletUnitWebXmlStream streamSource = new PortletUnitWebXmlStream();
        InputStream webXmlStream1 = streamSource.createStream(portletClass, appName, portletName);
        PortletUnitPortletDefinitionRegistryServiceImpl.addPortletApplicationDir(portletDir, appName, webXmlStream1);
        PortletRunner rtnval = new PortletRunner(appName);
        rtnval.setPortletName(portletName);
        String contentPath = "/" + appName;
        ServletRunner servletRunner2 = new ServletRunner(tempWebXml, contentPath);
        rtnval.setServletRunner(servletRunner2);
        ServletContext servletContext = ServletUnitHelper.getServletContext(servletRunner2);
        rtnval.setServletContext(servletContext);
        return rtnval;
    }

    private void setServletContext(ServletContext servletContext2) {
        this.servletContext = servletContext2;
        servletContext.setAttribute(PortletRunner.class.getName(), this);
    }

    private static File createTempWebXml(Class portletClass, File portletDir, String appName, String portletName) throws IOException, FileNotFoundException {
        File pathToWebInf = new File(portletDir, "WEB-INF");
        File tempWebXml = File.createTempFile("web", ".xml", pathToWebInf);
        tempWebXml.deleteOnExit();
        OutputStream webOutputStream = new FileOutputStream(tempWebXml);
        PortletUnitWebXmlStream streamSource = WEB_XML_STREAM_FACTORY;
        IOUtils.copy(streamSource.createStream(portletClass, appName, portletName), webOutputStream);
        webOutputStream.close();
        return tempWebXml;
    }

    public void setServletRunner(ServletRunner servletRunner) {
        this.servletRunner = servletRunner;
    }

    public static PortletUnitWebXmlStream getWebXmlStreamFactory() {
        return WEB_XML_STREAM_FACTORY;
    }

    public ServletContext getServletContext() {
        if (servletContext == null) {
            setServletContext(getSession(false).getServletContext());
        }
        return servletContext;
    }

    public void notifyInitListenersBefore(Portlet delegate, PortletConfig config) throws Exception {
        for (Iterator iter = initListeners.iterator(); iter.hasNext(); ) {
            PortletUnitInitListener listener = (PortletUnitInitListener) iter.next();
            listener.before(delegate, config);
        }
    }

    public void notifyInitListenersAfter(Portlet portlet, PortletConfig config) throws Exception {
        for (Iterator iter = initListeners.iterator(); iter.hasNext(); ) {
            PortletUnitInitListener listener = (PortletUnitInitListener) iter.next();
            listener.after(portlet, config);
        }
    }

    public void notifyDestroyListenersBefore(Portlet delegate) throws Exception {
        for (Iterator iter = destroyListeners.iterator(); iter.hasNext(); ) {
            PortletUnitDestroyListener listener = (PortletUnitDestroyListener) iter.next();
            listener.before(delegate);
        }
    }

    public void notifyDestroyListenersAfter(Portlet delegate) throws Exception {
        for (Iterator iter = destroyListeners.iterator(); iter.hasNext(); ) {
            PortletUnitDestroyListener listener = (PortletUnitDestroyListener) iter.next();
            listener.after(delegate);
        }
    }

    public void addInitListener(PortletUnitInitListener listener) {
        initListeners.add(listener);
    }

    public void addDestroyListener(PortletUnitDestroyListener listener) {
        destroyListeners.add(listener);
    }

    public void addRenderListener(PortletUnitRenderListener listener) {
        renderListeners.add(listener);
    }

    public void addProcessActionListener(PortletUnitProcessActionListener listener) {
        processActionListeners.add(listener);
    }

    public void notifyProcessActionListenersBefore(Portlet delegate, ActionRequest request, ActionResponse response) throws Exception {
        for (Iterator iter = processActionListeners.iterator(); iter.hasNext(); ) {
            PortletUnitProcessActionListener listener = (PortletUnitProcessActionListener) iter.next();
            listener.before(delegate, request, response);
        }
    }

    public void notifyProcessActionListenersAfter(Portlet delegate, ActionRequest request, ActionResponse response) throws Exception {
        for (Iterator iter = processActionListeners.iterator(); iter.hasNext(); ) {
            PortletUnitProcessActionListener listener = (PortletUnitProcessActionListener) iter.next();
            listener.after(delegate, request, response);
        }
    }

    public void notifyRenderListenersBefore(Portlet portlet, RenderRequest request, RenderResponse response) throws Exception {
        for (Iterator iter = renderListeners.iterator(); iter.hasNext(); ) {
            PortletUnitRenderListener listener = (PortletUnitRenderListener) iter.next();
            listener.before(portlet, request, response);
        }
    }

    public void notifyRenderListenersAfter(Portlet portlet, RenderRequest request, RenderResponse response) throws Exception {
        for (Iterator iter = renderListeners.iterator(); iter.hasNext(); ) {
            PortletUnitRenderListener listener = (PortletUnitRenderListener) iter.next();
            listener.after(portlet, request, response);
        }
    }

    public void addRole(String role) {
        roles.add(role);
    }
}
