package com.jstun.core.attribute;

public class MessageIntegrity extends MessageAttribute {

    public MessageIntegrity() {
        super(MessageAttribute.MessageAttributeType.MessageIntegrity);
    }

    public byte[] getBytes() {
        return new byte[0];
    }

    public static MessageIntegrity parse(byte[] data) {
        return new MessageIntegrity();
    }
}
