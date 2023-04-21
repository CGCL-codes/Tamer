package bank.domain.validation.impl;

import java.util.*;
import bank.domain.validation.*;
import com.google.common.collect.*;

public class DefaultMessage implements Message {

    private MessageType type;

    private String messageKey;

    private List<Object> contextOrdered = Lists.newArrayList();

    public DefaultMessage(MessageType type, String messageKey, Collection<Object> contextOrdered) {
        this.type = type;
        this.messageKey = messageKey;
        this.contextOrdered.addAll(contextOrdered);
    }

    public MessageType getType() {
        return type;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public List<Object> getContextOrdered() {
        return contextOrdered;
    }
}
