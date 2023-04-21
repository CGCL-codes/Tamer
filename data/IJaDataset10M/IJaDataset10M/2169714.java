package controller;

import java.util.LinkedList;
import java.util.List;
import transport.message.HearMessage;

/**
 *
 * @author rem
 */
public class Inbox {

    List<HearMessage> messages;

    public Inbox() {
        messages = new LinkedList<HearMessage>();
    }

    public synchronized List<HearMessage> getMessages() {
        List<HearMessage> temp = new LinkedList<HearMessage>();
        temp.addAll(messages);
        messages.clear();
        return temp;
    }

    public synchronized HearMessage getNextMessage() {
        if (messages.isEmpty()) return null;
        return messages.remove(0);
    }

    public synchronized void addMessage(HearMessage message) {
        messages.add(message);
    }
}
