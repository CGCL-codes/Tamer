package com.continuent.tungsten.manager.resource.proxy;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.manager.core.RequestStatus;
import com.continuent.tungsten.manager.core.ServiceManagerMBean;
import com.continuent.tungsten.manager.exception.ClusterManagerException;
import com.continuent.tungsten.manager.handler.ClusterManagementHandler;
import com.continuent.tungsten.manager.jmx.DynamicMBeanExec;

public class ClusterConfigurationManagerProxy extends DynamicMBeanExec {

    private static Logger logger = Logger.getLogger(ClusterConfigurationManagerProxy.class);

    public static final String CONFIGURATION = "configuration";

    public static final String CONFIG_PUT = "put";

    public static final String CONFIG_GET_DS = "get";

    public static final String CONFIG_RM = "remove";

    public static final String CONFIG_GET_ALL = "all";

    public static final String CONFIG_CREATE = "new";

    public ClusterConfigurationManagerProxy(ServiceManagerMBean serviceMgr, long connectionID, String clusterName, String defaultHostName) throws Exception {
        super(serviceMgr, connectionID, clusterName);
    }

    public void createConfiguration(String clusterName, String hostName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(ClusterManagementHandler.KEY_COMMAND, ClusterManagementHandler.CONFIG_CREATE);
        args.put(ClusterManagementHandler.KEY_SERVICE, clusterName);
        RequestStatus status = execConfigurationCommand(args, hostName, true);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Failed to create a new configuration for cluster '%s'", clusterName), status.getException());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, TungstenProperties> getDataSourceMap(String clusterName, String hostName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(ClusterManagementHandler.KEY_COMMAND, ClusterManagementHandler.CONFIG_GET_ALL);
        args.put(ClusterManagementHandler.KEY_SERVICE, clusterName);
        RequestStatus status = execConfigurationCommand(args, hostName, false);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Failed to get the data source map for cluster '%s', args=%s", clusterName, args), status.getException());
        }
        try {
            return (Map<String, TungstenProperties>) status.chooseResponse();
        } catch (Exception e) {
            throw new ClusterManagerException(status, String.format("Unable to get valid response"), e);
        }
    }

    public TungstenProperties getDataSource(String clusterName, String dsName, String hostName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(ClusterManagementHandler.KEY_COMMAND, ClusterManagementHandler.CONFIG_GET_DS);
        args.put(ClusterManagementHandler.KEY_SERVICE, clusterName);
        args.put(ClusterManagementHandler.KEY_DS_NAME, dsName);
        RequestStatus status = execConfigurationCommand(args, hostName, false);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Failed to get configuration for dataSource '%s'", dsName), status.getException());
        }
        return (TungstenProperties) status.chooseResponse();
    }

    public void removeDs(String clusterName, String dsName, String hostName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(ClusterManagementHandler.KEY_COMMAND, ClusterManagementHandler.CONFIG_RM);
        args.put(ClusterManagementHandler.KEY_SERVICE, clusterName);
        args.put(ClusterManagementHandler.KEY_DS_NAME, dsName);
        RequestStatus status = execConfigurationCommand(args, hostName, true);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Failed to remove configuration for dataSource '%s'", dsName), status.getException());
        }
    }

    public void writeDs(String clusterName, TungstenProperties dsToWrite, String hostName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(ClusterManagementHandler.KEY_COMMAND, ClusterManagementHandler.CONFIG_WRITE);
        args.put(ClusterManagementHandler.KEY_SERVICE, clusterName);
        args.put(ClusterManagementHandler.KEY_DS_NAME, dsToWrite.getString("name"));
        args.put(ClusterManagementHandler.KEY_DS_PROPS, dsToWrite);
        RequestStatus status = execConfigurationCommand(args, hostName, true);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Failed to write configuration for dataSource '%s'", dsToWrite), status.getException());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, TungstenProperties> sync(String clusterName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(ClusterManagementHandler.KEY_COMMAND, ClusterManagementHandler.CONFIG_SYNC);
        args.put(ClusterManagementHandler.KEY_SERVICE, clusterName);
        RequestStatus status = execConfigurationCommand(args, MEMBERNAME_ALL, true);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Failed to synchronize configuration for cluster '%s'", clusterName), status.getException());
        }
        try {
            return (Map<String, TungstenProperties>) status.chooseResponse();
        } catch (Exception e) {
            throw new ClusterManagerException(status, String.format("Unable to get valid response"), e);
        }
    }
}
