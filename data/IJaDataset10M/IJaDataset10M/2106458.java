package com.google.code.jahath.service.socks;

import java.io.IOException;
import org.osgi.framework.BundleContext;
import com.google.code.jahath.Connection;
import com.google.code.jahath.common.osgi.NamedServiceProxy;
import com.google.code.jahath.tcp.Gateway;
import com.google.code.jahath.tcp.SocketAddress;

public class GatewayProxy extends NamedServiceProxy<Gateway> implements Gateway {

    public GatewayProxy(BundleContext bundleContext, String name) {
        super(bundleContext, Gateway.class, name);
    }

    public Connection connect(SocketAddress socketAddress) throws IOException {
        return getTarget().connect(socketAddress);
    }
}
