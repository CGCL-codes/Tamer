package ch.iserver.ace.collaboration;

/**
 * The ParticipantSessionCallback extends the SessionCallback interface and
 * adds methods used only for sessions in which the local user is a 
 * participant.
 */
public interface ParticipantSessionCallback extends SessionCallback {

    /**
	 * Sets the document content to the given <var>doc</var>.
	 * 
	 * @param doc the new document content
	 */
    void setDocument(PortableDocument doc);

    /**
	 * Called to notify the document controller that the session was
	 * terminated, that is the publisher closed the document.
	 */
    void sessionTerminated();

    /**
	 * Called to notify the document controller that the local user has been
	 * kicked out of the session.
	 */
    void kicked();
}
