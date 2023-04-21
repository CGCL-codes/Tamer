package belvedere.net;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import belvedere.util.ExceptionEvent;
import belvedere.util.ExceptionListener;
import belvedere.util.Message;
import belvedere.util.MessageEvent;
import belvedere.util.MessageListener;

/**
 * Encapsulates the functionality of a socket running in a thread.
 *
 * @author Nathan Dwyer
 * @author David J. Burger
 */
public abstract class SocketThread extends Thread {

    /**
   * Flag that indicates termination.
   */
    private boolean done = false;

    /**
   * The actual socket.
   */
    protected Socket socket = null;

    /**
   * Output stream used to write to socket.
   */
    protected ObjectOutputStream out = null;

    /**
   * Input stream used to read from socket.
   */
    protected ObjectInputStream in = null;

    /**
   * Listeners of <code>MessageEvent</code>s dispatched from the read thread.
   */
    protected ArrayList messageListeners = null;

    /**
   * Listeners of <code>ExceptionEvent</code>s in the read thread.
   */
    protected ArrayList exceptionListeners = null;

    /**
   * Basic constructor.
   */
    protected SocketThread() {
        super("SocketThread");
        this.messageListeners = new ArrayList();
        this.exceptionListeners = new ArrayList();
    }

    /**
   * Adds a listener to the list of message listeners.
   */
    public void addMessageListener(MessageListener ml) {
        this.messageListeners.add(ml);
    }

    /**
   * Removes a listener from the list of message listeners.
   */
    public void removeMessageListener(MessageListener ml) {
        this.messageListeners.remove(ml);
    }

    /**
   * Adds a listener to the list of exception listeners.
   */
    public void addExceptionListener(ExceptionListener el) {
        this.exceptionListeners.add(el);
    }

    /**
   * Removes a listener from the list of exception listeners.
   */
    public void removeExceptionListener(ExceptionListener el) {
        this.exceptionListeners.remove(el);
    }

    /**
   * Notifies all message listeners of the given message event.
   */
    protected void notifyMessageListeners(MessageEvent event) {
        for (Iterator i = this.messageListeners.iterator(); i.hasNext(); ) {
            MessageListener ml = (MessageListener) i.next();
            ml.messageReceived(event);
        }
    }

    /**
   * Notifies all exception listeners of the given exception event.
   */
    protected void notifyExceptionListeners(ExceptionEvent event) {
        for (Iterator i = this.exceptionListeners.iterator(); i.hasNext(); ) {
            ExceptionListener el = (ExceptionListener) i.next();
            el.exceptionThrown(event);
        }
    }

    /**
   * Send a message out to the socket.
   *
   * @param message   message to be sent on this socket
   */
    public void send(Message message) throws IOException {
        this.out.writeObject(message);
    }

    /**
   * Shuts down the connection including streams, socket, and thread.
   */
    public void terminate() throws IOException {
        if (this.done) return;
        this.done = true;
        this.out.close();
        this.in.close();
        this.socket.close();
    }

    /**
   * Returns whether or not this <code>SocketThread</code> is still running.
   *
   * @return  true if running, otherwise false
   */
    public boolean isRunning() {
        return this.done == false;
    }

    /**
   * Implementation of <code>Thread</code>'s run that listens on the socket
   * and dispatches received messages.
   */
    public void run() {
        while (!this.done) {
            try {
                notifyMessageListeners(new MessageEvent(this, (Message) this.in.readObject()));
            } catch (Exception e) {
                if (this.done) break;
                notifyExceptionListeners(new ExceptionEvent(this, e));
            }
        }
    }
}
