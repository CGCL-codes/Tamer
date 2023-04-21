package jmri.jmrix.cmri.serial;

import java.util.Vector;

/**
 * Description:	    Stands in for the SerialTrafficController class
 * @author			Bob Jacobsen Copyright 2006
 * @version			$Revision: 17977 $
 */
public class SerialTrafficControlScaffold extends SerialTrafficController {

    public SerialTrafficControlScaffold() {
        if (log.isDebugEnabled()) log.debug("setting instance: " + this);
        self = this;
    }

    public boolean status() {
        return true;
    }

    /**
	 * record messages sent, provide access for making sure they are OK
	 */
    public Vector<SerialMessage> outbound = new Vector<SerialMessage>();

    public void sendSerialMessage(SerialMessage m, SerialListener reply) {
        if (log.isDebugEnabled()) log.debug("sendSerialMessage [" + m + "]");
        outbound.addElement(m);
    }

    /**
	 * forward a message to the listeners, e.g. test receipt
	 */
    protected void sendTestMessage(SerialMessage m, SerialListener l) {
        if (log.isDebugEnabled()) log.debug("sendTestMessage    [" + m + "]");
        notifyMessage(m, l);
        return;
    }

    public int numListeners() {
        return cmdListeners.size();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SerialTrafficControlScaffold.class.getName());
}
