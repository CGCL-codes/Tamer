package org.opennms.netmgt.snmp.snmp4j;

import org.opennms.netmgt.snmp.SnmpV2TrapBuilder;
import org.snmp4j.PDU;

public class Snmp4JV2InformBuilder extends Snmp4JV2TrapBuilder implements SnmpV2TrapBuilder {

    protected Snmp4JV2InformBuilder(Snmp4JStrategy strategy) {
        super(strategy, new PDU(), PDU.INFORM);
    }
}
