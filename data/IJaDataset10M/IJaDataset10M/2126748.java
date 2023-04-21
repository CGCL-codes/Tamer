package org.ws4d.osgi.management.properties;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Vector;

public class ManagementProperties {

    public static final String PROP_BUNDLE_REPOSITORIES = "bundle.repositories";

    public static final String PROP_SERIALIZER_ACTIVATE = "serializer.activation";

    public static final String PROP_OSGI_PLATFORM_ID = "platform.id";

    public static final String PROP_LOG_LEVEL = "log.level";

    public static final String PROP_LOG_DESTINATION = "log.destination";

    public static final String PROP_DPWS_LOG_LEVEL = "dpws.log.level";

    public static final String PROP_DPWS_NETWORK_INTERFACE = "dpws.networkInterface";

    public static final String PROP_EC_START = "eventConverter.start";

    public static final String PROP_EC_UUID = "ec.device.uuid";

    public static final String PROP_EC_LOG_DESTINATION = "ec.log.destination";

    public static final String PROP_EC_LOG_LEVEL = "ec.log.level";

    public static final String PROP_EC_DPWS_MONITORSERVICE_STATUS = "ec.dpwsMonitorService.status";

    public static final String PROP_EC_MONITOR_PIDS = "ec.monitorableServices.pid";

    public static final String PROP_EC_DPWS_EVENTSERVICE_STATUS = "ec.dpwsEventService.status";

    public static final String PROP_EC_REMOTE_DPWS_EVENTSERVICES = "ec.remoteEventServices.uuid";

    public static final String PROP_EC_REMOTE_DPWS_EVENTSERVICES_SECURE = "ec.remoteSecureEventServices.uuid";

    ;

    public static final String PROP_PG_START = "proxyGenerator.start";

    public static final String PROP_PG_LOG_DESTINATION = "pg.log.destination";

    public static final String PROP_PG_LOG_LEVEL = "pg.log.level";

    public static final String PROP_PG_SERVICES = "pg.services.filter";

    public static final String PROP_PG_GUI_ACTIVATE = "pg.gui.activate";

    public static final String PROP_PG_REMOVEPROXIES_ON_STOP = "pg.removeProxies.onStop";

    public static final String PROP_PG_REMOVE_FAULTY_BUNDLES = "pg.removeFaultyBundles";

    public static final String PROP_PG_SEARCH_DEVICES_ON_START = "pg.search.onStart";

    public static final String PROP_PG_REMOVEPROXIES_ON_DEVICEDOWN = "pg.removeProxies.onDeviceDown";

    public static final String PROP_PG_DATATYPES_USUPPORTED2STRING = "pg.dataTypes.unsupported2String";

    public static final String PROP_ADAPTER_START = "adapter.start";

    public static final String PROP_ADAPTER_LOG_LEVEL = "adpt.log.level";

    public static final String PROP_ADAPTER_LOG_DESTINATION = "adpt.log.destination";

    public static final String PROP_ADAPTER_SECURITY_ACTIVATE = "adpt.security.activation";

    public static final String PROP_ADAPTER_SERVICE_WHITELIST = "adpt.service.whitelist";

    public static final String PROP_ADAPTER_SECURE_SERVICE_WHITELIST = "adpt.service.secure.whitelist";

    public static final String PROP_ADAPTER_SERVICE_FALLBACK_WHITELIST = "adpt.service.fallback.whitelist";

    public static final String PROP_ADAPTER_SERIALIZER_PLUGIN_ACTIVATE = "adpt.serializer.plugin.activate";

    private boolean isConfigSet = false;

    private HashSet managementPropertiesSet;

    private ObservableHashtable propertiesTable = new ObservableHashtable();

    private static ManagementProperties instance;

