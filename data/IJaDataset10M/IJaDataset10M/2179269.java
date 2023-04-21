package org.opennms.netmgt.capsd;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import org.opennms.netmgt.utils.ParameterMap;
import org.opennms.protocols.jmx.connectors.ConnectionWrapper;
import org.opennms.protocols.jmx.connectors.Jsr160ConnectionFactory;

public class Jsr160Plugin extends JMXPlugin {

    public ConnectionWrapper getMBeanServerConnection(Map parameterMap, InetAddress address) {
        return Jsr160ConnectionFactory.getMBeanServerConnection(parameterMap, address);
    }

    public String getProtocolName(Map map) {
        return ParameterMap.getKeyedString(map, "friendlyname", "jsr160");
    }

    public boolean isProtocolSupported(InetAddress address) {
        HashMap map = new HashMap();
        map.put("port", "9004");
        map.put("factory", "JMXRMI");
        map.put("friendlyname", "jsr160");
        return isProtocolSupported(address, map);
    }
}
