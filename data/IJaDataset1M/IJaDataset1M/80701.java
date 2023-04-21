package net;

import util.Util;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.InterruptedIOException;

/**
 *
 * @author Kenroy Granville / Tim Hickey
 * @version "%I%, %G%"
 * @see RendezvousInfo
 */
public class RendezvousServer implements RendezvousInfo.ListPurging {

    public static final byte PING = 11;

    public static final byte REGISTER_RENDEZVOUS = 12;

    public static final byte SEND_RENDEZVOUS_INFO = 13;

    public static final byte SEND_RENDEZVOUS_INFO_LIST = 14;

    public static final String PING_RESULT = "PING!";

    public static final long PURGE_SLEEP = 1500L;

    Hashtable infoHash;

    /** The server socket that the RendezvousServer listens on. **/
    ServerSocket serverSocket;

    /** The thread that runs the the main server loop. **/
    Core coreThread;

    /** The thread that runs the the main rendezvous purging loop. **/
    RendezvousInfo.ListPurger purgerThread;

    /** The password used to validate a communication with the GroupServer **/
    String password;

    /** The port of the serverSocket. **/
    int port;

    private int sendCnt, requestCnt;

    /**
    * Creates a password protected RendezvousServer.
    * @param port  The port number on which the <code>RendezvousServer</code>
    *              will run.
    * @param pw  The password used to authenticate communication with the
    *            RendezvousServer. 
    **/
    public RendezvousServer(int port, String pw) {
        serverSocket = null;
        infoHash = new Hashtable();
        setPort(port);
        setPassword(pw);
    }

    /**
    * Returns whether or not the three RendezvousServer running state
    * necessities are met. Thus, the serverSocket must not be closed, the
    * coreThread and purgerThread must be live.
    * @return true only if the coreThread and purgerThread are alive and
    *         serverSocket is not closed.
    **/
    public boolean isRunning() {
        return (serverSocket != null && !serverSocket.isClosed() && coreThread != null && coreThread.isAlive() && purgerThread != null && purgerThread.isAlive());
    }

    /**
    * Starts a RendezvousServer by openning a ServerSocket on port and
    * starting the coreThread.
    **/
    public void start() throws IOException, SecurityException, IllegalThreadStateException {
        if (!isRunning()) {
            sendCnt = 0;
            requestCnt = 0;
            infoHash.clear();
            serverSocket = new ServerSocket(port);
            coreThread = new Core("T-RendezvousServerCore(" + port + ")");
            coreThread.start();
            purgerThread = new RendezvousInfo.ListPurger(this, "T-RendezvousPurger(" + port + ")");
            purgerThread.start();
            Util.syslog(this, "started on port " + port);
        } else if (isRunning()) {
            IllegalStateException e = new IllegalStateException("Tried to start while running!");
            Util.syslog(this, "start() requested while running: " + "coreThread.isAlive()=" + coreThread.isAlive() + ", serverSocket.isClosed()=" + serverSocket.isClosed(), e);
            throw e;
        }
    }

