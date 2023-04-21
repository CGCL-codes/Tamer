package net.sf.unruly.proxy.other;

import net.sf.unruly.ConfigurationTestCase;
import net.sf.unruly.connection.ConnectionFactory;
import net.sf.unruly.event.impl.RulesPropertyChangeEventImpl;
import net.sf.unruly.mock.ExpectTestConnection;
import net.sf.unruly.mock.model.ClassroomProxy;
import net.sf.unruly.mock.model.PersonProxy;
import net.sf.unruly.mock.model.School;
import net.sf.unruly.mock.model.SchoolProxy;
import net.sf.unruly.proxy.impl.ProxyConfiguration;
import net.sf.unruly.proxy.ProxySessionFactory;
import net.sf.unruly.proxy.ProxySession;
import net.sf.unruly.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;
import java.util.Arrays;

/**
 * @author Jeff Drost
 */
public class ProxyConfigurationTestCase extends ConfigurationTestCase {

    private static final Log LOG = LogFactory.getLog(ProxyConfigurationTestCase.class);

    @Test
    public void testCreateSession() {
        Class[] mappedClasses = { SchoolProxy.class, PersonProxy.class, ClassroomProxy.class };
        ProxyConfiguration configuration = new ProxyConfiguration();
        ConnectionFactory connectionFactory = new ExpectTestConnection.Factory();
        configuration.setConnectionFactory(connectionFactory);
        configuration.setMappedClasses(Arrays.asList(mappedClasses));
        ProxySessionFactory sessionFactory = configuration.createSessionFactory();
        ProxySession session = sessionFactory.openSession();
        ExpectTestConnection testConnection = (ExpectTestConnection) session.getConnection();
        School school = new School();
        SchoolProxy proxy = session.getProxy(SchoolProxy.class, school);
        validateSessionObject(proxy, session);
        printDiagnostics(proxy);
        Assert.isNull(school.getName());
        proxy.setName("school.1");
        testConnection.expect(new RulesPropertyChangeEventImpl(proxy, "name", null, "school.1"));
        Assert.equals(proxy.getName(), "school.1");
        proxy.setName("school.2");
        testConnection.expect(new RulesPropertyChangeEventImpl(proxy, "name", "school.1", "school.2"));
        Assert.equals(school.getName(), "school.2");
        Assert.equals(proxy.getName(), "school.2");
        Assert.equals(school.getName(), "school.2");
        school.setName("school.5");
        Assert.equals(proxy.getName(), "school.5");
        testConnection.expect(new RulesPropertyChangeEventImpl(proxy, "name", "school.2", "school.5"));
        proxy.setName("school.3");
        testConnection.expect(new RulesPropertyChangeEventImpl(proxy, "name", "school.5", "school.3"));
        Assert.equals(proxy.getName(), "school.3");
        Assert.equals(school.getName(), "school.3");
        session.flush();
    }
}
