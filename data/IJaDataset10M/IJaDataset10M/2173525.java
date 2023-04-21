package de.gearfore.simplenet;

import java.nio.ByteBuffer;

/**
 * The Header handler is responsible for dealing with packets header data.
 * 
 * <p>The Messages itself are meant to be designed in such a way that they only
 * deal with their respective payload data. The protocol header handling
 * (as dealing with message IDs or encryption) should be handled differently so
 * the PacketHandlers can concentrate on their private protocol data. Also this
 * ensures that the header can be extendet without nasty side effects.</p>
 * 
 * <p>When new packets arrive, first their contents are passed to a concrete
 * HeaderHandler implementation which deals with the headers.
 * If new messages are prepared to be sent over wire, header data is added.</p>
 * 
 * <p>An integral mandatory part of the Packet header is a type ID so individual
 * packet types can be distinguished from the wire representation. Typically
 * HeaderHandlers would implement some kind of packet type registry to handle that,
 * but that is considered an implementation detail.</p>
 * 
 * @author b.hallinger@gearfore.de
 * @version   $Id: HeaderHandler.java 23 2009-08-04 07:58:17Z benih $
 */
public interface HeaderHandler {

    /**
	 * Process the header parts of the message.
	 * 
	 * <p>Arbitary protocol functions could be implemented using header fields.
	 * If a new packet arrives, it will be passed to this method. This method
	 * should parse the header data, invoke necessary routines (eg. decompressing stuff or so)
	 * and build a "header message" object which is passed back to the caller.</p>
	 * 
	 * <p>If this method returns null, the PacketManager will not process the
	 * message any further, allowing you to implement header based message filtering
	 * or message processing that should only affect the header.</p>
	 * 
	 * @param buffer ByteBuffer with the entire packets wire data (header+payload)
	 * @return HeaderPacket containing core data for the MessageHandler, or null if no further processing should occur.
	 */
    public HeaderPacket processHeader(ByteBuffer buffer);

    /**
	 * Builds a network packet by adding header data to the payload.
	 * 
	 * <p>If new packets are to be sent over wire, they need header data.
	 * This method is the opposite of the {@code processHeader} method which will
	 * build the header for arbitary message packets.
	 * The header must be constructed in such a way that the {@code processHeader}
	 * method can parse it correctly.</p>
	 * 
	 * @param msg Message containing the message for which the header data should be generated
	 * @return Bytebuffer with the header data and the payload data
	 */
    public ByteBuffer buildPacket(Message msg);

    /**
	 * Get the ID of a packet type.
	 * 
	 * <p>The PacketManager uses this method to ask for the ID of a specific Packet object.</p>
	 * 
	 * <p>A naive implementation could do some funny "instanceof" stuff,
	 * complexer implementations would do that with a registry scheme.
	 * Maybe also some "hash" algorythm could be suitable. Choose whatever
	 * you like :)</p>
	 * 
	 * @param  msg A Message object
	 * @return     The ID for the Message type
	 */
    public Object getMessageID(Message msg);
}