    /**
    * Stops the GroupServer by closing the <code>serverSocket</code> if it is
    * open.
    */
    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) try {
            serverSocket.close();
        } catch (IOException e) {
            if (!serverSocket.isClosed()) {
                Util.syslog(this, "stop() failed to close serverSocket", e);
                return;
            }
        }
        Util.syslog(this, "stopped running on port " + port);
    }

    /**
    * Runs the main server loop that accepts socket connections and handles
    * them as new thread Requests.
    */
    class Core extends Thread {

        public Core(String name) {
            super(name);
        }

        public void run() {
            while (!serverSocket.isClosed()) {
                try {
                    Socket s = serverSocket.accept();
                    Request r = new Request(requestCnt++, s);
                    Util.syslog(this, r.toString());
                    r.start();
                } catch (SocketTimeoutException ste) {
                    Util.syslog(this, "run() socket loop", ste);
                } catch (InterruptedIOException iioe) {
                    Util.syslog(this, "run() socket loop", iioe);
                } catch (Exception e) {
                    Util.syslog(this, "run() socket loop problem", e);
                    RendezvousServer.this.stop();
                    return;
                }
            }
        }
    }

    /**
    * Each Request is spawned when a socket is accepted by the serverSocket in
    * the coreThread and follows the simple protocol below.
    * (1) Get I/O streams for reading and writing.
    * (2) Read the server's password
    * (3) Verify the password
    *     (a) if verification fails then jump to (6)
    * (4) Read the byte that specifies the action to take
    * (5) Take the specified action
    * (6) Close the socket and I/O streams 
    **/
    class Request extends Thread {

        Socket socket;

        ObjectInputStream in;

        ObjectOutputStream out;

        byte action;

        int id;

        /**
       * @param id  The ID number for this client connection.
       * @param socket  a Socket that was accepted by the serverSocket.
       **/
        public Request(int id, Socket s) {
            super("GroupServer.Request(" + id + ")");
            this.id = id;
            socket = s;
            action = PING;
        }

        public String toString() {
            return "Request(id=" + id + ", action=" + action + ", " + socket + ")";
        }

        /**
       * Opens the IO streams of socket and reads a password to authenticate
       * communication before reading an action byte and handling it via
       * {@link takeAction()}. If the wrong password is read the connection
       * is closed immediately.
       **/
        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                String pw = (String) in.readObject();
                if (password.equals(pw)) {
                    action = in.readByte();
                    takeAction();
                } else Util.syslog(this, "wrong password '" + pw + "' correct pw='" + getPassword() + "' - ACTION=" + action);
            } catch (Exception e) {
                Util.syslog(this, "run() problem " + socket, e);
            }
            close();
        }

        /** Close socket, in and out streams. **/
        public void close() {
            NetUtil.closeSocket(socket, in, out);
        }

        /** Take the specified RendezvousServer <b>action</b> requested. **/
        public void takeAction() throws Exception {
            switch(action) {
                case PING:
                    sendResult(PING_RESULT);
                    break;
                case REGISTER_RENDEZVOUS:
                    sendResult(registerRendezvous((RendezvousInfo) in.readObject()));
                    break;
                case SEND_RENDEZVOUS_INFO:
                    sendResult(getRendezvousInfo((String) in.readObject()));
                    break;
                case SEND_RENDEZVOUS_INFO_LIST:
                    sendResult(getInfoList());
                    break;
            }
        }

        /**
       * This sends a <code>result</code> Object over the output stream of the
       * Socket that started this Request.
       * @param result  the result being sent back to the client.
       */
        public void sendResult(Object result) throws IOException {
            out.writeObject(result);
            sendCnt++;
        }
    }

    /**
    * Synchronized because a number of Request threads could be accessing
    * infoHash at the same time.
    **/
    public RendezvousInfo[] getInfoList() {
        synchronized (infoHash) {
            return (RendezvousInfo[]) infoHash.values().toArray(new RendezvousInfo[0]);
        }
    }

    public boolean isPurging() {
        return !serverSocket.isClosed();
    }

    public boolean isCheckingMember() {
        return false;
    }

    public void removeInfo(RendezvousInfo info) {
        removeRendezvous(info.getDomainName());
    }

    public long purgingSleep() {
        return PURGE_SLEEP;
    }

    public void purgingUpdate() {
    }

    /**
    * Synchronized because a number of Request threads could be accessing
    * infoHash at the same time.
    **/
    public Boolean registerRendezvous(RendezvousInfo info) {
        synchronized (infoHash) {
            String key = info.getDomainName();
            Boolean b;
            if (!infoHash.containsKey(key)) if ((b = NetUtil.isRendezvous(info)) != null && b.booleanValue()) {
                infoHash.put(key, info);
                return b;
            }
        }
        return new Boolean(false);
    }

    /**
    * Synchronized because a number of Request threads could be accessing
    * infoHash at the same time.
    **/
    public RendezvousInfo getRendezvousInfo(String domainName) {
        synchronized (infoHash) {
            return (RendezvousInfo) infoHash.get(domainName);
        }
    }

    /**
    * Synchronized because a number of Request threads could be modifying
    * infoHash at the same time.
    **/
    public void removeRendezvous(String domainName) {
        synchronized (infoHash) {
            infoHash.remove(domainName);
        }
    }

    public int getRequestCount() {
        return requestCnt;
    }

    /**
    * Gets the password for communicating with this <i>RendezvousServer</i>.
    * @return the password for verifying communication.
    **/
    public String getPassword() {
        return password;
    }

    /**
    * Gets the port for communicating with this <i>RendezvousServer</i>.
    * @return the prot on which this server runs.
    **/
    public int getPort() {
        return port;
    }

    public int getRendezvousCount() {
        return infoHash.size();
    }

    /**
    * Returns the current send count of the RendezvousServer.
    * @return the sendCnt field of the RendezvousServer.
    **/
    public int getSendCount() {
        return sendCnt;
    }

    private void criticalUpdate(String msg) {
        if (isRunning()) {
            IllegalStateException e = new IllegalStateException(msg);
            Util.syslog(this, msg, e);
            throw e;
        }
    }

    /**
    * Changes the port on which  <code>serverSocket</code> is going to run.
    * @param port  the new port that <code>serverSocket</code> will run on.
    */
    public void setPort(int port) {
        criticalUpdate("Attempted to set port while server running!");
        this.port = port;
    }

    /**
    * Sets the password for communicating with this <i>RendezvousServer</i>.
    * @param pw  the password to authenticate <code>RendezvousServer</code>
    *            communication.
    **/
    public void setPassword(String pw) {
        criticalUpdate("Attempted to set password while server running!");
        if (pw == null) setPassword(NetUtil.NULL_PW); else {
            pw = pw.trim();
            if (pw.equals(NetUtil.NULL_PW)) password = NetUtil.NULL_PW; else password = pw;
        }
    }

    /**
    * Returns a String that represents this RendezvousServer.
    * @return a String that contains info about this RendezvousServer.
    **/
    public String toString() {
        return "RendezvousServer[serverSocket=" + serverSocket + ",requestCnt=" + getRequestCount() + ",sendCnt=" + getSendCount() + "]";
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            Util.debugln("RendezvousServer: illegal arguments! try again with:\n" + "  main(port, password)" + "\nPass in \"\" for no password protection");
            System.exit(0);
        } else {
            RendezvousServer rs = new RendezvousServer(Integer.parseInt(args[0]), args[1]);
            rs.start();
            String acts = "actions:\n  help, quit, reqc(requestCnt), " + "sc(sendCnt), rendc(rendezvousCnt)";
            System.out.print(acts + "\n\n>>");
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            String line;
            String[] split;
            while ((line = reader.readLine()) != null) {
                split = line.split(" ");
                if (split[0].equalsIgnoreCase("help")) System.out.print(acts); else if (split[0].equalsIgnoreCase("quit")) System.exit(0); else if (split[0].equalsIgnoreCase("reqc")) System.out.print(rs.getRequestCount()); else if (split[0].equalsIgnoreCase("sc")) System.out.print(rs.getSendCount()); else if (split[0].equalsIgnoreCase("rendc")) System.out.print(rs.getRendezvousCount());
                System.out.print("\n\n>>");
            }
        }
    }
}
