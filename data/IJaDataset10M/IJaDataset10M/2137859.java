package mx4j.examples.mbeans.dynamic;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.monitor.GaugeMonitor;

/**
 * Purpose of this example is to show how to use DynamicMBean in general, with the help
 * of the {@link mx4j.AbstractDynamicMBean AbstractDynamicMBean} class, see
 * {@link DynamicService}.
 * It also shows usage of the Monitor classes.
 *
 * @version $Revision: 1.4 $
 */
public class DynamicMBeanExample {

    public static void main(String[] args) throws Exception {
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        DynamicService serviceMBean = new DynamicService();
        ObjectName serviceName = new ObjectName("examples", "mbean", "dynamic");
        server.registerMBean(serviceMBean, serviceName);
        GaugeMonitor monitorMBean = new GaugeMonitor();
        ObjectName monitorName = new ObjectName("examples", "monitor", "gauge");
        server.registerMBean(monitorMBean, monitorName);
        monitorMBean.setThresholds(new Integer(8), new Integer(4));
        monitorMBean.setNotifyHigh(true);
        monitorMBean.setNotifyLow(true);
        monitorMBean.setDifferenceMode(false);
        monitorMBean.addObservedObject(serviceName);
        monitorMBean.setObservedAttribute("ConcurrentClients");
        monitorMBean.setGranularityPeriod(50L);
        monitorMBean.addNotificationListener(new NotificationListener() {

            public void handleNotification(Notification notification, Object handback) {
                System.out.println(notification);
            }
        }, null, null);
        monitorMBean.start();
        serviceMBean.start();
    }
}
