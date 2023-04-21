package org.apache.pluto.driver.config.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cn.vlabs.vwb.container.services.impl.Module;
import cn.vlabs.vwb.container.services.impl.ServiceFactoryInitFailed;
import cn.vlabs.vwb.container.services.impl.SpaceConfig;

/**
 * SpaceConfig
 * 
 * @author xiejj@cnic.cn
 * 
 * @creation Mar 13, 2009 11:22:07 AM
 */
public class VWBConfig {

    public VWBConfig() {
        spaces = new HashMap<String, SpaceConfig>();
        serviceTypes = new HashMap<String, Module>();
    }

    public List<Module> getModules() {
        ArrayList<Module> list = new ArrayList<Module>();
        list.addAll(serviceTypes.values());
        return list;
    }

    public Collection<SpaceConfig> getSpaces() {
        return spaces.values();
    }

    public SpaceConfig getSpace(String id) {
        return spaces.get(id);
    }

    public void addSpace(SpaceConfig space) {
        spaces.put(space.getName(), space);
    }

    public void addServiceType(Module serviceType) {
        try {
            serviceType.init();
            serviceTypes.put(serviceType.getTypename(), serviceType);
        } catch (ServiceFactoryInitFailed e) {
            LOG.error(e.getMessage());
            LOG.error("The factory class is :" + e.getClassName());
        }
    }

    public Module getModule(String type) {
        return serviceTypes.get(type);
    }

    private HashMap<String, SpaceConfig> spaces;

    private HashMap<String, Module> serviceTypes;

    public static final String VWBCONFIG_FILE = "WEB-INF/conf/vwb-space-config.xml";

    private static final Log LOG = LogFactory.getLog(VWBConfig.class);
}
