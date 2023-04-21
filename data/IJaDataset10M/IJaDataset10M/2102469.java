package org.localstorm.mcc.ejb.gtd.agent;

import java.util.List;
import org.localstorm.mcc.ejb.ContextLookup;
import org.localstorm.mcc.ejb.gtd.InboxManager;
import org.localstorm.mcc.ejb.gtd.entity.InboxEntry;
import org.localstorm.mcc.ejb.users.UserManager;

/**
 *
 * @author Alexey Kuznetsov
 */
public class InboxCommandHandler implements CommandHandler {

    @Override
    public String handle(int uid, String from, String to, String param) {
        InboxManager ibm = ContextLookup.lookup(InboxManager.class, InboxManager.BEAN_NAME);
        UserManager um = ContextLookup.lookup(UserManager.class, UserManager.BEAN_NAME);
        List<InboxEntry> entries = ibm.getInboxEntries(um.findById(uid));
        return this.buildResponse(entries);
    }

    private String buildResponse(List<InboxEntry> entries) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- INBOX messages ---\n");
        for (InboxEntry entry : entries) {
            sb.append(entry.getContent());
            sb.append('\n');
            sb.append("--------------------\n");
        }
        return sb.toString();
    }
}
