package org.embedchat.protocol.message.common;

import org.embedchat.protocol.message.AbstractMessage;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class TextMessage extends AbstractMessage {

    private static final long serialVersionUID = -3822372122905681108L;

    private int userId;

    private String messageString;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    @Override
    public String toString() {
        return "TextMessage: " + messageString;
    }

    @Override
    public int getMessageBodyLength() {
        return messageString.getBytes().length;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof TextMessage)) return false;
        TextMessage castOther = (TextMessage) other;
        return new EqualsBuilder().append(userId, castOther.userId).append(messageString, castOther.messageString).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(userId).append(messageString).toHashCode();
    }
}
