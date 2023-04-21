package org.atricore.idbus.capabilities.josso.main;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atricore.idbus.capabilities.josso.main.endpoints.*;
import org.atricore.idbus.kernel.main.mediation.camel.AbstractCamelEndpoint;
import java.util.Map;

/**
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public class JossoComponent extends DefaultComponent {

    private static final Log logger = LogFactory.getLog(JossoComponent.class);

    public JossoComponent() {
    }

    public JossoComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        logger.debug("Creating Camel Endpoint for [" + uri + "] [" + remaining + "]");
        AbstractCamelEndpoint endpoint;
        JossoService e = getJosso11Service(remaining);
        switch(e) {
            case SingleSignOnService:
                endpoint = new SingleSignOnEndpoint(uri, this, parameters);
                break;
            case SingleLogoutService:
                endpoint = new SingleLogoutEndpoint(uri, this, parameters);
                break;
            case AssertionConsumerService:
                endpoint = new AssertionConsumerEndpoint(uri, this, parameters);
                break;
            case IdentityManager:
                endpoint = new IdentityManagerEndpoint(uri, this, parameters);
                break;
            case IdentityProvider:
                endpoint = new IdentityProviderEndpoint(uri, this, parameters);
                break;
            case SessionManager:
                endpoint = new SessionManagerEndpoint(uri, this, parameters);
                break;
            default:
                throw new IllegalArgumentException("Unsupported JOSSO 1.x service type " + remaining);
        }
        endpoint.setAction(remaining);
        setProperties(endpoint, parameters);
        return endpoint;
    }

    protected JossoService getJosso11Service(String remaining) {
        for (JossoService et : JossoService.values()) {
            if (et.getQname().getLocalPart().equals(remaining)) return et;
        }
        throw new IllegalArgumentException("Invalid JOSSO 1.x service type " + remaining);
    }
}
