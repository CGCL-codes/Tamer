package com.teletalk.jserver.net.sns.server;

import com.teletalk.jserver.net.sns.Server;
import com.teletalk.jserver.net.sns.client.SnsClient;
import com.teletalk.jserver.tcp.messaging.Destination;
import com.teletalk.jserver.util.MessageQueueThread;

/**
 * The representation of a remote SNS client, used by SnsManager.
 * 
 * @author Tobias L�fstrand
 * 
 * @since 2.1 (20050502)
 */
class SnsClientDestination {

    private final Server server;

    private Destination destination;

    private SnsClient snsClient;

    private MessageQueueThread sequentialClientRemoteCallThread;

    /**
    * Creates a new RemoteSnsClient. 
    */
    public SnsClientDestination(final String uniqueServerName) {
        this.server = new Server(uniqueServerName);
        this.sequentialClientRemoteCallThread = new MessageQueueThread(uniqueServerName + ".SequentialClientRemoteCallThread");
    }

    /**
    * Destroys this SnsClientDestination.
    */
    public void destroy() {
        try {
            this.sequentialClientRemoteCallThread.destroy();
        } catch (Exception e) {
        }
        this.sequentialClientRemoteCallThread = null;
    }

    /**
    * Gets the associated {@link Server} object.
    */
    public Server getServer() {
        return server;
    }

    /**
    * Gets the associated {@link Destination} object.
    */
    public Destination getDestination() {
        return destination;
    }

    /**
    * Sets the associated {@link Destination} object.
    */
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    /**
    * Gets the associated {@link SnsClient} interface for RPC communication with the remote SNS client.
    */
    public SnsClient getSnsClient() {
        return snsClient;
    }

    /**
    * Sets the associated {@link SnsClient} interface for RPC communication with the remote SNS client.
    */
    public void setSnsClient(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    /**
    * Gets the sequential client remote call thread of this SnsClientDestination.
    */
    public MessageQueueThread getSequentialClientRemoteCallThread() {
        return sequentialClientRemoteCallThread;
    }

    /**
    * Gets the name of the server
    */
    public String getName() {
        return server.getName();
    }

    /**
    * Gets the time when the SNS lost the link to the server.
    */
    public long getLinkLostTime() {
        return server.getLinkLostTime();
    }

    /**
    * Sets the time when the SNS lost the link to the server.
    */
    public void setLinkLost(long linkLostTime) {
        this.destination = null;
        this.snsClient = null;
        server.setLinkLostTime(linkLostTime);
    }

    /**
    * Checks if the SNS has lost the link to the server.
    */
    public boolean isLinkLost() {
        return server.isLinkLost();
    }

    /**
    * Gets the address of the sns client of this server.
    */
    public String getSnsClientAddress() {
        return server.getSnsClientAddress();
    }
}
