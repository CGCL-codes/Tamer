package Beans.Requests.MachineManagement.Components;

import java.util.ArrayList;

/**
 * This class represents the NAT engine settings of a virtual machine's network interface.
 *
 * @author Angel Sanadinov
 */
public class NATEngineSettings extends MachineManagementComponent {

    private long MTU;

    private long socketSendBufferCapacity;

    private long socketReceiveBufferCapacity;

    private long initialTCPWindowSendingSize;

    private long initialTCPWindowReceivingSize;

    private String networkName;

    private String hostIP;

    private boolean isEngineConfigurationSet;

    private String tftpPrefix;

    private String tftpBootFile;

    private String tftpNextServer;

    private boolean isTFTPConfigurationSet;

    private boolean dnsPassHostDomain;

    private boolean dnsProxy;

    private boolean dnsUseHostResolver;

    private boolean isDNSConfigurationSet;

    private ArrayList<NATEngineForwardingRule> newForwardingRules;

    private ArrayList<String> forwardingRulesForRemoval;

    private long aliasMode;

    private boolean isAliasModeSet;

    /**
     * No-argument constructor. <br><br>
     * All fields are set to their invalid values and all data needs to be
     * explicitly set.
     */
    public NATEngineSettings() {
        this.MTU = 0;
        this.socketSendBufferCapacity = 0;
        this.socketReceiveBufferCapacity = 0;
        this.initialTCPWindowSendingSize = 0;
        this.initialTCPWindowReceivingSize = 0;
        this.networkName = null;
        this.hostIP = null;
        this.isEngineConfigurationSet = false;
        this.tftpPrefix = null;
        this.tftpBootFile = null;
        this.tftpNextServer = null;
        this.isTFTPConfigurationSet = false;
        this.dnsPassHostDomain = false;
        this.dnsProxy = false;
        this.dnsUseHostResolver = false;
        this.isDNSConfigurationSet = false;
        this.aliasMode = 0;
        this.isAliasModeSet = false;
        this.newForwardingRules = null;
        this.forwardingRulesForRemoval = null;
    }

    /**
     * Sets the main NAT engine configuration.
     *
     * @param MTU the maximum transmission unit of the NAT engine (in bytes)
     * @param socketSendBufferCapacity capacity of the socket send buffer (in bytes)
     * @param socketReceiveBufferCapacity capacity of the socket receive buffer (in bytes)
     * @param initialTCPWindowSendingSize initial size of the sending TCP window (in bytes)
     * @param initialTCPWindowReceivingSize initial size of the receiving TCP window (in bytes)
     * @param networkName name of the NAT network (for DHCP leases)
     * @param hostIP host interface IP to which all open sockets will be bound
     */
    public void setEngineConfiguration(long MTU, long socketSendBufferCapacity, long socketReceiveBufferCapacity, long initialTCPWindowSendingSize, long initialTCPWindowReceivingSize, String networkName, String hostIP) {
        this.MTU = MTU;
        this.socketSendBufferCapacity = socketSendBufferCapacity;
        this.socketReceiveBufferCapacity = socketReceiveBufferCapacity;
        this.initialTCPWindowSendingSize = initialTCPWindowSendingSize;
        this.initialTCPWindowReceivingSize = initialTCPWindowReceivingSize;
        this.networkName = networkName;
        this.hostIP = hostIP;
        this.isEngineConfigurationSet = true;
    }

    /**
     * Adds a new port forwarding rule to the engine, using the supplied data.
     *
     * @param rule port forwarding rule settings
     */
    public void addNewForwardingRule(NATEngineForwardingRule rule) {
        if (newForwardingRules == null) newForwardingRules = new ArrayList<NATEngineForwardingRule>(); else ;
        newForwardingRules.add(rule);
    }

    /**
     * Adds a port forwarding rule for removal.
     *
     * @param ruleName the name of the rule to be removed
     */
    public void removeExistingForwardingRule(String ruleName) {
        if (forwardingRulesForRemoval == null) forwardingRulesForRemoval = new ArrayList<String>(); else ;
        forwardingRulesForRemoval.add(ruleName);
    }

