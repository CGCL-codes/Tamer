package org.osll.tictactoe.transport.udp.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import org.osll.tictactoe.Control;
import org.osll.tictactoe.ServerConnection;
import org.osll.tictactoe.Team;
import org.osll.tictactoe.transport.DefaultOptions;
import org.osll.tictactoe.transport.tcp.LoginAcceptedResponse;
import org.osll.tictactoe.transport.tcp.LoginQuery;
import org.osll.tictactoe.transport.udp.SocketProcessor;

/**
 * Получение соединения с сервером через TCP
 */
public class ServerConnectionImpl extends SocketProcessor implements ServerConnection {

    String host = null;

    int port = 0;

    public ServerConnectionImpl() {
        this.host = DefaultOptions.getInstance().getHost();
        this.port = DefaultOptions.getInstance().getUdpPort();
    }

    @Override
    public synchronized Control connect(String name, Team team) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        LoginQuery query = new LoginQuery();
        query.setName(name);
        query.setTeam(team);
        write(socket, query);
        byte[] buf = new byte[30000];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could't send packet");
        }
        LoginAcceptedResponse response = (LoginAcceptedResponse) read(packet);
        System.out.println("Try to connect control to " + host + ":" + response.getPort());
        return new ControlImpl(host, response.getPort(), name, team);
    }
}
