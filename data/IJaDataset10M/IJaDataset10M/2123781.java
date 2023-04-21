package network.internals;

/**
A <em>Packet</em> represents a unit of information to be sent over the Local Area Network (LAN).
 */
public class Packet {

    /**
    Holds the actual message to be send over the network.
    */
    public String message_;

    /**
    Holds the name of the Node which initiated the request.
    */
    public String origin_;

    /**
    Holds the name of the Node which should receive the information.
    */
    public String destination_;

    /**
Construct a <em>Packet</em> with given #message and #destination.
 */
    public Packet(String message, String destination) {
        message_ = message;
        origin_ = "";
        destination_ = destination;
    }

    /**
Construct a <em>Packet</em> with given #message, #origin and #receiver.
 */
    public Packet(String message, String origin, String destination) {
        message_ = message;
        origin_ = origin;
        destination_ = destination;
    }
}
