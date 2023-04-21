package com.ibm.tuningfork.core.sharing;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import com.ibm.tuningfork.core.dialog.ErrorHandling;
import com.ibm.tuningfork.infra.Logging;

public class InitiationRequestReceiver extends SharingRequestReceiver {

    protected final String host;

    protected final int port;

    private ServerSocket serverSocket;

    protected int exceptionLimit() {
        return 1000;
    }

    public String toString() {
        return super.toString() + " " + getClass() + " " + host + ":" + port;
    }

    protected InitiationRequestReceiver(String host, int port) {
        super("request spawning receiver");
        log("new " + host + ":" + port);
        this.host = host;
        this.port = port;
    }

    protected boolean startProcessingRequests() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            return true;
        } catch (BindException be) {
            Logging.msgln("Unable to bind socket for sharing: " + be.getMessage());
            return false;
        }
    }

    protected void processARequest() throws Exception {
        asynchronouslyRespondOn(serverSocket.accept());
    }

    public void shutDown() {
        super.shutDown();
        try {
            serverSocket.close();
        } catch (Exception e) {
        }
    }

    public String whatIAmListeningTo() {
        return "port: " + port;
    }

    public int getPort() {
        return port;
    }

    private void asynchronouslyRespondOn(final Socket s) throws Exception {
        (new Thread() {

            public void run() {
                try {
                    SharingConduit.createForRespondingToInitiationRequest(s).respondToInitiationRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                    ErrorHandling.displayWarningFromAnyThread("Could not receive shared figure", e.getMessage(), false);
                }
            }
        }).start();
    }
}
