package org.fao.geonet.services.monitoring.services;

import jeeves.resources.dbms.Dbms;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Log;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.setting.SettingManager;
import java.util.HashMap;

/**
 * Class to manage service monitoring services
 *
 */
public class ServiceMonitorManager {

    public static final String DB_MONITOR_ID = "db";

    public static final String PRINT_MONITOR_ID = "printService";

    public static final String CSW_MONITOR_ID = "cswService";

    public static final String INDEXER_MONITOR_ID = "indexService";

    public static final String FREEDISK_MONITOR_ID = "freediskService";

    private HashMap<String, ServiceMonitor> monitorServices;

    public ServiceMonitorManager(Dbms dbms, ServiceContext context, SettingManager sm) {
        Log.info(Geonet.MONITORING, "Service monitoring init");
        monitorServices = new HashMap<String, org.fao.geonet.services.monitoring.services.ServiceMonitor>();
        addService(DB_MONITOR_ID, new org.fao.geonet.services.monitoring.services.DatabaseServiceMonitor(context));
        Log.info(Geonet.MONITORING, "Added database monitor");
        addService(PRINT_MONITOR_ID, new org.fao.geonet.services.monitoring.services.PrintServiceMonitor(context));
        Log.info(Geonet.MONITORING, "Added print service monitor");
        addService(CSW_MONITOR_ID, new org.fao.geonet.services.monitoring.services.CswServiceMonitor(context));
        Log.info(Geonet.MONITORING, "Added CSW service monitor");
        addService(INDEXER_MONITOR_ID, new org.fao.geonet.services.monitoring.services.IndexServiceMonitor(context));
        Log.info(Geonet.MONITORING, "Added indexer service monitor");
        addService(FREEDISK_MONITOR_ID, new org.fao.geonet.services.monitoring.services.FreeDiskServiceMonitor(context));
        Log.info(Geonet.MONITORING, "Added free disk service monitor");
    }

    /**
     * Adds a service to the ServiceMonitorManager
     *
     * @param serviceId             ServiceMonitor identifier
     * @param serviceMonitor        Class that implements the ServiceMonitorManager
     */
    public void addService(String serviceId, ServiceMonitor serviceMonitor) {
        monitorServices.put(serviceId, serviceMonitor);
    }

    /**
     * Creates a report status of all monitored services
     *
     * @param context
     * @param report
     */
    public void createServicesStatusReport(ServiceContext context, ServiceMonitorReport report) {
        for (ServiceMonitor servMonitor : monitorServices.values()) {
            try {
                servMonitor.exec(context, report);
            } catch (ServiceMonitorException ex) {
                Log.error(Geonet.MONITORING, "Error code: " + ex.getCode() + ", Exception:" + ex.getMessage());
            }
        }
    }

    /**
     * Cheacks the status of monitored services. Throws ServiceMonitorException if a service fails
     *
     * @param context
     * @param report
     */
    public void checkServicesStatus(ServiceContext context, ServiceMonitorReport report) throws ServiceMonitorException {
        for (ServiceMonitor servMonitor : monitorServices.values()) {
            servMonitor.exec(context, report);
        }
    }
}
