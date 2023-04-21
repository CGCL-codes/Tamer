package org.actioncenters.listeners.contributionsservice;

import org.actioncenters.core.constants.Channels;
import org.actioncenters.core.contribution.data.IContribution;
import org.actioncenters.core.contribution.data.IRelationship;
import org.actioncenters.core.contribution.svc.notification.listeners.IAddRelationshipListener;

/**
 * The Class AddRelationshipListener.
 * 
 * @author dougk
 */
public class AddRelationshipListener extends AbstractListener implements IAddRelationshipListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void relationshipAdded(IRelationship newRelationship) {
        IContribution subordinate = newRelationship.getSubordinate();
        if (subordinate != null && subordinate.getRelationships() != null && !subordinate.getRelationships().isEmpty()) {
            String channel = Channels.getAddRelationshipChannel(newRelationship);
            publish(subordinate, channel);
            String subIdChannel = Channels.getAddRelationshipBySubIdChannel(newRelationship);
            publish(subordinate, subIdChannel);
        }
    }
}
