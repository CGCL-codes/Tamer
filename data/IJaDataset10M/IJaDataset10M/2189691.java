package com.amazon.merchants.jmx;

import javax.management.*;
import javax.management.MBeanServer;

public class MBeanServerManager {

    private static MBeanServerManager _instance;

    private MBeanServer mbeanServer;

    private MBeanServerManager() {
        mbeanServer = MBeanServerFactory.createMBeanServer();
    }

    public static MBeanServer server() {
        return instance().getServer();
    }

    private static MBeanServerManager instance() {
        if (_instance == null) {
            _instance = new MBeanServerManager();
        }
        return _instance;
    }

    private MBeanServer getServer() {
        return mbeanServer;
    }
}
