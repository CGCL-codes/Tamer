package org.openymsg.legacy.network;

/**
 * Exception indication an error during parsing of a YMSG9Packet.
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 * @author S.E. Morris
 */
public class YMSG9BadFormatException extends RuntimeException {

    private static final long serialVersionUID = 2722272866191388316L;

    /**
     * The packet of which parsing caused this exception to be thrown.
     */
    private final transient YMSG9Packet packet;

    /**
     * Constructs a new YMSG9BadFormatException with the specified detail message.
     * 
     * @param message
     *            Optional additional detail message.
     * @param packet
     *            Packet of which parsing caused this exception to be thrown.
     * @param cause
     *            The cause.
     */
    public YMSG9BadFormatException(String message, YMSG9Packet packet, Throwable cause) {
        super(constructMessage(message, packet), cause);
        this.packet = packet;
    }

    /**
     * Convenience method for constructing the detailled Exception message;
     * 
     * @param message
     *            Optional additional detail message.
     * @param packet
     *            Packet of which parsing caused this exception to be thrown.
     * @return A String object combining the text representation of both parameters.
     */
    private static String constructMessage(String message, YMSG9Packet packet) {
        final StringBuilder result = new StringBuilder("Bad format for packet");
        if (packet != null) {
            result.append(": [");
            result.append(packet.toString());
            result.append("]");
        }
        result.append('.');
        if (message != null) {
            result.append(System.getProperty("line.separator"));
            result.append(message);
        }
        return result.toString();
    }

    /**
     * Returns the packet that caused this Exception.
     * 
     * @return the packet that caused this Exception.
     */
    public YMSG9Packet getPacket() {
        return packet;
    }
}
