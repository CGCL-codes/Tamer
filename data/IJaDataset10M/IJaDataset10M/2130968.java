package joom;

import joom.commands.*;
import joom.state.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class handles a new Socket connection in a seperate threads.
 * @author Christopher Brind
 */
public final class SocketHandler implements Runnable {

    private static Logger l = Logger.getLogger();

    private static int iInstance;

    private Socket socket;

    private BufferedReader in;

    private EchoReader er;

    private PrintWriter out;

    private Player p;

    private boolean bRunning = true;

    private final Vector messageList = new Vector();

    private boolean bClosed;

    private int iID;

    /** Construct and start a new socket handler. */
    public SocketHandler(Socket socket) {
        this.socket = socket;
        this.iID = iInstance++;
        Thread t = new Thread(this);
        t.setName(Globals.JOOM + " " + getClass().getName() + " " + iID);
        t.start();
    }

    public int getID() {
        return iID;
    }

    /** Get the PrintWriter currently being used to write to the output 
     * stream. 
     */
    public PrintWriter getPrintWriter() {
        return out;
    }

    /** Get the current BufferedReader being used to read from the input 
     * stream.
     */
    public BufferedReader getBufferedReader() {
        return in;
    }

    /**
     * Get the echo reader currently reading from the input stream.
     */
    public EchoReader getEchoReader() {
        return er;
    }

    public void prompt(String s) {
        prompt();
        print(s);
    }

    /** Displays a prompt. */
    public void prompt() {
        print(Globals.JOOM + " $ ");
    }

    /** Add a message to the list of messages to send to this user. */
    public void addPlayerMessage(PlayerMessage m) {
        messageList.addElement(m);
    }

    /** Has this socket handler been closed? */
    public boolean isClosed() {
        return bClosed;
    }

    /** Sends a message now, usually called by PlayerMessageSender inner
     * class.
     */
    private void sendPlayerMessageNow(PlayerMessage m) throws Exception {
        println(m.toString());
    }

    /** Opens the stream an initialises the socket. */
    private void open() throws IOException {
        int iTimeout = 0;
        try {
            iTimeout = Integer.parseInt(Globals.config.getProperty("client.timeout", "5"));
        } catch (Exception ex) {
            throw new IOException("client.timeout is not valid");
        }
        socket.setSoTimeout(iTimeout * 1000 * 60);
        out = new PrintWriter(socket.getOutputStream(), true);
        er = new EchoReader(out, socket.getInputStream());
        in = new BufferedReader(er);
    }

    /** Closes all the streams and the socket. */
    public void close() {
        try {
            socket.close();
        } catch (Exception ex) {
        }
        try {
            in.close();
        } catch (Exception ex) {
        }
        try {
            out.close();
        } catch (Exception ex) {
        }
        bClosed = true;
    }

    /** Print a blank line to the terminal. */
    public void println() {
        println("");
    }

    /** Print the specified string and a new line to the terminal. */
    public void println(String s) {
        out.println(s);
        out.flush();
    }

    /** Print the specified string to the terminal. */
    public void print(String s) {
        out.print(s);
        out.flush();
    }

    /** Read a line from the terminal. */
    public String readln() {
        String s = null;
        try {
            s = in.readLine();
        } catch (Exception ex) {
        }
        return s;
    }

    /** Get the next command, or null if there are no more commands. */
    public CommandHandler nextCommand() throws JOOMException {
        while (true) {
            prompt();
            String sInput = readln();
            if (null != sInput) {
                String asArgs[] = CommandHandler.toArgs(sInput);
                CommandHandler c = CommandHandler.parseCommand(sInput);
                if (null != c) {
                    c.setArgs(asArgs);
                    return c;
                } else {
                    if (sInput.trim().length() > 0) {
                        println(Globals.msgs.getMessage("unknown.command", 0));
                    }
                }
            } else {
                return null;
            }
        }
    }

    public void run() {
        try {
            l.log(this, "connection from: " + socket.toString());
            open();
            new PlayerMessageSender(this);
            JOOMState s = new ConnectionState();
            while (null != (s = s.next(this))) {
                l.log(this, "run() state changed to: " + s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            l.log(this, "run() > " + ex.toString());
        } finally {
            close();
        }
        l.log(this, "disconnection: " + socket.toString());
    }

    public void stop() {
        bRunning = false;
    }

    public void setPlayer(Player p) {
        Thread t = Thread.currentThread();
        t.setName(Globals.JOOM + " " + getClass().getName() + " player: " + p.getUserName());
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    public Socket getSocket() {
        return socket;
    }

    /** This class sends messages from the list to the terminal. */
    private class PlayerMessageSender implements Runnable {

        private SocketHandler handler;

        public PlayerMessageSender(SocketHandler handler) {
            this.handler = handler;
            Thread t = new Thread(this);
            t.setName(Globals.JOOM + " " + getClass().getName() + " " + handler.getID());
            t.start();
        }

        public void run() {
            while (true) {
                if (handler.isClosed()) {
                    return;
                }
                if (messageList.size() > 0) {
                    synchronized (handler.getPrintWriter()) {
                        PlayerMessage message = (PlayerMessage) messageList.elementAt(0);
                        try {
                            handler.println();
                            handler.sendPlayerMessageNow(message);
                            messageList.removeElementAt(0);
                            handler.prompt(handler.getEchoReader().getPartialString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                }
                if (null != handler.getPlayer()) {
                    Player p = handler.getPlayer();
                    Thread t = Thread.currentThread();
                    t.setName(Globals.JOOM + " " + getClass().getName() + " player: " + p.getUserName());
                }
            }
        }
    }
}
