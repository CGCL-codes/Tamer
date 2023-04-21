package org.skycastle.kernel.impl.local;

import org.skycastle.kernel.EntityId;
import org.skycastle.kernel.messaging.Message;
import org.skycastle.kernel.messaging.MessageHandler;

/**
 * @author Hans Haggstrom
 */
final class ExternalMessageHandler implements MessageHandler {

    private final LocalEntityContainer myLocalContainer;

    public ExternalMessageHandler(final LocalEntityContainer localContainer) {
        myLocalContainer = localContainer;
    }

    public void onMessage(final EntityId sender, final EntityId target, final Message message) {
        myLocalContainer.sendMessage(sender, target, message);
    }
}
