package quizgame.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import quizgame.protocol.Authenticate;
import quizgame.protocol.AuthenticationFailed;
import quizgame.protocol.AuthenticationSuccessful;
import quizgame.protocol.Challange;
import quizgame.protocol.InsufficientPrivileges;
import quizgame.protocol.InternalServerError;
import quizgame.protocol.Packet;

/**
 *
 * @author rheo
 */
public abstract class ClientConnection extends Thread {

    private int serverPort = 8888;

    private Authenticate authenticate;

    private ObjectInputStream objectInput;

    private ObjectOutputStream objectOutput;

    private SSLSocket socket;

    private String serverHost;

    /**
     * Creates a new instance of ClientConnection
     * @param serverHost address to the server to fetch remote objects from
     * @param username username to use when authenticating to the server
     * @param password password to use when authenticating to the server
     */
    public ClientConnection(String serverHost, Authenticate authenticate) throws IOException {
        this.authenticate = authenticate;
        int index = serverHost.indexOf(':');
        if (index < 0) {
            this.serverHost = serverHost;
        } else {
            this.serverHost = serverHost.substring(0, index);
            try {
                this.serverPort = Integer.parseInt(serverHost.substring(index + 1));
            } catch (NumberFormatException ex) {
                throw new IOException("The host address is invalid.");
            }
        }
        try {
            connect();
            start();
        } catch (ClassNotFoundException ex) {
            throw new IOException("The type of an object recieved from the server could not be resolved.");
        }
    }

    private void connect() throws ClassNotFoundException, IOException {
        Challange challange;
        MessageDigest md;
        Object inputObject;
        SSLSocketFactory sslFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) sslFact.createSocket(serverHost, serverPort);
        String[] cipher = { "TLS_DH_anon_WITH_AES_128_CBC_SHA" };
        socket.setEnabledCipherSuites(cipher);
        String[] protocols = { "TLSv1" };
        socket.setEnabledProtocols(protocols);
        objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectInput = new ObjectInputStream(socket.getInputStream());
        writeObject(authenticate);
        inputObject = objectInput.readObject();
        if (inputObject instanceof AuthenticationFailed) {
            throw new IOException(((AuthenticationFailed) inputObject).getMessage());
        } else if (inputObject instanceof InsufficientPrivileges) {
            throw new IOException("You don't have the privileges required to access this resource.");
        } else if (inputObject instanceof InternalServerError) {
            throw new IOException("An internal server error occured.");
        } else if (!(inputObject instanceof AuthenticationSuccessful)) {
            throw new IOException("Another object type than expected was recieved from the server.");
        }
    }

    public void run() {
        for (; ; ) {
            Object inputObject;
            try {
                inputObject = objectInput.readObject();
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                return;
            }
            if (inputObject == null) {
                System.out.println("Received null packet from server.");
            } else if (inputObject instanceof Packet) {
                handleObject((Packet) inputObject);
            } else {
                System.out.println("Received an object that did not implement Packet from server. Interesting :)");
            }
        }
    }

    protected void writeObject(Packet object) throws IOException {
        synchronized (objectOutput) {
            objectOutput.writeObject(object);
            objectOutput.flush();
            objectOutput.reset();
        }
    }

    protected abstract void handleObject(Packet packet);
}
