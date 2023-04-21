package com.volantis.map.operation.impl;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.Operation;
import com.volantis.map.operation.OperationEngine;
import com.volantis.map.operation.OperationNotFoundException;
import com.volantis.map.operation.ResourceDescriptorNotFoundException;
import com.volantis.map.operation.Result;
import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStore;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreException;
import com.volantis.synergetics.log.LogDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

/**
 * The default operation engine. This engine asks each of its plugins to
 * perform the operation. If a plugin can perform the operation it returns
 * true, if it cannot perform the operation it returns false. They throw
 * exceptions to indicate that a a fatal error occurred.
 *
 * Operations should not read from the servlet request or write to the servlet
 * response unless they can perform the operation.
 */
public class DefaultOperationEngine implements OperationEngine {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(DefaultOperationEngine.class);

    /**
     * The component context
     */
    private ComponentContext context;

    /**
     * The resource descriptor store
     */
    private ResourceDescriptorStore store;

    /**
     * Process the request
     *
     * @param externalID the external ID
     * @param request the servlet request
     * @param response the servlet response
     * @throws ResourceDescriptorNotFoundException
     * @throws OperationNotFoundException
     */
    public void processRequest(String externalID, HttpServletRequest request, HttpServletResponse response) throws Exception {
        com.volantis.map.operation.ResourceDescriptor localDescriptor = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Obtaining descriptor for id " + externalID);
            }
            ResourceDescriptor descriptor = store.getDescriptor(externalID);
            localDescriptor = new DelegatingResourceDescriptor(descriptor);
        } catch (ResourceDescriptorStoreException e) {
            throw new ResourceDescriptorNotFoundException(e);
        }
        try {
            ServiceReference[] references = context.getBundleContext().getServiceReferences(Operation.class.getName(), null);
            if (null == references || references.length < 1) {
                throw new OperationNotFoundException("operation-type-not-found", localDescriptor.getResourceType());
            } else {
                Result result = Result.UNSUPPORTED;
                for (int i = 0; i < references.length && result == Result.UNSUPPORTED; i++) {
                    ServiceReference ref = references[i];
                    Object service = context.getBundleContext().getService(ref);
                    if (null == service) {
                        Object[] params = new Object[] { ref.getProperty("service.pid"), ref.getBundle().getSymbolicName() };
                        LOGGER.error("service-has-been-unregistered", params);
                    } else {
                        Operation operation = (Operation) service;
                        result = operation.execute(localDescriptor, request, response);
                    }
                }
                if (result == Result.UNSUPPORTED) {
                    throw new OperationNotFoundException("no-plugin-available", new String[] { localDescriptor.getExternalID(), localDescriptor.getResourceType() });
                }
            }
        } catch (InvalidSyntaxException e) {
            throw new OperationNotFoundException("syntax-error-in-filter", null, e);
        }
    }

    /**
     * Set the descriptor store
     * @param store
     */
    protected void setStore(ResourceDescriptorStore store) {
        this.store = store;
    }

    /**
     * Unset the store
     *
     * @param store
     */
    protected void unsetStore(ResourceDescriptorStore store) {
        this.store.shutdown();
        this.store = null;
    }

    /**
     * Activate this component
     *
     * @param context the component context
     */
    protected void activate(ComponentContext context) {
        this.context = context;
    }

    /**
     * Deactivate this component
     *
     * @param context the Component context
     */
    protected void deactivate(ComponentContext context) {
        this.context = null;
    }
}
