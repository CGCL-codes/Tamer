package org.armedbear.j.mail;

public final class UnreadMailboxFilter extends MailboxFilter {

    public final boolean accept(MailboxEntry entry) {
        return entry.isUnread();
    }
}
