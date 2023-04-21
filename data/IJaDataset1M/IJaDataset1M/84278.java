package proper.remote;

import proper.app.Application;
import proper.io.TextFile;
import proper.net.Data;
import proper.remote.messages.Message;
import proper.util.ProperVector;
import proper.util.Timestamp;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;

/**
* This abstract class contains some basic functionality for a server.<br>
* <br>
* For a complete list of commandline parameters just run it with the option
* "-help".
*
*
* @author         FracPete
* @version $Revision: 1.2 $
*/
public abstract class Server extends Application {

    /** with this request the we just send an empty message to the server 
       (this can be used as a notification message) */
    public static final String REQUEST_NOP = "nop";

    /** with this request the we just send an empty message to the server 
       (this can be used as a notification message) */
    public static final String REQUEST_ISALIVE = "is_alive";

    /** the identifier for the clients */
    public static final int LIST_CLIENTS = 0;

    /** the interval in msec to check the alive status of the clients */
    public static final int ALIVE_CHECK = 30000;

    protected int defaultPort;

    protected ServerSocket server;

    protected Message msg;

    protected ClientList clients;

    protected Data sender;

    protected String errorLog;

    protected String accessLog;

    protected boolean leaveLogs;

    protected int acceptTimeout;

    protected boolean hadClients;

    /**
   * initializes the object
   */
    public Server() {
        super();
        server = null;
        clients = new ClientList();
        sender = new Data();
        hadClients = false;
    }

    /**
   * here the initialization of member variables takes place, that are
   * displayed in the parameters
   */
    protected void defaultParameters() {
        super.defaultParameters();
        defaultPort = 10000;
        errorLog = "errors.log";
        accessLog = "access.log";
        leaveLogs = false;
        acceptTimeout = ALIVE_CHECK;
    }

    /**
   * here all the available command line parameters are defined
   */
    protected void defineParameters() {
        super.defineParameters();
        addDefinition("port", "the port to bind to, default is " + defaultPort, true, "<int>", true);
        addDefinition("error_log", "the file to write errors to, default is " + errorLog, true, "<filename>", true);
        addDefinition("access_log", "the file to write accesses to, default is " + accessLog, true, "<filename>", true);
        addDefinition("leave_logs", "doesn't delete the logs when the server starts, default: " + leaveLogs, false, "", true);
        addDefinition("accept_timeout", "the timeout in msec to wait for a connection, default: " + acceptTimeout, true, "<int>", true);
    }

    /**
   * returns the port the server is listening to
   */
    public int getPort() {
        return defaultPort;
    }

