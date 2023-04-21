package org.waveprotocol.box.server.rpc;

import com.glines.socketio.common.DisconnectReason;
import com.glines.socketio.common.SocketIOException;
import com.glines.socketio.server.SocketIOFrame;
import com.glines.socketio.server.SocketIOInbound;
import org.waveprotocol.wave.util.logging.Log;

/**
 * The server side of WebSocketChannel.
 */
public class SocketIOServerChannel extends WebSocketChannel implements SocketIOInbound {

    private static final Log LOG = Log.get(SocketIOServerChannel.class);

    private SocketIOOutbound outbound;

    /**
   * Creates a new WebSocketServerChannel using the callback for incoming messages.
   *
   * @param callback A ProtoCallback instance called with incoming messages.
   */
    public SocketIOServerChannel(ProtoCallback callback) {
        super(callback);
    }

    /**
   * This isn't used at the moment.
   */
    @Override
    public String getProtocol() {
        return null;
    }

    /**
   * Handles an incoming connection
   *
   * @param outbound The outbound direction of the new connection.
   */
    @Override
    public void onConnect(SocketIOOutbound outbound) {
        this.outbound = outbound;
    }

    /**
   * Pass on an incoming String message.
   *
   * @param data The message data itself
   */
    @Override
    public void onMessage(int messageType, String data) {
        if (SocketIOFrame.TEXT_MESSAGE_TYPE == messageType) {
            handleMessageString(data);
        } else {
            LOG.warning("Recieved message of unexpected type: " + messageType);
        }
    }

    /**
   * Handle a client disconnect.
   */
    @Override
    public void onDisconnect(DisconnectReason reason, String errorMessage) {
        if (errorMessage == null) {
            LOG.info("websocket disconnected[" + reason + "]");
        } else {
            LOG.info("websocket disconnected[" + reason + "]: " + errorMessage);
        }
        synchronized (this) {
            outbound = null;
        }
    }

    /**
   * Send the given data String
   *
   * @param data
   */
    @Override
    protected void sendMessageString(String data) throws SocketIOException {
        synchronized (this) {
            if (outbound == null) {
                LOG.warning("Websocket is not connected");
            } else {
                outbound.sendMessage(data);
            }
        }
    }
}
