package com.teracode.prototipogwt.backend.extras.extensions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

/**
 * @author Maxi
 *
 */
@Provider
@ServerInterceptor
public class ContentTypeInterceptor implements PreProcessInterceptor {

    public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
        request.setAttribute("resteasy.provider.multipart.inputpart.defaultContentType", "*/*; charset=utf-8");
        return null;
    }
}
