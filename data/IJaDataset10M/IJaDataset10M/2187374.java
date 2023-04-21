package org.gridbus.broker.services.application;

import java.util.Hashtable;
import java.util.Map;
import org.apache.log4j.Logger;
import org.gridbus.broker.common.Job;
import org.gridbus.broker.common.Service;
import org.gridbus.broker.common.security.UserCredential;
import org.gridbus.broker.constants.JobStatus;
import org.gridbus.broker.constants.MiddlewareType;
import org.gridbus.broker.constants.ServiceType;
import org.gridbus.broker.exceptions.GridBrokerException;
import org.gridbus.broker.services.application.ws.DynamicInvoker;

/**
 * @author Krishna
 *
 */
public class ApplicationService extends Service {

    private String url;

    private String namespace;

    private String serviceName;

    private Map accessPoints;

    /**
	 * Logger for this class
	 */
    private final transient Logger logger = Logger.getLogger(ApplicationService.class);

    static Map activeCalls = new Hashtable();

    /**
	 * 
	 */
    public ApplicationService() {
        super();
        accessPoints = new Hashtable();
    }

    /**
	 * @return Returns the url.
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * @param url The url to set.
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * @see org.gridbus.broker.common.Service#getServiceType()
	 */
    public int getServiceType() {
        return ServiceType.APPLICATION;
    }

    /**
	 * 
	 */
    protected boolean discoverProperties(UserCredential uc) throws GridBrokerException {
        setAvailable(true);
        return this.isAvailable();
    }

    /**
	 * @return Returns the namespace.
	 */
    public String getNamespace() {
        return namespace;
    }

    /**
	 * @param namespace The namespace to set.
	 */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
	 * @see org.gridbus.broker.common.ComputeServer#queryJobStatus(org.gridbus.broker.common.Job)
	 */
    public int queryJobStatus(Job job) throws Exception {
        int status = JobStatus.UNKNOWN;
        String key = job.getHandle();
        DynamicInvoker wsCall = (DynamicInvoker) activeCalls.get(key);
        if (wsCall != null) {
            if (wsCall.isAlive()) {
                status = JobStatus.ACTIVE;
            } else {
                activeCalls.remove(key);
                if (wsCall.getFailedException() != null) {
                }
                status = JobStatus.STAGE_OUT;
            }
        } else {
            logger.warn("Job " + job.getName() + " not found in the active call lists");
        }
        return status;
    }

    /**
	 * @return service name
	 */
    public String getServiceName() {
        return serviceName;
    }

    /**
	 * @param serviceName
	 */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    static String createHandle(Job job, DynamicInvoker inv) {
        String handle = inv.getName() + "." + System.currentTimeMillis();
        return handle;
    }

    /**
	 * @return the accessPoints
	 */
    public Map getAccessPoints() {
        return accessPoints;
    }

    /**
	 * @param accessPoints the accessPoints to set
	 */
    public void setAccessPoints(Map accessPoints) {
        this.accessPoints = accessPoints;
    }
}
