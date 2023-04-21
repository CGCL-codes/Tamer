package org.sempere.commons.jmx;

import org.junit.*;
import javax.management.*;
import java.lang.management.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests class for JMXManagerImpl class.
 *
 * @author bsempere
 */
public class JMXManagerImplTest {

    private JMXManagerImpl manager;

    @Before
    public void before() throws Exception {
        this.manager = new JMXManagerImpl();
    }

    @Test
    public void newInstanceWithoutParameter() throws Exception {
        JMXManagerImpl instance = new JMXManagerImpl();
        assertEquals(ManagementFactory.getPlatformMBeanServer(), instance.getMBeanServerConnection());
    }

    @Test
    public void setMBeanServerConnection() throws Exception {
        MBeanServerConnection connection = mock(MBeanServerConnection.class);
        this.manager.setConnection(connection);
        assertSame(connection, this.manager.getMBeanServerConnection());
    }
}
