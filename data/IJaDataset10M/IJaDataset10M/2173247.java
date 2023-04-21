package hotdog.client;

import hotdog.NetWrapper;
import java.net.*;
import java.io.*;

public class ClientTest {

    public NetWrapper socket;

    public ClientTest(int port) throws SocketException {
        socket = new NetWrapper(port);
    }

    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, ClassNotFoundException {
        ClientTest me = new ClientTest(26000);
        while (true) {
            Object foo = me.socket.receiveData();
            if (foo != null) System.out.println(foo);
        }
    }
}
