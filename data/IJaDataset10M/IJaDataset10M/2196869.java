package org.ugr.bluerose.messages;

import org.ugr.bluerose.ByteStreamWriter;

/**
 * Class for modelling an empty message.
 *
 * @author Carlos Rodriguez Dominguez
 * @date 12-10-2009
 */
public class Message {

    public MessageHeader header;

    /**< Header of the message */
    public Encapsulation body;

    /**< Body of the message */
    protected static Object mutex = new Object();

    protected static ByteStreamWriter writer = new ByteStreamWriter();

    /**
	 * Default constructor
	 */
    public Message() {
        header = null;
        body = null;
    }

    /**
	 * Returns the full message (header+body, if body exists) as a byte sequence
	 *
	 * @return Bytes of the message
	 */
    public java.util.Vector<Byte> getBytes() {
        java.util.Vector<Byte> result = null;
        synchronized (mutex) {
            if (body != null) {
                java.util.Vector<Byte> hd = header.getBytes();
                header.messageSize = hd.size() + 6 + body.byteCollection.size();
                hd = header.getBytes();
                writer.writeRawBytes(hd);
                writer.writeInteger(body.size);
                writer.writeByte(body.major);
                writer.writeByte(body.minor);
                writer.writeRawBytes(body.byteCollection);
            } else {
                writer.writeRawBytes(header.getBytes());
            }
            result = writer.toVector();
            writer.reset();
        }
        return result;
    }

    /**
	 * Adds bytes to the body
	 *
	 * @param bytes Bytes to add
	 */
    public void addToEncapsulation(java.util.Vector<Byte> bytes) {
        if (body != null) {
            body.size += bytes.size();
            body.byteCollection.addAll(bytes);
        }
    }
}
