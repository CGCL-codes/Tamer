package net.sf.erm.service.snmp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.erm.service.TargetDevice;
import net.sf.erm.service.Service;
import net.sf.erm.service.ServiceRegistry;
import net.sf.erm.service.TaskException;
import net.sf.erm.service.config.ConfigurationService;

/**
 * @author lorban
 */
public class SnmpService extends Service {

    private static final Log log = LogFactory.getLog(SnmpService.class);

    private String[] roCommunities;

    private String[] rwCommunities;

    private Snmp snmp;

    public SnmpService() {
    }

    public synchronized void startup() throws Exception {
        if (log.isDebugEnabled()) log.debug("starting up SNMP service");
        ConfigurationService config = (ConfigurationService) ServiceRegistry.getInstance().getService(ConfigurationService.class);
        roCommunities = config.getStringArray("config/snmp/community[@type='readonly']");
        if (log.isDebugEnabled()) log.debug(roCommunities.length + " read only communities configured");
        rwCommunities = config.getStringArray("config/snmp/community[@type='readwrite']");
        if (log.isDebugEnabled()) log.debug(rwCommunities.length + " read write communities configured");
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
        if (log.isDebugEnabled()) log.debug("SNMP service started");
    }

    public synchronized void shutdown() throws Exception {
        if (log.isDebugEnabled()) log.debug("shutting down SNMP service");
        snmp.close();
        if (log.isDebugEnabled()) log.debug("SNMP service shut down");
    }

    /**
     * Tasks failures are never propagated because if a device does not respond,
     * we consider it as a network outage. In this case a SnmpServiceException
     * is thrown to inform that the task did not run successfully.
     * 
     * Note: A new target is created at each invocation. This way, the task can
     * easily override default parameters, like forcing the snmp version.
     */
    public void execute(SnmpServiceTask task, TargetDevice deviceTarget) throws SnmpServiceException {
        if (!task.requiresWriteAccess()) {
            if (log.isDebugEnabled()) log.debug("task " + task.getClass().getName() + " does not need rw community, trying ro ones first");
            if (executeUsingCommunities(roCommunities, task, deviceTarget)) {
                return;
            }
        }
        if (log.isDebugEnabled()) log.debug("trying rw communities");
        if (executeUsingCommunities(rwCommunities, task, deviceTarget)) {
            return;
        }
        throw new SnmpServiceException("device " + deviceTarget.getDeviceName() + " had no answer for task " + task.getClass().getName() + " with any configured community");
    }

    private boolean executeUsingCommunities(String[] communities, SnmpServiceTask task, TargetDevice deviceTarget) throws SnmpServiceException {
        for (int i = 0; i < communities.length; i++) {
            String community = communities[i];
            try {
                task.executeOn(createTarget(community, deviceTarget.getDeviceName(), deviceTarget.getPort(), task), snmp);
                if (log.isDebugEnabled()) log.debug("task executed successfully, no need to try other community");
                return true;
            } catch (TaskException ex) {
                if (log.isDebugEnabled()) log.debug("task did not execute successfully with this community", ex);
                continue;
            }
        }
        return false;
    }

    private CommunityTarget createTarget(String community, String deviceName, Integer port, SnmpServiceTask task) throws SnmpServiceException {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        String targetName = deviceName + "/" + port;
        if (log.isDebugEnabled()) log.debug("SNMP target is: " + targetName);
        try {
            if (log.isDebugEnabled()) log.debug("checking device name");
            InetAddress.getByName(deviceName);
            if (log.isDebugEnabled()) log.debug("creating CommunityTarget");
            target.setAddress(new UdpAddress(targetName));
        } catch (IllegalArgumentException ex) {
            throw new SnmpServiceException("invalid device configured: " + deviceName, ex);
        } catch (UnknownHostException ex) {
            throw new SnmpServiceException("unresolveable device name: " + deviceName, ex);
        }
        target.setVersion(SnmpConstants.version2c);
        target.setRetries(1);
        target.setTimeout(3000);
        return target;
    }
}
