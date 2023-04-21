package de.jochenbrissier.backyard.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.inject.Inject;
import de.jochenbrissier.backyard.util.SendFailException;

public class MemberImpl implements Member {

    private Log log = LogFactory.getLog(MemberImpl.class);

    private String name;

    private String id;

    private Event actualEvent;

    private long timestamp;

    private Message lastMessage;

    private Queue<Message> messages = new ConcurrentLinkedQueue<Message>();

    @Inject
    public MemberImpl(Event ev) {
        this.timestamp = System.currentTimeMillis();
        this.actualEvent = ev;
    }

    public MemberImpl(String id) {
        this.id = id;
    }

    public String getMemberName() {
        return this.name;
    }

    public String getMemberlId() {
        return this.id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setMemberId(String id) {
        this.id = id;
    }

    public void setMemberName(String name) {
        this.name = name;
    }

    public Event getEvent() {
        return this.actualEvent;
    }

    public void setEvent(Event ev) {
        this.actualEvent = ev;
    }

    @Override
    public boolean equals(Object obj) {
        MemberImpl aMember = (MemberImpl) obj;
        return aMember.getMemberlId().equals(this.id);
    }

    public void sendMessage(Message message) {
        log.debug("add Message");
        message.setMemberid(id);
        if (this.name != null) message.setMemberNane(name);
        messages.add(message);
        if (actualEvent != null) {
            log.debug("Event is ready:" + this.actualEvent.isReady());
            log.debug("Event has error:" + this.actualEvent.isError());
        } else {
            log.debug("Event is null");
        }
        if (this.actualEvent != null && this.actualEvent.isReady()) {
            log.debug("Event is ready");
            LinkedList<Message> sendMessage = new LinkedList<Message>();
            Iterator it = this.messages.iterator();
            while (it.hasNext()) {
                log.debug("Send Message:");
                Message curMessage = (Message) it.next();
                sendMessage.add(message);
                actualEvent.addMessage(message);
            }
            try {
                log.debug("close Event");
                actualEvent.close();
                messages.clear();
            } catch (SendFailException e) {
                log.warn("can't close event add to queue");
                messages.addAll(sendMessage);
            }
            actualEvent = null;
        } else try {
            actualEvent.close();
        } catch (Exception e) {
        }
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message message) {
        this.lastMessage = message;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
