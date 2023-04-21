package edu.arizona.arid.akshen;

import java.io.IOException;
import edu.arizona.arid.akshen.entity.Message;

/**
 * 
 * @author Sumin Byeon
 *
 */
public interface ServerDelegate {

    public void messageReceived(Client from, Message message);

    public void sendMessage(Client client, Message message) throws IOException, Exception;

    public void exceptionRaised(Exception e);

    public void disconnected(Client client);
}
