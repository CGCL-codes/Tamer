package uk.co.westhawk.snmp.event;

import uk.co.westhawk.snmp.stack.*;

/**
 * The TrapEvent class. This class is delivered when a trap is received.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.8 $ $Date: 2006/02/09 14:30:18 $
 */
public class TrapEvent extends DecodedPduEvent {

    private static final String version_id = "@(#)$Id: TrapEvent.java,v 1.8 2006/02/09 14:30:18 birgit Exp $ Copyright Westhawk Ltd";

    /** 
 * The constructor for a decoded trap pdu event. The SnmpContext
 * classes will fire decoded trap pdu events.
 *
 * @param source The source (SnmpContext) of the event
 * @param pdu The pdu
 * @param port The remote port number of the host where the pdu came from
 *
 */
    public TrapEvent(Object source, Pdu pdu, int port) {
        super(source, pdu, port);
    }
}
