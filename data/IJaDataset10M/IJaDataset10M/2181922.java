package org.apache.axis2.jaxws.feature;

import org.apache.axis2.jaxws.core.MessageContext;
import org.apache.axis2.jaxws.spi.Binding;
import org.apache.axis2.jaxws.spi.BindingProvider;

/**
 * 
 */
public interface ClientConfigurator {

    /**
     * Perform client-side configuration for a <code>WebServiceFeature</code>.
     * 
     * @param messageContext
     * @param provider
     */
    public void configure(MessageContext messageContext, BindingProvider provider);

    /**
     * Indicates whether the configurator supports the specified binding.
     * 
     * @param binding the binding to test
     * @return <code>true</code> if the configurator supports the binding, <code>false</code>
     * otherwise.
     */
    public boolean supports(Binding binding);
}
