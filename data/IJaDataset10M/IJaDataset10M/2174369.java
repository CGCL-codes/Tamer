package GUI;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnection {

    private String url;

    private boolean isHost;

    private final int PORT = 11250;

    private ServerSocket host;

    private Socket connection;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    /**
	 * Create a SocketConnection. 
	 * @param url
	 * @param host True if host of connection.
	 */
    public SocketConnection(String url, boolean host) {
        this.url = url;
        isHost = host;
        this.host = null;
        connection = null;
        input = null;
        output = null;
    }

    /**
	 * Establish a hosting connection, wait for connection
	 * @return True if connection is ok.
	 */
    public boolean getConnection() {
        if (connection == null) {
            try {
                if (isHost) {
                    host = new ServerSocket(PORT);
                    connection = host.accept();
                } else {
                    connection = new Socket(url, PORT);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            if (connection.isConnected()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
	 * Get an object input stream.
	 * @return
	 */
    public ObjectInputStream getInput() {
        if (input == null && connection != null) {
            try {
                input = new ObjectInputStream(connection.getInputStream());
                return input;
            } catch (Exception e) {
                return null;
            }
        } else if (connection == null) {
            return null;
        } else {
            return input;
        }
    }

    /**
	 * Get an objectoutput stream
	 * @return
	 */
    public ObjectOutputStream getOutput() {
        if (output == null && connection != null) {
            try {
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                return output;
            } catch (Exception e) {
                return null;
            }
        } else if (connection == null) {
            return null;
        } else {
            return output;
        }
    }

    /**
	 * Closes input, output and socket
	 */
    public void closeAll() {
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
