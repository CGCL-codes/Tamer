package de.hsofttec.monitoring.test;

import de.hsofttec.monitoring.gui.datasource.JdbcDataSource;
import org.testng.annotations.BeforeClass;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id: AbstractTestDb.java 2 2007-09-01 11:09:16Z shomburg $
 */
public class AbstractTestDb {

    private JdbcDataSource _dataSource;

    @BeforeClass
    public void initIOC() {
        _dataSource = new JdbcDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.115.1:3307/hfl", "root", "qdpdsys");
    }

    public JdbcDataSource getDataSource() {
        return _dataSource;
    }
}