    public static synchronized ManagementProperties getInstance() {
        if (instance == null) {
            instance = new ManagementProperties();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    private ManagementProperties() {
        managementPropertiesSet = new HashSet();
        managementPropertiesSet.add(PROP_OSGI_PLATFORM_ID);
        managementPropertiesSet.add(PROP_BUNDLE_REPOSITORIES);
        managementPropertiesSet.add(PROP_SERIALIZER_ACTIVATE);
        managementPropertiesSet.add(PROP_LOG_LEVEL);
        managementPropertiesSet.add(PROP_LOG_DESTINATION);
        managementPropertiesSet.add(PROP_DPWS_LOG_LEVEL);
        managementPropertiesSet.add(PROP_DPWS_NETWORK_INTERFACE);
        managementPropertiesSet.add(PROP_ADAPTER_LOG_DESTINATION);
        managementPropertiesSet.add(PROP_ADAPTER_LOG_LEVEL);
        managementPropertiesSet.add(PROP_ADAPTER_SECURITY_ACTIVATE);
        managementPropertiesSet.add(PROP_ADAPTER_START);
        managementPropertiesSet.add(PROP_ADAPTER_SERVICE_WHITELIST);
        managementPropertiesSet.add(PROP_ADAPTER_SECURE_SERVICE_WHITELIST);
        managementPropertiesSet.add(PROP_ADAPTER_SERVICE_FALLBACK_WHITELIST);
        managementPropertiesSet.add(PROP_ADAPTER_SERIALIZER_PLUGIN_ACTIVATE);
        managementPropertiesSet.add(PROP_EC_DPWS_EVENTSERVICE_STATUS);
        managementPropertiesSet.add(PROP_EC_DPWS_MONITORSERVICE_STATUS);
        managementPropertiesSet.add(PROP_EC_LOG_DESTINATION);
        managementPropertiesSet.add(PROP_EC_LOG_LEVEL);
        managementPropertiesSet.add(PROP_EC_MONITOR_PIDS);
        managementPropertiesSet.add(PROP_EC_REMOTE_DPWS_EVENTSERVICES);
        managementPropertiesSet.add(PROP_EC_REMOTE_DPWS_EVENTSERVICES_SECURE);
        managementPropertiesSet.add(PROP_EC_START);
        managementPropertiesSet.add(PROP_EC_UUID);
        managementPropertiesSet.add(PROP_PG_GUI_ACTIVATE);
        managementPropertiesSet.add(PROP_PG_LOG_DESTINATION);
        managementPropertiesSet.add(PROP_PG_LOG_LEVEL);
        managementPropertiesSet.add(PROP_PG_REMOVEPROXIES_ON_DEVICEDOWN);
        managementPropertiesSet.add(PROP_PG_REMOVEPROXIES_ON_STOP);
        managementPropertiesSet.add(PROP_PG_SEARCH_DEVICES_ON_START);
        managementPropertiesSet.add(PROP_PG_SERVICES);
        managementPropertiesSet.add(PROP_PG_START);
        managementPropertiesSet.add(PROP_PG_DATATYPES_USUPPORTED2STRING);
    }

    public void setProperty(String name, Object value) {
        if (propertiesTable == null) {
            propertiesTable = new ObservableHashtable();
        }
        propertiesTable.put(name, value);
    }

    public void setConfig(Dictionary config) {
        isConfigSet = true;
        propertiesTable.fill(config);
    }

    private Object getInternalProperty(String name) {
        if (propertiesTable == null) {
            return null;
        }
        return propertiesTable.get(name);
    }

    public boolean isConfigSet() {
        return isConfigSet;
    }

    public boolean getBooleanProperty(String name) {
        return ((Boolean) getInternalProperty(name)).booleanValue();
    }

    public int getIntProperty(String name) {
        return ((Integer) getInternalProperty(name)).intValue();
    }

    public Vector getVectorProperty(String name) {
        return (Vector) getInternalProperty(name);
    }

    public String getProperty(String name) {
        return (String) getInternalProperty(name);
    }

    public Dictionary getPropertyTable() {
        return propertiesTable;
    }

    public HashSet getManagementPropertyKeys() {
        return managementPropertiesSet;
    }

    public void registerObserver(PropertyObserver propOb) {
        this.propertiesTable.register(propOb);
    }
}
