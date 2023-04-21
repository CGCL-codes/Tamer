package edu.upf.da.p2p.sm;

import org.jivesoftware.smack.XMPPConnection;

public class SetLogForTest {

    public static void set() {
        String loglevel = "trace";
        System.setProperty("org.apache.commons.logging.simplelog.defaultlog", loglevel);
        XMPPConnection.DEBUG_ENABLED = true;
    }
}
