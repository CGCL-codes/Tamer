package org.opennms.netmgt.capsd.plugins;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.log4j.Category;
import org.opennms.netmgt.capsd.AbstractPlugin;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.core.utils.ParameterMap;
import org.opennms.core.utils.ThreadCategory;

/**
 * This class is used to test passed address for SNMP support. The configuration
 * used to determine the SNMP information is managed by the
 * {@link SnmpPeerFactorySnmpPeerFactory} class.
 * 
 * @author <A HREF="mailto:weave@oculan.com">Brian Weaver </A>
 * @author <A HREF="http://www.opennms.org">OpenNMS </A>
 * 
 */
public final class DiskUsagePlugin extends AbstractPlugin {

    /**
     * The protocol supported by this plugin
     */
    private static final String PROTOCOL_NAME = "DiskUsage";

    /**
     * The system object identifier to retreive from the remote agent.
     */
    private static final String DEFAULT_OID = ".1.3.6.1.2.1.1.2.0";

    private static final String hrStorageDescr = ".1.3.6.1.2.1.25.2.3.1.3";

    /**
     * The available match-types for this plugin
     */
    private static final int MATCH_TYPE_EXACT = 0;

    private static final int MATCH_TYPE_STARTSWITH = 1;

    private static final int MATCH_TYPE_ENDSWITH = 2;

    private static final int MATCH_TYPE_REGEX = 3;

    /**
     * Returns the name of the protocol that this plugin checks on the target
     * system for support.
     * 
     * @return The protocol name for this plugin.
     */
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    /**
     * Returns true if the protocol defined by this plugin is supported. If the
     * protocol is not supported then a false value is returned to the caller.
     * 
     * @param address
     *            The address to check for support.
     * 
     * @return True if the protocol is supported by the address.
     */
    public boolean isProtocolSupported(InetAddress address) {
        try {
            SnmpAgentConfig agentConfig = SnmpPeerFactory.getInstance().getAgentConfig(address);
            return (getValue(agentConfig, DEFAULT_OID) != null);
        } catch (Throwable t) {
            throw new UndeclaredThrowableException(t);
        }
    }

    private String getValue(SnmpAgentConfig agentConfig, String oid) {
        SnmpValue val = SnmpUtils.get(agentConfig, SnmpObjId.get(oid));
        return (val == null ? null : val.toString());
    }

    /**
     * Returns true if the protocol defined by this plugin is supported. If the
     * protocol is not supported then a false value is returned to the caller.
     * The qualifier map passed to the method is used by the plugin to return
     * additional information by key-name. These key-value pairs can be added to
     * service events if needed.
     * 
     * @param address
     *            The address to check for support.
     * @param qualifiers
     *            The map where qualification are set by the plugin.
     * 
     * @return True if the protocol is supported by the address.
     */
    public boolean isProtocolSupported(InetAddress address, Map<String, Object> qualifiers) {
        int matchType = MATCH_TYPE_EXACT;
        try {
            String disk = ParameterMap.getKeyedString(qualifiers, "disk", null);
            SnmpAgentConfig agentConfig = SnmpPeerFactory.getInstance().getAgentConfig(address);
            if (qualifiers != null) {
                if (qualifiers.get("port") != null) {
                    int port = ParameterMap.getKeyedInteger(qualifiers, "port", agentConfig.getPort());
                    agentConfig.setPort(port);
                }
                if (qualifiers.get("timeout") != null) {
                    int timeout = ParameterMap.getKeyedInteger(qualifiers, "timeout", agentConfig.getTimeout());
                    agentConfig.setTimeout(timeout);
                }
                if (qualifiers.get("retry") != null) {
                    int retry = ParameterMap.getKeyedInteger(qualifiers, "retry", agentConfig.getRetries());
                    agentConfig.setRetries(retry);
                }
                if (qualifiers.get("force version") != null) {
                    String version = (String) qualifiers.get("force version");
                    if (version.equalsIgnoreCase("snmpv1")) agentConfig.setVersion(SnmpAgentConfig.VERSION1); else if (version.equalsIgnoreCase("snmpv2") || version.equalsIgnoreCase("snmpv2c")) agentConfig.setVersion(SnmpAgentConfig.VERSION2C); else if (version.equalsIgnoreCase("snmpv3")) agentConfig.setVersion(SnmpAgentConfig.VERSION3);
                }
                if (qualifiers.get("match-type") != null) {
                    String matchTypeStr = ParameterMap.getKeyedString(qualifiers, "match-type", "exact");
                    if (matchTypeStr.equalsIgnoreCase("exact")) {
                        matchType = MATCH_TYPE_EXACT;
                    } else if (matchTypeStr.equalsIgnoreCase("startswith")) {
                        matchType = MATCH_TYPE_STARTSWITH;
                    } else if (matchTypeStr.equalsIgnoreCase("endswith")) {
                        matchType = MATCH_TYPE_ENDSWITH;
                    } else if (matchTypeStr.equalsIgnoreCase("regex")) {
                        matchType = MATCH_TYPE_REGEX;
                    } else {
                        throw new RuntimeException("Unknown value '" + matchTypeStr + "' for parameter 'match-type'");
                    }
                }
            }
            SnmpObjId hrStorageDescrSnmpObject = SnmpObjId.get(hrStorageDescr);
            Map<SnmpInstId, SnmpValue> descrResults = SnmpUtils.getOidValues(agentConfig, "DiskUsagePoller", hrStorageDescrSnmpObject);
            if (descrResults.size() == 0) {
                return false;
            }
            for (Map.Entry<SnmpInstId, SnmpValue> e : descrResults.entrySet()) {
                log().debug("capsd: SNMPwalk succeeded, addr=" + address.getHostAddress() + " oid=" + hrStorageDescrSnmpObject + " instance=" + e.getKey() + " value=" + e.getValue());
                if (isMatch(e.getValue().toString(), disk, matchType)) {
                    log().debug("Found disk '" + disk + "' (matching hrStorageDescr was '" + e.getValue().toString() + "'");
                    return true;
                }
            }
            return false;
        } catch (Throwable t) {
            throw new UndeclaredThrowableException(t);
        }
    }

    private boolean isMatch(String candidate, String target, int matchType) {
        boolean matches = false;
        log().debug("isMessage: candidate is '" + candidate + "', matching against target '" + target + "'");
        if (matchType == MATCH_TYPE_EXACT) {
            log().debug("Attempting equality match: candidate '" + candidate + "', target '" + target + "'");
            matches = candidate.equals(target);
        } else if (matchType == MATCH_TYPE_STARTSWITH) {
            log().debug("Attempting startsWith match: candidate '" + candidate + "', target '" + target + "'");
            matches = candidate.startsWith(target);
        } else if (matchType == MATCH_TYPE_ENDSWITH) {
            log().debug("Attempting endsWith match: candidate '" + candidate + "', target '" + target + "'");
            matches = candidate.endsWith(target);
        } else if (matchType == MATCH_TYPE_REGEX) {
            log().debug("Attempting endsWith match: candidate '" + candidate + "', target '" + target + "'");
            matches = Pattern.compile(target).matcher(candidate).find();
        }
        log().debug("isMatch: Match is positive");
        return matches;
    }

    private Category log() {
        return ThreadCategory.getInstance(getClass());
    }
}
