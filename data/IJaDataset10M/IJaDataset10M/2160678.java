package org.opennms.netmgt.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.opennms.netmgt.dao.hibernate.AlarmDaoHibernate;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsCategory;
import org.springframework.orm.hibernate3.HibernateCallback;

public class AuthorizationTest extends AbstractTransactionalDaoTestCase {

    public void testAuthorizedAlarms() {
        Collection<OnmsAlarm> matching = getAlarmDao().findAll();
        assertNotNull(matching);
        assertEquals(1, matching.size());
        System.err.println(matching);
        enableAuthorizationFilter("NonExistentGroup");
        Collection<OnmsAlarm> matching2 = getAlarmDao().findAll();
        assertNotNull(matching2);
        assertEquals(0, matching2.size());
        System.err.println(matching2);
        disableAuthorizationFilter();
        Collection<OnmsAlarm> matching3 = getAlarmDao().findAll();
        assertNotNull(matching3);
        assertEquals(1, matching3.size());
        System.err.println(matching3);
    }

    public void testGetCategoriesWithAuthorizedGroups() {
        List<OnmsCategory> categories = getCategoryDao().getCategoriesWithAuthorizedGroup("RoutersGroup");
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Routers", categories.get(0).getName());
    }

    public void enableAuthorizationFilter(final String... groupNames) {
        HibernateCallback cb = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                session.enableFilter("authorizedOnly").setParameterList("userGroups", groupNames);
                return null;
            }
        };
        ((AlarmDaoHibernate) getAlarmDao()).getHibernateTemplate().execute(cb);
    }

    public void disableAuthorizationFilter() {
        HibernateCallback cb = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                session.disableFilter("authorizedOnly");
                return null;
            }
        };
        ((AlarmDaoHibernate) getAlarmDao()).getHibernateTemplate().execute(cb);
    }
}
