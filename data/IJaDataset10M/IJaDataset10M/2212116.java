package org.smslib.modem.athandler;

import java.io.IOException;
import org.smslib.GatewayException;
import org.smslib.TimeoutException;
import org.smslib.modem.ModemGateway;

/**
 * AT Handler for Multitech modems.
 */
public class ATHandler_MultiTech extends ATHandler {

    public ATHandler_MultiTech(ModemGateway gateway) {
        super(gateway);
        terminators[11] = "\\+CRING:\\s*VOICE\\s";
        storageLocations = "SM";
    }

    public void init() throws TimeoutException, GatewayException, IOException, InterruptedException {
        super.init();
        modemDriver.write("AT+WIND=0\r");
    }
}
