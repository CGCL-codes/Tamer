package org.ceno.communication.srv;

/**
 * To get notified when servers are started od shutted down.
 * 
 * @author Andre Albert &lt;andre.albert82@googlemail.com&gt
 * @created 06.11.2010
 * @since 0.0.7
 */
public interface ICommunicatorServiceListener {

    void serverStarted(String identifier);

    void serverStopped(String identifier);
}
