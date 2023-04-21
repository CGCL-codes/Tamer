package com.aelitis.azureus.core.networkmanager;

import java.net.InetSocketAddress;
import org.gudy.azureus2.core3.config.COConfigurationManager;
import com.aelitis.azureus.core.networkmanager.impl.tcp.VirtualBlockingServerChannelSelector;
import com.aelitis.azureus.core.networkmanager.impl.tcp.VirtualNonBlockingServerChannelSelector;

public class VirtualServerChannelSelectorFactory {

    public static VirtualServerChannelSelector createBlocking(InetSocketAddress bind_address, int so_rcvbuf_size, VirtualServerChannelSelector.SelectListener listener) {
        return (new VirtualBlockingServerChannelSelector(bind_address, so_rcvbuf_size, listener));
    }

    public static VirtualServerChannelSelector createNonBlocking(InetSocketAddress bind_address, int so_rcvbuf_size, VirtualServerChannelSelector.SelectListener listener) {
        return (new VirtualNonBlockingServerChannelSelector(bind_address, so_rcvbuf_size, listener));
    }

    public static VirtualServerChannelSelector createTest(InetSocketAddress bind_address, int so_rcvbuf_size, VirtualServerChannelSelector.SelectListener listener) {
        int range = COConfigurationManager.getIntParameter("TCP.Listen.Port.Range", -1);
        if (range == -1) {
            return (createBlocking(bind_address, so_rcvbuf_size, listener));
        } else {
            return (new VirtualNonBlockingServerChannelSelector(bind_address.getAddress(), bind_address.getPort(), range, so_rcvbuf_size, listener));
        }
    }
}
