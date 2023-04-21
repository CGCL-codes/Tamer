package siena.base.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.model.Database;
import siena.PersistenceManager;
import siena.jdbc.JdbcPersistenceManager;
import siena.jdbc.ddl.DdlGenerator;

public class JdbcTest extends BaseTest {

    @Override
    public PersistenceManager createPersistenceManager(List<Class<?>> classes) throws Exception {
        Properties p = new Properties();
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";
        String url = "jdbc:mysql://localhost/siena";
        p.setProperty("driver", driver);
        p.setProperty("user", username);
        p.setProperty("password", password);
        p.setProperty("url", url);
        Class.forName(driver);
        BasicDataSource dataSource = new BasicDataSource();
        dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxWait(2000);
        DdlGenerator generator = new DdlGenerator();
        for (Class<?> clazz : classes) {
            generator.addTable(clazz);
        }
        Database database = generator.getDatabase();
        Platform platform = PlatformFactory.createNewPlatformInstance("mysql");
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println(platform.getAlterTablesSql(connection, database));
        platform.alterTables(connection, database, true);
        connection.close();
        JdbcPersistenceManager pm = new JdbcPersistenceManager();
        pm.init(p);
        return pm;
    }

    @Override
    public boolean supportsAutoincrement() {
        return true;
    }

    @Override
    public boolean supportsMultipleKeys() {
        return true;
    }

    @Override
    public boolean mustFilterToOrder() {
        return false;
    }
}
