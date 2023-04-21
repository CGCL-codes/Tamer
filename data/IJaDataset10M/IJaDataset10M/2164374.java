package ants.p2p;

import org.apache.log4j.*;

public class SenderThread extends Thread {

    Message m;

    Neighbour n;

    public static int queryQueueLimit = 30;

    public static int queueLimit = 100;

    public static final int waitTime = 8;

    static Logger _logger = Logger.getLogger(SenderThread.class.getName());

    private boolean send = true;

    public SenderThread(Message m, Neighbour n) {
        this.m = m;
        this.n = n;
        this.setPriority(10);
    }

    public void run() {
        try {
            if (n.getQueuedMessages() > SenderThread.queryQueueLimit && m instanceof ants.p2p.query.QueryMessage && (m.getType() == 2 || m.getType() == 0)) {
                _logger.debug(n.getIdent() + " Neighbour stuck [too many queued messages: " + n.getQueuedMessages() + "]. Dircarding query...");
                return;
            } else if (n.getQueuedMessages() > SenderThread.queueLimit) {
                _logger.debug(n.getIdent() + " Neighbour stuck [too many queued messages: " + n.getQueuedMessages() + "]. Dircarding message...");
                return;
            }
            n.incQueuedMessages();
            n.send(m);
            n.decQueuedMessages();
        } catch (Exception e) {
            _logger.error("Error in sending packet through Socket", e);
            n.decQueuedMessages();
        }
    }

    public void interruptSending() {
        this.send = false;
    }

    public boolean mustSend() {
        return this.send;
    }
}
