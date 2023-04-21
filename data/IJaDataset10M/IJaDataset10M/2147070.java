package org.opennms.netmgt.collectd;

import java.net.InetAddress;
import java.util.Set;
import org.opennms.netmgt.model.OnmsIpInterface.PrimaryType;
import org.opennms.netmgt.snmp.SnmpAgentConfig;

/**
 * 
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public interface CollectionAgentService {

    public abstract String getHostAddress();

    public abstract int getNodeId();

    public abstract int getIfIndex();

    public abstract String getSysObjectId();

    public abstract PrimaryType getIsSnmpPrimary();

    public abstract SnmpAgentConfig getAgentConfig();

    public abstract Set<SnmpIfData> getSnmpInterfaceData();

    public abstract InetAddress getInetAddress();
}
