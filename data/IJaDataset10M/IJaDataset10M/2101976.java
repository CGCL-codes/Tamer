package org.opennms.netmgt;

import org.opennms.netmgt.dao.db.AbstractTransactionalTemporaryDatabaseSpringContextTests;
import org.opennms.netmgt.eventd.EventIpcManager;
import org.opennms.test.DaoTestConfigBean;

/**
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class OpenNmsDaemonApplicationContextTest extends AbstractTransactionalTemporaryDatabaseSpringContextTests {

    private EventIpcManager m_eventIpcManager;

    @Override
    protected void setUpConfiguration() {
        DaoTestConfigBean bean = new DaoTestConfigBean();
        bean.afterPropertiesSet();
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath:META-INF/opennms/applicationContext-dao.xml", "classpath*:/META-INF/opennms/component-dao.xml", "classpath:META-INF/opennms/applicationContext-daemon.xml" };
    }

    public void testEventIpcManagerNonNull() throws Exception {
        assertNotNull("eventIpcManager bean", m_eventIpcManager);
    }

    public EventIpcManager getEventIpcManager() {
        return m_eventIpcManager;
    }

    public void setEventIpcManager(EventIpcManager eventIpcManager) {
        m_eventIpcManager = eventIpcManager;
    }
}
