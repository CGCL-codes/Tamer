package jp.whitedog.jgroups;

import java.io.Serializable;

/**
 * Peer introduction message new comer must send.
 * @author Takao Nakaguchi
 */
public class PeerIntroduction implements Serializable {

    /**
	 * Constructor.
	 * @param peerId Peer ID
	 */
    public PeerIntroduction(String peerId) {
        this.peerId = peerId;
    }

    /**
	 * Returns the Peer ID passed by Constructor.
	 * @return Peer ID
	 */
    public String getPeerId() {
        return peerId;
    }

    private String peerId;

    private static final long serialVersionUID = 8166783221699559427L;
}
