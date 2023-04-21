package pl.bristleback.server.bristle.message.sender;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import java.util.Arrays;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-08-23 17:45:30 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component(PlainObjectSender.SENDER_NAME)
public class PlainObjectSender extends AbstractObjectSender<List<WebsocketConnector>> {

    private static Logger log = Logger.getLogger(PlainObjectSender.class.getName());

    public static final String SENDER_NAME = "system.sender.plain";

    public void sendMessage(WebsocketMessage message, List<WebsocketConnector> connectors) {
        message.setRecipients(connectors);
        getMessageDispatcher().addMessage(message);
    }

    public void sendMessage(WebsocketMessage message, WebsocketConnector... connectors) {
        message.setRecipients(Arrays.asList(connectors));
        getMessageDispatcher().addMessage(message);
    }
}
