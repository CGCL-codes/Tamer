package org.ddsteps.fixture.dbunit;

import javax.sql.DataSource;
import junit.framework.TestCase;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import org.ddsteps.dbunit.DatabaseConnectionFactory;
import org.ddsteps.dbunit.StandardConnectionFactory;
import org.ddsteps.fixture.Fixture;
import org.ddsteps.mock.MockUtils;
import org.easymock.MockControl;

public class DbUnitFixtureLoaderSupportTest extends TestCase {

    public MockControl cfControl = MockControl.createControl(DatabaseConnectionFactory.class);

    public DatabaseConnectionFactory cfMock = (DatabaseConnectionFactory) cfControl.getMock();

    public MockControl dsControl = MockControl.createControl(DataSource.class);

    public DataSource dsMock = (DataSource) dsControl.getMock();

    private final class Tested extends DbUnitFixtureLoaderSupport {

        protected CacheEntryFactory getCacheEntryFactory() {
            return null;
        }

        protected String getCacheName() {
            return ExcelDbUnitFixtureLoader.CACHE_NAME;
        }

        public Fixture loadFixture(TestCase testCase) {
            return null;
        }

        public Fixture loadFixture(String resourcename) {
            return null;
        }
    }

    /**
	 * Test method for
	 * 'org.ddsteps.fixture.dbunit.DbUnitFixtureLoaderSupport.afterPropertiesSet()'
	 * 
	 */
    public void testAfterPropertiesSet_connectionFactory() throws Exception {
        DbUnitFixtureLoaderSupport tested = new Tested();
        MockUtils.replay(this);
        tested.setConnectionFactory(cfMock);
        tested.afterPropertiesSet();
        assertSame(cfMock, tested.getConnectionFactory());
        MockUtils.verify(this);
    }

    /**
	 * Test method for
	 * 'org.ddsteps.fixture.dbunit.DbUnitFixtureLoaderSupport.afterPropertiesSet()'
	 * 
	 */
    public void testAfterPropertiesSet_dataSource() throws Exception {
        DbUnitFixtureLoaderSupport tested = new Tested();
        MockUtils.replay(this);
        tested.setDataSource(dsMock);
        tested.setSchema("foo");
        tested.afterPropertiesSet();
        StandardConnectionFactory actual = (StandardConnectionFactory) tested.connectionFactory;
        assertSame(dsMock, actual.getDataSource());
        assertEquals("foo", actual.getSchema());
        MockUtils.verify(this);
    }
}
