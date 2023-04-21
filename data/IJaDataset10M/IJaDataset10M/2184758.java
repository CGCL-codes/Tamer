package com.trilead.ssh2;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.trilead.ssh2.channel.ChannelManager;
import com.trilead.ssh2.channel.DynamicAcceptThread;

/**
 * A <code>DynamicPortForwarder</code> forwards TCP/IP connections to a local
 * port via the secure tunnel to another host which is selected via the
 * SOCKS protocol. Checkout {@link Connection#createDynamicPortForwarder(int)}
 * on how to create one.
 * 
 * @author Kenny Root
 * @version $Id: $
 */
public class DynamicPortForwarder {

    ChannelManager cm;

    DynamicAcceptThread dat;

    DynamicPortForwarder(ChannelManager cm, int local_port) throws IOException {
        this.cm = cm;
        dat = new DynamicAcceptThread(cm, local_port);
        dat.setDaemon(true);
        dat.start();
    }

    DynamicPortForwarder(ChannelManager cm, InetSocketAddress addr) throws IOException {
        this.cm = cm;
        dat = new DynamicAcceptThread(cm, addr);
        dat.setDaemon(true);
        dat.start();
    }

    /**
	 * Stop TCP/IP forwarding of newly arriving connections.
	 * 
	 * @throws IOException
	 */
    public void close() throws IOException {
        dat.stopWorking();
    }
}
