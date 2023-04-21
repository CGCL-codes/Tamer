package junitTests.org.jmx4odp.notificationWorkers;

import junit.framework.TestCase;
import javax.management.*;
import javax.management.timer.*;
import org.jmx4odp.j4oNet.*;
import org.jmx4odp.notificationWorkers.*;

/**
 * @author Lucas McGregor
 */
public class NotificationLoggerFixture extends MainFixture {

    public void test_sendNotification() throws Exception {
        NotificationLogger notificationLogger = new NotificationLogger();
        notificationLogger.registerLogger(this, timerObjectName, mbs);
        notificationLogger.log(this, timerNotificationType, "TIMER EVENT");
        notificationLogger.unregisterLogger();
        fail();
    }
}
