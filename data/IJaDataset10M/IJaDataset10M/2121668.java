package mybridge.handle.mysqlproxy;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.digester.Digester;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionPool {

    static ConnectionPool config;

    static ConnectionPool pool;

    static List<ComboPooledDataSource> dsPool = new ArrayList<ComboPooledDataSource>();

    public void addDataSource(ComboPooledDataSource ds) {
        dsPool.add(ds);
    }

    public Connection getMaster() {
        try {
            if (dsPool.size() == 0) {
                return null;
            }
            return dsPool.get(0).getConnection();
        } catch (SQLException e) {
            return null;
        }
    }

    public Connection getSlave() {
        try {
            if (dsPool.size() == 0) {
                return null;
            }
            if (dsPool.size() == 1) {
                return dsPool.get(0).getConnection();
            }
            Random rnd = new Random();
            int index = rnd.nextInt(dsPool.size() - 1) + 1;
            return dsPool.get(index).getConnection();
        } catch (SQLException e) {
            return null;
        }
    }

    public static void init() throws Exception {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("mybridge/pool", ConnectionPool.class.getName());
        digester.addSetProperties("mybridge/pool");
        digester.addObjectCreate("mybridge/pool/datasource", ComboPooledDataSource.class.getName());
        digester.addSetProperties("mybridge/pool/datasource");
        digester.addSetNext("mybridge/pool/datasource", "addDataSource", ComboPooledDataSource.class.getName());
        pool = (ConnectionPool) digester.parse(new File("./conf/database.xml"));
    }
}
