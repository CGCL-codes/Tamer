package de.objectcode.time4u.server.ejb;

import java.util.Date;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.timer.Timer;
import javax.management.timer.TimerMBean;
import javax.management.timer.TimerNotification;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.annotation.ejb.Depends;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;
import de.objectcode.time4u.server.api.local.SynchronizeServiceLocal;

@Service(objectName = "time4u:service=synchronize")
@Management(SynchronizeManagement.class)
public class SynchronizeService implements SynchronizeManagement, NotificationListener, NotificationFilter {

    private Log log = LogFactory.getLog(SynchronizeService.class);

    private static final long serialVersionUID = 7333435569640449721L;

    @Depends("time4u:service=timer")
    TimerMBean m_timer;

    @Depends("time4u:service=timer")
    ObjectName m_timerName;

    int m_timerID;

    MBeanServer m_server;

    public void start() throws Exception {
        log.debug("SynchronizeService starting");
        m_timerID = m_timer.addNotification("Time4U", "Time4U Notification", null, new Date(System.currentTimeMillis() + Timer.ONE_HOUR), Timer.ONE_HOUR);
        m_server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
        m_server.addNotificationListener(m_timerName, this, this, null);
    }

    public void stop() throws Exception {
        log.debug("SynchronizeService stopping");
        m_timer.removeAllNotifications();
        m_server.removeNotificationListener(m_timerName, this);
    }

    public boolean isNotificationEnabled(Notification pNotification) {
        if (pNotification instanceof TimerNotification) {
            TimerNotification lTimerNotification = (TimerNotification) pNotification;
            return lTimerNotification.getNotificationID().equals(m_timerID);
        }
        return false;
    }

    public void handleNotification(Notification notification, Object handback) {
        log.debug("SynchronizeService handleNotification");
        try {
            InitialContext ctx = new InitialContext();
            SynchronizeServiceLocal synchronizeService = (SynchronizeServiceLocal) ctx.lookup("time4u-server/SynchronizeServiceBean/local");
            synchronizeService.synchronizeAll();
        } catch (Exception e) {
        }
    }
}
