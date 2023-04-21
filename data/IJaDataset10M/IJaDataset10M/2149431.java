package org.nexopenframework.management.jdbc3.monitor.jdbc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import org.nexopenframework.management.jdbc3.monitor.jdbc.DriverRepository;

/**
 * <p>NexOpen Framework</p>
 *
 * <p>Simple TestCase for dealing with functionalities of {@link DriverRepository} component.</p>
 *
 * @see org.nexopenframework.management.jdbc3.monitor.jdbc.DriverRepository
 * @author Francesc Xavier Magdaleno
 * @version $Revision ,$Date 26/04/2009 19:06:14
 * @since 1.0.0.GA
 */
public class DriverRepositoryTest {

    @Test
    public void retrieveDriversByURL() throws SQLException {
        assertNotNull(DriverRepository.getDriverByUrl("jdbc:hsqldb:hsql://localhost/nexopenusePreparedStatementPool=true"));
        assertNotNull(DriverRepository.getDriverByUrl("jdbc:derby:derbyDB"));
    }

    @Test(expected = SQLException.class)
    public void notRetrieveDriversByURL() throws SQLException {
        DriverRepository.getDriverByUrl("jdbc:oracle:thin:@localhost:1521:XE");
        fail();
    }

    @Before
    public void init() {
        DriverRepository.initDrivers();
    }
}
