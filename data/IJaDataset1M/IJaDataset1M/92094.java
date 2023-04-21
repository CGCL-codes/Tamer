package de.fu_berlin.inf.dpp.activities.serializable;

import de.fu_berlin.inf.dpp.activities.business.IActivity;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.project.ISarosSession;

/**
 * An interface for all things that occur in a shared project session such as
 * editing a file, opening or closing editors, changing permissions, etc.
 * 
 * All activityDataObjects should be implemented using the value pattern, i.e.
 * created activityDataObjects should be immutable.
 * 
 * @author rdjemili
 * 
 * @valueObject All IActivityDataObject subclasses should be Value Objects, i.e.
 *              they should be immutable
 */
public interface IActivityDataObject {

    /**
     * Returns the JID of the user which has caused this activityDataObject.
     */
    public JID getSource();

    /**
     * Turn this IActivityDataObject (which is detached from a SarosSession)
     * into an IActivity, which is live within a session.
     */
    public IActivity getActivity(ISarosSession sarosSession);
}
