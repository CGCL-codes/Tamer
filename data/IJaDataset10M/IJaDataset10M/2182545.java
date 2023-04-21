package sc.fgrid.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;
import sc.fgrid.common.Constants;
import sc.fgrid.common.Util;
import sc.fgrid.engine.ClientException;
import sc.fgrid.engine.Engine;
import sc.fgrid.engine.EngineException;
import sc.fgrid.engine.InternalException;
import sc.fgrid.stubs.ServiceException;
import sc.fgrid.types.ErrorEnum;
import sc.fgrid.types.ServiceFault;

public class BaseService {

    private static Logger log = Logger.getLogger(ClientService.class);

    @javax.annotation.Resource
    WebServiceContext wsContext;

    /**
     * Used only in case this is not a web service. In case this is a web
     * service, the engine shall be taken from the ServletContext.
     * 
     * @see
     * @{link {@link #getEngine()},
     * @{link {@link #setEngine()}
     */
    Engine engine_local = null;

    /**
     * Used only in case this is not a web service. In case this is a web
     * service, the engine shall be taken from the ServletContext.
     * 
     * @see
     * @{link {@link #getEngine()}
     */
    Engine auth_local = null;

    @PostConstruct
    public void init() {
        String classname = this.getClass().getSimpleName();
        log.info("======== " + classname + " Web Service start up. =========");
    }

    @PreDestroy
    public void shutDown() {
        String classname = this.getClass().getSimpleName();
        log.info("======== " + classname + " Web Service shut down. =========");
    }

    /**
     * get Engine from servlet context or what was set with
     * 
     * @{link #setEngine(Engine)} .
     */
    Engine getEngine() {
        if (wsContext == null && engine_local == null) {
            throw new RuntimeException("If not called as WebService, the engine needs to be set first.");
        }
        if (engine_local != null) {
            return engine_local;
        }
        MessageContext mc = wsContext.getMessageContext();
        if (mc == null) throw new RuntimeException("MessageContext==null!");
        ServletContext servletContext = (ServletContext) mc.get(MessageContext.SERVLET_CONTEXT);
        if (servletContext == null) throw new RuntimeException("servletContext==null, call this only from a running request!");
        Engine engine = (Engine) servletContext.getAttribute(Constants.servletContext_Engine);
        if (engine == null) {
            throw new RuntimeException("A listener (registered in web.xml) should have set the " + "engine to the Servlet Context with 'engine'=Engine");
        }
        return engine;
    }

    /**
     * Get the Auth object out of the Servlet Context. Call this only in the
     * context of a request, because the injected Servlet Context is needed.
     */
    Auth getAuth() {
        if (wsContext == null) {
            throw new RuntimeException("Call this only in the context of a request, because the injected Servlet Context is needed.");
        }
        MessageContext mc = wsContext.getMessageContext();
        ServletContext servletContext = (ServletContext) mc.get(MessageContext.SERVLET_CONTEXT);
        Auth auth = (Auth) servletContext.getAttribute(Constants.servletContext_Auth);
        if (auth == null) {
            throw new RuntimeException("A listener (registered in web.xml) should have set the " + "engine to the Servlet Context with 'auth'=Auth");
        }
        return auth;
    }

    protected void setEngine(Engine engine) {
        engine_local = engine;
    }

    /**
     * Access to ServletContext: no standard way:
     * http://www.nabble.com/Access-ServletContext-from-JAX-WS-web-service-td14950831.html
     */
    @Deprecated
    void contexte() {
        log.debug("--- <contexte> ----");
        EndpointReference ep = wsContext.getEndpointReference();
        log.debug("ep=" + ep.toString());
        MessageContext mc = wsContext.getMessageContext();
        if (mc == null) log.fatal("mc==null!");
        ServletContext servletContext = (ServletContext) mc.get(MessageContext.SERVLET_CONTEXT);
        if (servletContext == null) log.fatal("servletContext==null");
        HttpServletRequest httpServletRequest = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        if (httpServletRequest == null) log.fatal("httpServletRequest==null");
        HttpServletResponse httpServletResponse = (HttpServletResponse) mc.get(MessageContext.SERVLET_RESPONSE);
        if (httpServletResponse == null) log.fatal("httpServletResponse==null");
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession == null) log.fatal("httpSession==null");
        log.debug("--- </contexte> ----");
    }

    /**
     * Utility function which converts an {@link EngineException} to a
     * {@link ServiceException}.
     * 
     * @param se
     *            is used to fill the stack trace of the thrown exception
     * @throws ServiceException
     *             is always thrown
     */
    void dealExceptions(String doing_what, EngineException se) throws ServiceException {
        Engine engine = getEngine();
        boolean stacktrace = engine.getConfig().isStackTrace();
        BaseService.dealExceptions(doing_what, se, stacktrace, log);
    }

    /**
     * Utility function which converts a RuntimeException to a ServiceException.
     * 
     * @param ex
     *            is used to fill the stack trace of the thrown exception
     * @throws ServiceException
     *             is always thrown
     */
    void dealExceptions(String doing_what, RuntimeException ex) throws ServiceException {
        String message = "Software problem during " + doing_what + ":\n" + ex.getMessage();
        log.fatal(message, ex);
        ServiceFault fault = new ServiceFault();
        fault.setFaultcode(ErrorEnum.BUG);
        throw new ServiceException(message, fault, ex);
    }

    /**
     * Utility function which converts an {@link EngineException} to a
     * {@link ServiceException}
     * 
     * @param ee
     *            is used to fill the stack trace of the thrown exception
     * @throws ServiceException
     *             is always thrown
     */
    public static void dealExceptions(String doing_what, EngineException ee, boolean stacktrace, Logger log) throws ServiceException {
        try {
            throw ee;
        } catch (InternalException ex) {
            String message = "Failed caused by problem on the " + "Server during " + doing_what;
            if (stacktrace) log.error(message, ex); else log.error(message + ":\n" + Util.getMessageChain(ex));
            ServiceFault fault = new ServiceFault();
            fault.setFaultcode(ErrorEnum.SERVER_ERROR);
            throw new ServiceException(message + "\n" + Util.getMessageChain(ex), fault, ex);
        } catch (ClientException ex) {
            long instanceID = ex.getInstanceID();
            String message = "Failed caused by problem from the calling client for " + "instance with ID=" + instanceID + " during " + doing_what;
            if (stacktrace) log.error(message, ex); else log.error(message + ":\n" + Util.getMessageChain(ex));
            ServiceFault fault = new ServiceFault();
            fault.setFaultcode(ErrorEnum.CLIENT_ERROR);
            fault.setInstanceID(instanceID);
            ServiceException se2 = new ServiceException(message + "\n" + Util.getMessageChain(ex), fault, ex);
            throw se2;
        } catch (EngineException ex) {
            String message = "Failed caused by problem of unspecific source " + "during " + doing_what;
            if (stacktrace) log.error(message, ex); else log.error(message + ":\n" + Util.getMessageChain(ex));
            ServiceFault fault = new ServiceFault();
            fault.setFaultcode(ErrorEnum.ERROR);
            throw new ServiceException(message + "\n" + Util.getMessageChain(ex), fault, ex);
        }
    }
}
