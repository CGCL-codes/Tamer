package org.imajie.server.web.imajiematch.matchsServers.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.imajie.server.web.log.LoggingOutputStream;
import org.imajie.server.web.log.StdOutErrLevel;
import org.imajie.server.web.Constants;

public class MainServer implements Runnable {

    public static int port = Constants.MAIN_SERVER_PORT;

    private static boolean kill = false;

    public static boolean processFinish = false;

    private Socket client;

    public static MainServerProtocol imp;

    private static Logger log;

    private static LogManager logManager;

    private static LoggingOutputStream los;

    public static PrintWriter out = null;

    public static BufferedReader in = null;

    public static ArrayList ports = new ArrayList();

    private static boolean debug = false;

    MainServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            out = null;
            in = null;
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + port + ".");
            }
            System.out.println("New player Socket connection");
            String inputLine, outputLine;
            imp = new MainServerProtocol();
            outputLine = imp.processInput(null);
            out.println(outputLine);
            while ((inputLine = in.readLine()) != null) {
                outputLine = imp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Me?")) {
                    kill = true;
                    break;
                }
                if (outputLine.equals("Bye.")) {
                    break;
                }
            }
            out.close();
            in.close();
            try {
                this.finalize();
            } catch (Throwable ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setLogger() {
        try {
            Handler fileHandler = new FileHandler("MainServer-LOG", 300000, 10, true);
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fileHandler);
            PrintStream stdout = System.out;
            PrintStream stderr = System.err;
            LoggingOutputStream los;
            log = Logger.getLogger("stdout");
            if (debug == false) {
                los = new LoggingOutputStream(log, StdOutErrLevel.STDOUT);
                System.setOut(new PrintStream(los, true));
            }
            log = Logger.getLogger("stderr");
            if (debug == false) {
                los = new LoggingOutputStream(log, StdOutErrLevel.STDERR);
                System.setErr(new PrintStream(los, true));
            }
            log = Logger.getLogger("test");
            log = Logger.getLogger("test");
            log.info("");
            log.info("");
            log.info("------------------------------------------------------------------");
            log.info("------------------------------------------------------------------");
            log.info("ImajieMatch Main Server | The ultimate tool for GEO MMORPG Game");
            log.info("Copyright Carl Tremblay and imajie.tv 1990-2011");
            log.info("------------------------------------------------------------------");
            log.info("");
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        setLogger();
        for (int i = 0; i < Constants.MAX_PLAYER_NUMBER; i++) {
            Ports p = new Ports("", Constants.MAIN_SERVER_PORT + 1 + i);
            ports.add(p);
            System.out.println("Creating port:" + p.toString());
        }
        System.out.println("ImajieMatch Server Started");
        MainListener.listenSocket();
    }

    public static void timeout(String username) {
    }
}

class MainListener {

    public static ServerSocket server = null;

    MainListener() {
    }

    public static void listenSocket() {
        try {
            server = new ServerSocket(4000);
            System.out.println("ImajieMatch Server Listening");
        } catch (IOException e) {
            System.out.println("Could not listen on port 4000");
        }
        while (true) {
            MainServer w;
            try {
                w = new MainServer(server.accept());
                Thread t = new Thread(w);
                t.start();
            } catch (IOException e) {
                System.out.println("Accept failed: 4000");
                System.exit(-1);
            }
        }
    }

    protected void finalize() {
        try {
            server.close();
        } catch (IOException e) {
            System.out.println("Could not close socket");
            System.exit(-1);
        }
    }
}

class Ports {

    private String username;

    private int port;

    public Ports() {
    }

    public Ports(String username, int port) {
        this.username = username;
        this.port = port;
    }

    /**
     * @return Returns the socket port.
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port The socket port to set.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "## Username : " + this.username + ", Port : " + this.port;
    }
}
