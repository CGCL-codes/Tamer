package de.fu_berlin.inf.dpp.project;

import org.eclipse.core.runtime.SubMonitor;
import de.fu_berlin.inf.dpp.User;

/**
 * A listener for SarosSession life-cycle related events.
 * 
 * @author bkahlert
 */
public class AbstractSarosSessionListener implements ISarosSessionListener {

    public void preIncomingInvitationCompleted(SubMonitor subMonitor) {
    }

    public void postOutgoingInvitationCompleted(SubMonitor subMonitor, User user) {
    }

    public void sessionStarting(ISarosSession newSarosSession) {
    }

    public void sessionStarted(ISarosSession newSarosSession) {
    }

    public void sessionEnding(ISarosSession oldSarosSession) {
    }

    public void sessionEnded(ISarosSession oldSarosSession) {
    }

    public void projectAdded(String projectID) {
    }
}
