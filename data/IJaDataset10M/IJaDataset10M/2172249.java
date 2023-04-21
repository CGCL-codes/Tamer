package org.xebra.wado.server.com;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.imageio.spi.IIORegistry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.xebra.dcm.io.DCMImageReaderSpi;
import org.xebra.dcm.util.ErrorMessages;
import org.xebra.wado.server.auth.UserPermission;
import org.xebra.wado.server.util.ServerProperties;

/**
 * This class is used to implement a subset of servlet methods in order to enhance
 * server security.  By extending this class, many of the servlet methods have already
 * been implemented to throw errors.  Only a newly defined 
 * {@link #processRequest(HttpServletRequest, HttpServletResponse)} method needs to be
 * implemented by the extending classes.  This method is set up to be called by both
 * the GET and POST requests.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.2 $
 */
public abstract class AbstractServlet extends HttpServlet {

    private static final long serialVersionUID = 2964017463946248132L;

    protected static Logger _log = null;

    /**
	 * This method is overriden in order to load the Log4J properties file.
	 * 
	 * @throws ServletException 
	 *         thrown if there is an error initializing the servlet.
	 *
	 * @see javax.servlet.GenericServlet#init()
	 */
    public void init() throws ServletException {
        String prefix = getServletContext().getRealPath("/");
        String log4jFilename = getInitParameter("log4j-init-file");
        if (log4jFilename != null && log4jFilename.length() > 0) {
            DOMConfigurator.configureAndWatch(prefix + log4jFilename);
            _log = Logger.getLogger(AbstractServlet.class);
            _log.info("LOG4j Configuration: " + prefix + log4jFilename);
        }
        String pacsConf = getInitParameter("pacs-db-conf");
        if (pacsConf != null && pacsConf.length() > 0) {
            _log.info("PACS Db Configuration: " + prefix + pacsConf);
            System.setProperty("pacs.db.conf", prefix + pacsConf);
        }
        _log.info("Initializing servlet: " + getServletName());
        ServerProperties props = ServerProperties.getSingletonInstance();
        String permissionClass = getInitParameter("permission-class");
        _log.info("Permission Class: " + permissionClass);
        if (permissionClass != null) {
            try {
                Class<? extends UserPermission> c = Class.forName(permissionClass).asSubclass(UserPermission.class);
                props.setPermissionClass(c);
            } catch (Throwable t) {
                _log.error("Error initializing user permissions", t);
            }
        }
        String dfltBuf = getInitParameter("default-data-buffer");
        if (dfltBuf != null && Pattern.matches("^\\d+$", dfltBuf)) {
            props.setDefaultDataBuffer(dfltBuf);
        }
        if (IIORegistry.getDefaultInstance().getServiceProviderByClass(DCMImageReaderSpi.class) == null) {
            _log.info("Registering service provider");
            IIORegistry.getDefaultInstance().registerServiceProvider(new DCMImageReaderSpi());
        }
    }

    /**
     * Authenticates the user.
     *
     * @param request The HTTP Servlet Request.
     * @param response The HTTP Servlet Response.
     * 
     * @return Returns <code>true</code> if the user was successfully 
     *         authenticated; <code>false</code> otherwise.
     *         
     * @throws IOException
     */
    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        _log.debug("trying to authenticate: " + (ServerProperties.getSingletonInstance().getPermission()).getClass().getCanonicalName());
        if (!ServerProperties.getSingletonInstance().getPermission().userPermitted(request, response)) {
            return false;
        }
        return true;
    }

    /**
	 * This method is called by both <code>doGet</code> and <code>doPost</code>.  This is 
	 * the method that actually processes the request to the servlet, allowing only one method
	 * to be overriden, instead of having to override two different methods.
	 * 
	 * @param request 
	 *        The <code>HttpServletRequest</code> object that contains the request 
	 *        the client made of the servlet.
	 *        
	 * @param response
	 *        The <code>HttpServletResponse</code> object that contains the response 
	 *        the servlet returns to the client.
	 *        
	 * @see #doGet(HttpServletRequest, HttpServletResponse)
	 * @see #doPost(HttpServletRequest, HttpServletResponse)
	 * 
	 * @throws ServletException if the request for the TRACE cannot be handled.
	 * @throws IOException if an input or output error occurs while the servlet 
	 *         is handling the TRACE request.
	 */
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
	 * Performed during a <code>GET</code> request.  This method has been overriden 
	 * to point to the {@link #processRequest(HttpServletRequest, HttpServletResponse)} 
	 * method.
	 * 
	 * @param request 
	 *        The <code>HttpServletRequest</code> object that contains the request 
	 *        the client made of the servlet.
	 *        
	 * @param response
	 *        The <code>HttpServletResponse</code> object that contains the response 
	 *        the servlet returns to the client.
	 * 
	 * @see #processRequest(HttpServletRequest, HttpServletResponse)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (authenticate(request, response)) {
            processRequest(request, response);
        }
    }

    /**
	 * Performed during a <code>POST</code> request.  This method has been overriden 
	 * to point to the {@link #processRequest(HttpServletRequest, HttpServletResponse)} 
	 * method.
	 * 
	 * @param request 
	 *        The <code>HttpServletRequest</code> object that contains the request 
	 *        the client made of the servlet.
	 *        
	 * @param response
	 *        The <code>HttpServletResponse</code> object that contains the response 
	 *        the servlet returns to the client.
	 * 
	 * @see #processRequest(HttpServletRequest, HttpServletResponse)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (authenticate(request, response)) {
            processRequest(request, response);
        }
    }

    /**
	 * Performed during a <code>PUT</code> request, similar to sending a file via
	 * <code>FTP</code>.  This method has been overriden to send error code <code>405 - 
	 * Method Not Allowed</code>.
	 * 
	 * @param request 
	 *        The <code>HttpServletRequest</code> object that contains the request 
	 *        the client made of the servlet.
	 *        
	 * @param response
	 *        The <code>HttpServletResponse</code> object that contains the response 
	 *        the servlet returns to the client.
	 * 
	 * @see #processRequest(HttpServletRequest, HttpServletResponse)
	 */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ErrorMessages.METHOD_NOT_ALLOWED_ERROR);
    }

    /**
	 * Performed during a <code>DELETE</code> request, which allows a client to
	 * delete a file from the server. This method has been overriden to send error 
	 * code <code>405 - Method Not Allowed</code>.
	 * 
	 * @param request 
	 *        The <code>HttpServletRequest</code> object that contains the request 
	 *        the client made of the servlet.
	 *        
	 * @param response
	 *        The <code>HttpServletResponse</code> object that contains the response 
	 *        the servlet returns to the client.
	 * 
	 * @see #processRequest(HttpServletRequest, HttpServletResponse)
	 */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ErrorMessages.METHOD_NOT_ALLOWED_ERROR);
    }
}
