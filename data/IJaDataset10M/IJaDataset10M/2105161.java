package lia.gsi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import lia.gsi.net.GSIBaseServer;
import lia.gsi.net.Peer;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.jaas.UserNamePrincipal;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

/**
 * Generic GSI Server
 * 
 * @author Adrian Muraru 
 */
public class GSIServer extends GSIBaseServer {

    private static Logger logger = Logger.getLogger(GSIServer.class.getName());

    protected static final int PORT = 54320;

    /**
	 * initializes the GSIServer with the default credentials and starting listening on default port
	 * 
	 * @throws IOException
	 */
    public GSIServer(int port) throws Exception {
        this((String) null, (String) null, port);
    }

    /**
	 * initializes the GSIServer with the default credentials and starting listening on the default port
	 * 
	 * @throws IOException
	 */
    public GSIServer() throws Exception {
        this((String) null, (String) null, PORT);
    }

    /**
	 * initializes the GSIServer with the provided credentials and starts to listen to client connections on the port passed as a parameter
	 * 
	 * @param cred
	 *            the credentials used by this server, if null then default credentials are used.
	 * @param port
	 *            the port number used by this server
	 */
    public GSIServer(GSSCredential cred, int port) throws Exception {
        super(cred, port);
    }

    /**
	 * initializes the GSIServer with the provided credentials and starts to listen to client connections on the port passed as a parameter
	 * 
	 * @param serverKey
	 * @param serverCert
	 * @param port
	 * @throws Exception
	 */
    public GSIServer(String serverKey, String serverCert, int port) throws Exception {
        super(generateGSSCredential(serverKey, serverCert), port);
    }

    public static GSSCredential generateGSSCredential(String serverKey, String serverCert) throws GlobusCredentialException, IOException, GSSException {
        GlobusCredential credentials = null;
        if (serverKey == null && serverCert == null) {
            serverKey = System.getProperty("X509_SERVICE_KEY");
            serverCert = System.getProperty("X509_SERVICE_CERT");
        }
        if (serverKey == null && serverCert == null) {
            serverKey = System.getenv("X509_HOST_KEY");
            serverCert = System.getenv("X509_HOST_CERT");
        }
        if (serverKey == null && serverCert == null) {
            serverKey = "/etc/grid-security/hostkey.pem";
            serverCert = "/etc/grid-security/hostcert.pem";
        }
        if (serverKey == null && serverCert == null) {
            credentials = GlobusCredential.getDefaultCredential();
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, "Using user proxy certificate:" + credentials.getSubject());
            }
        } else if (serverKey != null && serverCert != null) {
            credentials = new GlobusCredential(serverCert, serverKey);
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, "Using host certificate:" + credentials.getSubject());
            }
        } else {
            throw new IOException("Error: Service credentials could not be loaded.");
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "credentials:" + credentials);
        }
        return new GlobusGSSCredentialImpl(credentials, GSSCredential.ACCEPT_ONLY);
    }

    /**
	 * Initialization of the GSI server searching and setting the authorization plugin: <br>
	 * org.globus.gsi.gssapi.auth.AuthorizationPlugin system property may use to define the class implementing an GT2 Authorization Plugin.<br>
	 * If not defined the default grid-mapfile authorization method is used.
	 * 
	 * @see org.globus.gsi.gssapi.auth.Authorization
	 * @throws IOException
	 * @throws IOException
	 *             if grid-map file cannot be loaded
	 */
    protected void initialize() {
        super.initialize();
        setGssMode(GSIConstants.MODE_GSI);
    }

    /**
	 * Handles individual client connections by starting a different thread.
	 * 
	 * @param socket
	 *            is connected to a client ready to send request to the gatekeeper.
	 * @throws IOException
	 *             if authentication/authorization exception
	 */
    protected void handleConnection(Peer peer) {
        Socket socket = peer.getSocket();
        Subject peerSubject = null;
        if (logger.isLoggable(Level.FINE)) {
            logger.info("Client connected: " + socket.getInetAddress() + ":" + socket.getPort());
        }
        try {
            socket.getOutputStream();
            socket.getInputStream();
            peerSubject = peer.getPeerSubject();
        } catch (IOException e) {
            logger.log(Level.INFO, "Authentication failed:", e);
            if (!socket.isClosed()) {
                try {
                    socket.close();
                    if (logger.isLoggable(Level.INFO)) {
                        logger.log(Level.INFO, "Client disconnected");
                    }
                } catch (IOException e1) {
                }
            }
            return;
        }
        handleConversation(this, socket, peerSubject);
    }

    /**
	 * This method needs to be implemented by subclasses. <br>
	 * Optimally, it should be a non-blocking call starting a separate thread to handle the client: i.e:
	 * 
	 * <pre>
	 *   ControlClient c = new ControlClient(parent, socket,Subject peerSubject); 
	 *   c.start() 
	 * </pre>
	 * 
	 * The default implementation just put an int on the wire.
	 */
    protected void handleConversation(GSIServer parent, Socket client, Subject peerSubject) {
        logger.info("Client connected :" + client + "\n" + peerSubject);
        if (peerSubject != null) {
            UserNamePrincipal up = (UserNamePrincipal) peerSubject.getPrincipals(UserNamePrincipal.class).toArray()[0];
            System.out.println("LocalID:" + up.getName());
        }
        try {
            PrintWriter pw = new PrintWriter(client.getOutputStream());
            pw.println("HELLO From GSI Server. Your order please!");
            pw.flush();
            System.out.println("Sent");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContact() {
        String gid = null;
        try {
            gid = getCredentials().getName().toString();
        } catch (GSSException e) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        url.append(getHost()).append(":").append(String.valueOf(getPort())).append(":").append(gid);
        return url.toString();
    }

    public static void main(String[] args) throws Exception {
        GSIServer ctrlServer = new GSIServer();
        ctrlServer.start();
        System.out.println("Started");
    }
}
