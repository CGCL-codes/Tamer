package org.beepcore.beep.lib;

import org.beepcore.beep.core.*;

/**
 * This class acts as a sink for all replies to the <code>sendMSG()</code>.
 * The reply is received by <code>NullReplyListener</code>, freed from the
 * channel's receive buffers and discarded.
 *
 * @author Eric Dixon
 * @author Huston Franklin
 * @author Jay Kint
 * @author Scott Pead
 * @version $Revision: 1.4 $, $Date: 2001/11/08 05:51:34 $
 */
public class NullReplyListener implements ReplyListener {

    private static NullReplyListener listener = new NullReplyListener();

    private NullReplyListener() {
    }

    public static NullReplyListener getListener() {
        return listener;
    }

    public void receiveRPY(Message message) {
        message.getDataStream().close();
    }

    public void receiveERR(Message message) {
        message.getDataStream().close();
    }

    public void receiveANS(Message message) {
        message.getDataStream().close();
    }

    public void receiveNUL(Message message) {
    }
}
