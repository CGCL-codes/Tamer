package org.mobicents.slee.container.management.console.server.mbeans;

import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.slee.management.SleeProvider;
import javax.slee.management.SleeProviderFactory;
import javax.slee.management.SleeState;
import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeManagementMBeanUtils {

    private MBeanServerConnection mbeanServer;

    private ObjectName sleeManagementMBean;

    private ObjectName mobicentsManagementMBean;

    private DeploymentMBeanUtils deploymentMBeanUtils;

    private ServiceManagementMBeanUtils serviceManagementMBeanUtils;

    private ProfileProvisioningMBeanUtils profileProvisioningMBeanUtils;

    private ResourceManagementMBeanUtils resourceManagementMBeanUtils;

    private ActivityManagementMBeanUtils activityManagementMBeanUtils;

    private SbbEntitiesMBeanUtils sbbEntitiesMBeanUtils;

    private LogManagementMBeanUtils logManagementMBeanUtils;

    private AlarmMBeanUtils alarmMBeanUtils;

    public SleeManagementMBeanUtils() throws ManagementConsoleException {
        try {
            InitialContext ctx;
            ctx = new InitialContext();
            mbeanServer = (MBeanServerConnection) ctx.lookup("jmx/rmi/RMIAdaptor");
            SleeProvider sleeProvider = SleeProviderFactory.getSleeProvider("org.mobicents.slee.container.management.jmx.SleeProviderImpl");
            sleeManagementMBean = sleeProvider.getSleeManagementMBean();
            mobicentsManagementMBean = new ObjectName("org.mobicents.slee:service=MobicentsManagement");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
        }
        deploymentMBeanUtils = new DeploymentMBeanUtils(mbeanServer, sleeManagementMBean);
        serviceManagementMBeanUtils = new ServiceManagementMBeanUtils(mbeanServer, sleeManagementMBean);
        profileProvisioningMBeanUtils = new ProfileProvisioningMBeanUtils(mbeanServer, sleeManagementMBean);
        resourceManagementMBeanUtils = new ResourceManagementMBeanUtils(mbeanServer, sleeManagementMBean);
        activityManagementMBeanUtils = new ActivityManagementMBeanUtils(mbeanServer, sleeManagementMBean);
        sbbEntitiesMBeanUtils = new SbbEntitiesMBeanUtils(mbeanServer, sleeManagementMBean);
        logManagementMBeanUtils = new LogManagementMBeanUtils(mbeanServer, sleeManagementMBean);
        alarmMBeanUtils = new AlarmMBeanUtils(mbeanServer, sleeManagementMBean);
    }

    public DeploymentMBeanUtils getDeploymentMBeanUtils() {
        return deploymentMBeanUtils;
    }

    public ActivityManagementMBeanUtils getActivityManagementMBeanUtils() {
        return activityManagementMBeanUtils;
    }

    public ServiceManagementMBeanUtils getServiceManagementMBeanUtils() {
        return serviceManagementMBeanUtils;
    }

    public SleeState getState() throws ManagementConsoleException {
        try {
            return (SleeState) mbeanServer.getAttribute(sleeManagementMBean, "State");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
        }
    }

    public void shutdown() throws ManagementConsoleException {
        try {
            mbeanServer.invoke(sleeManagementMBean, "shutdown", new Object[] {}, new String[] {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
        }
    }

    public void start() throws ManagementConsoleException {
        try {
            mbeanServer.invoke(sleeManagementMBean, "start", new Object[] {}, new String[] {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
        }
    }

    public void stop() throws ManagementConsoleException {
        try {
            mbeanServer.invoke(sleeManagementMBean, "stop", new Object[] {}, new String[] {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
        }
    }

    public void addNotificationListener(NotificationListener listener) throws ManagementConsoleException {
        try {
            mbeanServer.addNotificationListener(sleeManagementMBean, listener, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
        }
    }

    public void removeNotificationListener(NotificationListener listener) throws ManagementConsoleException {
        try {
            mbeanServer.removeNotificationListener(sleeManagementMBean, listener);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
        }
    }

    public String getVersion() throws ManagementConsoleException {
        try {
            return (String) mbeanServer.getAttribute(mobicentsManagementMBean, "Version");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagementConsoleException(e);
        }
    }

    public ProfileProvisioningMBeanUtils getProfileProvisioningMBeanUtils() {
        return profileProvisioningMBeanUtils;
    }

    public ResourceManagementMBeanUtils getResourceManagementMBeanUtils() {
        return resourceManagementMBeanUtils;
    }

    public SbbEntitiesMBeanUtils getSbbEntitiesMBeanUtils() {
        return sbbEntitiesMBeanUtils;
    }

    public LogManagementMBeanUtils getLogManagementMBeanUtils() {
        return logManagementMBeanUtils;
    }

    public AlarmMBeanUtils getAlarmMBeanUtils() {
        return alarmMBeanUtils;
    }

    public static String doMessage(Throwable t) {
        StringBuffer sb = new StringBuffer();
        if (t instanceof MBeanException) {
            Throwable tCause = t;
            while (tCause.getCause() != null) {
                tCause = tCause.getCause();
            }
            sb.append(tCause.getMessage() + "<br />\n");
            sb.append("Check the JAIN SLEE logs for more detailed information about this error.");
        } else {
            int tick = 0;
            Throwable e = t;
            do {
                StackTraceElement[] trace = e.getStackTrace();
                if (tick++ == 0) sb.append(e.getClass().getCanonicalName() + ":" + e.getLocalizedMessage() + "<br/>\n"); else sb.append("Caused by: " + e.getClass().getCanonicalName() + ":" + e.getLocalizedMessage() + "<br/>\n");
                for (StackTraceElement ste : trace) sb.append("\t" + ste + "<br/>\n");
                e = e.getCause();
            } while (e != null);
        }
        return sb.toString();
    }
}
