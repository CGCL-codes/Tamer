package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.Service;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:13:42
 */
public interface ChannelService {

    InetSocketAddress getListening();

    void startListening();

    void end() throws IOException;
}
