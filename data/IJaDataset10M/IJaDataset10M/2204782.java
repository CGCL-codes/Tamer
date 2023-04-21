package org.wapps.driver.service.impl;

import java.lang.management.ManagementFactory;
import org.wapps.driver.service.ISystemInfoService;

public class SystemInfoService implements ISystemInfoService {

    public String getOperatingSystemName() {
        return ManagementFactory.getOperatingSystemMXBean().getName();
    }

    public String getOperatingSystemArch() {
        return ManagementFactory.getOperatingSystemMXBean().getArch();
    }

    public String getOperatingSystemVersion() {
        return ManagementFactory.getOperatingSystemMXBean().getVersion();
    }

    public int getAvailableProcessors() {
        return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }
}
