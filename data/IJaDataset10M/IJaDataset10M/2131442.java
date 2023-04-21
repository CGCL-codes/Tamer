package net.sbbi.upnp.jmx;

import java.util.Set;

/**
 * Class to handle HTTP UPNP SUBSCRIBE and UNSUBSCRIBE requests on UPNPMBeanDevices
 * @author <a href="mailto:superbonbon@sbbi.net">SuperBonBon</a>
 * @version 1.0
 */
public class HttpSubscriptionRequest implements HttpRequestHandler {

    private static final HttpSubscriptionRequest instance = new HttpSubscriptionRequest();

    public static HttpRequestHandler getInstance() {
        return instance;
    }

    private HttpSubscriptionRequest() {
    }

    public String service(Set devices, HttpRequest request) {
        return null;
    }
}
