package spooky.control.web;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import spooky.config.*;
import spooky.control.event.*;
import spooky.security.*;
import spooky.security.auth.*;
import spooky.security.auth.login.*;
import spooky.model.web.*;
import spooky.view.web.*;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;

/** 
 * <p>
 * The controller class is a Servlet that is the entry point for request
 * to the framework. The Controller servlet activates the request handler
 * and event handlers to process the request that was sent to the controller.
 * </p>
 *
 * <p>
 * The controller determines the request's target from the URL path. For
 * example if the url is [host]/framework/control/MyAction, the controller
 * will look inside the configurations for MyAction event and map try to
 * map the request to this event. For more information how this mapping is
 * done look at the RequestProcessor class.
 * </p>
 *
 * @author Erik Karlsson
 * @version 0.4
 * @history 22.7.2001 (7/22/2001) Code was commented
 *
 */
public class Controller extends HttpServlet {

    private static String CONFIGURATION_FILE = "/WEB-INF/xml/config.xml";

    /** <p>
     * Function is called at the start up of the Servlet. Note that the function
     * is called only once when the servlet is started at the first time. The function
     * will load the configuration file for the framework. This configuration is
     * saved to objects for latter access.
     * </p>
     *
     * @param config The servlet Configurations
     * @throws ServletException An exception is throw for example if the 
     *          configuration files where not
     *          available or the loading was unsuccesfully.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.err.println("Controller::init");
        ServletContext servletContext = getServletContext();
        if (servletContext.getAttribute(Configurations.CONTEXT_NAME) == null) {
            try {
                System.err.println("Controller::init::getConfigurationFile");
                String configFile = getConfigurationFile();
                System.err.println("Controller::init::getResource");
                String configurationFile = servletContext.getResource(configFile).toString();
                System.err.println("Controller::init::New Url");
                URL configFileUrl = new URL(configurationFile);
                System.err.println("Controller::init::New Configurations");
                Configurations theConfigurations = new Configurations(configFileUrl.openStream());
                servletContext.setAttribute(Configurations.CONTEXT_NAME, theConfigurations);
            } catch (Exception e) {
                throw new ServletException("Init:: " + e.toString());
            }
        }
    }

    public ModelManager getModelManager(ServletContext servletContext, HttpSession httpSession) {
        ModelManager modelManager = (ModelManager) httpSession.getAttribute(ModelManager.CONTEXT_NAME);
        if (modelManager == null) {
            modelManager = new ModelManager(servletContext, httpSession);
            httpSession.setAttribute(ModelManager.CONTEXT_NAME, modelManager);
        }
        return modelManager;
    }

    /**
     *  <p>
     *  Method returns the configuration file's path. 
     *  </p>
     *  @return the configuration file's path
     */
    public String getConfigurationFile() {
        return Controller.CONFIGURATION_FILE;
    }

    /** 
     * Destroys the servlet.
     */
    public void destroy() {
    }

    /**
     * Returns the framework's configurations as an Configuration object stored 
     * to the servlet context
     *
     * @return Configurations object
     */
    public Configurations getConfigurations() {
        ServletContext servletContext = getServletContext();
        Configurations configurations = (Configurations) servletContext.getAttribute(Configurations.CONTEXT_NAME);
        return configurations;
    }

    /**
     * Function creates a new Request Processor.
     *
     * @param theSession current HTTP session (for the user)
     * @param servletContext current application context
     * @return a Request Processor from the context, or new one
     */
    public RequestProcessor getRequestProcessor(HttpSession theSession) {
        RequestProcessor requestProcessor = (RequestProcessor) theSession.getAttribute(RequestProcessor.CONTEXT_NAME);
        if (requestProcessor == null) {
            ServletContext servletContext = getServletContext();
            requestProcessor = new RequestProcessor(theSession, servletContext);
            theSession.setAttribute(RequestProcessor.CONTEXT_NAME, requestProcessor);
        }
        return requestProcessor;
    }

    /**
     * Method returns the current view manager for the application
     *
     * @return a view manager
     */
    public ViewManager getViewManager(HttpSession theSession) {
        ViewManager viewManager = (ViewManager) theSession.getAttribute(ViewManager.CONTEXT_NAME);
        if (viewManager == null) {
            Configurations theConfig = (Configurations) getServletContext().getAttribute(Configurations.CONTEXT_NAME);
            viewManager = new ViewManager(theConfig.getViewMappings());
            theSession.setAttribute(ViewManager.CONTEXT_NAME, viewManager);
        }
        return viewManager;
    }

