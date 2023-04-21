package com.hyk.proxy.plugin.spac.event.forward;

import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import com.hyk.proxy.framework.common.Misc;
import com.hyk.proxy.framework.event.HttpProxyEventService;
import com.hyk.proxy.framework.event.HttpProxyEventServiceFactory;

/**
 *
 */
public class ForwardProxyEventServiceFactory implements HttpProxyEventServiceFactory {

    public static final String NAME = "FORWARD";

    private static ForwardProxyEventServiceFactory instance = new ForwardProxyEventServiceFactory();

    public static ForwardProxyEventServiceFactory getInstance() {
        return instance;
    }

    protected ClientSocketChannelFactory factory;

    protected ForwardProxyEventServiceFactory() {
    }

    @Override
    public HttpProxyEventService createHttpProxyEventService() {
        return new ForwardProxyEventService(factory);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void init() throws Exception {
        factory = new OioClientSocketChannelFactory(Misc.getGlobalThreadPool());
    }

    @Override
    public void destroy() throws Exception {
    }
}
