package org.suren.core.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.suren.core.msg.Destination;

/**
 * @author SuRen
 */
public class MsgServer {

    private int PORT = 1234;

    private Destination dest;

    private ServerSocket server;

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1234);
        Socket socket = null;
        while (true) {
            socket = server.accept();
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                System.out.println(tmp);
            }
            reader.close();
        }
    }

    public void init(Destination dest, boolean up, int port) {
        this.dest = dest;
        PORT = port;
        if (up) startUp();
    }

    public void startUp() {
        startUp(1000);
    }

    public void startUp(final int delay) {
        new Thread() {

            public void run() {
                try {
                    Thread.sleep(delay);
                    server();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void server() throws IOException {
        server = new ServerSocket(PORT);
        while (true) {
            if (server.isClosed()) break;
            final Socket socket = server.accept();
            new Thread() {

                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        StringBuffer buffer = new StringBuffer();
                        String tmp = reader.readLine();
                        while (tmp != null) {
                            buffer.append(tmp);
                            tmp = reader.readLine();
                        }
                        dest.put(buffer.toString());
                        reader.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public void shutDown() {
        if (server != null) try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