    /**
     * Sets the NAT engine's TFTP configuration.
     *
     * @param prefix TFTP prefix attribute for the NAT engine's DHCP server
     * @param bootFile TFTP boot file attribute for the NAT engine's DHCP server
     * @param nextServer TFTP server attribute for the NAT engine's DHCP server
     */
    public void setTFTPConfiguration(String prefix, String bootFile, String nextServer) {
        this.tftpPrefix = prefix;
        this.tftpBootFile = bootFile;
        this.tftpNextServer = nextServer;
        this.isTFTPConfigurationSet = true;
    }

    /**
     * Sets the NAT engine's DNS configuration.
     *
     * @param passHostDomain whether the DHCP server should pass the host's DNS domain
     * @param dnsProxy whether the DHCP server should pass the address of the DNS proxy
     *                 and process traffic using DNS servers registered on the host.
     * @param useHostResolver Whether the DHCP server should pass the address of the DNS
     *                 proxy and process traffic using the host resolver mechanism.
     */
    public void setDNSConfiguration(boolean passHostDomain, boolean dnsProxy, boolean useHostResolver) {
        this.dnsPassHostDomain = passHostDomain;
        this.dnsProxy = dnsProxy;
        this.dnsUseHostResolver = useHostResolver;
        this.isDNSConfigurationSet = true;
    }

    /**
     * Sets the NAT engine's alias mode.
     *
     * @param mode the alias mode
     */
    public void setAliasMode(long mode) {
        this.aliasMode = mode;
        this.isAliasModeSet = true;
    }

    /**
     * Retrieves the NAT engine's MTU.
     *
     * @return the MTU
     *
     * @see NATEngineSettings#setEngineConfiguration(long, long, long, long,
     *      long, java.lang.String, java.lang.String) More Info
     */
    public long getMTU() {
        return MTU;
    }

    /**
     * Retrieves the NAT engine's socket send buffer capacity.
     *
     * @return the capacity (in bytes)
     *
     * @see NATEngineSettings#setEngineConfiguration(long, long, long, long,
     *      long, java.lang.String, java.lang.String) More Info
     */
    public long getSocketSendBufferCapacity() {
        return socketSendBufferCapacity;
    }

    /**
     * Retrieves the NAT engine's socket receive buffer capacity.
     *
     * @return the capacity (in bytes)
     *
     * @see NATEngineSettings#setEngineConfiguration(long, long, long, long,
     *      long, java.lang.String, java.lang.String) More Info
     */
    public long getSocketReceiveBufferCapacity() {
        return socketReceiveBufferCapacity;
    }

    /**
     * Retrieves the NAT engine's initial TCP window sending size.
     *
     * @return the size (in bytes)
     *
     * @see NATEngineSettings#setEngineConfiguration(long, long, long, long,
     *      long, java.lang.String, java.lang.String) More Info
     */
    public long getInitialTCPWindowSendingSize() {
        return initialTCPWindowSendingSize;
    }

    /**
     * Retrieves the NAT engine's initial TCP windows receiving size.
     *
     * @return the size (in bytes)
     *
     * @see NATEngineSettings#setEngineConfiguration(long, long, long, long,
     *      long, java.lang.String, java.lang.String) More Info
     */
    public long getInitialTCPWindowReceivingSize() {
        return initialTCPWindowReceivingSize;
    }

    /**
     * Retrieves the NAT engine's network name.
     *
     * @return the network name
     *
     * @see NATEngineSettings#setEngineConfiguration(long, long, long, long,
     *      long, java.lang.String, java.lang.String) More Info
     */
    public String getNetworkName() {
        return networkName;
    }

    /**
     * Retrieves the NAT engine's host interface IP address.
     *
     * @return the host IP
     *
     * @see NATEngineSettings#setEngineConfiguration(long, long, long, long,
     *      long, java.lang.String, java.lang.String) More Info
     */
    public String getHostIP() {
        return hostIP;
    }

    /**
     * Returns whether the main NAT engine configuration was set.
     *
     * @return <code>true</code> if the configuration was set or <code>false</code>
     *         otherwise
     */
    public boolean isEngineConfigurationSet() {
        return isEngineConfigurationSet;
    }

