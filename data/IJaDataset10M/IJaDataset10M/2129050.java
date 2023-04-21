package pt.uc.dei.sdist.mytwitter.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author Alexandre Vieira
 */
public class Client {

    /** . */
    private Socket socket;

    /** . */
    private InputStream in;

    /** . */
    private OutputStream out;

    /** . */
    private BufferedReader reader;

    /** . */
    private BufferedWriter writer;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    protected synchronized void sendMessage(String message) throws IOException {
        writer.write(enforceNewLine(message));
        writer.flush();
    }

    protected synchronized String receiveMessage() throws IOException {
        return reader.readLine();
    }

    protected void dispose() throws IOException {
        System.out.printf("server.Client: dispose()\n");
        in.close();
        out.close();
        socket.close();
    }

    private static String enforceNewLine(String message) {
        return (message.endsWith("\n") ? message : (message += "\n"));
    }
}
