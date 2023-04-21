package uk.co.gidley.teamAlert.listenerServices;

import org.jivesoftware.smack.packet.Message;
import uk.co.gidley.teamAlert.vo.Alert;

/**
 * Created by IntelliJ IDEA. User: ben Date: Jun 17, 2008 Time: 7:04:51 AM
 */
public interface MessageDecoder {

    public Alert decodeMessage(Message message) throws UnableToDecodeException;
}
