package net.sbbi.upnp.jmx.upnp;

import java.io.IOException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import net.sbbi.upnp.jmx.UPNPMBeanDevice;

/**
 * Basic implementation, will deploy all MBeans as simple UPNP devices
 * @author <a href="mailto:superbonbon@sbbi.net">SuperBonBon</a>
 * @version 1.0
 */
public class UPNPMBeanBuilderImpl implements UPNPMBeanBuilder {

    protected String domain = "sbbi.net";

    protected String vendor = "SuperBonBon Industries";

    public boolean select(ObjectName objectName, String className) {
        return true;
    }

    public UPNPMBeanDevice buildUPNPMBean(MBeanServer server, ObjectInstance objectInstance, MBeanInfo info) throws IOException {
        String descr = info.getDescription();
        if (descr == null) descr = "No MBean description available";
        String beanName = objectInstance.getObjectName().getKeyProperty("name");
        if (beanName == null) beanName = "No MBean name available";
        UPNPMBeanDevice dv = new UPNPMBeanDevice(domain, objectInstance.getClassName(), 1, vendor, descr, beanName, objectInstance.getObjectName().toString());
        dv.addService(info, objectInstance.getObjectName(), server, Integer.toString(info.getClassName().hashCode()), info.getClassName(), 1);
        return dv;
    }
}
