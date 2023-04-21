package com.taobao.common.smonitor;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.log4j.Logger;

/**
 * @author xiaoxie
 * @create time��2010-4-7 ����11:56:48
 * @description
 */
public class MBeanServerService {

    private static Map<String, MBeanServer> mBeanServers = new HashMap<String, MBeanServer>();

    private static Logger default_log = Logger.getLogger(MBeanServerService.class);

    public static MBeanServer getMBeanServerByDomain(String domain) {
        if (domain == null) {
            return null;
        }
        MBeanServer mbeanServer = mBeanServers.get(domain);
        if (mbeanServer == null) {
            if ("platform".equals(domain)) {
                mbeanServer = ManagementFactory.getPlatformMBeanServer();
            } else {
                List<MBeanServer> list = MBeanServerFactory.findMBeanServer(null);
                if ((list != null) && (list.size() > 0)) {
                    for (int i = 0; i < list.size(); i++) {
                        MBeanServer ms = (MBeanServer) list.get(i);
                        if (domain.equalsIgnoreCase(ms.getDefaultDomain())) {
                            mbeanServer = ms;
                            break;
                        }
                    }
                }
            }
            if (mbeanServer != null) {
                mBeanServers.put(domain, mbeanServer);
            }
        }
        return mbeanServer;
    }

    public static Set<ObjectName> getObjectName(String filter) {
        ObjectName filterName = null;
        if (filter != null) {
            try {
                filterName = new ObjectName(filter);
            } catch (MalformedObjectNameException e) {
                default_log.error("", e);
            } catch (NullPointerException e) {
                default_log.error("", e);
            }
        }
        MBeanServer server = getMBeanServerByDomain("jboss");
        if (server != null) {
            return server.queryNames(filterName, null);
        }
        return null;
    }

    public static String getMBeanAttribute(String object, String attribute) {
        String ret = null;
        try {
            MBeanServer mbeanServer = getMBeanServerByDomain("jboss");
            if (mbeanServer != null) {
                ObjectName objectName = ObjectName.getInstance(object);
                Object value = mbeanServer.getAttribute(objectName, attribute);
                ret = (value == null) ? "" : value.toString();
            }
        } catch (Exception e) {
            default_log.error("", e);
        }
        return ret;
    }

    public static String getMBeanAttribute(ObjectName objectName, String attribute) {
        String ret = null;
        try {
            MBeanServer mbeanServer = getMBeanServerByDomain("jboss");
            if (mbeanServer != null) {
                Object value = mbeanServer.getAttribute(objectName, attribute);
                ret = (value == null) ? "" : value.toString();
            }
        } catch (Exception e) {
            default_log.error("", e);
        }
        return ret;
    }

    public static String getMBeanAttribute(String domain, String object, String attribute) {
        String ret = null;
        try {
            MBeanServer mbeanServer = getMBeanServerByDomain(domain);
            if (mbeanServer != null) {
                ObjectName objectName = ObjectName.getInstance(object);
                Object value = mbeanServer.getAttribute(objectName, attribute);
                ret = (value == null) ? "" : value.toString();
            }
        } catch (Exception e) {
            default_log.error("", e);
        }
        return ret;
    }
}