    /**
   * returns the hostname of the server
   */
    public String getHostName() {
        String host;
        host = "";
        try {
            host = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (Exception e) {
            host = server.getInetAddress().getCanonicalHostName();
        }
        return host;
    }

    /**
   * writes the message to the given logfile
   */
    protected void addToLog(String log, String msg) {
        Vector lines;
        lines = new ProperVector();
        lines.add((new Timestamp() + msg).toString());
        TextFile.save(log, lines, true);
    }

    /**
   * adds the message to the error log
   */
    public void addToErrorLog(String msg) {
        addToLog(errorLog, msg);
    }

    /**
   * adds the message to the access log
   */
    public void addToAccessLog(String msg) {
        addToLog(accessLog, msg);
    }

    /**
   * creates a new Message with the IP and Port of the server
   */
    public Message createMessage() {
        return new Message(getHostName(), getPort());
    }

    /**
   * creates a new Message with the IP and Port of the server and with the
   * given Request
   */
    public Message createMessage(String request) {
        Message msg;
        msg = createMessage();
        msg.setType(request);
        return msg;
    }

    /**
   * reads the Request from the data the client sent
   */
    protected String getRequest() {
        return msg.getType();
    }

    /**
   * checks whether the given address is still alive
   */
    protected boolean isAlive(InetSocketAddress addr) {
        Message msg;
        msg = createMessage();
        msg.setType(REQUEST_ISALIVE);
        return sender.send(addr, msg.toString(), true);
    }

    /**
   * checks all the clients in the list whether they are still alive, if not
   * they are removed from the list
   */
    public void checkClients() {
        int i;
        InetSocketAddress addr;
        String msg;
        if (clients.size() > 0) hadClients = true;
        i = 0;
        while (i < clients.size()) {
            addr = clients.get(i);
            if (!isAlive(addr)) {
                beforeRemoveClient(clients.get(i), clients);
                clients.remove(i);
                msg = addr + " removed from clients.";
                addToErrorLog(msg);
                println(msg);
            } else {
                if (getVerbose()) println(addr + " is still alive.");
                i++;
            }
        }
        afterCheckClients();
    }

    /**
   * additional code before the client is removed
   */
    protected void beforeRemoveClient(InetSocketAddress client, ClientList list) {
    }

    /**
   * for additional calls after the clients were checked
   */
    protected void afterCheckClients() {
        String msg;
        if ((hadClients) && (clients.size() == 0)) {
            msg = "WARNING: no more clients!";
            addToErrorLog(msg);
            println(msg);
        }
    }

    /**
   * returns the specified clientlist
   */
    public ClientList getClientList(int type) {
        if (type == LIST_CLIENTS) return clients; else return null;
    }

    /**
   * deletes the logs
   */
    protected void deleteLogs() {
        File file;
        try {
            file = new File(accessLog);
            if (file.exists()) file.delete();
            file = new File(errorLog);
            if (file.exists()) file.delete();
        } catch (Exception e) {
        }
    }

    /**
   * starts the server
   */
    protected boolean startup() {
        boolean result;
        int port;
        String msg;
        port = defaultPort;
        try {
            if (cl.exists("port")) port = Integer.parseInt(cl.getValue("port")); else port = defaultPort;
            if (cl.exists("error_log")) errorLog = cl.getValue("error_log");
            if (cl.exists("access_log")) accessLog = cl.getValue("access_log");
            if (!leaveLogs) deleteLogs();
            if (cl.exists("accept_timeout")) acceptTimeout = Integer.parseInt(cl.getValue("accept_timeout"));
            defaultPort = port;
            server = new ServerSocket(defaultPort);
            server.setSoTimeout(acceptTimeout);
            result = true;
        } catch (Exception e) {
            println(e);
            result = false;
        }
        if (result) {
            msg = "Running on port " + server.getLocalPort();
            addToAccessLog(msg);
            println(msg);
        } else {
            msg = "Failed to initialize on port " + port;
            addToErrorLog(msg);
            println(msg);
        }
        return result;
    }

    /**
   * this method determines whether the server should continue accepting 
   * connections or not
   */
    public boolean isOperational() {
        return true;
    }

    /**
   * before a connection is accepted this method is called
   * @see            #process()
   */
    protected void beforeAccept() {
    }

    /**
   * this method is called right after a connection has been accepted
   * @see            #process()
   */
    protected void afterAccept(Socket client) {
        String msg;
        msg = "Request from: " + client;
        addToAccessLog(msg);
        if (getVerbose()) println(msg);
    }

    /**
   * creates a new processor object that processes the requests
   */
    protected abstract Processor createProcessor(Socket client);

    /**
   * contains the loop that processes requests (accepting connections etc.)
   * @see               #beforeAccept()
   * @see               #afterAccept(Socket)
   */
    protected boolean execute() {
        Socket client;
        Processor proc;
        boolean result;
        result = isOperational();
        while (isOperational()) {
            try {
                beforeAccept();
                try {
                    client = server.accept();
                } catch (SocketTimeoutException se) {
                    checkClients();
                    continue;
                }
                afterAccept(client);
                proc = createProcessor(client);
                proc.initialize();
                proc.start();
            } catch (Exception e) {
                println(e);
            }
        }
        return result;
    }

    /**
   * used for cleaning up
   */
    protected void shutdown() {
    }

    /**
   * runs the server
   */
    protected boolean process() throws Exception {
        boolean result;
        result = startup();
        if (result) result = execute();
        shutdown();
        return result;
    }
}
