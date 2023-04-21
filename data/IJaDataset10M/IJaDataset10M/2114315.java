package com.volantis.mps.bms.impl;

import com.volantis.mps.bms.Message;
import com.volantis.mps.bms.Recipient;
import com.volantis.mps.bms.SendRequest;
import com.volantis.mps.bms.Address;
import com.volantis.mps.bms.Sender;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation of SendRequest.
 */
public class DefaultSendRequest implements SendRequest {

    private Collection recipients;

    private Sender sender = new DefaultSender();

    private Message message;

    public Recipient[] getRecipients() {
        Recipient[] recipientsArray = new Recipient[0];
        if (recipients != null) {
            recipientsArray = (Recipient[]) recipients.toArray(new Recipient[recipients.size()]);
        }
        return recipientsArray;
    }

    public void addRecipient(Recipient recipient) {
        checkForNull(recipient, "recipient");
        if (null == recipients) {
            this.recipients = new ArrayList();
        }
        recipients.add(recipient);
    }

    public Sender getSender() {
        if (sender == null) {
            sender = new DefaultSender();
        }
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        checkForNull(message, "message");
        this.message = message;
    }

    /**
     * Test param to see if it is null.
     *
     * @param param
     * @param fieldName
     * @throws IllegalArgumentException if the param is null, using the
     *                                  fieldName in the detail message.
     */
    private void checkForNull(Object param, String fieldName) {
        if (null == param) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}
