package net.entropysoft.jmx.plugin.jmx;

public interface IMBeanServerRegistryListener extends IMBeanServerListener {

    void mbeanServerAdded(MBeanServer mbeanServer);

    void mbeanServerRemoved(MBeanServer mBeanServer);
}
