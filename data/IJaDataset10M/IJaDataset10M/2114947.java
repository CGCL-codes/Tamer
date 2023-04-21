package org.opennms.netmgt.poller.monitors;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.util.Map;
import org.apache.log4j.Level;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ParameterMap;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.Distributable;
import org.opennms.netmgt.poller.DistributionContext;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.NetworkInterface;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;

@Distributable(DistributionContext.DAEMON)
public final class OmsaStorageMonitor extends SnmpMonitorStrategy {

    private static final String m_serviceName = "OMSAStorage";

    static final String snmpAgentConfigKey = "org.opennms.netmgt.snmp.SnmpAgentConfig";

    private static final String virtualDiskRollUpStatus = ".1.3.6.1.4.1.674.10893.1.20.140.1.1.19";

    private static final String arrayDiskLogicalConnectionVirtualDiskNumber = ".1.3.6.1.4.1.674.10893.1.20.140.3.1.5";

    private static final String arrayDiskNexusID = ".1.3.6.1.4.1.674.10893.1.20.130.4.1.26";

    private static final String arrayDiskLogicalConnectionArrayDiskNumber = ".1.3.6.1.4.1.674.10893.1.20.140.3.1.3";

    private static final String arrayDiskState = ".1.3.6.1.4.1.674.10893.1.20.130.4.1.4";

    public String serviceName() {
        return m_serviceName;
    }

