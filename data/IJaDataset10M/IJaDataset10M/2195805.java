package btp2p;

/**
 * This interface is atached on the client to inform for synchronization reasons the peer about in which state the client is.
 * Nothing further use of this interface is in need;
 *@author  Tenentes Vassilis
 */
public interface ClientListener {

    public void clientConnectionsComplete();

    public void clientSearchComplete();

    public void clientBTInitializationFinished();
}
