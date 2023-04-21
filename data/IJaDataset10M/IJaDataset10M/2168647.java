package org.opennms.web.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.dao.db.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.db.OpenNMSConfigurationExecutionListener;
import org.opennms.netmgt.dao.db.TemporaryDatabaseExecutionListener;
import org.opennms.web.filter.Filter;
import org.opennms.web.notification.filter.AcknowledgedByFilter;
import org.opennms.web.notification.filter.NotificationCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ OpenNMSConfigurationExecutionListener.class, TemporaryDatabaseExecutionListener.class, DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-dao.xml", "classpath*:/META-INF/opennms/component-dao.xml", "classpath:/jdbcWebNotificationRepositoryTestContext.xml" })
@JUnitTemporaryDatabase()
public class JdbcWebNotificationRepositoryTest {

    @Autowired
    DatabasePopulator m_dbPopulator;

    @Autowired
    WebNotificationRepository m_notificationRepo;

    @Before
    public void setUp() {
        assertNotNull(m_notificationRepo);
        m_dbPopulator.populateDatabase();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testNotificationCount() {
        List<Filter> filterList = new ArrayList<Filter>();
        Filter[] filters = filterList.toArray(new Filter[0]);
        AcknowledgeType ackType = AcknowledgeType.UNACKNOWLEDGED;
        int notificationCount = m_notificationRepo.countMatchingNotifications(new NotificationCriteria(ackType, filters));
        assertEquals(1, notificationCount);
    }

    @Test
    public void testGetMatchingNotifications() {
        List<Filter> filterList = new ArrayList<Filter>();
        int limit = 10;
        int multiple = 0;
        AcknowledgeType ackType = AcknowledgeType.UNACKNOWLEDGED;
        SortStyle sortStyle = SortStyle.DEFAULT_SORT_STYLE;
        Filter[] filters = filterList.toArray(new Filter[0]);
        Notification[] notices = m_notificationRepo.getMatchingNotifications(new NotificationCriteria(filters, sortStyle, ackType, limit, limit * multiple));
        assertEquals(1, notices.length);
        assertEquals("This is a test notification", notices[0].getTextMessage());
    }

    @Test
    public void testGetNotification() {
        Notification notice = m_notificationRepo.getNotification(1);
        assertNotNull(notice);
    }

    @Test
    public void testAcknowledgeNotification() {
        m_notificationRepo.acknowledgeMatchingNotification("TestUser", new Date(), new NotificationCriteria());
        int notifCount = m_notificationRepo.countMatchingNotifications(new NotificationCriteria(new AcknowledgedByFilter("TestUser")));
        assertEquals(1, notifCount);
    }
}
