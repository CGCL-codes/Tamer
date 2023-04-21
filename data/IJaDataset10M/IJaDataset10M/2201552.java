package org.asteriskjava.manager.event;

/**
 * A VoicemailUserEntryCompleteEvent is triggered after the details of all voicemail users has
 * been reported in response to a VoicemailUsersListAction.<p>
 * It is implemented in <code>apps/app_voicemail.c</code>
 * <p>
 * Available since Asterisk 1.6
 *
 * @see VoicemailUserEntryEvent
 * @see org.asteriskjava.manager.action.VoicemailUsersListAction
 * @author srt
 * @version $Id: VoicemailUserEntryCompleteEvent.java 946 2008-01-30 02:52:35Z srt $
 * @since 1.0.0
 */
public class VoicemailUserEntryCompleteEvent extends ResponseEvent {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 0L;

    /**
     * Creates a new instance.
     *
     * @param source
     */
    public VoicemailUserEntryCompleteEvent(Object source) {
        super(source);
    }
}
