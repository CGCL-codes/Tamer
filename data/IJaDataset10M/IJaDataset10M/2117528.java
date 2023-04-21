package org.atricore.idbus.capabilities.sso.main.sp.endpoints;

import org.apache.camel.Component;
import org.apache.camel.Producer;
import org.atricore.idbus.capabilities.sso.main.sp.producers.SPNameIDManagementProducer;
import org.atricore.idbus.kernel.main.mediation.camel.AbstractCamelEndpoint;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.CamelMediationExchange;
import java.util.Map;

public class SPNameIDManagementEndpoint extends AbstractCamelEndpoint<CamelMediationExchange> {

    public SPNameIDManagementEndpoint(String endpointURI, Component component, Map parameters) throws Exception {
        super(endpointURI, component, parameters);
    }

    public Producer createProducer() throws Exception {
        return new SPNameIDManagementProducer(this);
    }
}
