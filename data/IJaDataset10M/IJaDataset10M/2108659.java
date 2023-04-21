package btp2p;

/**
 *This interface can be implemented by anyone who wants to be informed by the message that come to a peer.
 *It can be added to the peer.
 *@author  Tenentes Vassilis
 */
public interface MessageListener {

    public void NetworkConnected(final int handlerId);

    public void NetworkDisconnected(final int handlerId);

    /**
     *@param m the message that arrived
     *@param handlerId who was that handler that gave this message
     *@return MessageHandler.CONSUME in order this message not to be sent to other listeners or MessageHandler.NORMAL to be 
     * sent generally user applications should return NORMAL which is false by the way
     */
    public boolean MessageArrived(Message m, final int handlerId);
}