    public void initialize(Map<String, Object> parameters) {
        try {
            SnmpPeerFactory.init();
        } catch (MarshalException ex) {
            log().fatal("initialize: Failed to load SNMP configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (ValidationException ex) {
            log().fatal("initialize: Failed to load SNMP configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (IOException ex) {
            log().fatal("initialize: Failed to load SNMP configuration", ex);
            throw new UndeclaredThrowableException(ex);
        }
        return;
    }

    public void initialize(MonitoredService svc) {
        NetworkInterface iface = svc.getNetInterface();
        super.initialize(svc);
        InetAddress ipAddr = (InetAddress) iface.getAddress();
        SnmpAgentConfig agentConfig = SnmpPeerFactory.getInstance().getAgentConfig(ipAddr);
        if (log().isDebugEnabled()) {
            log().debug("initialize: SnmpAgentConfig address: " + agentConfig);
            log().debug("initialize: setting SNMP peer attribute for interface " + ipAddr.getHostAddress());
        }
        iface.setAttribute(snmpAgentConfigKey, agentConfig);
        log().debug("initialize: interface: " + agentConfig.getAddress() + " initialized.");
        return;
    }

    public PollStatus poll(MonitoredService svc, Map<String, Object> parameters) {
        NetworkInterface iface = svc.getNetInterface();
        PollStatus status = PollStatus.available();
        InetAddress ipaddr = (InetAddress) iface.getAddress();
        String returnValue = new String();
        SnmpAgentConfig agentConfig = configureAgent(parameters, iface, ipaddr);
        Integer virtualDiskNumber = ParameterMap.getKeyedInteger(parameters, "virtualDiskNumber", 1);
        if (log().isDebugEnabled()) log().debug("poll: service= SNMP address= " + agentConfig);
        try {
            if (log().isDebugEnabled()) {
                log().debug("OMSAStorageMonitor.poll: SnmpAgentConfig address: " + agentConfig);
            }
            SnmpObjId virtualDiskRollUpStatusSnmpObject = SnmpObjId.get(virtualDiskRollUpStatus + "." + virtualDiskNumber);
            SnmpValue virtualDiskRollUpStatus = SnmpUtils.get(agentConfig, virtualDiskRollUpStatusSnmpObject);
            if (virtualDiskRollUpStatus == null || virtualDiskRollUpStatus.isNull()) {
                log().debug("SNMP poll failed: no results, addr=" + ipaddr.getHostAddress() + " oid=" + virtualDiskRollUpStatusSnmpObject);
                return PollStatus.unavailable();
            }
            if (virtualDiskRollUpStatus.toInt() != 3) {
                returnValue = "log vol(" + virtualDiskNumber + ") degraded";
                SnmpObjId arrayDiskLogicalConnectionVirtualDiskNumberSnmpObject = SnmpObjId.get(arrayDiskLogicalConnectionVirtualDiskNumber);
                Map<SnmpInstId, SnmpValue> arrayDisks = SnmpUtils.getOidValues(agentConfig, "OMSAStorageMonitor", arrayDiskLogicalConnectionVirtualDiskNumberSnmpObject);
                SnmpObjId arrayDiskLogicalConnectionArrayDiskNumberSnmpObject = SnmpObjId.get(arrayDiskLogicalConnectionArrayDiskNumber);
                Map<SnmpInstId, SnmpValue> arrayDiskConnectionNumber = SnmpUtils.getOidValues(agentConfig, "OMSAStorageMonitor", arrayDiskLogicalConnectionArrayDiskNumberSnmpObject);
                for (Map.Entry<SnmpInstId, SnmpValue> disk : arrayDisks.entrySet()) {
                    log().debug("OMSAStorageMonitor :: arrayDiskNembers=" + disk.getValue());
                    if (disk.getValue().toInt() == virtualDiskNumber) {
                        log().debug("OMSAStorageMonitor :: Disk Found! ");
                        log().debug("OMSAStorageMonitor :: Found This Array Disk Value " + disk.getKey());
                        SnmpObjId arrayDiskStateSnmpObject = SnmpObjId.get(arrayDiskState + "." + arrayDiskConnectionNumber.get(disk.getKey()));
                        SnmpValue diskValue = SnmpUtils.get(agentConfig, arrayDiskStateSnmpObject);
                        log().debug("OmsaStorageMonitor :: Disk State=" + diskValue);
                        if (diskValue.toInt() != 3) {
                            String arrayDiskState = getArrayDiskStatus(diskValue);
                            SnmpObjId arrayDiskNexusIDSnmpObject = SnmpObjId.get(arrayDiskNexusID + "." + disk.getKey().toString());
                            SnmpValue nexusValue = SnmpUtils.get(agentConfig, arrayDiskNexusIDSnmpObject);
                            returnValue += " phy drv(" + nexusValue + ") " + arrayDiskState;
                        }
                    }
                }
                return PollStatus.unavailable(returnValue);
            }
        } catch (NumberFormatException e) {
            status = logDown(Level.ERROR, "Number operator used on a non-number " + e.getMessage());
        } catch (IllegalArgumentException e) {
            status = logDown(Level.ERROR, "Invalid Snmp Criteria: " + e.getMessage());
        } catch (Throwable t) {
            status = logDown(Level.WARN, "Unexpected exception during SNMP poll of interface " + ipaddr.getHostAddress(), t);
        }
        return status;
    }

    private SnmpAgentConfig configureAgent(Map<String, Object> parameters, NetworkInterface iface, InetAddress ipaddr) throws RuntimeException {
        SnmpAgentConfig agentConfig = (SnmpAgentConfig) iface.getAttribute(snmpAgentConfigKey);
        if (agentConfig == null) throw new RuntimeException("SnmpAgentConfig object not available for interface " + ipaddr);
        agentConfig.setTimeout(ParameterMap.getKeyedInteger(parameters, "timeout", agentConfig.getTimeout()));
        agentConfig.setRetries(ParameterMap.getKeyedInteger(parameters, "retry", ParameterMap.getKeyedInteger(parameters, "retries", agentConfig.getRetries())));
        agentConfig.setPort(ParameterMap.getKeyedInteger(parameters, "port", agentConfig.getPort()));
        return agentConfig;
    }

    private String getArrayDiskStatus(SnmpValue diskValue) {
        switch(diskValue.toInt()) {
            case 1:
                return "Ready";
            case 2:
                return "Failed";
            case 3:
                return "Online";
            case 4:
                return "Offline";
            case 6:
                return "Degraded";
            case 7:
                return "Recovering";
            case 11:
                return "Removed";
            case 15:
                return "Resynching";
            case 24:
                return "Rebuilding";
            case 25:
                return "noMedia";
            case 26:
                return "Formating";
            case 28:
                return "Running Diagnostics";
            case 35:
                return "Initializing";
            default:
                break;
        }
        return null;
    }
}
