package com.monad.homerun.msgmgt.impl;

import java.util.Iterator;
import com.monad.homerun.message.Message;
import com.monad.homerun.msgmgt.MessageService;

/**
 * MessageIterator allows scrolling through messages
 * for a given recipient, forward  or backward.
 */
public class MessageIterator implements Iterator<Message> {

    private MessageService msgSvc = null;

    private boolean forward = false;

    private String[] keys = null;

    private int index = 0;

    public MessageIterator(MessageService svc, String recip, boolean newest, boolean forward) {
        this.msgSvc = svc;
        this.forward = forward;
        keys = svc.getMessageKeys(recip);
        if (newest) {
            reverse();
        }
        index = forward ? 0 : keys.length - 1;
    }

    public boolean hasNext() {
        return forward ? (index < keys.length) : (index >= 0);
    }

    public Message next() {
        Message msg = null;
        if (hasNext()) {
            msg = msgSvc.getMessage(keys[index]);
            if (forward) {
                index++;
            } else {
                index--;
            }
        }
        return msg;
    }

    public void remove() {
        throw new RuntimeException("Remove not implemented");
    }

    private void reverse() {
        int left = 0;
        int right = keys.length - 1;
        while (left < right) {
            String temp = keys[left];
            keys[left++] = keys[right];
            keys[right--] = temp;
        }
    }
}
