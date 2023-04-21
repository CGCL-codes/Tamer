package sms;

import java.util.ArrayList;

/**
 * The Simple Messaging System
 * <p>
 * This system allows for the communication of components in a program when the
 * reciever of the message is neccesarily visible. The components only need a
 * handle to an instance of this class to be able to send messages or register
 * message handlers.
 * 
 * @author Matthew Korzeniowski
 * @version 1.2.5
 */
public class MessagingSystem implements Runnable {

    /**
   * Translation table item
   * 
   * @author Matthew Korzeniowski
   * @version 1.0
   */
    private class MessageTypeToHandler {

        public MessageHandler handler;

        public MessageType type;

        public MessageTypeToHandler(MessageHandler handler, MessageType type) {
            this.handler = handler;
            this.type = type;
        }
    }

    private ArrayList<MessageHandler> dump;

    private MessagePump pump;

    private ArrayList<MessageTypeToHandler> regisrar;

    private boolean run;

    /**
   * Creates new MessageSystem
   */
    public MessagingSystem() {
        pump = new MessagePump();
        regisrar = new ArrayList<MessageTypeToHandler>();
        dump = new ArrayList<MessageHandler>();
        run = true;
    }

    /**
   * Tells MessaginSystem to send a copy of every message to this handler
   * 
   * @since 1.1.04
   * @param handler The handler to send the messages to
   */
    public synchronized void registerDumpHandler(MessageHandler handler) {
        dump.add(handler);
    }

    /**
   * Tells MessagingSystem where to send messages of a certain type
   * 
   * @param handler The place to send the message to
   * @param type The type of message to send
   */
    public synchronized void registerHandler(MessageHandler handler, MessageType type) {
        regisrar.add(new MessageTypeToHandler(handler, type));
    }

    /**
   * Automatically runs the update() method.
   * 
   * @see java.lang.Runnable#run()
   */
    @Override
    public void run() {
        run = true;
        while (run) {
            update();
            Thread.yield();
        }
    }

    /**
   * Creates a new thread to run the update() method automatically. This returns
   * immediately.
   * 
   * @see MessagingSystem#run()
   * @since 1.1.04
   * @return A handle to the new Thread
   */
    public Thread runBackground() {
        Thread me = new Thread(this);
        me.start();
        return me;
    }

    /**
   * Stops the messaging system if it's running
   * @since 1.2.5
   */
    public void stop() {
    }

    /**
   * Places a message into the MessagePump
   * 
   * @param msg The message to send
   */
    public synchronized void sendMessage(Message msg) {
        pump.pushMessage(msg);
    }

    /**
   * Handles the next message from MessagePump
   */
    public synchronized void update() {
        Message toHandle = pump.popMessage();
        if (toHandle == null) {
            return;
        }
        for (MessageTypeToHandler tth : regisrar) {
            if (tth.type.getName().equals(toHandle.getType().getName())) {
                tth.handler.onReceivedMessage(toHandle);
            }
        }
        for (MessageHandler handler : dump) {
            handler.onReceivedMessage(toHandle);
        }
    }
}
