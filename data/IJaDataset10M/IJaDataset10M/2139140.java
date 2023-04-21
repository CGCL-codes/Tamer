package com.volantis.mcs.service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Helper method to store at retrived {@link ServiceDefinition} instances
 * from a request
 */
public class ServiceDefinitionHelper {

    /**
     * To be used as a key when storing/retrieving implematations of this
     * from data structures
     */
    static final String SERVICE_DEFINITION_KEY = ServiceDefinition.class.getName();

    /**
     * Helper method to retrieve a ServiceDefinition from a
     * {@link HttpServletRequest} instance.
     * @param request the request
     * @return a {@link ServiceDefinition} instance.
     */
    public static ServiceDefinition retrieveService(ServletRequest request) {
        return (ServiceDefinition) request.getAttribute(SERVICE_DEFINITION_KEY);
    }

    /**
     * Helper method to store a {@link ServiceDefinition} away in a
     * {@link ServletRequest} instance.
     * @param request the request
     * @param service a ServiceDefintion
     */
    public static void storeService(ServletRequest request, ServiceDefinition service) {
        request.setAttribute(SERVICE_DEFINITION_KEY, service);
    }
}