    public FrameworkContext getFrameworkContext(HttpServletRequest request, HttpServletResponse response, HttpSession theSession) throws LoginException {
        System.err.println("Controller::getFrameworkContext");
        FrameworkContext frameworkContext = null;
        Cookie[] theCookies = request.getCookies();
        Cookie authenticationSessionCookie = null;
        if (theCookies != null) {
            for (int a = 0; a < theCookies.length; a++) {
                if (theCookies[a].getName().equals("user_session")) {
                    System.err.println("Controller::getFrameworkContext::cookie found");
                    authenticationSessionCookie = theCookies[a];
                    a = theCookies.length;
                }
            }
        }
        if (authenticationSessionCookie == null) {
            System.err.println("Controller::getFrameworkContext::cookie not found");
            FrameworkLoginContext login = new FrameworkLoginContext("Framework", new FrameworkCallbackHandler(request));
            System.err.println("Controller::getFrameworkContext::login.login()");
            login.login();
            frameworkContext = new FrameworkContext(login);
            System.err.println("Controller::getFrameworkContext::theSession.setAttribute()");
            theSession.setAttribute(FrameworkContext.CONTEXT_NAME, frameworkContext);
            authenticationSessionCookie = new Cookie("user_session", "****");
            response.addCookie(authenticationSessionCookie);
        } else {
            frameworkContext = (FrameworkContext) theSession.getAttribute(FrameworkContext.CONTEXT_NAME);
        }
        return frameworkContext;
    }

    /** 
     *  Method is the entry point for the request that is sent to the Conroller
     *  servlet. Both the GET and POST request are processed by this method. The
     *  method uses the RequestProcessor and ViewManager to perform the requested
     *  operation. Note that these components process the request in more details,
     *  this function only turns the request to data that is understandable for 
     *  the other components.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        try {
            System.err.println("Controller::processRequest");
            String nextView = "EMPTY";
            HttpSession theSession = request.getSession();
            ServletContext theContext = getServletContext();
            String requestedURL = request.getPathInfo();
            ModelManager theModelManager = getModelManager(theContext, theSession);
            ViewManager viewManager = getViewManager(theSession);
            Configurations configurations = getConfigurations();
            HashMap eventMappings = configurations.getEventMappings();
            System.err.println("Controller::processRequest::containsKey::" + requestedURL);
            if (eventMappings.containsKey(requestedURL)) {
                EventProcessorConfig eventProcessorConfig = (EventProcessorConfig) eventMappings.get(requestedURL);
                if (eventProcessorConfig.requiresAuthentication()) {
                    System.err.println("Controller::processRequest::requires authentication");
                    try {
                        RequestProcessor theProcessor = getRequestProcessor(theSession);
                        FrameworkContext frameworkContext = getFrameworkContext(request, response, theSession);
                        nextView = theProcessor.processRequest(request, frameworkContext);
                    } catch (LoginException le) {
                        System.err.println("Controller::processRequest::requires authentication::error::" + le.toString());
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setErrorMessage(le.toString());
                        nextView = "ERROR401";
                    }
                } else {
                    System.err.println("Controller::processRequest::doesn't require authentication");
                    try {
                        System.err.println("Controller::processRequest::getRequestProcessor");
                        RequestProcessor theProcessor = getRequestProcessor(theSession);
                        System.err.println("Controller::processRequest::processRequest");
                        nextView = theProcessor.processRequest(request, null);
                    } catch (RequestException re) {
                        throw new ServletException(re.toString());
                    } catch (Exception e) {
                        throw new ServletException(e.toString());
                    }
                }
            } else {
                ErrorBean errorBean = new ErrorBean();
                errorBean.setErrorMessage("requested page: " + requestedURL + " couldn't be found");
                request.setAttribute(ErrorBean.CONTEXT_NAME, errorBean);
                nextView = "ERROR_404";
            }
            ServletConfig servletConfig = getServletConfig();
            ServletContext configContext = servletConfig.getServletContext();
            RequestDispatcher dispatcher = configContext.getRequestDispatcher(viewManager.getTemplate(nextView));
            dispatcher.forward(request, response);
        } catch (Exception e) {
            System.err.println("Controller::error::" + e.toString());
            PrintWriter out = response.getWriter();
            out.println(e.toString());
        }
    }

    /** Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
    */
    public String getServletInfo() {
        return "Short description";
    }
}
