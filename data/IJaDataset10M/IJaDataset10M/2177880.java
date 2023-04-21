package joom;

import joom.commands.*;
import joom.items.*;
import java.io.*;
import java.net.*;

/**
 * This class runs a joom server.
 * @author Christopher Brind
 */
public class Main implements Runnable {

    private static final boolean DEBUG = true;

    private static Logger l = Logger.getLogger();

    private ServerSocket ss;

    private Thread t;

    private boolean bRunning;

    /** The constructor. */
    private Main() {
    }

    private void init() throws Exception {
        initGlobals();
        CommandHandler.readCommands();
        World.readWorld();
        ItemManager.readItems();
        int iPort = Globals.config.getInt("server.port");
        ss = createServerSocket(iPort);
        t = new Thread(this);
        t.setName(Globals.JOOM + " " + getClass().getName());
        t.start();
    }

    /** Create a server socket and make it timeout every second. */
    private ServerSocket createServerSocket(int iPort) throws IOException {
        ServerSocket ss = new ServerSocket(iPort);
        ss.setSoTimeout(1000);
        return ss;
    }

    private void initGlobals() throws Exception {
        Globals.joomDir = new File(System.getProperty("joom.dir", "."));
        Globals.config = new Config(new File(Globals.joomDir, "xml/config.xml"));
        Globals.msgs = new Messages(new File(Globals.joomDir, "xml/messages.xml"));
    }

    public void run() {
        try {
            l.log(this, "waiting for connections");
            bRunning = true;
            while (bRunning) {
                try {
                    Socket socket = ss.accept();
                    new SocketHandler(socket);
                } catch (InterruptedIOException iioex) {
                }
            }
        } catch (Exception ex) {
            l.log(this, "run() fatal exception: " + ex);
            if (DEBUG) {
                ex.printStackTrace();
            }
        } finally {
            try {
                ss.close();
            } catch (Exception ex) {
            }
        }
    }

    public void stop() {
        l.log(this, "stopping...");
        bRunning = false;
        t.interrupt();
    }

    /** Everything starts here... */
    public static void main(String asArgs[]) {
        l.log("joom.Main*", ">>>" + Globals.JOOM + "<<<");
        final Main main = new Main();
        ThreadGroup tg = new ThreadGroup(Globals.JOOM + " threads");
        Thread t = new Thread(tg, new Runnable() {

            public void run() {
                try {
                    main.init();
                } catch (Exception ex) {
                    System.out.println("FATAL EXCEPTION: " + ex.getMessage());
                    if (DEBUG) {
                        ex.printStackTrace(System.out);
                    }
                    System.exit(1);
                }
            }
        });
        t.setName(">>>> " + Globals.JOOM + " master thread <<<<");
        t.start();
        if (Boolean.getBoolean("server.manager")) {
            new joom.gui.ServerGUI(main);
        }
    }
}
