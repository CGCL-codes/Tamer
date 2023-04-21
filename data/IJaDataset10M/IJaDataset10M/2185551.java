package de.jochenbrissier.backyard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * standard implementation of the interface channel.
 * 
 * it represents a channel
 * @author jochen
 *
 */
public class ChannelImpl implements Channel {

    private Log log = LogFactory.getLog(ChannelImpl.class);

    protected ArrayList<Member> members = new ArrayList<Member>();

    protected ChannelListener cL;

    private String name;

    private long id;

    /**
	 * Is member in the channel
	 */
    public boolean isMember(String id) {
        synchronized (members) {
            for (Member m : members) {
                if (m != null && m.equals(new MemberImpl(id))) return true;
            }
        }
        return false;
    }

    public void sendMessage(Message message) {
        log.debug("Send Message to" + members);
        if (cL != null) {
            String lmess = cL.newMessage(message.getData());
            if (lmess != null) message.setData(lmess);
        }
        message.setChannelid(id);
        message.setChannelName(name);
        synchronized (members) {
            for (Member dto : members) {
                if (dto != null) {
                    dto.sendMessage(message);
                }
            }
        }
    }

    public void ClearMembers() {
        synchronized (members) {
            members.clear();
        }
    }

    public void addListener(ChannelListener cL) {
        this.cL = cL;
    }

    public void addMember(Member member) {
        if (isMember(member)) {
            log.debug("is already member");
            return;
        }
        if (cL != null) {
            Member lmember = cL.newMember(member);
            if (lmember == null) {
                return;
            }
            if (lmember != null) {
            }
        }
        synchronized (members) {
            members.add(member);
        }
    }

    public long getChannelId() {
        return this.id;
    }

    public String getChannelName() {
        return this.name;
    }

    public Collection<Member> getMembers() {
        return members;
    }

    public void removeMember(Member member) {
        synchronized (members) {
            for (Member m : members) {
                if (m.equals(member)) {
                    synchronized (members) {
                        members.remove(m);
                    }
                }
            }
        }
    }

    public void setChannelId(long id) {
        this.id = id;
    }

    public void setChannelName(String name) {
        this.name = name;
    }

    public void sendMessage(String data) {
        this.sendMessage(new Message(data));
    }

    public boolean isMember(Member member) {
        synchronized (members) {
            for (Member m : members) {
                if (m.equals(member)) return true;
            }
        }
        return false;
    }

    public boolean hasEvent(Member member) {
        synchronized (members) {
            for (Member m : members) {
                if (m.equals(member)) {
                    if (member.getEvent() != null && member.getEvent().isReady()) return true; else return false;
                }
            }
        }
        return false;
    }
}