    /**
     * Retrieves the NAT engine's TFTP prefix attribute.
     *
     * @return the TFTP prefix
     *
     * @see NATEngineSettings#setTFTPConfiguration(java.lang.String,
     *      java.lang.String, java.lang.String) More Info
     */
    public String getTftpPrefix() {
        return tftpPrefix;
    }

    /**
     * Retrieves the NAT engine's TFTP boot file attribute.
     *
     * @return the TFTP boot file
     *
     * @see NATEngineSettings#setTFTPConfiguration(java.lang.String,
     *      java.lang.String, java.lang.String) More Info
     */
    public String getTftpBootFile() {
        return tftpBootFile;
    }

    /**
     * Retrieves the NAT engine's TFTP server attribute.
     *
     * @return the TFTP server attribute
     *
     * @see NATEngineSettings#setTFTPConfiguration(java.lang.String,
     *      java.lang.String, java.lang.String) More Info
     */
    public String getTftpNextServer() {
        return tftpNextServer;
    }

    /**
     * Returns whether the TFTP configuration was set.
     *
     * @return <code>true</code> if the TFTP configuration was set or <code>false</code>
     *         otherwise
     */
    public boolean isTFTPConfigurationSet() {
        return isTFTPConfigurationSet;
    }

    /**
     * Retrieves the NAT engine's DNS pass host domain state.
     *
     * @return <code>true</code> if the state is enabled or <code>false</code>
     *         if it is not
     *
     * @see NATEngineSettings#setDNSConfiguration(boolean, boolean, boolean) More Info
     */
    public boolean getDnsPassHostDomain() {
        return dnsPassHostDomain;
    }

    /**
     * Retrieves the NAT engine's DNS proxy state.
     *
     * @return <code>true</code> if the state is enabled or <code>false</code>
     *         if it is not
     *
     * @see NATEngineSettings#setDNSConfiguration(boolean, boolean, boolean) More Info
     */
    public boolean getDnsProxy() {
        return dnsProxy;
    }

    /**
     * Retrieves the NAT engine's DNS use host resolver state.
     *
     * @return <code>true</code> if the state is enabled or <code>false</code>
     *         if it is not
     *
     * @see NATEngineSettings#setDNSConfiguration(boolean, boolean, boolean) More Info
     */
    public boolean getDnsUseHostResolver() {
        return dnsUseHostResolver;
    }

    /**
     * Returns whether the DNS configuration was set.
     *
     * @return <code>true</code> if the DNS configuration was set or <code>false</code>
     *         otherwise
     */
    public boolean isDNSConfigurationSet() {
        return isDNSConfigurationSet;
    }

    /**
     * Retrieves the NAT engine's alias mode.
     *
     * @return the alias mode
     */
    public long getAliasMode() {
        return aliasMode;
    }

    /**
     * Returns whether alias mode was set.
     *
     * @return <code>true</code> if alias mode was set or <code>false</code> otherwise
     */
    public boolean isAliasModeSet() {
        return isAliasModeSet;
    }

    /**
     * Retrieves all port forwarding rules for removal.
     *
     * @return a list of port forwarding rules for removal
     */
    public ArrayList<String> getForwardingRulesForRemoval() {
        return forwardingRulesForRemoval;
    }

    /**
     * Retrieves all new port forwarding rules.
     *
     * @return a list of new port forwarding rules
     */
    public ArrayList<NATEngineForwardingRule> getNewForwardingRules() {
        return newForwardingRules;
    }

    /**
     * Checks the validity of all available NAT port forwarding rules.
     *
     * @return <code>true</code> if the rules are valid or <code>false</code> otherwise
     */
    private boolean areNATForwardingRulseValid() {
        if (newForwardingRules != null) {
            for (NATEngineForwardingRule rule : newForwardingRules) {
                if (rule != null && !rule.isValid()) return false; else ;
            }
        } else ;
        return true;
    }

    @Override
    public boolean isValid() {
        if ((!isEngineConfigurationSet || (MTU >= 0 && socketSendBufferCapacity >= 0 && socketReceiveBufferCapacity >= 0 && initialTCPWindowSendingSize >= 0 && initialTCPWindowReceivingSize >= 0)) && areNATForwardingRulseValid()) return true; else return false;
    }
}
