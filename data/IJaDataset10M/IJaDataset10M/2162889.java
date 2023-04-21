package de.nec.nle.remote.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import de.nec.nle.remote.ConfigParser;
import de.nec.nle.remote.Remote;

public class CommHandler implements Runnable {

    Logger logger = Logger.getLogger(this.getClass());

    private Socket sock;

    private SocketAddress server;

    private BufferedReader responseReceiver;

    private PrintWriter commandSender;

    private Remote controller;

    public CommHandler(Remote controller, ConfigParser cp) throws UnknownHostException {
        this.server = cp.getServerAddr();
        this.controller = controller;
    }

    public void run() {
        while (!controller.isEnded()) {
            if (sock == null || !sock.isConnected() || sock.isClosed()) {
                try {
                    connectSocket();
                } catch (IOException e) {
                    logger.debug("Couldn't connect to the server.");
                }
                doWait(1000);
            } else {
                logger.debug("Connection's still up");
                doWait(5000);
            }
        }
    }

    public synchronized void doWait(int millis) {
        try {
            wait(millis);
        } catch (InterruptedException e) {
            logger.trace("Sleep interrupted");
        }
    }

    private void connectSocket() throws IOException {
        logger.debug("Connecting socket");
        if (sock != null && !sock.isClosed()) {
            sock.close();
        }
        sock = new Socket();
        sock.connect(server);
        responseReceiver = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        commandSender = new PrintWriter(sock.getOutputStream());
        logger.info("Connected to the server");
        controller.updateConnectionStatus();
    }

    public final synchronized String doCommand(String command) throws IOException {
        if (sock != null && !sock.isConnected() || commandSender == null || responseReceiver == null) {
            logger.info("The source was Not connected to the server. " + "Attempting to connect now...");
            connectSocket();
        }
        logger.info("Sending command: " + command);
        commandSender.println(command);
        if (commandSender.checkError()) {
            logger.error("Error writing to server");
            sock.close();
            return null;
        }
        boolean remoteSideClosed = false;
        String response = null;
        try {
            logger.debug(sock.getSoTimeout());
            response = responseReceiver.readLine();
        } catch (SocketTimeoutException e) {
            logger.warn("The remote side took too long to answer");
            remoteSideClosed = true;
        }
        if (remoteSideClosed || response == null) {
            logger.warn("Server side ended.");
            sock.close();
            return null;
        } else {
            return response;
        }
    }

    public synchronized boolean isConnected() {
        if (!sock.isConnected() || sock.isClosed()) {
            return false;
        } else {
            int oldTimeout = 2000;
            try {
                oldTimeout = sock.getSoTimeout();
                sock.setSoTimeout(1);
                InputStream is;
                is = sock.getInputStream();
                if (is.read() == -1) {
                    sock.close();
                    return false;
                }
                logger.debug("Restoring timeout to" + oldTimeout);
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!sock.isClosed()) {
                        sock.setSoTimeout(oldTimeout);
                    }
                } catch (SocketException e) {
                    logger.warn("Couldn't restore socket timeout.");
                }
            }
            return true;
        }
    }
}
