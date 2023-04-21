package org.granite.gravity.generic;

import org.granite.gravity.AbstractChannelFactory;
import org.granite.gravity.Channel;

/**
 * @author William DRAI
 */
public class GenericChannelFactory extends AbstractChannelFactory {

    public Channel newChannel(String id) {
        return new GenericChannel(getServletConfig(), getGravityConfig(), id);
    }
}
