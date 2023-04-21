package org.p2pws.loaddistribution;

import java.util.SortedSet;
import java.util.TreeSet;
import org.p2pws.loaddistribution.group.GroupMember;
import org.p2pws.loaddistribution.message.MessageSender;
import org.p2pws.loaddistribution.message.P2PSystemMessage;
import org.p2pws.loaddistribution.request.P2PRequestEvent;

/**
 * Default implementation of the LoadDistributer.
 *  
 * @author panisson
 *
 */
public abstract class AbstractLoadDistributer implements LoadDistributer {

    private SortedSet<GroupMember> members = new TreeSet<GroupMember>();

    private GroupMember me;

    private MessageSender sender = null;

    /**
	 * Constructor for the LoadDistributer classes
	 * 
	 * @param me
	 */
    public AbstractLoadDistributer(GroupMember me) {
        this.me = me;
        members.add(me);
    }

    public void eventReceived(P2PRequestEvent event) {
    }

    public void loadMetricMessage(GroupMember member, String msgName, String msgValue) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }

    public LoadMetricMessage getLoadMetricMessage() {
        LoadMetricMessage message = new LoadMetricMessage(me);
        return message;
    }

    public <T extends P2PSystemMessage> T setLoadMetricElements(T message) {
        return message;
    }

    public void addMember(GroupMember member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }

    public void removeMember(GroupMember member) {
        members.remove(member);
    }

    public final void registerMessageSender(MessageSender sender) {
        this.sender = sender;
    }

    /**
	 * Helper method to the subclasses get the message sender
	 * @return
	 */
    protected final MessageSender getMessageSender() {
        return sender;
    }

    /**
	 * Helper method to the subclasses get the members
	 * @return
	 */
    protected final SortedSet<GroupMember> getMembers() {
        return members;
    }

    /**
	 * Helper method to the subclasses get this member reference
	 * @return
	 */
    protected final GroupMember getMe() {
        return this.me;
    }
}
