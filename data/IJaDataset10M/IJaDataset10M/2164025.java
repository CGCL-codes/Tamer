package org.personalsmartspace.sre.slm.api.platform;

import java.util.Properties;

/**
 * @author Mitja Vardjan
 */
public class DeploymentInfo {

    /**
	 * Key for CPU clock speed in GHz
	 */
    public static final String DEVICE_CPU_SPEED = "DEVICE_CPU_SPEED";

    /**
	 * Key for amount of RAM in GiB
	 */
    public static final String DEVICE_MEMORY = "DEVICE_MEMORY";

    /**
	 * Key for network bandwidth in Mb/s
	 */
    public static final String DEVICE_NET_BANDWIDTH = "DEVICE_NET_BANDWIDTH";

    /**
     * Key for base of service ID (preliminary part of service ID)
     */
    public static final String SERVICE_ID = "SERVICE_ID";

    /**
     *
     * @param cpuSped CPU clock speed in GHz
     * @param memory Amount of RAM in GiB
     * @param netBandwidth Network bandwidth in Mb/s
     * @param serviceIdBase Base of service ID (preliminary part of service ID)
     * @return Properties object that can be used as deploymentInfo that is
     * passed to {@link IServiceLifecycleManager#install(java.net.URI,
     * java.util.Properties, java.lang.String, java.lang.String,
     * org.personalsmartspace.onm.api.pss3p.ICallbackListener) }.
     */
    public static Properties create(String cpuSped, String memory, String netBandwidth, String serviceIdBase) {
        Properties deploymentInfo;
        deploymentInfo = new Properties();
        deploymentInfo.setProperty(DEVICE_CPU_SPEED, cpuSped);
        deploymentInfo.setProperty(DEVICE_MEMORY, memory);
        deploymentInfo.setProperty(DEVICE_NET_BANDWIDTH, netBandwidth);
        deploymentInfo.setProperty(SERVICE_ID, serviceIdBase);
        return deploymentInfo;
    }
}
