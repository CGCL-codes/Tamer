package org.parallelj.mda.rt.server;

import java.util.List;
import org.parallelj.mda.rt.controlflow.engine.config.ConfigurationProcessor;

/**
 * Class that reads the ParallelJ Configuration, looking for information related
 * to ParallelJ Server
 * 
 * @author Atos Worldline
 * 
 */
public class ParallelJServerConfiguration extends ConfigurationProcessor {

    private static String TAG_MBEAN = "mbean";

    private static String TAG_CLASS = "class";

    private static String MBEAN_TAG_SERVER = "mbean-server";

    private static String TELNET_TAG_SERVER = "telnet-server";

    private static String TAG_SERVER_HOST = "host";

    private static String TAG_SERVER_PORT = "port";

    public static List<String> getMBeanNames() {
        return ConfigurationProcessor.readConfiguration(TAG_MBEAN, TAG_CLASS);
    }

    public static List<String> getMBeanServerHosts() {
        return ConfigurationProcessor.readConfiguration(MBEAN_TAG_SERVER, TAG_SERVER_HOST);
    }

    public static List<String> getMBeanServerPorts() {
        return ConfigurationProcessor.readConfiguration(MBEAN_TAG_SERVER, TAG_SERVER_PORT);
    }

    public static List<String> getTelnetServerHosts() {
        return ConfigurationProcessor.readConfiguration(TELNET_TAG_SERVER, TAG_SERVER_HOST);
    }

    public static List<String> getTelnetServerPorts() {
        return ConfigurationProcessor.readConfiguration(TELNET_TAG_SERVER, TAG_SERVER_PORT);
    }
}
