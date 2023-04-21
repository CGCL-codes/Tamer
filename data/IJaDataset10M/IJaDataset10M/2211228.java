package com.db4o.f1.chapter5;

import com.db4o.*;
import com.db4o.messaging.*;

/**
 * starts a db4o server with the settings from {@link ServerConfiguration}. 
 * <br><br>This is a typical setup for a long running server.
 * <br><br>The Server may be stopped from a remote location by running
 * StopServer. The StartServer instance is used as a MessageRecipient and
 * reacts to receiving an instance of a StopServer object.
 * <br><br>Note that all user classes need to be present on the server
 * side and that all possible Db4o.configure() calls to alter the db4o
 * configuration need to be executed on the client and on the server.
 */
public class StartServer implements ServerConfiguration, MessageRecipient {

    /**
   * setting the value to true denotes that the server should be closed
   */
    private boolean stop = false;

    /**
   * starts a db4o server using the configuration from
   * {@link ServerConfiguration}.
   */
    public static void main(String[] arguments) {
        new StartServer().runServer();
    }

    /**
   * opens the ObjectServer, and waits forever until close() is called
   * or a StopServer message is being received.
   */
    public void runServer() {
        synchronized (this) {
            ObjectServer db4oServer = Db4o.openServer(FILE, PORT);
            db4oServer.grantAccess(USER, PASS);
            db4oServer.ext().configure().setMessageRecipient(this);
            Thread.currentThread().setName(this.getClass().getName());
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            try {
                if (!stop) {
                    this.wait(Long.MAX_VALUE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            db4oServer.close();
        }
    }

    /**
   * messaging callback
   * @see com.db4o.messaging.MessageRecipient#processMessage(ObjectContainer, Object)
   */
    public void processMessage(ObjectContainer con, Object message) {
        if (message instanceof StopServer) {
            close();
        }
    }

    /**
   * closes this server.
   */
    public void close() {
        synchronized (this) {
            stop = true;
            this.notify();
        }
    }
}
