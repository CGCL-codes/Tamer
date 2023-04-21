package org.skycastle.old.messaging.modifications.action;

import org.skycastle.core.old.GameObject;
import org.skycastle.core.old.GameObjectId;
import org.skycastle.old.messaging.modifications.AbstractModificationMessage;

/**
 * A {@link org.skycastle.old.messaging.Message} to stop a specified action that a {@link org.skycastle.core.old.GameObject} has
 * previously started or scheduled.
 *
 * @author Hans H�ggstr�m
 */
public final class StopActionMessage extends AbstractModificationMessage {

    private final long myActionId;

    public StopActionMessage(final GameObjectId senderId, final GameObjectId targetId, final long actionId) {
        super(senderId, targetId);
        myActionId = actionId;
    }

    public void applyModificationToTarget(final GameObject target) {
        throw new UnsupportedOperationException("This method has not yet been implemented.");
    }
}
