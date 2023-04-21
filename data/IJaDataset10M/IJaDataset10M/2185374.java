package net.jxta.protocol;

import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentUtils;
import net.jxta.peer.PeerID;

/**
 * This abstract class define the PeerInfoService Query message.
 *
 **/
public abstract class PeerInfoQueryMessage {

    /**
     * The Sender's (or Querying) Peer Id.
     */
    private PeerID spid = null;

    /**
     * Peer Id of the peer which published this advertisement.
     */
    private PeerID tpid = null;

    private StructuredDocument request = null;

    public PeerInfoQueryMessage() {
        super();
    }

    /**
     * returns the Message type
     * @return a string
     *
     * @since JXTA 1.0
     */
    public static String getMessageType() {
        return "jxta:PeerInfoQueryMessage";
    }

    /**
     * returns the sender's pid
     * @return a string representing the peer's id
     *
     * @since JXTA 1.0
     */
    public PeerID getSourcePid() {
        return spid;
    }

    /**
     * sets the sender's pid
     * @param pid a string representing a peer's id
     *
     * @since JXTA 1.0
     */
    public void setSourcePid(PeerID pid) {
        this.spid = pid;
    }

    /**
     * returns the target pid
     * @return a string representing the peer's id
     *
     * @since JXTA 1.0
     */
    public PeerID getTargetPid() {
        return tpid;
    }

    /**
     * sets the target's pid
     * @param pid a string representing a peer's id
     *
     * @since JXTA 1.0
     */
    public void setTargetPid(PeerID pid) {
        this.tpid = pid;
    }

    /**
     * returns the request
     * @return a structured document representing request
     *
     * @since JXTA 1.0
     */
    public Element getRequest() {
        if (null != request) {
            return StructuredDocumentUtils.copyAsDocument(request);
        } else {
            return null;
        }
    }

    /**
     * sets the request
     * @param request a structured document representing a peerinfo request
     *
     * @since JXTA 1.0
     */
    public void setRequest(Element request) {
        if (null != request) {
            this.request = StructuredDocumentUtils.copyAsDocument(request);
        } else {
            this.request = null;
        }
    }

    public abstract Document getDocument(MimeMediaType encodeAs);
}
