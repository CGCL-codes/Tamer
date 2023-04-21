package net.sf.catchup.common.packet;

import net.sf.catchup.common.PeerSocketWrapper;
import net.sf.catchup.server.conference.ConferenceSession;
import net.sf.catchup.server.conference.ConferenceSessionPool;

public class LeaveConferenceRequest implements AsyncTransaction {

    private final String sessionID;

    /**
	 * @param fromUser
	 * @param sessionID
	 */
    public LeaveConferenceRequest(String sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public void executeOnClient() {
    }

    @Override
    public void executeOnServer(PeerSocketWrapper peerSocketWrapper) {
        final ConferenceSession session = ConferenceSessionPool.getSession(sessionID);
        if (session != null) {
            session.removeFromSession(peerSocketWrapper);
        }
    }
}
