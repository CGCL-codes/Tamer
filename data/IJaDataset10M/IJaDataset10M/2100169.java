package de.tud.kom.nat.comm.serialization;

import java.nio.ByteBuffer;
import de.tud.kom.nat.comm.msg.IMessage;

/**
 * A message serializer is used to (de-)serialize messages. To support different
 * ways to do so, this interface declares the needed functions and one instance
 * of a class which implements this interface is created in the <tt>SerializerLoader</tt>.
 *
 * @author Matthias Weinert
 */
public interface IMessageSerializer {

    /**
	 * Serialize a message to a byte array. If the <tt>withSize</tt>-flag is true,
	 * the byte array begins with 4 bytes which describe the size of the byte array. This is useful, since
	 * the receiver has to know the size of a message to be able to react correctly.
	 * 
	 * @param msg the message which is to be serialized
	 * @param withSize true if the size should be inserted in the first 4 bytes of the array
	 * @return the serialized message in a byte array, possibly with leading size
	 */
    public abstract byte[] serializeMessage(IMessage msg, boolean withSize);

    /**
	 * Deserializes a message based on the given byte array.
	 * @param msgData the byte array [message data]
	 * @return the message or null, if no message object could be created
	 */
    public abstract IMessage deserializeMessage(byte[] msgData);

    /**
	 * Deserializes a message based on the given content in the byte array, described by the offset and length.
	 * 
	 * @param data byte array
	 * @param offset offset where to start in bytearray
	 * @param length length of the message
	 * @return the message or null, if no message object could be created
	 */
    public abstract IMessage deserializeMessage(byte[] data, int offset, int length);

    /**
	 * Deserializes a message based on the content of the bytebuffer and the given length.
	 * @param bb bytebuffer
	 * @param length length of message
	 * @return the message or null, if no message object could be created
	 */
    public abstract IMessage deserializeMessage(ByteBuffer bb, int length);
}
