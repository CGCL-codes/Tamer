package org.skycastle.old.messaging.modifications;

import org.skycastle.core.old.GameObject;
import org.skycastle.core.old.GameObjectId;
import org.skycastle.old.messaging.Message;
import org.skycastle.util.parameters.metadata.ParameterValidationException;

/**
 * Removes a property from the target {@link org.skycastle.core.old.GameObject}.
 *
 * @author Hans Häggström
 */
public final class RemovePropertyMessage extends MemberModificationMessage {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@link org.skycastle.old.messaging.modifications.RemovePropertyMessage}.
     *
     * @param senderId           the id of the {@link GameObject} that sent this {@link Message}.
     * @param targetId           the id of the  {@link GameObject} that this message attempts to modify.
     * @param propertyIdentifier the identifier of the property to remove.
     */
    public RemovePropertyMessage(final GameObjectId senderId, final GameObjectId targetId, final String propertyIdentifier) {
        super(senderId, targetId, propertyIdentifier);
    }

    public void applyModificationToTarget(final GameObject target) throws ParameterValidationException {
        target.removeProperty(getMemberIdentifier());
    }
}
