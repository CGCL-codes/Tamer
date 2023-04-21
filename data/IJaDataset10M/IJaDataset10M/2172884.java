package org.atricore.idbus.kernel.main.mediation.camel;

import org.atricore.idbus.kernel.main.mediation.Channel;
import org.atricore.idbus.kernel.main.mediation.endpoint.IdentityMediationEndpoint;

/**
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public class AbstractMediationService {

    protected Channel channel;

    protected IdentityMediationEndpoint endpoint;

    public void init(Channel channel, IdentityMediationEndpoint endpoint) {
        this.channel = channel;
        this.endpoint = endpoint;
    }
}
