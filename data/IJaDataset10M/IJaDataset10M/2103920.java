package jaxlib.ee.socketserver.jca;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.ObjectName;
import javax.resource.ResourceException;
import jaxlib.ee.socketserver.ServerSocketDrivenBean;
import jaxlib.ee.socketserver.spi.SocketServerChannelActivationSpec;
import jaxlib.jmx.Jmx;
import jaxlib.logging.Log;
import jaxlib.net.socket.ServerSocketConfiguration;
import jaxlib.net.socket.SocketConfiguration;
import jaxlib.net.socket.SocketConnectionHandler;
import jaxlib.net.socket.SocketServerChannel;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: ServerSocketEndpoint.java 3021 2011-12-07 05:00:34Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
final class ServerSocketEndpoint extends SocketServerChannel<SocketConnectionHandler> {

    static final Log log = SocketServerResourceAdapter.log;

    final String addrMBeanName;

    final String configMBeanName;

    final String mbeanName;

    private NetworkInterfaceMBeanImpl[] nics;

    ServerSocketEndpoint(final SocketServerDrivenContextImpl context, final SocketServerChannelActivationSpec activationSpec, final int port, final ServerSocketDrivenBean connectionHandler) throws ResourceException {
        super(context.getResourceAdapter().getSocketServer(), new InetSocketAddress(port), connectionHandler);
        this.addrMBeanName = context.getResourceAdapter().getSocketServer().mbeanName + ",bindaddress=" + ObjectName.quote(getAddress().getAddress().getHostAddress());
        this.mbeanName = this.addrMBeanName + ",port=TCP[" + getAddress().getPort() + "]";
        this.configMBeanName = this.mbeanName + ",serverportcfg=configuration";
        super.setBlocking(Boolean.TRUE.equals(activationSpec.getBlocking()));
        try {
            cfgSockets(activationSpec);
        } catch (final IOException ex) {
            throw new ResourceException(ex);
        }
        super.addSocketServerPortListener(connectionHandler);
    }

    private void cfgSockets(final SocketServerChannelActivationSpec spec) throws IOException {
        final ServerSocketConfiguration srv = new ServerSocketConfiguration();
        final SocketConfiguration cli = new SocketConfiguration();
        Integer i = spec.getBacklog();
        if (i != null) srv.setBacklog(i);
        i = spec.getReceiveBufferSize();
        if (i != null) srv.setReceiveBufferSize(i);
        i = spec.getSendBufferSize();
        if (i != null) cli.setSendBufferSize(i);
        i = spec.getSoIdleTimeout();
        if (i != null) super.setSoIdleTimeout(i);
        i = spec.getSoLinger();
        if (i != null) cli.setSoLinger(i);
        i = spec.getSoTimeout();
        if (i != null) cli.setSoTimeout(i);
        i = spec.getTrafficClass();
        if (i != null) cli.setTrafficClass(i);
        Boolean v = spec.getReuseAddress();
        if (v != null) srv.setReuseAddress(v);
        v = spec.getTcpNoDelay();
        if (v != null) cli.setTcpNoDelay(v);
        cli.setConfigurationReadOnly();
        srv.setConfigurationReadOnly();
        super.setClientSocketConfiguration(cli);
        super.setServerSocketConfiguration(srv);
    }

    private void postCloseEndpoint() {
        if (this.nics != null) {
            NetworkInterfaceMBeanImpl.unregister(this.nics);
            this.nics = null;
        }
        try {
            if (Jmx.removeMBean(this.configMBeanName)) log.info("unregistered MBean: %s", this.configMBeanName);
        } catch (final Exception ex) {
            log.warning("ignoring exception", ex);
        }
        try {
            if (Jmx.removeMBean(this.mbeanName)) log.info("unregistered MBean: %s", this.mbeanName);
        } catch (final Exception ex) {
            log.warning("ignoring exception", ex);
        }
        if (InetAddressMBeanImpl.unregisterShared(this.addrMBeanName)) log.info("unregistered MBean: %s", this.addrMBeanName);
    }

    private void postOpenEndpoint() {
        if (InetAddressMBeanImpl.registerShared(getAddress().getAddress(), this.addrMBeanName)) log.info("registered MBean: %s", this.addrMBeanName);
        try {
            Jmx.replaceMBean(new SocketServerChannelMBeanImpl(this), this.mbeanName);
            log.info("registered MBean: %s", this.mbeanName);
        } catch (final Exception ex) {
            log.warning("ignoring exception", ex);
        }
        try {
            Jmx.replaceMBean(new SocketServerChannelConfigMBeanImpl(this), this.configMBeanName);
            log.info("registered MBean: %s", this.configMBeanName);
        } catch (final Exception ex) {
            log.warning("ignoring exception", ex);
        }
        this.nics = NetworkInterfaceMBeanImpl.register(this.addrMBeanName, getAddress().getAddress());
    }

    @Override
    public final synchronized void close() throws IOException {
        synchronized (this) {
            if (isOpen()) {
                try {
                    super.close();
                } finally {
                    if (!isOpen()) {
                        AccessController.doPrivileged(new PrivilegedAction() {

                            @Override
                            public final Object run() {
                                postCloseEndpoint();
                                return null;
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public final synchronized void open() throws IOException {
        if (!isOpen()) {
            try {
                super.open();
            } finally {
                if (isOpen()) {
                    AccessController.doPrivileged(new PrivilegedAction() {

                        @Override
                        public final Object run() {
                            postOpenEndpoint();
                            return null;
                        }
                    });
                }
            }
        }
    }
}
