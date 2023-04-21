package org.coos.messaging.impl;

import org.coos.messaging.Message;
import org.coos.messaging.MessageContext;
import org.coos.messaging.Serializer;
import org.coos.messaging.SerializerFactory;
import org.coos.messaging.util.Log;
import org.coos.messaging.util.LogFactory;
import org.coos.util.serialize.AFClassLoader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Knut Eilif Husa, Tellu AS
 */
public class DefaultMessage implements Message, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4708035750894575811L;

    protected static final Log logger = LogFactory.getLog(DefaultMessage.class.getName());

    protected String receiverEndpointUri;

    protected String senderEndpointUri;

    protected Hashtable<String, String> headers = new Hashtable<String, String>();

    protected Object body;

    protected byte[] serializedbody;

    protected transient MessageContext messageContext = new MessageContext();

    protected byte version;

    private AFClassLoader serializeCl;

    public DefaultMessage() {
        setHeader(MESSAGE_NAME, DEFAULT_MESSAGE_NAME);
        setHeader(TYPE, TYPE_MSG);
    }

    public DefaultMessage(String signalName) {
        setHeader(Message.MESSAGE_NAME, signalName);
        setHeader(TYPE, TYPE_MSG);
    }

    public DefaultMessage(String signalName, String type) {
        setHeader(Message.MESSAGE_NAME, signalName);
        setHeader(TYPE, type);
    }

    public DefaultMessage(DataInputStream din) throws Exception {
        deserialize(din);
    }

    public String getReceiverEndpointUri() {
        return receiverEndpointUri;
    }

    public Message setReceiverEndpointUri(String receiverEndpointUri) {
        this.receiverEndpointUri = receiverEndpointUri;
        return this;
    }

    public String getSenderEndpointUri() {
        return senderEndpointUri;
    }

    public Message setSenderEndpointUri(String senderEndpointUri) {
        this.senderEndpointUri = senderEndpointUri;
        return this;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Message setHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public void setDeserializeClassLoader(AFClassLoader cl) {
        this.serializeCl = cl;
    }

    public Hashtable<String, String> getHeaders() {
        return headers;
    }

    public String getType() {
        return headers.get(TYPE);
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public Message setBody(byte[] bytesBody) {
        headers.put(CONTENT_TYPE, CONTENT_TYPE_BYTES);
        body = bytesBody;
        return this;
    }

    @SuppressWarnings("unchecked")
    public Message setBody(Hashtable propertyBody) {
        headers.put(CONTENT_TYPE, CONTENT_TYPE_PROPERTY);
        body = propertyBody;
        return this;
    }

    public Message setBody(String stringBody) {
        headers.put(CONTENT_TYPE, CONTENT_TYPE_STRING);
        body = stringBody;
        return this;
    }

    public Message setBody(Object objectBody) {
        headers.put(CONTENT_TYPE, CONTENT_TYPE_OBJECT);
        body = objectBody;
        return this;
    }

    public byte[] getBodyAsBytes() {
        try {
            deserializeBody();
        } catch (Exception e) {
            logger.error("Unknown Exception caught", e);
        }
        return (byte[]) body;
    }

    @SuppressWarnings("unchecked")
    public Hashtable getBodyAsProperties() {
        try {
            deserializeBody();
        } catch (Exception e) {
            logger.error("Unknown Exception caught", e);
        }
        return (Hashtable) body;
    }

    public String getBodyAsString() {
        try {
            deserializeBody();
        } catch (Exception e) {
            logger.error("Unknown Exception caught", e);
        }
        return (String) body;
    }

    public Object getBody() {
        try {
            deserializeBody();
        } catch (Exception e) {
            logger.error("Unknown Exception caught", e);
        }
        return body;
    }

    public void deserialize(DataInputStream din) throws Exception {
        din.readInt();
        version = din.readByte();
        if (din.readBoolean()) {
            receiverEndpointUri = din.readUTF();
        }
        if (din.readBoolean()) {
            senderEndpointUri = din.readUTF();
        }
        int headerSize = din.readInt();
        for (int i = 0; i < headerSize; i++) {
            String key = din.readUTF();
            String value = din.readUTF();
            headers.put(key, value);
        }
        int length = din.readInt();
        if (length == 0) {
            return;
        }
        serializedbody = new byte[length];
        din.readFully(serializedbody);
    }

    private void deserializeBody() throws Exception {
        if ((body == null) && (serializedbody != null) && (serializedbody.length > 0)) {
            String contentType = headers.get(Message.CONTENT_TYPE);
            if (contentType == null) {
                throw new Exception("No content type indicated in message header");
            }
            if (contentType.equals(Message.CONTENT_TYPE_BYTES)) {
                body = serializedbody;
            } else if (contentType.equals(Message.CONTENT_TYPE_STRING)) {
                body = new String(serializedbody, "UTF-8");
            } else {
                String serMethod = headers.get(SERIALIZATION_METHOD);
                if (serMethod != null) {
                    Serializer serializer = SerializerFactory.getSerializer(serMethod);
                    body = serializer.deserialize(serializedbody, serializeCl);
                } else {
                    throw new Exception("No serialization method indicated in message header");
                }
            }
        }
    }

    public byte[] serialize() throws Exception {
        ByteArrayOutputStream bouth = new ByteArrayOutputStream();
        DataOutputStream douth = new DataOutputStream(bouth);
        douth.writeByte(version);
        douth.writeBoolean(receiverEndpointUri != null);
        if (receiverEndpointUri != null) {
            douth.writeUTF(receiverEndpointUri);
        }
        douth.writeBoolean(senderEndpointUri != null);
        if (senderEndpointUri != null) {
            douth.writeUTF(senderEndpointUri);
        }
        if ((body != null) && (serializedbody == null)) {
            if (body instanceof byte[]) {
                headers.put(CONTENT_TYPE, CONTENT_TYPE_BYTES);
                serializedbody = (byte[]) body;
            } else if (body instanceof String) {
                headers.put(CONTENT_TYPE, CONTENT_TYPE_STRING);
                serializedbody = ((String) body).getBytes("UTF-8");
            } else {
                if (body instanceof Hashtable<?, ?>) {
                    headers.put(CONTENT_TYPE, CONTENT_TYPE_PROPERTY);
                } else {
                    headers.put(CONTENT_TYPE, CONTENT_TYPE_OBJECT);
                }
                String serMethod = headers.get(SERIALIZATION_METHOD);
                if (serMethod != null) {
                    Serializer serializer = SerializerFactory.getSerializer(serMethod);
                    if (serializer != null) {
                        serializedbody = serializer.serialize(body);
                    } else {
                        throw new Exception("Serialization method not registered: " + serMethod);
                    }
                } else {
                    try {
                        Serializer serializer = SerializerFactory.getDefaultSerializer();
                        serializedbody = serializer.serialize(body);
                        headers.put(SERIALIZATION_METHOD, SERIALIZATION_METHOD_DEFAULT);
                    } catch (Exception e) {
                        Serializer serializer = SerializerFactory.getSerializer(SERIALIZATION_METHOD_JAVA);
                        if (serializer != null) {
                            serializedbody = serializer.serialize(body);
                            headers.put(SERIALIZATION_METHOD, SERIALIZATION_METHOD_JAVA);
                        } else {
                            throw new Exception("Serialization failed");
                        }
                    }
                }
            }
        }
        douth.writeInt(headers.size());
        Enumeration<String> enumer = headers.keys();
        while (enumer.hasMoreElements()) {
            String key = enumer.nextElement();
            String value = headers.get(key);
            douth.writeUTF(key);
            douth.writeUTF(value);
        }
        if (serializedbody != null) {
            douth.writeInt(serializedbody.length);
            douth.write(serializedbody);
        } else {
            douth.writeInt(0);
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        byte[] body = bouth.toByteArray();
        dout.writeInt(body.length);
        dout.write(body);
        return bout.toByteArray();
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(MessageContext ctx) {
        this.messageContext = ctx;
    }

    public String getContentType() {
        return headers.get(CONTENT_TYPE);
    }

    public String getName() {
        return headers.get(MESSAGE_NAME);
    }

    public Message copy() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.serialize());
        DataInputStream din = new DataInputStream(bais);
        return new DefaultMessage(din);
    }

    public String getReceiverEndpointName() {
        return headers.get(RECEIVER_ENDPOINT_NAME);
    }

    public String getSenderEndpointName() {
        return headers.get(SENDER_ENDPOINT_NAME);
    }

    public void setReceiverEndpointName(String endpointName) {
        if (endpointName != null) {
            headers.put(RECEIVER_ENDPOINT_NAME, endpointName);
        }
    }

    public void setSenderEndpointName(String endpointName) {
        if (endpointName != null) {
            headers.put(SENDER_ENDPOINT_NAME, endpointName);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Receiver: ");
        buf.append(receiverEndpointUri);
        buf.append(", Sender: ");
        buf.append(senderEndpointUri);
        Enumeration<String> enumer = headers.keys();
        while (enumer.hasMoreElements()) {
            String key = enumer.nextElement();
            String value = headers.get(key);
            buf.append(", ");
            buf.append(key);
            buf.append(":");
            buf.append(value);
        }
        return buf.toString();
    }

    public byte[] getSerializedBody() {
        return serializedbody;
    }

    public void setSerializedBody(byte[] body) {
        headers.put(CONTENT_TYPE, CONTENT_TYPE_BYTES);
        serializedbody = body;
    }
}
