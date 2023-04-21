package common.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import messages.Message;
import org.apache.log4j.Logger;

/**
 * This class is responsible for sending the messages over the network. When
 * sendMsg() is called a message is appended to the private message queue. This
 * message queue is polled in a looping thread, blocking until a new message
 * becomes available.
 * 
 * @author Steffen, Daniel 
 * TODO remove "implements Observer and corresponding methods"
 */
public abstract class MsgSender extends Thread implements Observer {

    private static final Logger logger = Logger.getLogger(MsgSender.class);

    BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<Message>();

    protected ObjectOutputStream out;

    /**
	 * Creates a new MsgSender.
	 * 
	 * @param socket
	 *            TODO it may be better to throw the exception, so the client
	 *            can be notified
	 */
    public MsgSender(Socket socket) {
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.fatal("Couldn't create ObjectOutputStream!", e);
        }
    }

    /**
	 * Takes messages out of the message queue and sends them over the network.
	 */
    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                try {
                    Message msg = msgQueue.take();
                    out.writeObject(msg);
                    out.flush();
                } catch (InterruptedException e) {
                    logger.warn("Take() was interrupted!", e);
                }
            }
        } catch (IOException e) {
            logger.info("Lost Connection");
        }
    }

    /**
	 * Enqueues a message to be sent over the network.
	 * 
	 * @param msg
	 */
    public void sendMsg(Message msg) {
        msgQueue.add(msg);
    }

    /**
	 * This method is called when an observerable this object is observing is
	 * updated.
	 */
    @Override
    public abstract void update(Observable observable, Object obj);
}
