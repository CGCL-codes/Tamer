package org.actioncenters.listeners.contributionsservice;

import java.util.ArrayList;
import java.util.List;
import org.actioncenters.core.constants.Channels;
import org.actioncenters.core.contribution.data.IRelationship;
import org.actioncenters.core.contribution.data.IValueObject;
import org.actioncenters.core.contribution.svc.notification.listeners.IMoveContributionListener;

/**
 * @author dkjeldgaard
 *
 */
public class MoveContributionListener extends AbstractListener implements IMoveContributionListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void contributionMoved(IRelationship oldRelationship, IRelationship newRelationship) {
        String channel = Channels.getRemoveRelationshipChannel(oldRelationship);
        if (isAnybodyListening(channel)) {
            List<IValueObject> vos = new ArrayList<IValueObject>();
            vos.add(oldRelationship.getSubordinate());
            vos.add(newRelationship);
            publish(vos, channel);
        }
    }
}
