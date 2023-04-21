package de.fu_berlin.inf.dpp.invitation;

import org.apache.log4j.Logger;
import org.picocontainer.annotations.Inject;
import de.fu_berlin.inf.dpp.SarosContext;
import de.fu_berlin.inf.dpp.exceptions.StreamException;
import de.fu_berlin.inf.dpp.net.ITransmitter;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.net.internal.StreamServiceManager;
import de.fu_berlin.inf.dpp.net.internal.StreamSession;
import de.fu_berlin.inf.dpp.net.internal.StreamSession.StreamSessionListener;
import de.fu_berlin.inf.dpp.observables.InvitationProcessObservable;
import de.fu_berlin.inf.dpp.project.SarosSessionManager;

/**
 * @author rdjemili
 * @author sotitas
 */
public abstract class InvitationProcess {

    private static final Logger log = Logger.getLogger(InvitationProcess.class);

    @Inject
    protected ITransmitter transmitter;

    protected JID peer;

    protected String description;

    protected final int colorID;

    @Inject
    protected StreamServiceManager streamServiceManager;

    @Inject
    protected ArchiveStreamService archiveStreamService;

    @Inject
    protected SarosSessionManager sarosSessionManager;

    protected StreamSession streamSession;

    @Inject
    protected InvitationProcessObservable invitationProcesses;

    protected boolean error = false;

    protected StreamSessionListener sessionListener = new StreamSessionListener() {

        public void sessionStopped() {
            if (streamSession != null) {
                streamSession.shutdownFinished();
                streamSession = null;
            }
        }

        public void errorOccured(StreamException e) {
            log.error("Got error while streaming project archive: ", e);
            error = true;
        }
    };

    public InvitationProcess(JID peer, String description, int colorID, SarosContext sarosContext) {
        this.peer = peer;
        this.description = description;
        this.colorID = colorID;
        sarosContext.initComponent(this);
        this.invitationProcesses.addInvitationProcess(this);
    }

    /**
     * @return the peer that is participating with us in this process. For an
     *         incoming invitation this is the inviter. For an outgoing
     *         invitation this is the invitee.
     */
    public JID getPeer() {
        return this.peer;
    }

    /**
     * @return the user-provided informal description that can be provided with
     *         an invitation.
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "InvitationProcess(peer:" + this.peer + ")";
    }

    public abstract void remoteCancel(String errorMsg);
}
