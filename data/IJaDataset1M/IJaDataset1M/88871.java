package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Deze klasse maakt de verbinding met de server.
 * @author David Covemaeker, Maarten Minnebo, Tim Van Thuyne, Toon Kint
 */
public class Connector {

    private static Socket socket = null;

    private static BufferedWriter bufferedWriter;

    /**
	 * Maakt de verbinding met de server.
	 * @param server De gekozen server
	 * @param login De gekozen loginnaam
	 * @param port De gekozen poort
	 * @return
	 */
    public static Socket connection(String server, String login, int port) throws IOException {
        try {
            socket = new Socket(server, port);
        } catch (SocketException se) {
            return null;
        }
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("NICK " + login + "\r\n");
            bufferedWriter.write("USER " + login + " 8 * : Java IRC Client\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    /**
	 * Verbreekt de connectie met de socket.
	 * @throws IOException
	 */
    public static void closeSocket() throws IOException {
        socket.close();
    }
}
