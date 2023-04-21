package engine.server.senders.ServerClient;

import engine.QueueIF;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class ServerClientQueue implements QueueIF {

    private static ServerClientQueue _instance = null;

    private static Logger log = Logger.getLogger(ServerClientQueue.class.getName());

    private boolean available = false;

    ChannelBuffer bufferedMessage = ChannelBuffers.dynamicBuffer(1);

    private ServerClientQueue() {
    }

    public static synchronized ServerClientQueue getInstance() {
        if (_instance == null) {
            _instance = new ServerClientQueue();
        }
        return _instance;
    }

    public synchronized ChannelBuffer get() {
        while (available == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        available = false;
        notifyAll();
        ChannelBuffer b = bufferedMessage.copy();
        bufferedMessage.clear();
        return b;
    }

    public synchronized void put(byte[] input) {
        while (available == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        bufferedMessage.writeBytes(input);
        available = true;
        notifyAll();
    }

    public synchronized void put(ChannelBuffer input) {
        while (available == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        bufferedMessage.writeBytes(input);
        available = true;
        notifyAll();
    }
}
