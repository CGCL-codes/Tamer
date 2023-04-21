package org.softmed.rest.server.core.restlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.softmed.rest.config.Application;
import org.softmed.rest.config.HttpMethod;
import org.softmed.rest.config.Module;
import org.softmed.rest.config.Resource;
import org.softmed.rest.server.core.handler.HandlerProcessParameters;
import org.softmed.rest.server.core.handler.HandlerProcessor;
import org.softmed.rest.server.core.http.HttpMethodProcessor;
import com.google.inject.Inject;

public class ResourceHandler {

    @Inject
    static HandlerProcessor handlerSolver;

    @Inject
    static HttpMethodProcessor mainHttpMethodProcessor;

    HttpMethodProcessor httpMethodProcessor;

    HandlerProcessor instanceHandlerSolver;

    private HashMap<String, Object> sequenceParameters;

    private Map<String, Object> uriParameters;

    private Form queryParameters;

    public ResourceHandler(Application application, Module module, Resource resource, Context context, Request request, Response response) {
        instanceHandlerSolver = handlerSolver.getClone();
        httpMethodProcessor = mainHttpMethodProcessor.clone();
        sequenceParameters = new HashMap<String, Object>();
        uriParameters = request.getAttributes();
        queryParameters = request.getResourceRef().getQueryAsForm();
        HandlerProcessParameters parameters = new HandlerProcessParameters(application, module, resource, null, null, context, request, response, sequenceParameters, uriParameters, queryParameters);
        if (resource.getHandler() != null) {
            try {
                instanceHandlerSolver.solve(resource.getHandler(), parameters);
            } catch (Throwable e) {
                e.printStackTrace();
                response.setStatus(Status.SERVER_ERROR_INTERNAL);
                return;
            }
            if (!instanceHandlerSolver.isContinueProcessing()) return;
        }
        HttpMethod requestHttpMethod = null;
        String methodName = request.getMethod().getName();
        List<HttpMethod> methods = resource.getHttpMethods();
        for (HttpMethod httpMethod : methods) {
            if (httpMethod.getName().equalsIgnoreCase(methodName)) {
                requestHttpMethod = httpMethod;
                break;
            }
        }
        if (requestHttpMethod == null) {
            if (methodName == "OPTIONS") {
                Set<Method> allowed = new HashSet<Method>();
                response.setStatus(Status.SUCCESS_OK);
                for (HttpMethod httpMethod : methods) {
                    if (httpMethod.getName() == "GET") allowed.add(Method.GET); else if (httpMethod.getName() == "POST") allowed.add(Method.POST); else if (httpMethod.getName() == "PUT") allowed.add(Method.POST); else if (httpMethod.getName() == "DELETE") allowed.add(Method.POST);
                }
                response.setAllowedMethods(allowed);
            } else response.setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
            return;
        }
        if (!requestHttpMethod.isActive()) {
            response.setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
            return;
        }
        try {
            httpMethodProcessor.process(requestHttpMethod, instanceHandlerSolver, parameters);
        } catch (Throwable e) {
            e.printStackTrace();
            response.setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }
        if (response.isEntityAvailable()) {
            System.out.println("COOL");
        } else System.out.println("NOT COOL");
    }
}
