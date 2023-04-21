package org.dbmaintain.database;

import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.dbmaintain.datasource.SimpleDataSource.createDataSource;
import static org.dbmaintain.util.TestUtils.getHsqlDatabaseInfo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 * @since 3-jan-2009
 */
public class DbMaintainDataSourceTest {

    private DatabaseInfo databaseInfo;

    private DataSource dataSource;

    @Before
    public void setUp() {
        databaseInfo = getHsqlDatabaseInfo();
        dataSource = createDataSource(databaseInfo);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertEquals(databaseInfo.getUrl(), conn.getMetaData().getURL());
    }

    @Test
    public void testDataSourceEqualsHashcode() {
        DataSource otherDataSource = createDataSource(databaseInfo);
        assertFalse(dataSource.equals(otherDataSource));
        DataSource yetAnotherDataSource = createDataSource(databaseInfo);
        assertFalse(dataSource.hashCode() == otherDataSource.hashCode() && dataSource.hashCode() == yetAnotherDataSource.hashCode());
    }
}
