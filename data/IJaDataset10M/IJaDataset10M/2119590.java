package org.opennms.protocols.snmp;

import java.net.InetAddress;

/**
 * <P>The SnmpAgentHandler interface is implemented by an object that
 * wishs to receive callbacks when an SNMP protocol data unit
 * is received from a manager.</P>
 *
 * @author  <a href="http://www.opennms.org/">OpenNMS</a>
 * @author  <a href="mailto:bsnider@seekone.com">Bob Snider</a>
 *
 */
public interface SnmpAgentHandler {

    /**
     * <P>This method is defined to handle SNMP requests
     * that are received by the session. The parameters
     * allow the handler to determine the host, port, and
     * community string of the received PDU</P>
     *
     * @param session The SNMP session
     * @param manager The manager IP address
     * @param port    The remote senders port
     * @param community   The community string
     * @param pdu     The SNMP pdu
     *
     */
    void snmpReceivedPdu(SnmpAgentSession session, InetAddress manager, int port, SnmpOctetString community, SnmpPduPacket pdu);

    /**
     * <P>This method is invoked if an error occurs in 
     * the session. The error code that represents
     * the failure will be passed in the second parameter,
     * 'error'. The error codes can be found in the class
     * SnmpAgentSession class.</P>
     *
     * <P>If a particular PDU is part of the error condition
     * it will be passed in the third parameter, 'pdu'. The
     * pdu will be of the type SnmpPduRequest or SnmpPduTrap
     * object. The handler should use the "instanceof" operator
     * to determine which type the object is. Also, the object
     * may be null if the error condition is not associated
     * with a particular PDU.</P>
     *
     * @param session The SNMP Session
     * @param error   The error condition value.
     * @param ref     The PDU reference, or potentially null.
     *            It may also be an exception.
     *
     *
     */
    void snmpAgentSessionError(SnmpAgentSession session, int error, Object ref);
}
