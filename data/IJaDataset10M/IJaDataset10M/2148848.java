package com.continuent.tungsten.manager.resource.proxy;

import java.util.HashMap;
import java.util.Map;
import com.continuent.tungsten.commons.cluster.resource.logical.DataService;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.directory.Directory;
import com.continuent.tungsten.manager.core.RequestStatus;
import com.continuent.tungsten.manager.core.ServiceManagerMBean;
import com.continuent.tungsten.manager.core.SuccessCriterion;
import com.continuent.tungsten.manager.exception.ClusterManagerException;
import com.continuent.tungsten.manager.jmx.ExtendedDynamicMBeanExec;

public class ClusterServicesProxy extends ExtendedDynamicMBeanExec {

    private static final String MANAGER_PROCESS_NAME = "manager";

    private static final int SERVICE_MGR_PORT = 9997;

    private static final String MANAGER_RESOURCEMANAGER_NAME = "ClusterManagementHelper";

    private static final String DATASERVICEGET = "dataServiceGet";

    private static final String GLOBALDATASERVICEGET = "globalDataServiceGet";

    public ClusterServicesProxy(ServiceManagerMBean serviceMgr, long connectionID) throws Exception {
        super(serviceMgr, connectionID, MANAGER_PROCESS_NAME, MANAGER_RESOURCEMANAGER_NAME, SERVICE_MGR_PORT);
    }

    public DataService dataServiceGet(ServiceManagerMBean remoteServiceMgr, String siteName, String clusterName, String remoteManagerHost, String dataServiceName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("dataServiceName", dataServiceName);
        RequestStatus status = execRMMethod(remoteServiceMgr, siteName, clusterName, remoteManagerHost, DATASERVICEGET, args, remoteManagerHost, SuccessCriterion.ALL_MEMBERS, -1, false);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Cannot get dataservice %s,", dataServiceName), status.getException());
        }
        return (DataService) status.chooseResponse();
    }

    public DataService globalDataServiceGet(ServiceManagerMBean remoteServiceMgr, String siteName, String clusterName, String remoteManagerHost, String dataServiceName) throws ClusterManagerException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("dataServiceName", dataServiceName);
        RequestStatus status = execRMMethod(remoteServiceMgr, siteName, clusterName, remoteManagerHost, GLOBALDATASERVICEGET, args, remoteManagerHost, SuccessCriterion.ALL_MEMBERS, -1, false);
        if (status.getStatus() != RequestStatus.Status.SUCCESS) {
            throw new ClusterManagerException(status, String.format("Cannot get dataservice %s,", dataServiceName), status.getException());
        }
        return (DataService) status.chooseResponse();
    }
}
