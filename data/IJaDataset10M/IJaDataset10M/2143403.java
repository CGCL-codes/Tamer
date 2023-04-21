package nzdis.agent.soap;

import java.util.EventListener;

/**
 * Interface that describes the SOAP JMS listener for the asynchronous communication pattern (client-side)
 * 
 * @author julien
 *
 */
public interface SoapJMSListener extends EventListener {

    /**
     * Method that is used by the server to reply to the client
     * 
     * @param data reply of the server
     */
    public void onResult(String data);

    public void stop();

    public void onException(Exception e);
}
