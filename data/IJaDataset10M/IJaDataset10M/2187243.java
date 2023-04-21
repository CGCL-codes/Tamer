package com.hs.mail.imap.message.request.ext;

import com.hs.mail.imap.ImapSession;
import com.hs.mail.imap.ImapSession.State;
import com.hs.mail.imap.message.request.ImapRequest;

/**
 * 
 * @author Won Chul Doh
 * @since Apr 23, 2010
 *
 */
public class NamespaceRequest extends ImapRequest {

    public NamespaceRequest(String tag, String command) {
        super(tag, command);
    }

    @Override
    public boolean validForState(State state) {
        return (state == ImapSession.State.AUTHENTICATED || state == ImapSession.State.SELECTED);
    }
}
