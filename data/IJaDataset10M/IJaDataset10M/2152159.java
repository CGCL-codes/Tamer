package uk.co.westhawk.snmp.stack;

/**
 * Thrown to indicate that the response PDU was received OK, but the PDU
 * contains an error. 
 * 
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.5 $ $Date: 2006/01/17 17:43:54 $
 */
public class AgentException extends PduException {

    private static final String version_id = "@(#)$Id: AgentException.java,v 3.5 2006/01/17 17:43:54 birgit Exp $ Copyright Westhawk Ltd";

    /** 
 * Constructs an AgentException with no specified detail message. 
 *
 */
    public AgentException() {
        super();
    }

    /** 
 * Constructs an AgentException with the specified detail
 * message. 
 *
 * @param str The detail message.
 */
    public AgentException(String str) {
        super(str);
    }
}
