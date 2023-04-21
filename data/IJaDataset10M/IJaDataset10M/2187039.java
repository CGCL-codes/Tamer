package it.sebsibenal.server.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GwtRpcController extends RemoteServiceServlet implements Controller, ServletContextAware {

    private static final Logger LOG = Logger.getLogger(GwtRpcController.class);

    private ServletContext servletContext;

    private RemoteService remoteService;

    private Class remoteServiceClass;

    /**
         * @return the remoteService
         */
    public RemoteService getRemoteService() {
        return remoteService;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (remoteService instanceof RequestAware) {
            ((RequestAware) remoteService).setRequest(request);
        }
        super.doPost(request, response);
        return null;
    }

    public String processCall(String payload) throws SerializationException {
        try {
            RPCRequest rpcRequest = RPC.decodeRequest(payload, this.remoteServiceClass);
            return RPC.invokeAndEncodeResponse(this.remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
        } catch (IncompatibleRemoteServiceException irse) {
            LOG.error(irse.getMessage(), irse);
            return RPC.encodeResponseForFailure(null, irse);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return RPC.encodeResponseForFailure(null, e);
        }
    }

    public void setRemoteService(RemoteService remoteService) {
        this.remoteService = remoteService;
        this.remoteServiceClass = this.remoteService.getClass();
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
