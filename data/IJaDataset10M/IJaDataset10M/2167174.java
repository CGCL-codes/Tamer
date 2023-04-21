package com.unitt.servicemanager.websocket;

public interface MessageSerializer {

    public static final short SERIALIZER_TYPE_JSON = 1;

    public static final short SERIALIZER_TYPE_XML = 2;

    public short getSerializerType();

    public byte[] serializeHeader(MessageRoutingInfo aRoutingInfo);

    public byte[] serializeBody(Object aObject);

    public MessageRoutingInfo deserializeHeader(byte[] aHeader);

    public DeserializedMessageBody deserializeBody(MessageRoutingInfo aInfo, byte[] aBody);
}
