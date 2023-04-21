package org.smslib.handler;

import java.io.IOException;
import org.smslib.*;
import org.apache.log4j.*;

public class CATHandler_SonyEricsson_W550i extends CATHandler {

    public CATHandler_SonyEricsson_W550i(CSerialDriver serialDriver, Logger log, CService srv) {
        super(serialDriver, log, srv);
    }

    protected boolean disableIndications() throws IOException {
        serialDriver.send("AT+CNMI=2,0,0,0,0\r");
        return (serialDriver.getResponse().matches("\\s+OK\\s+"));
    }
}
