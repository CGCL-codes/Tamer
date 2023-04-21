package org.apache.axis2.jaxws.server;

import org.apache.axis2.jaxws.core.MessageContext;

/**
 * Code wishing to create web service implementation instances for JAX-WS
 * requests must implement this interface and register it with the the
 * JAX-WS FactoryRegistry. The instance of this factory will be looked up
 * and utilized by the JAX-WS runtime in order to create or otherwise
 * obtain a web service implementation instance for any request.
 *
 */
public interface ServiceInstanceFactory {

    /**
         * This method will create, or otherwise obtain a reference to an 
         * instance of a web service implementation class. The expectation
         * is that upon the completion of this method there will be a web
         * service implementation instance cretaed and all necessary resource
         * injection will have taken place.
         */
    public Object createServiceInstance(MessageContext request, Class serviceClass) throws Exception;
}
