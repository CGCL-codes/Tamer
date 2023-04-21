package pl.sind.blip;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.sind.blip.BlipConnector;
import pl.sind.blip.BlipConnectorException;
import pl.sind.blip.entities.DirectMessage;

@RunWith(JUnit4.class)
public class BlipConnectorDirectedMessagesTest extends BaseBlipTestCase {

    @Test
    public void testCreateDirectedMessageUpdateAndDelete() throws BlipConnectorException {
        BlipConnector bc = newConnector();
        DirectMessage ret = bc.createDirectedMessage("API test", "blip");
        bc.deleteDirectedMessage(ret.getId().longValue());
        System.out.println(ret);
    }

    @Test
    public void testGetDirectedMessages() throws BlipConnectorException {
        BlipConnector bc = newConnector();
        List<DirectMessage> res = bc.getDirectedMessages(10, 10, 0, 0);
        System.out.println(res);
    }

    @Test
    public void testGetAllDirectedMessages() throws BlipConnectorException {
        BlipConnector bc = newConnector();
        List<DirectMessage> res = bc.getAllDirectedMessages(10, 10, 0, 0);
        System.out.println(res);
    }

    @Test
    public void testGetUserDirectedMessages() throws BlipConnectorException {
        BlipConnector bc = newConnector();
        List<DirectMessage> res = bc.getUserDirectedMessages(10, 10, 0, 0, "wooda");
        System.out.println(res);
    }
}
