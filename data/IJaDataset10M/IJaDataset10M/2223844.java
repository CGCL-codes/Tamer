package com.quickwcm.admin.ui.server.rpc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * GuiceRemoteServiceServlet is a wrapper servlet for GWT RPC utilizing Guice for
 * managing RemoteService implementation object's lifecycle.
 *
 * This servlet requires two(2) parameters:
 *    guice.module - class name of the guice module. Used for instantiation of
 *                   Guice injector.
 *    service.class - class name of the RemoteService implementation.
 *
 * @author pavel.jbanov
 */
@SuppressWarnings("serial")
public class GuiceRemoteServiceServlet extends RemoteServiceServlet {

    private static final String INJECTOR_ATTR_NAME = "___GuiceRemoteServiceServlet_GWT_injector";

    @SuppressWarnings("unchecked")
    @Override
    public void init() throws ServletException {
        super.init();
        String guiceModuleClassName = getInitParameter("guice.module");
        Class<Module> module;
        try {
            module = (Class<Module>) Class.forName(guiceModuleClassName);
            Injector injector = Guice.createInjector(module.newInstance());
            getServletContext().setAttribute(INJECTOR_ATTR_NAME, injector);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Injector getInjector() {
        return (Injector) getServletContext().getAttribute(INJECTOR_ATTR_NAME);
    }

    @SuppressWarnings("unchecked")
    private RemoteService getServiceInstance() {
        try {
            String serviceClassName = getInitParameter("service.class");
            Class<RemoteService> serviceClass = (Class<RemoteService>) Class.forName(serviceClassName);
            return getInjector().getInstance(serviceClass);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String processCall(String payload) throws SerializationException {
        RemoteService service = getServiceInstance();
        try {
            RPCRequest rpcRequest = RPC.decodeRequest(payload, service.getClass(), this);
            return RPC.invokeAndEncodeResponse(service, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
        } catch (IncompatibleRemoteServiceException ex) {
            getServletContext().log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
            return RPC.encodeResponseForFailure(null, ex);
        }
    }

    public HttpServletRequest getHttpServletRequest() {
        return super.getThreadLocalRequest();
    }

    public HttpServletResponse HttpServletResponse() {
        return super.getThreadLocalResponse();
    }
}
