package TwoClientGameConnection;

/**
 * An exception that should be used when describing message 
 * issues with TCGClients and TCGServers. Extends the TCGGeneral Exception.
 * @author Jace Ferguson
 * @filename TCGPacketReadException.java
 */
public class TCGPacketReadException extends TCGGeneralException {

    public TCGPacketReadException() {
        super("A packet read exception has occured.");
    }

    public TCGPacketReadException(String msg) {
        super(msg);
    }

    public TCGPacketReadException(String msg, Throwable e) {
        super(msg, e);
    }
}
