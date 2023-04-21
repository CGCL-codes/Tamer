package cn.vlabs.dlog.persitent;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import cn.vlabs.dlog.util.Config;

public class MyDataSource {

    public static DataSource getDataSource() {
        if (_datasource == null) {
            Config dlog_config = Config.getInstance();
            String connectURI = dlog_config.getStringProp("Database.jdbcurl", "jdbc:mysql://localhost:3306/dlog?useUnicode=true&characterEncoding=utf8");
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(dlog_config.getStringProp("Database.driver", "com.mysql.jdbc.Driver"));
            ds.setUsername(dlog_config.getStringProp("Database.auth.username", "scott"));
            ds.setPassword(dlog_config.getStringProp("Database.auth.password", "tiger"));
            ds.setUrl(connectURI);
            _datasource = ds;
        }
        return _datasource;
    }

    private static DataSource _datasource = null;
}
