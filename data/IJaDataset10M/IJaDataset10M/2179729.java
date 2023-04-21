package mujmail.connections.original;

import java.io.*;
import javax.microedition.io.*;

/**
 * Class from original mujMail version 1.07.02.
 * 
 * It is here just for testing new connections. 
 * 
 * To replace new connections with these connections from original mujMail,
 * replace field of class mujmail.protocols.InProtocol 
 *  protected ConnectionInterface connection; with
 *  protected BasicConnection connection;
 * and than in constructor of such class replace:
 *  connection = new ConnectionCompressed(); with
 *  connection = new SocketConnection();
 * 
 * @author Son Tung
 *
 */
public class SocketConnection extends BasicConnection {

    OutputStream outputStream;

    InputStream inputStream;

    StreamConnection streamConnection;

    protected void write(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }

    protected int read(byte[] buffer, int offset, int length) throws IOException {
        return inputStream.read(buffer, offset, length);
    }

    protected int available() throws IOException {
        return inputStream.available();
    }

    public void close() {
        connected = false;
        pos = 0;
        len = 0;
        quit = false;
        try {
            streamConnection.close();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(String url, boolean ssl, byte sslType) {
        try {
            if (ssl) {
                streamConnection = (StreamConnection) Connector.open("ssl://" + url);
                available_bug = true;
            } else streamConnection = (StreamConnection) Connector.open("socket://" + url, Connector.READ_WRITE, true);
            inputStream = streamConnection.openInputStream();
            outputStream = streamConnection.openOutputStream();
            connected = true;
            pos = 0;
            len = 0;
            quit = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
