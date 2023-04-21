package com.hs.mail.imap.message.request;

/**
 * 
 * @author Won Chul Doh
 * @since Jan 28, 2010
 *
 */
public class SelectRequest extends AbstractMailboxRequest {

    public SelectRequest(String tag, String command, String mailbox) {
        super(tag, command, mailbox);
    }
}
