package j_waste.network;

import java.nio.ByteBuffer;
import org.bouncycastle.util.encoders.Hex;

/** 
 * This is the class for the Chat packet type. 
 * 
 * @author Jan Lindblom (cl0wn@users.sourceforge.net)
 * @version 0.2
 */
public class ChatPacket extends HeadedPacket implements NetworkConstants {

    String sender = "";

    String recipient = "";

    String message = "";

    /** 
	 * Base constructor, creates an empty ChatPacket. 
	 */
    public ChatPacket() {
        super(TYPE_CHAT, null);
    }

    /** 
	 * Constructs a ChatPacket with given sender, recipient
	 * and message.
	 * 
	 * @param s the sender.
	 * @param r the recipient.
	 * @param m the message.
	 */
    public ChatPacket(String s, String r, String m) {
        super(TYPE_CHAT, null);
        sender = s;
        recipient = r;
        message = m;
    }

    /** 
	 * Constructs a ChatPacket with given sender, recipient,
	 * message and GUID.
	 * 
	 * @param s the sender.
	 * @param r the recipient.
	 * @param m the message.
	 * @param guid the GUID.
	 */
    public ChatPacket(String s, String r, String m, byte[] guid) {
        super(TYPE_CHAT, guid);
        sender = s;
        recipient = r;
        message = m;
    }

    /** 
	 * Constructs a ChatPacket given a byte array representation
	 * of the Packet.
	 * 
	 * @param pkg the Packet.
	 */
    public ChatPacket(byte[] pkg) throws InvalidPacketException {
        super(pkg);
        byte[] pkgData = getData();
        int part = 0;
        for (int x = 0; x < pkgData.length; x++) {
            if (pkgData[x] == 0 && x < pkgData.length) {
                part++;
            } else {
                if (part == 0) sender += (char) pkgData[x]; else if (part == 1) recipient += (char) pkgData[x]; else message += (char) pkgData[x];
            }
        }
    }

    /** 
	 * Sets the sender of this ChatPacket. 
	 * 
	 * @param s the sender.
	 */
    public void setSender(String s) {
        sender = s;
    }

    /** 
	 * Returns the sender of this ChatPacket. 
	 * 
	 * @return the sender.
	 */
    public String getSender() {
        return sender;
    }

    /** 
	 * Sets the recipient of this ChatPacket. 
	 * 
	 * @param r the recipient.
	 */
    public void setRecipient(String r) {
        recipient = r;
    }

    /** 
	 * Returns the recipient of this ChatPacket. 
	 * 
	 * @return the recipient.
	 */
    public String getRecipient() {
        return recipient;
    }

    /** 
	 * Sets the message of this ChatPacket. 
	 * 
	 * @param m the message.
	 */
    public void setMessage(String m) {
        message = m;
    }

    /** 
	 * Returns the message of this ChatPacket. 
	 * 
	 * @return the message.
	 */
    public String getMessage() {
        return message;
    }

    /** 
	 * Finalizes this ChatPacket. 
	 */
    public void finalizePacket() {
        if (sender == null && recipient == null && message == null) System.err.println("fel");
        ByteBuffer payload = ByteBuffer.allocate(sender.length() + recipient.length() + message.length() + 3);
        payload.put(sender.getBytes());
        payload.put((byte) 0);
        payload.put(recipient.getBytes());
        payload.put((byte) 0);
        payload.put(message.getBytes());
        payload.put((byte) 0);
        setLength((payload.array().length));
        packetData = ByteBuffer.allocate(getLength());
        packetData.put(payload.array());
        packetData.rewind();
        super.finalizePacket();
    }

    /** 
	 * Returns a byte array representation of the packet.
	 * 
	 * @return a byte array representation of the packet.
	 */
    public byte[] toByteArray() {
        if (iscomplete) return packet;
        return null;
    }
}
