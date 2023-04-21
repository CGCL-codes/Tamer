package CSApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import tools.APILogger;
import tools.JListPrintStream;

/**
 *
 * @author Baschenis Anthony GLO2 - Polytech' Savoie
 */
public abstract class ComClient implements Runnable {

    /**
     *
     */
    public static String LOG_PREFIX = "Client - ";

    /**
     *  Disconnec prefix, will be deprecated in future releases
     */
    public static String PREFIX_DISCONNECT = "DC$";

    private InetAddress ip;

    private int port;

    private APILogger log;

    private Socket socket;

    private boolean asking;

    private PrintWriter out;

    private BufferedReader in;

    private boolean running;

    private boolean ready;

    /**
     *
     * @param ip
     * @param port
     * @param log
     * @param logStream
     * @param errorStream
     */
    protected ComClient(InetAddress ip, int port, int log, PrintStream logStream, PrintStream errorStream) {
        init(ip, port, log, logStream, errorStream);
    }

    /**
     *
     * @param ip
     * @param port
     * @param log
     * @param logStream
     * @param errorStream
     */
    public void init(InetAddress ip, int port, int log, PrintStream logStream, PrintStream errorStream) {
        ready = false;
        this.ip = ip;
        this.port = port;
        this.asking = false;
        setLog(new APILogger(log, logStream, errorStream, LOG_PREFIX));
        try {
            socket = new Socket(ip, port);
            getLog().log("Assigning socket input and output streams", APILogger.LOG_DEBUG);
            this.out = new PrintWriter(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            getLog().log("Init done", log);
        } catch (IOException ex) {
            getLog().logError("Error connection to socket for " + ip.getHostAddress() + ":" + port);
            ex.printStackTrace(getLog().getErrorLogStream());
            kill();
        }
    }

    /**
     *
     */
    public void disconnect() {
        try {
            getLog().log("Disconnecting from server...", APILogger.LOG_NORMAL);
            send(PREFIX_DISCONNECT);
            if (!this.socket.isClosed()) {
                getLog().log("Closing socket", APILogger.LOG_DEBUG);
                this.socket.close();
            }
            getLog().log("Disconnecting from server...", APILogger.LOG_NORMAL);
        } catch (IOException ex) {
            getLog().logError("Socket closing error");
            ex.printStackTrace(log.getErrorLogStream());
        }
    }

    /**
     *
     */
    public void kill() {
        try {
            disconnect();
            in.close();
            out.flush();
            out.close();
            this.setRunning(false);
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace(log.getErrorLogStream());
        }
    }

    /**
     *
     * @param msg
     */
    public void ask(Message msg) {
        setAsking(true);
        if (socket.isConnected() && this.in != null) {
            send(msg.toGetRequestString());
        } else {
            getLog().logError("Socket not connected");
        }
        setAsking(false);
    }

    /**
     *
     * @param m
     */
    public abstract void react(Message m);

    /**
     *
     * @param fromServer
     */
    public abstract void react(String fromServer);

    public void setPrintStream(JListPrintStream logPrintStream, JListPrintStream errLogPrintStream) {
        getLog().setErrorLogStream(errLogPrintStream);
        getLog().setLogStream(logPrintStream);
    }

    private String receive() throws IOException {
        String fromServer = in.readLine();
        react(fromServer);
        return fromServer;
    }

    private void send(String str) {
        if (str.length() >= 30) {
            getLog().log("Sending \"" + str.substring(0, 30) + "...\"", APILogger.LOG_VERBOSE);
        } else {
            getLog().log("Sending \"" + str + "\"", APILogger.LOG_VERBOSE);
        }
        this.out.println(str);
        this.out.flush();
        getLog().log("Message sent.", APILogger.LOG_VERBOSE);
    }

    /**
     * Return whether or not the client is ready to continue his processing.
     * @return true if the client is ready
     */
    public boolean isReady() {
        return ready;
    }

    /**
     *
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     *
     * @return
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     *
     * @return
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     *
     * @return
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return
     */
    public boolean isAsking() {
        return asking;
    }

    /**
     *
     * @param asking
     */
    public void setAsking(boolean asking) {
        this.asking = asking;
    }

    /**
     *
     * @return
     */
    public APILogger getLog() {
        return log;
    }

    /**
     *
     * @param log
     */
    public void setLog(APILogger log) {
        this.log = log;
    }

    /**
     *
     * @return
     */
    public boolean isRunning() {
        return running;
    }

    /**
     *
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        try {
            getLog().log("First run", APILogger.LOG_VERBOSE);
            setRunning(true);
            while (running && getSocket() != null && getSocket().isConnected() && !getSocket().isClosed()) {
                ready = true;
                String fromServer;
                getLog().log("Reading output from server", APILogger.LOG_DEBUG);
                while ((fromServer = receive()) != null && !getSocket().isClosed() && getSocket().isConnected()) {
                    getLog().log("Received: " + fromServer, APILogger.LOG_NORMAL);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(log.getErrorLogStream());
        } finally {
            getLog().log("Socket not connected exiting...", APILogger.LOG_NORMAL);
        }
    }
}
