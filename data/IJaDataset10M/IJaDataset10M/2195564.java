package org.ultichat.packets;

import java.io.IOException;
import org.ultichat.Container;
import org.ultichat.net.Packet;
import org.xsocket.connection.INonBlockingConnection;

/**
 * Login message packet
 * 
 * @author Anthony
 */
public class LoginMessage extends Container implements Packet {

    public void handlePacket(String[] data, INonBlockingConnection session) throws IOException {
        String username = formatUsername(data[1]);
        if (!isValidUsername(username)) return;
        String sessionId = data[3];
        if (!isValidSession(username, sessionId)) return;
        write(2, "[" + username + " has signed on]_@orange@_online=" + usersOnline());
    }
}
