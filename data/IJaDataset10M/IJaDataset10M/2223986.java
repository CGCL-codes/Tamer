package com.exadel.flamingo.flex.amf;

import java.io.Serializable;
import flex.messaging.messages.Message;

/**
 * @author Franck WOLFF
 */
public class AMF3Object implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Object value;

    public AMF3Object(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (value == null ? "null" : value.toString());
    }

    public String toString(String indent) {
        if (value instanceof Message) return ((Message) value).toString(indent);
        return new StringBuilder().append(indent).append(value).toString();
    }
}
