package org.soda.dpws.transport;

import org.soda.dpws.handler.HandlerSupport;
import org.soda.dpws.internal.DPWSContextImpl;
import org.soda.dpws.server.ServicePort;
import org.soda.dpws.service.Binding;

/**
 * Transport
 * 
 */
public interface Transport extends ChannelFactory, HandlerSupport {

    /**
   * @param uri
   * @return is uri supported
   */
    boolean isUriSupported(String uri);

    /**
   * @return supported binding
   */
    String[] getSupportedBindings();

    /**
   * 
   */
    void dispose();

    /**
   * @param context
   * @param service
   * @return the {@link Binding}
   */
    Binding findBinding(DPWSContextImpl context, ServicePort service);
}
