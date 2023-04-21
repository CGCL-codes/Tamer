package com.markatta.hund.core;

import com.markatta.hund.model.Host;
import com.markatta.hund.model.Status;
import com.markatta.hund.plugin.spi.HostPlugin;
import com.markatta.hund.util.PluginUtil;
import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * Executes the checking plugin for one host (not the services of the host)
 * 
 * @author johan
 */
public class HostCheckWorkItem extends WorkItem<Host> implements Serializable {

    private static final Logger logger = Logger.getLogger(HostCheckWorkItem.class);

    protected void setupLabel(Host host) {
        setLabel("Host check for " + host.getLabel());
    }

    public Status executePlugin(Host host) {
        HostPlugin hostPlugin = PluginUtil.getHostPluginInstance(host.getPlugin());
        if (hostPlugin == null) {
            throw new RuntimeException("Cannot find plugin class " + host.getPlugin() + " on classpath");
        }
        logger.debug("Checking host " + host.getLabel() + " with plugin " + hostPlugin.getClass().getName());
        return hostPlugin.check(host);
    }
}
