package org.jboss.jmx.adaptor.snmp.agent;

import javax.management.Notification;
import org.jboss.jmx.adaptor.snmp.config.notification.Mapping;
import org.snmp4j.PDUv1;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;

/**
 * <tt>TrapFactory </tt> takes care of translation of Notifications into
 * SNMP V1 and V2 traps
 *
 * Trap-PDU ::=
 * [4]
 * IMPLICIT SEQUENCE {
 *    enterprise          -- type of object generating
 *                        -- trap, see sysObjectID in [5]
 *    OBJECT IDENTIFIER,
 * agent-addr         -- address of object generating
 *    NetworkAddress, -- trap
 * generic-trap       -- generic trap type
 *    INTEGER {
 *        coldStart(0),
 *        warmStart(1),
 *        linkDown(2),
 *        linkUp(3),
 *        authenticationFailure(4),
 *        egpNeighborLoss(5),
 *        enterpriseSpecific(6)
 *    },
 * specific-trap   -- specific code, present even
 *    INTEGER,     -- if generic-trap is not
 *                 -- enterpriseSpecific
 * time-stamp      -- time elapsed between the last
 *    TimeTicks,   -- (re)initialization of the network
 *                 -- entity and the generation of the
 *                   trap
 * variable-bindings -- "interesting" information
 *    VarBindList
 * }
 * 
 * @version $Revision: 110455 $
 *
 * @author  <a href="mailto:spol@intracom.gr">Spyros Pollatos</a>
 * @author  <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
**/
public interface TrapFactory {

    /**
    * Sets the name of the file containing the notification/trap mappings,
    * the uptime clock and the trap counter
   **/
    public void set(Clock uptime, Counter count);

    public void set(Clock uptime, Counter count, RequestHandler rh);

    /**
    * Performs all the required initialisation in order for the mapper to 
    * commence operation (e.g. reading of the resource file)
   **/
    public void start() throws Exception;

    /**
    * Traslates a Notification to an SNMP V1 trap.
    *
    * @param the notification to be translated
   **/
    public PDUv1 generateV1Trap(Notification n, Mapping m, NotificationWrapper wrapper) throws MappingFailedException;

    public PDU generateV2cTrap(Notification n, Mapping m, NotificationWrapper wrapper) throws MappingFailedException;

    /**
    * Traslates a Notification to an SNMP V3 trap.
    *
    * @param the notification to be translated
   **/
    public ScopedPDU generateV3Trap(Notification n, Mapping m, NotificationWrapper wrapper) throws MappingFailedException;
}
