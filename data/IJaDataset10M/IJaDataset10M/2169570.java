package org.fgraph.relay;

/**
 *
 *  @version   $Revision: 562 $
 *  @author    Paul Speed
 */
public interface MessageListener<S> {

    public void newMessage(MessageBroker<S> broker, long index, Message<S> m);
}
