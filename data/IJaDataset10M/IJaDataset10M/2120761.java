package ch.ethz.dcg.spamato.stats.server.process.handler.collaborative.earlgrey.common.msg;

import java.net.URL;
import java.util.Vector;
import ch.ethz.dcg.spamato.base.common.util.msg.Message;
import ch.ethz.dcg.spamato.base.common.util.msg.data.VectorObject;

public abstract class MessageFactory {

    private static Message createUsernameMessage(String msgType, String username) {
        Message msg = new Message(msgType);
        msg.setUsername(username);
        return msg;
    }

    public static Message createCheckMessage(String username, Vector<URL> urls) {
        Message msg = createUsernameMessage(MessageConstants.Client.CHECK, username);
        msg.setData(new VectorObject(urls));
        return msg;
    }
}
