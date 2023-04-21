package org.smpp.pdu;

import org.smpp.Data;

/**
 * @author Logica Mobile Networks SMPP Open Source Team
 * @version $Revision: 1.1 $
 */
public class BindTransmitter extends BindRequest {

    public BindTransmitter() {
        super(Data.BIND_TRANSMITTER);
    }

    protected Response createResponse() {
        return new BindTransmitterResp();
    }

    public boolean isTransmitter() {
        return true;
    }

    public boolean isReceiver() {
        return false;
    }
}
