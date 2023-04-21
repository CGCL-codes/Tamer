package org.tanso.ts.mts;

import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This is one implementation for Blocked MTS.
 * 
 * @author Haiping Huang
 */
public class BlockedMTSImpl extends MTSBase implements BlockedMTS {

    /**
	 * Message queue for containing messages
	 */
    private List<MTSMessage> messageQueue = new ArrayList<MTSMessage>();

    /**
	 * Protected constructor for blocked MTS impl.
	 */
    protected BlockedMTSImpl() {
    }

    @Override
    protected void onRecvTCP(Socket clientSocket) {
        MTSMessage newMsg = super.recvTCP(clientSocket);
        if (newMsg != null) {
            notifyReceiver(newMsg);
        }
    }

    @Override
    protected void onRecvUDP(DatagramPacket ip) {
        MTSMessage newMsg = super.recvUDP(ip);
        if (newMsg != null) {
            notifyReceiver(newMsg);
        }
    }

    private void notifyReceiver(MTSMessage msg) {
        synchronized (messageQueue) {
            messageQueue.add(msg);
            messageQueue.notify();
        }
    }

    public MTSMessage recvMessage(int blockTime) {
        synchronized (messageQueue) {
            while (0 == this.messageQueue.size()) {
                try {
                    messageQueue.wait(blockTime);
                    if (0 == this.messageQueue.size()) {
                        return null;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            MTSMessage newMsg = messageQueue.get(0);
            messageQueue.remove(0);
            return newMsg;
        }
    }
}
