package br.gov.frameworkdemoiselle.monitoring.example.trapper;

import br.gov.frameworkdemoiselle.monitoring.annotation.SNMP;
import br.gov.frameworkdemoiselle.monitoring.annotation.snmp.MIB;
import br.gov.frameworkdemoiselle.monitoring.annotation.snmp.OID;
import br.gov.frameworkdemoiselle.monitoring.annotation.snmp.SpecificTrap;
import br.gov.frameworkdemoiselle.monitoring.annotation.snmp.type.Counter32;
import br.gov.frameworkdemoiselle.monitoring.annotation.snmp.type.Gauge32;
import br.gov.frameworkdemoiselle.monitoring.stereotype.Trapper;

/**
 * @author SERPRO
 */
@Trapper
@SNMP
@MIB(".1.1")
public class MonitoringSNMPTrapper implements IMonitoringTrapper {

    @OID(".1")
    @SpecificTrap(1)
    public void sendAvailability(final String server, final String version, final long uptime) {
    }

    @OID(".2")
    public void sendActiveSessions(final String server, @Gauge32 final int sessions) {
    }

    @SpecificTrap(3)
    public void sendHttpRequestData(final String server, final String uri, @Counter32 final long countTotal, @Counter32 final long countFail, @Gauge32 final long minDuration, @Gauge32 final long maxDuration, @Gauge32 final long avgDuration) {
    }
}
