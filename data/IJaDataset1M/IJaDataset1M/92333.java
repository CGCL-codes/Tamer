package org.openymsg.contact.group;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openymsg.YahooContactGroup;
import org.openymsg.execute.Executor;
import org.openymsg.util.CollectionUtils;

public class SessionGroupImpl implements SessionGroup, SessionGroupCallback {

    private Executor executor;

    private String username;

    private Set<YahooContactGroup> contactGroups = Collections.synchronizedSet(new HashSet<YahooContactGroup>());

    public SessionGroupImpl(Executor executor, String username) {
        this.executor = executor;
        this.username = username;
    }

    public void renameGroup(YahooContactGroup group, String newName) throws IllegalStateException, IOException {
        this.executor.execute(new ContactGroupRenameMessage(username, group, newName));
    }

    @Override
    public Set<YahooContactGroup> getContactGroups() {
        return CollectionUtils.protectedSet(this.contactGroups);
    }

    @Override
    public void addedGroups(Set<YahooContactGroup> contactGroups) {
        this.contactGroups.addAll(contactGroups);
    }

    public boolean possibleAddGroup(ContactGroupImpl group) {
        return false;
    }

    @Override
    public void addGroup(String groupName) {
    }

    public boolean possibleRemoveGroup(ContactGroupImpl group) {
        return false;
    }
}
