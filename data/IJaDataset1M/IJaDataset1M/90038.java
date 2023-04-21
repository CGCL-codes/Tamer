package org.nexopenframework.management.jdbc4.monitor.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hsqldb.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nexopenframework.management.jdbc.monitor.support.PreparedStatementMonitorMBean;
import org.nexopenframework.management.jdbc4.model.Person;
import org.nexopenframework.management.jdbc4.model.Phone;
import org.nexopenframework.management.jdbc4.model.PhoneType;
import org.nexopenframework.management.jdbc4.monitor.jdbcx.MonitorableDataSource;
import org.nexopenframework.management.module.core.ModuleRepository;
import org.nexopenframework.management.monitor.core.MonitorManager;
import org.nexopenframework.management.support.MBeanServerHandle;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Simple JUnit TestCase for checking functionalities of {@link MonitorableDataSource}
 * with Hibernate3, <a href="http://proxool.sourceforge.net">Proxool</a> and Derby 10.4.x.</p>
 * 
 * @see org.nexopenframework.management.jdbc4.monitor.jdbc.DelegateDriverManagerDriver
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0.m2
 */
public class Hibernate3ProxoolDelegateDriverTest {

    /**Hibernate3 {@link SessionFactory} GoF Factory*/
    private SessionFactory sf;

    /**Manager of monitors*/
    private final MonitorManager manager = new MonitorManager();

    /**Repository of modules*/
    private final ModuleRepository repository = new ModuleRepository();

    /**JMX {@link MBeanServer} implementation for J2SE 5.0*/
    private final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

    /**HSQLDB Server*/
    private final Server server = new Server();

    @Test
    public void persistAndFind() {
        Session session = null;
        try {
            session = sf.openSession();
            final Transaction tx = session.beginTransaction();
            final Person p = new Person("Francesc", "Magdaleno", 35);
            p.addPhone(new Phone("935678956", PhoneType.HOME));
            p.addPhone(new Phone("935678988", PhoneType.WORK));
            p.addPhone(new Phone("935678959", PhoneType.WORK));
            p.addPhone(new Phone("936574567", PhoneType.MOBILE));
            p.addPhone(new Phone("936574537", PhoneType.MOBILE));
            p.addPhone(new Phone("934574567", PhoneType.WORK));
            session.persist(p);
            final Person p2 = new Person("Francesc Xavier", "Escar", 35);
            p2.addPhone(new Phone("935678956", PhoneType.HOME));
            p2.addPhone(new Phone("935678988", PhoneType.WORK));
            p2.addPhone(new Phone("935678959", PhoneType.WORK));
            p2.addPhone(new Phone("936574567", PhoneType.MOBILE));
            p2.addPhone(new Phone("936574537", PhoneType.MOBILE));
            p2.addPhone(new Phone("934574567", PhoneType.WORK));
            p2.addPhone(new Phone("934574567", PhoneType.WORK));
            for (int k = 0; k < 10; k++) {
                p2.addPhone(new Phone("93457456" + k, PhoneType.WORK));
            }
            for (int k = 0; k < 10; k++) {
                p2.addPhone(new Phone("93657456" + k, PhoneType.MOBILE));
            }
            session.persist(p2);
            tx.commit();
            final ObjectName monitor = manager.getMonitor(PreparedStatementMonitorMBean.MONITOR_KEY);
            final PreparedStatementMonitorMBean mbean = (PreparedStatementMonitorMBean) MBeanServerInvocationHandler.newProxyInstance(mbeanServer, monitor, PreparedStatementMonitorMBean.class, true);
            assertNotNull(mbean);
            System.out.println("[INFO] Open java.sql.PreparedStatements :: " + mbean.getOpenPreparedStatements());
            System.out.println("[INFO] Closed java.sql.PreparedStatements :: " + mbean.getClosedPreparedStatements());
            assertTrue(mbean.getOpenPreparedStatements() == mbean.getClosedPreparedStatements());
            assertEquals(0, mbean.getCountOfSlowQueries());
            session.evict(p);
            final Person p3 = (Person) session.load(Person.class, p.getId());
            assertEquals(p.getAge(), p3.getAge());
            assertEquals(p.getName(), p3.getName());
            assertEquals(p.getSurname(), p3.getSurname());
            assertEquals(p.getPhones().size(), p3.getPhones().size());
            System.out.println("[INFO] (2) Open java.sql.PreparedStatements :: " + mbean.getOpenPreparedStatements());
            System.out.println("[INFO] (2) Closed java.sql.PreparedStatements :: " + mbean.getClosedPreparedStatements());
            assertTrue(mbean.getOpenPreparedStatements() == mbean.getClosedPreparedStatements());
            assertEquals(0, mbean.getCountOfSlowQueries());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Before
    public void init() {
        server.setDatabaseName(0, "nexopen.proxool");
        server.setPort(9102);
        server.setDatabasePath(0, "target/hsqldb");
        server.start();
        new MBeanServerHandle(mbeanServer);
        manager.setServer(mbeanServer);
        manager.init();
        repository.setServer(mbeanServer);
        repository.init();
        final AnnotationConfiguration cfg = new AnnotationConfiguration().addAnnotatedClass(Person.class);
        cfg.addAnnotatedClass(Phone.class).configure("hibernate.proxoolcfg.xml");
        sf = cfg.buildSessionFactory();
    }

    @After
    public void destroy() {
        if (sf != null) {
            sf.close();
        }
        repository.destroy();
        manager.stop();
        server.stop();
    }
}
