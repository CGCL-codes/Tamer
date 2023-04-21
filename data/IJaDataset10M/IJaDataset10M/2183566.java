package net.sf.webwarp.modules.datasource.impl;

import java.sql.Connection;
import net.sf.webwarp.modules.datasource.DataSource;
import net.sf.webwarp.modules.datasource.DataSourceDAO;
import net.sf.webwarp.modules.datasource.impl.DataSourceImpl;
import org.junit.Test;

public class DataSourceTest extends ATestBase {

    private DataSourceDAO<DataSource> dataSourceDAO;

    @Test
    public void testDS() throws Exception {
        DataSourceImpl dataSource = new DataSourceImpl();
        dataSource.setName("test");
        dataSource.setUrl("jdbc:hsqldb:mem:testdb");
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUsername("sa");
        dataSourceDAO.save(dataSource, "test");
        dataSource = (DataSourceImpl) dataSourceDAO.getDataSource("test");
        Connection connection = dataSource.createDataSource().getConnection();
        connection.commit();
    }

    public void setDataSourceDAO(DataSourceDAO<DataSource> dataSourceDAO) {
        this.dataSourceDAO = dataSourceDAO;
    }
}
