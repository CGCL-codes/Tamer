package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * Retrieves a list of all defined IAX peers.<p>
 * For each peer that is found a PeerEntryEvent is sent by Asterisk containing
 * the details. When all peers have been reported a PeerlistCompleteEvent is
 * sent.<p>
 * Available since Asterisk 1.6
 *
 * @author srt
 * @version $Id: IaxPeerListAction.java 1300 2009-04-30 00:28:00Z srt $
 * @see org.asteriskjava.manager.event.PeerEntryEvent
 * @see org.asteriskjava.manager.event.PeerlistCompleteEvent
 * @since 1.0.0
 */
public class IaxPeerListAction extends AbstractManagerAction implements EventGeneratingAction {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 0L;

    /**
     * Creates a new IaxPeersAction.
     */
    public IaxPeerListAction() {
    }

    @Override
    public String getAction() {
        return "IAXpeerlist";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass() {
        return PeerlistCompleteEvent.class;
    }
}
