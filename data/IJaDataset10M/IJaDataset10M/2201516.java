package org.smslib.v3;

import java.util.List;

/**
 * RoundRobinLoadBalancer is the default SMSLib load balancer. In case you have
 * defined many gateways, this balancer forwards each message via each gateway
 * in turns.
 */
public final class RoundRobinLoadBalancer extends LoadBalancer {

    private int currentGateway;

    public RoundRobinLoadBalancer(Service service) {
        super(service);
        currentGateway = 0;
    }

    /**
	 * This Load Balancing implementation returns every other available gateway
	 * on each invocation.
	 */
    @SuppressWarnings("unchecked")
    public AGateway balance(OutboundMessage msg, List candidates) {
        if (currentGateway >= candidates.size()) currentGateway = 0;
        return ((AGateway) candidates.get(currentGateway++));
    }
}
