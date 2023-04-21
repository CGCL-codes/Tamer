package practica4;

import java.io.IOException;
import java.net.ServerSocket;

public class PooledServer extends Thread {

    private ServerSocket ssc;

    public static void main(String[] args) {
        int port = 1234;
        try {
            if (args.length >= 1) port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Port incorrecte. Utilitzant port per defecte: " + port);
        }
        new PooledServer(port).start();
    }

    public PooledServer(int port) {
        try {
            ssc = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error al crear el ServerSocket: " + e.getMessage());
            System.exit(-1);
        }
    }

    public void run() {
        SocketBuffer sb = new SocketBuffer(5);
        for (int i = 0; i < 2; i++) {
            new EchoService(sb).start();
        }
        while (true) {
            try {
                sb.put(ssc.accept());
            } catch (IOException e) {
                System.err.println("Error al crear el Servei: " + e.getMessage());
                System.exit(-1);
            }
        }
    }
}
