package org.remus.infomngmnt.connector.twitter.jobs;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.remus.SynchronizableObject;
import org.eclipse.remus.SynchronizationState;
import org.remus.infomngmnt.connector.twitter.TwitterActivator;
import org.remus.infomngmnt.connector.twitter.TwitterRepository;
import org.remus.infomngmnt.connector.twitter.preferences.TwitterPreferenceInitializer;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class RefreshDirectMessagesJob extends RefreshTwitterJob {

    @Override
    protected boolean isTwitterElementSatisfied(EObject eObject) {
        return ((SynchronizableObject) eObject).getSynchronizationMetaData() != null && ((SynchronizableObject) eObject).getSynchronizationMetaData().getSyncState() == SynchronizationState.IN_SYNC && ((SynchronizableObject) eObject).getSynchronizationMetaData().getUrl().endsWith("/" + TwitterRepository.ID_DIRECT_MESSAGES);
    }

    @Override
    public int getInterval() {
        return TwitterActivator.getDefault().getPreferenceStore().getInt(TwitterPreferenceInitializer.RELOAD_DIRECT_MESSAGES_FEED);
    }
}
