package royere.cwi.framework.edit;

import royere.cwi.framework.RoyereMessageHandler;

/**
 * Royere message handler for SelectionMessage.
 *
 * @see SelectionMessage
 * @see RoyereMessageHandler
 * @author yugen
 */
public interface SelectionMessageHandler extends RoyereMessageHandler {

    public void handleSelectionMessage(SelectionMessage m);
}
