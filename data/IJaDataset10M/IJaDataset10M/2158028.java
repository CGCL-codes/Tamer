package grammarbrowser.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Message firer (=implementation)
 * 
 * @author Bernard Bou
 *
 */
public class MessageFirer implements IMessageFirer {

    /**
     * Listeners
     */
    private List<IMessageListener> theListeners = new ArrayList<IMessageListener>();

    @Override
    public void addListener(IMessageListener thisListener) {
        theListeners.add(thisListener);
    }

    @Override
    public void removeListener(IMessageListener thisListener) {
        theListeners.remove(thisListener);
    }

    /**
     * Fire message
     * @param thisMessage
     */
    public void fireMessage(Message thisMessage) {
        for (IMessageListener thisListener : theListeners) thisListener.notified(thisMessage);
    }
}
