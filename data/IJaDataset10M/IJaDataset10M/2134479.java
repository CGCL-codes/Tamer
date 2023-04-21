package org.opennms.netmgt.collectd.wmi;

import org.opennms.protocols.wmi.WmiManager;
import org.opennms.protocols.wmi.WmiAgentConfig;
import org.opennms.protocols.wmi.IWmiClient;
import org.opennms.protocols.wmi.WmiClient;
import org.opennms.protocols.wmi.WmiException;
import org.opennms.netmgt.config.WmiPeerFactory;
import org.opennms.core.utils.ThreadCategory;
import org.apache.log4j.Category;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.net.InetAddress;

/**
 * <P>
 * Contains a WmiManager and WmiClient instance referring to the agent loaded via
 * the InetAddress parameter provided in the constructor. Uses the InetAddress to
 * look up the agent configuration to properly connect the client and manager
 * to the remote agent. Provides the collector with access to the client and manager
 * as well as information regarding the availability of WPM (Windows Performance Metric)
 * groups.
 * </P>
 *
 * @author <A HREF="mailto:matt.raykowski@gmail.com">Matt Raykowski </A>
 * @author <A HREF="http://www.opennsm.org">OpenNMS </A>
 */
public class WmiAgentState {

    private WmiManager m_manager;

    private IWmiClient m_wmiClient;

    private WmiAgentConfig m_agentConfig;

    private String m_address;

    private HashMap<String, WmiGroupState> m_groupStates = new HashMap<String, WmiGroupState>();

    public WmiAgentState(InetAddress address, Map parameters) {
        m_address = address.getHostAddress();
        m_agentConfig = WmiPeerFactory.getInstance().getAgentConfig(address);
        m_manager = new WmiManager(m_address, m_agentConfig.getUsername(), m_agentConfig.getPassword(), m_agentConfig.getDomain());
        try {
            m_wmiClient = new WmiClient(m_address);
        } catch (WmiException e) {
            log().error("Failed to create WMI client: " + e.getMessage(), e);
        }
    }

    public void connect() {
        try {
            m_wmiClient.connect(m_agentConfig.getDomain(), m_agentConfig.getUsername(), m_agentConfig.getPassword());
        } catch (WmiException e) {
            log().error("Failed to connect to host: " + e.getMessage(), e);
        }
    }

    public String getAddress() {
        return m_address;
    }

    public WmiManager getManager() {
        return m_manager;
    }

    public boolean groupIsAvailable(String groupName) {
        WmiGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            return false;
        }
        return groupState.isAvailable();
    }

    public void setGroupIsAvailable(String groupName, boolean available) {
        WmiGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            groupState = new WmiGroupState(available);
        }
        groupState.setAvailable(available);
        m_groupStates.put(groupName, groupState);
    }

    public boolean shouldCheckAvailability(String groupName, int recheckInterval) {
        WmiGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            return true;
        }
        Date lastchecked = groupState.getLastChecked();
        Date now = new Date();
        return (now.getTime() - lastchecked.getTime() > recheckInterval);
    }

    public void didCheckGroupAvailability(String groupName) {
        WmiGroupState groupState = m_groupStates.get(groupName);
        if (groupState == null) {
            log().warn("didCheckGroupAvailability called on a group without state - this is odd");
            return;
        }
        groupState.setLastChecked(new Date());
    }

    public IWmiClient getWmiClient() {
        return m_wmiClient;
    }

    public void setWmiClient(IWmiClient wmiClient) {
        this.m_wmiClient = wmiClient;
    }

    private Category log() {
        return ThreadCategory.getInstance(getClass());
    }
}
