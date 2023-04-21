package pl.bristleback.server.bristle.message.response;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.action.ActionMessage;
import pl.bristleback.server.bristle.api.action.Response;
import pl.bristleback.server.bristle.message.sender.ActionSenderUtil;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-31 21:50:26 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class VoidResponse implements Response {

    private static Logger log = Logger.getLogger(VoidResponse.class.getName());

    public static final Response RESPONSE = new VoidResponse();

    private VoidResponse() {
    }

    @Override
    public ActionMessage getMessage() {
        return null;
    }

    @Override
    public void chooseRecipients(WebsocketConnector sender) {
    }

    @Override
    public void sendResponse(ActionSenderUtil actionSenderUtil) throws Exception {
    }
}
