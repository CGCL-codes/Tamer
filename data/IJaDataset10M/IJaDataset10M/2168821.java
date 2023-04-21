package edu.arsc.fullmetal.server.drivers;

import edu.arsc.fullmetal.commons.ControlType;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serial protocol handler that throws out whatever it's given.
 * @author cgranade
 */
public class MockProtocol implements SerialProtocolHandler {

    public void setStreams(InputStream is, OutputStream os) {
    }

    public void sendCommand(ControlType type, int magnitude) {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Mock protocol is sending some command: " + type.toString() + " " + magnitude);
    }

    public void sendStart() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Mock protocol is sending init string.");
    }
}
