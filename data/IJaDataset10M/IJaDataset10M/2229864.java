package common;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class MessageReader implements Runnable {

    private Socket connection;

    private MessageReceiver receiver;

    private boolean running;

    public MessageReader(MessageReceiver par, Socket conn) {
        Thread reader = new Thread(this);
        connection = conn;
        receiver = par;
        running = true;
        reader.start();
    }

    public void run() {
        while (running) {
            try {
                ObjectInputStream objIn = new ObjectInputStream(connection.getInputStream());
                Message msg = (Message) objIn.readObject();
                receiver.receiveMessage(msg, connection);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                running = false;
            }
        }
    }

    public void stop() {
        running = false;
    }
}
