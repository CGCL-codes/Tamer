package com.hs.mail.imap.processor.ext;

import com.hs.mail.imap.ImapSession;
import com.hs.mail.imap.mailbox.Mailbox;
import com.hs.mail.imap.message.request.ImapRequest;
import com.hs.mail.imap.message.request.ext.NamespaceRequest;
import com.hs.mail.imap.message.responder.Responder;
import com.hs.mail.imap.processor.AbstractImapProcessor;

/**
 * 
 * @author Won Chul Doh
 * @since Apr 23, 2010
 *
 */
public class NamespaceProcessor extends AbstractImapProcessor {

    @Override
    protected void doProcess(ImapSession session, ImapRequest message, Responder responder) throws Exception {
        NamespaceRequest request = (NamespaceRequest) message;
        responder.untagged(request.getCommand() + " ((\"\" \"" + Mailbox.folderSeparator + "\")) NIL NIL\r\n");
        responder.okCompleted(request);
    }
}
