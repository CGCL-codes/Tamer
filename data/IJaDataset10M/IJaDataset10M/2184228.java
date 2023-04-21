package net.sourceforge.javautil.network.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import net.sourceforge.javautil.network.protocol.IProtocol;
import net.sourceforge.javautil.network.transport.INetworkTransport;

/**
 * 
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface ISocketTransport extends INetworkTransport {

    public static long LISTENER_STATE_TIMEOUT = 30000;

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    void sendMessage(byte[] message);

    void sendMessage(byte[] message, int offset, int length);

    OutputStream getMessageOutputStream();

    void startListening(ISocketTransportListener listener);

    void stopListening();

    boolean isListening();

    boolean isConnected();

    void setKeepAlive(boolean flag, long interval);

    void close();
}
