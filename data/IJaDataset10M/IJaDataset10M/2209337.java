package org.t2framework.t2.format.amf.message.data;

public class MessageHeader {

    private int length = -1;

    private String name;

    private boolean required;

    private Object value;

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public void setValue(final Object value) {
        this.value = value;
    }
}
