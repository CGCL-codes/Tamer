package org.npsnet.v.servers;

import java.io.*;
import java.net.*;
import org.npsnet.v.kernel.Module;
import org.npsnet.v.kernel.ModuleDestructionEvent;
import org.npsnet.v.kernel.ModuleEvent;
import org.npsnet.v.kernel.ModuleEventListener;
import org.npsnet.v.kernel.ModuleReplacementEvent;
import org.npsnet.v.properties.console.Console;

/**
 * A connection to the remote console server.
 *
 * @author Andrzej Kapolka
 */
public class RemoteConsoleServerConnection extends TelnetServerConnection implements Runnable {

    /**
     * The remote console server that created this connection.
     */
    private RemoteConsoleServer remoteConsoleServer;

    /**
     * The remote console.
     */
    private Console remoteConsole;

    /**
     * The remote console listener.
     */
    private ModuleEventListener remoteConsoleListener;

    /**
     * Constructor.
     *
     * @param pSocket the connected socket
     * @param pServer the server module that created the connection
     */
    public RemoteConsoleServerConnection(Socket pSocket, RemoteConsoleServer pServer) {
        super(pSocket, pServer);
        remoteConsoleServer = pServer;
        remoteConsoleListener = new ModuleEventListener() {

            public void moduleEventFired(ModuleEvent me) {
                if (me instanceof ModuleReplacementEvent) {
                    remoteConsole = (Console) ((ModuleReplacementEvent) me).getReplacementModule();
                } else {
                    remoteConsole = null;
                    close();
                }
            }
        };
        Thread consoleThread = new Thread(this);
        consoleThread.start();
    }

    /**
     * Closes the connection.
     */
    public void close() {
        Module consoleModule = (Module) remoteConsole;
        if (consoleModule != null) {
            consoleModule.getContainer().deregisterModule(consoleModule);
        }
        super.close();
    }

    /**
     * The console thread execution method.
     */
    public void run() {
        try {
            outputStream.println();
            outputStream.println("NPSNET-V Remote Console Server v" + server.getClassDescriptor().getImplementationVersion() + " (" + InetAddress.getLocalHost().getHostName() + ")");
            outputStream.println();
            if (remoteConsoleServer.getPassword() != null) {
                boolean correctPasswordEntered = false;
                for (int i = 0; i < 3 && !correctPasswordEntered; i++) {
                    outputStream.print("Password: ");
                    String entry = inputStream.readLine(false);
                    if (remoteConsoleServer.getPassword().equals(entry)) {
                        correctPasswordEntered = true;
                    }
                    outputStream.println();
                }
                if (!correctPasswordEntered) {
                    close();
                    return;
                }
            }
            remoteConsole = remoteConsoleServer.newConsole(inputStream, outputStream);
            ((Module) remoteConsole).addEventListener(ModuleDestructionEvent.class, remoteConsoleListener);
            ((Module) remoteConsole).addEventListener(ModuleReplacementEvent.class, remoteConsoleListener);
        } catch (IOException ioe) {
            close();
        }
    }
}
