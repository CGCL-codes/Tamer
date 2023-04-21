package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 
 * In this class, all the resources are controlled(Sockets, Files, etc)
 * Otherwise, there are problems for example if multiple sockets a created
 * 
 * @author Dominik
 */
public final class Connectgen {

    private static ServerSocket ss;

    private static Socket s;

    private static Logger log = new Logger();

    private static HashMap<String, OutputStreamWriter> hash = new HashMap<String, OutputStreamWriter>();

    private static OutputStreamWriter systemoutwriter = new OutputStreamWriter(System.out);

    static int numsore = 0;

    static boolean socketdone = false;

    static boolean initdone = false;

    /**
  * Creates a socket: makes sure that there is only one
  */
    static void createServerSocket() {
        if (!socketdone) {
            try {
                ss = new ServerSocket(Variables.getPortNumber());
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            socketdone = true;
        }
    }

    /**
  * Returns the ServerSocket
  * 
  * @return the corresponding server-socket is returned
  */
    public static ServerSocket getServerSocket() {
        return ss;
    }

    /**
  * Initialize everything
  */
    public static void init() {
        if (!initdone) {
            createServerSocket();
            initdone = true;
            addDest("systemout", new OutputStreamWriter(System.out));
        }
    }

    /**
  * 
  * Returns the current socket to which the server is connected. So mainly used
  * for the client.
  * 
  */
    public static Socket getSocket() throws Exception {
        s = new Socket(Variables.getServerName(), Variables.getPortNumber());
        return (s);
    }

    /**
  * Returns the current socket to which the server is connected. So mainly used
  * for the client.
  * 
  * @return returns a socket which is connected with the ServerName and to port
  *         PortNumber.
  * 
  */
    public static Socket getSocket(String ServerName, int PortNumber) throws Exception {
        s = new Socket(ServerName, PortNumber);
        return (s);
    }

    /**
  * 
  * Returns the current socket to which the server is connected. So mainly used
  * for the client.
  * 
  * @return returns a socket to which the server is connected
  */
    public static Socket getAktSocket() throws Exception {
        if (s == null || !s.isBound()) {
            s = new Socket(Variables.getServerName(), Variables.getPortNumber());
        }
        return (s);
    }

    /**
  * Closes the socket: Only use it if you know what you're doing
  */
    public static void killSocket() {
        try {
            s.close();
            socketdone = false;
            init();
        } catch (Exception e) {
            log.krit(e);
        }
    }

    /**
  * Adds the file "file" to a list and returns an outputstreamwriter
  */
    public static OutputStreamWriter addDest(File file) {
        FileWriter out = null;
        OutputStreamWriter out2;
        if (hash.containsKey(file.getName())) {
            out2 = (hash.get(file.getName()));
        } else {
            try {
                out = new FileWriter(file, true);
            } catch (Exception e) {
                log.krit(e);
            }
            hash.put(file.getName(), out);
            out2 = (OutputStreamWriter) out;
        }
        return (out2);
    }

    /**
  * Adds an OutputStreamWriter The name has to be unique, otherwise it can not
  * be guaranteed, that any- one else doesn't lock the Dest
  * 
  * @param unique name of the stream
  */
    public static void addDest(String name, OutputStreamWriter writer) {
        if (!hash.containsKey(name)) {
            hash.put(name, writer);
        }
    }

    /**
  * Returns the OutputStreamWriter by name
  * 
  * @return returns the OutputStreamWrtier with the name given
  */
    public static OutputStreamWriter getStream(String name) {
        return (hash.get(name));
    }

    /**
  * Returns a OutputStreamWirter with the System out. There is only one
  * System-out-Writer
  */
    public static OutputStreamWriter getSystemWriter() {
        return (systemoutwriter);
    }
}
