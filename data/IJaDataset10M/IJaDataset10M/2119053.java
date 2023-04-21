package com.csam.jwebsockets.server;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread deques messages from Server to the various client threads
 *
 * @author Nathan Crause <nathan at crause.name>
 * @version 1.0
 */
public class DequeThread extends Thread {

    private Server server;

    private boolean running = false;

    public DequeThread(Server server) {
        super("JWebSockets Deque");
        this.server = server;
        setPriority(MIN_PRIORITY);
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            while (!server.messages.isEmpty()) {
                String message = server.messages.poll();
                for (Client client : server.clients) {
                    client.messages.add(message);
                }
            }
            try {
                synchronized (this) {
                    this.wait(100);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(DequeThread.class.getName()).log(Level.WARNING, "Deque wait interrupted", ex);
            }
        }
        running = false;
    }

    public void terminate() {
        running = false;
    }
}
