package communicator.utility;

import java.io.*;
import java.net.*;

/**
 *
 * <p>Title:Server communicator </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author Farid Haji Zeinalabedin
 *         farid.zeinalabedin@gmail.com
 * @version 1.0
 */
public class ReceiveMessage extends Thread {

    private Socket connection;

    private MessageListener listener;

    private boolean keepLisetening = false;

    private BufferedReader input;

    public ReceiveMessage(Socket connection, MessageListener listener) {
        try {
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connection.setSoTimeout(ComConstants.READ_TIME_OUT);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        set(connection, listener);
    }

    private void set(Socket connection, MessageListener listener) {
        this.connection = connection;
        this.listener = listener;
    }

    public void run() {
        StringBuffer message = new StringBuffer("");
        char ch;
        setKeepLisetening(true);
        while (keepLisetening) {
            try {
                ch = (char) input.read();
                if (ch == '\0') {
                    listener.socketMsgRecieve(message.toString());
                    message = new StringBuffer("");
                } else {
                    message.append(ch);
                }
            } catch (InterruptedIOException intIOException) {
                continue;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                break;
            }
        }
        try {
            input.close();
            connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void stopListening() {
        setKeepLisetening(false);
    }

    private void setKeepLisetening(boolean keepLisetening) {
        this.keepLisetening = keepLisetening;
    }
}
