package extrae;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import com.sun.javafx.runtime.Entry;

/**
 *
 * @author lojkoro
 */
public class ServidorXML implements SocketListener {

    private ServerSocket serverSocket;

    public BufferedReader in;

    public BufferedWriter out;

    private Socket server;

    public Boolean conectado = false;

    public Integer port = 8080;

    public SocketListener fxListener;

    public ServidorXML(SocketListener sock) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(5000);
        this.fxListener = sock;
    }

    public void run() {
        while (true) {
            while (!conectado) {
                try {
                    server = serverSocket.accept();
                    in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
                    out.flush();
                    if (server.isConnected()) {
                        conectado = true;
                    }
                } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            while (conectado) {
                try {
                    if (in.ready()) {
                        onMessage(in.readLine());
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    public void writeOut(String msg) {
        try {
            out.write(msg, 0, msg.length());
            out.newLine();
            out.flush();
        } catch (Exception e) {
        }
    }

    public void desconectar() throws IOException {
        server.close();
        serverSocket.close();
        conectado = false;
    }

    @Override
    public void onMessage(final String line) {
        Entry.deferAction(new Runnable() {

            @Override
            public void run() {
                fxListener.onMessage(line);
            }
        });
    }

    @Override
    public void onClosedStatus(Boolean isClosed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
