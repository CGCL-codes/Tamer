package com.darrylsite.task;

import com.darrylsite.listener.QueueListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nabster
 */
public class MessageProcessor {

    List<QueueListener> msgReceived;

    List<QueueListener> msgTimed;

    List<QueueListener> msgSent;

    List<QueueListener> msgToSent;

    public MessageProcessor() {
        msgReceived = new ArrayList<QueueListener>();
        msgTimed = new ArrayList<QueueListener>();
        msgSent = new ArrayList<QueueListener>();
        msgToSent = new ArrayList<QueueListener>();
    }

    public void removeMsgReceived(QueueListener q) {
        msgReceived.remove(q);
    }

    public void addMsgReceived(QueueListener msgReceived) {
        this.msgReceived.add(msgReceived);
    }

    public void removeMsgTimed(QueueListener q) {
        msgTimed.remove(q);
    }

    public void addMsgTimed(QueueListener msgReceived) {
        msgTimed.add(msgReceived);
    }

    public void removeMsgSent(QueueListener q) {
        msgSent.remove(q);
    }

    public void addMsgSent(QueueListener msgReceived) {
        msgSent.add(msgReceived);
    }

    public void removeMsgToSent(QueueListener q) {
        msgToSent.remove(q);
    }

    public void addMsgToSent(QueueListener q) {
        msgToSent.add(q);
    }
}
