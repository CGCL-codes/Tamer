package org.smslib.routing;

import java.util.Collection;
import org.smslib.AGateway;
import org.smslib.OutboundMessage;

/**
 * Default Router implementation which actually doesn't perform any custom routing, instead relies on basic routing of ARouter.
 * 
 * @author Bassam Al-Sarori
 *
 */
public class DefaultRouter extends ARouter {

    @Override
    public Collection<AGateway> customRoute(OutboundMessage msg, Collection<AGateway> gateways) {
        return gateways;
    }
}
