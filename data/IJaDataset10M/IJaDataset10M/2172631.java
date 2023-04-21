package org.red5.server.net.rtmps;

import java.net.InetSocketAddress;
import java.util.Map;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.red5.server.net.rtmp.RTMPClient;
import org.red5.server.net.rtmp.RTMPClientConnManager;
import org.red5.server.net.rtmp.codec.RTMP;

/**
 * RTMPS client object
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Paul Gregoire (mondain@gmail.com)
 * @author Kevin Green (kevygreen@gmail.com)
 */
public class RTMPSClient extends RTMPClient {

    private final RTMPSMinaIoHandler ioHandler;

    /** Constructs a new RTMPClient. */
    public RTMPSClient() {
        ioHandler = new RTMPSMinaIoHandler();
        ioHandler.setCodecFactory(getCodecFactory());
        ioHandler.setMode(RTMP.MODE_CLIENT);
        ioHandler.setHandler(this);
        ioHandler.setRtmpConnManager(RTMPClientConnManager.getInstance());
    }

    public Map<String, Object> makeDefaultConnectionParams(String server, int port, String application) {
        Map<String, Object> params = super.makeDefaultConnectionParams(server, port, application);
        if (!params.containsKey("tcUrl")) {
            params.put("tcUrl", String.format("rtmps://%s:%s/%s", server, port, application));
        }
        return params;
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    protected void startConnector(String server, int port) {
        socketConnector = new NioSocketConnector();
        socketConnector.setHandler(ioHandler);
        future = socketConnector.connect(new InetSocketAddress(server, port));
        future.addListener(new IoFutureListener() {

            public void operationComplete(IoFuture future) {
                try {
                    future.getSession();
                } catch (Throwable e) {
                    handleException(e);
                }
            }
        });
        future.getSession().close(false);
        future.awaitUninterruptibly(CONNECTOR_WORKER_TIMEOUT);
        socketConnector.dispose();
    }
}
