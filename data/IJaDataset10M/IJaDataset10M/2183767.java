package com.dyuproject.protostuff.parser;

/**
 * Represents a message field defined in a {@code Proto}.
 *
 * @author David Yu
 * @created Dec 19, 2009
 */
public class MessageField extends Field<Message> {

    Message message;

    public MessageField() {
        super(false);
    }

    public MessageField(Message message) {
        this();
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public java.lang.String getJavaType() {
        StringBuilder buffer = new StringBuilder();
        Message.computeName(message, owner, buffer);
        return buffer.toString();
    }

    public java.lang.String getRegularType() {
        java.lang.String javaType = getJavaType();
        Proto messageProto = message.getProto();
        java.lang.String javaPackage = messageProto.getJavaPackageName();
        java.lang.String protoPackage = messageProto.getPackageName();
        if (javaType.startsWith(javaPackage) && !javaPackage.equals(protoPackage)) return javaType.replace(javaPackage, protoPackage);
        return javaType;
    }

    public java.lang.String getDefaultValueAsString() {
        return "null";
    }

    public boolean isDelimited() {
        return true;
    }

    public boolean isSamePackage() {
        return getOwner().getProto() == getMessage().getProto();
    }

    public java.lang.String getRelativePath() {
        if (isSamePackage()) return "";
        java.lang.String currentPackage = getOwner().getProto().getPackageName();
        java.lang.String targetPackage = getMessage().getProto().getPackageName();
        java.lang.String path = "../";
        for (int idx = currentPackage.indexOf('.'); idx != -1; idx = currentPackage.indexOf('.', idx + 1)) path += "../";
        return path + targetPackage.replace('.', '/') + "/";
    }
}
