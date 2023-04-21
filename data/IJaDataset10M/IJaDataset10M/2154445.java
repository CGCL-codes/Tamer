package org.actioncenters.listeners.contributionsservice;

import org.actioncenters.core.constants.Channels;
import org.actioncenters.core.contribution.data.IContribution;
import org.actioncenters.core.contribution.svc.notification.listeners.IUpdateContributionListener;

/**
 * The Class UpdateContributionListener.
 * 
 * @author dougk
 */
public class UpdateContributionListener extends AbstractListener implements IUpdateContributionListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void contributionUpdated(IContribution contribution) {
        if (contribution != null) {
            String channel = Channels.getUpdateContributionChannel(contribution);
            if (isAnybodyListening(channel)) {
                publish(contribution, channel);
            }
        }
    }
}
