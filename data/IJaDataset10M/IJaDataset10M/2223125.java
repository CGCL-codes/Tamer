package org.ignition.blojsom.extension.xmlrpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcServer;
import org.ignition.blojsom.blog.Blog;
import org.ignition.blojsom.blog.BlojsomConfigurationException;
import org.ignition.blojsom.extension.xmlrpc.handlers.AbstractBlojsomAPIHandler;
import org.ignition.blojsom.fetcher.BlojsomFetcher;
import org.ignition.blojsom.fetcher.BlojsomFetcherException;
import org.ignition.blojsom.util.BlojsomConstants;
import org.ignition.blojsom.BlojsomException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Blojsom XML-RPC Servlet
 *
 * This servlet uses the Jakarta XML-RPC Library (http://ws.apache.org/xmlrpc)
 *
 * @author Mark Lussier
 * @version $Id: BlojsomXMLRPCServlet.java,v 1.18 2003-08-04 14:55:10 intabulas Exp $
 */
public class BlojsomXMLRPCServlet extends HttpServlet implements BlojsomConstants, BlojsomXMLRPCConstants {

    private Log _logger = LogFactory.getLog(BlojsomXMLRPCServlet.class);

    protected Blog _blog = null;

    private BlojsomFetcher _fetcher;

    XmlRpcServer _xmlrpc;

    /**
     * Construct a new Blojsom XML-RPC servlet instance
     */
    public BlojsomXMLRPCServlet() {
    }

    /**
     * Configure the XML-RPC API Handlers
     *
     * @param servletConfig Servlet configuration information
     */
    private void configureAPIHandlers(ServletConfig servletConfig) {
        String templateConfiguration = servletConfig.getInitParameter(BLOG_XMLRPC_CONFIGURATION_IP);
        Properties handlerMapProperties = new Properties();
        InputStream is = servletConfig.getServletContext().getResourceAsStream(templateConfiguration);
        try {
            handlerMapProperties.load(is);
            is.close();
            Iterator handlerIterator = handlerMapProperties.keySet().iterator();
            while (handlerIterator.hasNext()) {
                String handlerName = (String) handlerIterator.next();
                String handlerClassName = handlerMapProperties.getProperty(handlerName);
                Class handlerClass = Class.forName(handlerClassName);
                AbstractBlojsomAPIHandler handler = (AbstractBlojsomAPIHandler) handlerClass.newInstance();
                handler.setBlog(_blog);
                handler.setFetcher(_fetcher);
                _xmlrpc.addHandler(handler.getName(), handler);
                _logger.debug("Added [" + handler.getName() + "] API Handler : " + handlerClass);
            }
        } catch (InstantiationException e) {
            _logger.error(e);
        } catch (IllegalAccessException e) {
            _logger.error(e);
        } catch (ClassNotFoundException e) {
            _logger.error(e);
        } catch (IOException e) {
            _logger.error(e);
        } catch (BlojsomException e) {
            _logger.error(e);
        }
    }

    /**
     * Configure the authorization table blog (user id's and and passwords)
     *
     * @param servletConfig Servlet configuration information
     */
    private void configureAuthorization(ServletConfig servletConfig) {
        Map _authorization = new HashMap();
        String authConfiguration = servletConfig.getInitParameter(BLOG_AUTHORIZATION_IP);
        Properties authProperties = new Properties();
        InputStream is = servletConfig.getServletContext().getResourceAsStream(authConfiguration);
        try {
            authProperties.load(is);
            is.close();
            Iterator authIterator = authProperties.keySet().iterator();
            while (authIterator.hasNext()) {
                String userid = (String) authIterator.next();
                String password = authProperties.getProperty(userid);
                _authorization.put(userid, password);
            }
            if (!_blog.setAuthorization(_authorization)) {
                _logger.error("Authorization table could not be assigned");
            }
        } catch (IOException e) {
            _logger.error(e);
        }
    }

    /**
     * Load blojsom configuration information
     *
     * @param servletConfig Servlet configuration information
     * @param filename blojsom configuration file to be loaded
     */
    private void processBlojsomConfiguration(ServletConfig servletConfig, String filename) {
        Properties _configuration = new Properties();
        InputStream _cis = servletConfig.getServletContext().getResourceAsStream(filename);
        try {
            _configuration.load(_cis);
            _cis.close();
            _blog = new Blog(_configuration);
        } catch (IOException e) {
            _logger.error(e);
        } catch (BlojsomConfigurationException e) {
            _logger.error(e);
        }
    }

    /**
     * Configure the {@link BlojsomFetcher} that will be used to fetch categories and
     * entries
     *
     * @param servletConfig Servlet configuration information
     * @throws ServletException If the {@link BlojsomFetcher} class could not be loaded and/or initialized
     */
    private void configureFetcher(ServletConfig servletConfig) throws ServletException {
        String fetcherClassName = _blog.getBlogFetcher();
        if ((fetcherClassName == null) || "".equals(fetcherClassName)) {
            fetcherClassName = BLOG_DEFAULT_FETCHER;
        }
        try {
            Class fetcherClass = Class.forName(fetcherClassName);
            _fetcher = (BlojsomFetcher) fetcherClass.newInstance();
            _fetcher.init(servletConfig, _blog);
            _logger.info("Added blojsom fetcher: " + fetcherClassName);
        } catch (ClassNotFoundException e) {
            _logger.error(e);
            throw new ServletException(e);
        } catch (InstantiationException e) {
            _logger.error(e);
            throw new ServletException(e);
        } catch (IllegalAccessException e) {
            _logger.error(e);
            throw new ServletException(e);
        } catch (BlojsomFetcherException e) {
            _logger.error(e);
            throw new ServletException(e);
        }
    }

    /**
     * Initialize the blojsom XML-RPC servlet
     *
     * @param servletConfig Servlet configuration information
     * @throws ServletException If there is an error initializing the servlet
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        String _cfgfile = servletConfig.getInitParameter(BLOG_CONFIGURATION_IP);
        if (_cfgfile == null || _cfgfile.equals("")) {
            _logger.info("blojsom configuration not specified, using " + DEFAULT_BLOJSOM_CONFIGURATION);
            _cfgfile = DEFAULT_BLOJSOM_CONFIGURATION;
        }
        _xmlrpc = new XmlRpcServer();
        XmlRpc.setEncoding(UTF8);
        processBlojsomConfiguration(servletConfig, _cfgfile);
        configureAuthorization(servletConfig);
        configureFetcher(servletConfig);
        configureAPIHandlers(servletConfig);
        _logger.info("blojsom home is [" + _blog.getBlogHome() + ']');
    }

    /**
     * Service an XML-RPC request by passing the request to the proper handler
     *
     * @param httpServletRequest Request
     * @param httpServletResponse Response
     * @throws ServletException If there is an error processing the request
     * @throws IOException If there is an error during I/O
     */
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            httpServletRequest.setCharacterEncoding(UTF8);
        } catch (UnsupportedEncodingException e) {
            _logger.error(e);
        }
        byte[] result = _xmlrpc.execute(httpServletRequest.getInputStream());
        String content = new String(result, UTF8);
        httpServletResponse.setContentType("text/xml;chartset=UTF-8");
        httpServletResponse.setContentLength(content.length());
        OutputStreamWriter osw = new OutputStreamWriter(httpServletResponse.getOutputStream(), UTF8);
        osw.write(content);
        osw.flush();
    }

    /**
     * Called when removing the servlet from the servlet container
     */
    public void destroy() {
        try {
            _fetcher.destroy();
        } catch (BlojsomFetcherException e) {
            _logger.error(e);
        }
    }
}
