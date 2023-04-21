package org.apache.axis.configuration;

import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.java.JavaSender;
import org.apache.axis.transport.local.LocalSender;

/**
 * A SimpleProvider set up with hardcoded basic configuration for a client
 * (i.e. http and local transports).
 *
 * @author Glen Daniels (gdaniels@apache.org)
 */
public class BasicClientConfig extends SimpleProvider {

    /**
     * Constructor - deploy client-side basic transports.
     */
    public BasicClientConfig() {
        deployTransport("java", new SimpleTargetedChain(new JavaSender()));
        deployTransport("local", new SimpleTargetedChain(new LocalSender()));
        deployTransport("http", new SimpleTargetedChain(new HTTPSender()));
    }
}
