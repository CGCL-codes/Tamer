package net.sf.opendf.hades.des;

/**
*   MessageListeners are the targets of message events and keep a record of the message 
*   producers they are connected to, thus allowing for collective disconnection.
*
*   @see    MessageProducer
*   @see    MessageEvent
*/
public interface MessageListener extends java.util.EventListener, java.io.Serializable {

    public void notifyAddProducer(MessageProducer mp);

    public void notifyRemoveProducer(MessageProducer mp);

    public void disconnect();

    public void message(Object msg, double time, Object source);

    public void notifyControl(Object ce, Object source);

    public void control(Object ce, Object source);
}
