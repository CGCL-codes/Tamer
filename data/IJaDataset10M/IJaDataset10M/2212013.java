package org.apache.axis2.description;

import org.apache.axis2.AxisFault;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisEvent;
import org.apache.axis2.i18n.Messages;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AxisServiceGroup extends AxisDescription {

    private boolean foundWebResources;

    private ArrayList<String> modulesList = new ArrayList<String>();

    private HashMap<String, ModuleConfiguration> moduleConfigmap;

    private ClassLoader serviceGroupClassLoader;

    private String serviceGroupName;

    public AxisServiceGroup() {
        moduleConfigmap = new HashMap<String, ModuleConfiguration>();
    }

    public AxisServiceGroup(AxisConfiguration axisDescription) {
        this();
        setParent(axisDescription);
    }

    /**
     * Adds module configuration, if there is moduleConfig tag in service.
     *
     * @param moduleConfiguration the ModuleConfiguration to add
     */
    public void addModuleConfig(ModuleConfiguration moduleConfiguration) {
        if (moduleConfigmap == null) {
            moduleConfigmap = new HashMap<String, ModuleConfiguration>();
        }
        moduleConfigmap.put(moduleConfiguration.getModuleName(), moduleConfiguration);
    }

    public void addModuleref(String moduleref) {
        modulesList.add(moduleref);
    }

    public void addService(AxisService service) throws AxisFault {
        if (service == null) {
            return;
        }
        if (serviceGroupName == null) {
            serviceGroupName = service.getName();
        }
        service.setParent(this);
        AxisConfiguration axisConfig = getAxisConfiguration();
        if (axisConfig != null) {
            for (Iterator<AxisModule> iterator = getEngagedModules().iterator(); iterator.hasNext(); ) {
                Object o = iterator.next();
                AxisModule axisModule;
                if (o instanceof AxisModule) {
                    axisModule = (AxisModule) o;
                } else if (o instanceof String) {
                    String moduleName = (String) o;
                    axisModule = axisConfig.getModule(moduleName);
                    if (axisModule == null) {
                        throw new AxisFault(Messages.getMessage("modulenotavailble", moduleName));
                    }
                } else {
                    throw new AxisFault(Messages.getMessage("modulenotavailble"));
                }
                service.engageModule(axisModule);
            }
        }
        service.setLastUpdate();
        addChild(service);
        if (axisConfig != null) {
            axisConfig.addToAllServicesMap(service);
        }
    }

    /**
     *
     * @param service
     * @throws Exception
     * @deprecated please use addService() instead
     */
    public void addToGroup(AxisService service) throws Exception {
        addService(service);
    }

    /**
     * When a module gets engaged on a ServiceGroup, we have to engage it for each Service.
     *
     * @param module the newly-engaged AxisModule
     * @param engager
     * @throws AxisFault if there is a problem
     */
    protected void onEngage(AxisModule module, AxisDescription engager) throws AxisFault {
        for (Iterator<AxisService> serviceIter = getServices(); serviceIter.hasNext(); ) {
            AxisService axisService = (AxisService) serviceIter.next();
            axisService.engageModule(module, engager);
        }
    }

    public void onDisengage(AxisModule module) throws AxisFault {
        for (Iterator<AxisService> serviceIter = getServices(); serviceIter.hasNext(); ) {
            AxisService axisService = (AxisService) serviceIter.next();
            axisService.disengageModule(module);
        }
    }

    public void removeService(String name) throws AxisFault {
        AxisService service = getService(name);
        if (service != null) {
            getAxisConfiguration().notifyObservers(AxisEvent.SERVICE_REMOVE, service);
        }
        removeChild(name);
    }

    public ModuleConfiguration getModuleConfig(String moduleName) {
        return (ModuleConfiguration) moduleConfigmap.get(moduleName);
    }

    public ArrayList<String> getModuleRefs() {
        return modulesList;
    }

    public AxisService getService(String name) throws AxisFault {
        return (AxisService) getChild(name);
    }

    public ClassLoader getServiceGroupClassLoader() {
        return serviceGroupClassLoader;
    }

    public String getServiceGroupName() {
        return serviceGroupName;
    }

    public Iterator<AxisService> getServices() {
        return (Iterator<AxisService>) getChildren();
    }

    public void setAxisDescription(AxisConfiguration axisDescription) {
        setParent(axisDescription);
    }

    public void setServiceGroupClassLoader(ClassLoader serviceGroupClassLoader) {
        this.serviceGroupClassLoader = serviceGroupClassLoader;
    }

    public void setServiceGroupName(String serviceGroupName) {
        this.serviceGroupName = serviceGroupName;
    }

    public Object getKey() {
        return this.serviceGroupName;
    }

    public boolean isFoundWebResources() {
        return foundWebResources;
    }

    public void setFoundWebResources(boolean foundWebResources) {
        this.foundWebResources = foundWebResources;
    }
}
