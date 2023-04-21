package net.rptools.clientserver.hessian.server;

import java.io.IOException;
import java.text.NumberFormat;
import net.rptools.clientserver.hessian.HessianUtils;

/**
 * @author drice
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServerConnection extends net.rptools.clientserver.simple.server.ServerConnection {

    public ServerConnection(int port) throws IOException {
        super(port);
    }

    public void broadcastCallMethod(String method, Object... parameters) {
        broadcastMessage(HessianUtils.methodToBytesGZ(method, parameters));
    }

    public void broadcastCallMethod(String[] exclude, String method, Object... parameters) {
        byte[] data = HessianUtils.methodToBytesGZ(method, parameters);
        broadcastMessage(exclude, data);
    }

    public void callMethod(String id, String method, Object... parameters) {
        byte[] data = HessianUtils.methodToBytesGZ(method, parameters);
        sendMessage(id, null, data);
    }

    public void callMethod(String id, Object channel, String method, Object... parameters) {
        byte[] data = HessianUtils.methodToBytesGZ(method, parameters);
        sendMessage(id, channel, data);
    }
}
