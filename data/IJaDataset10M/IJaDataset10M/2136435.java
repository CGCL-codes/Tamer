package fitnesse.socketservice;

import java.net.Socket;

public interface SocketServer {

    public void serve(Socket s);
}
