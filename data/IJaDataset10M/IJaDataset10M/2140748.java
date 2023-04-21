package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * Retrieves a list of all defined SIP peers.<p>
 * For each peer that is found a PeerEntryEvent is sent by Asterisk containing
 * the details. When all peers have been reported a PeerlistCompleteEvent is
 * sent.<p>
 * Available since Asterisk 1.2
 *
 * @author srt
 * @version $Id: SipPeersAction.java 1148 2008-08-21 18:13:49Z srt $
 * @see org.asteriskjava.manager.event.PeerEntryEvent
 * @see org.asteriskjava.manager.event.PeerlistCompleteEvent
 * @since 0.2
 */
public class SipPeersAction extends AbstractManagerAction implements EventGeneratingAction {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 921037572305993779L;

    /**
     * Creates a new SipPeersAction.
     */
    public SipPeersAction() {
    }

    @Override
    public String getAction() {
        return "SIPPeers";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass() {
        return PeerlistCompleteEvent.class;
    }
}
