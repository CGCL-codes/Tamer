package com.netx.config.R1;

import java.util.Map;
import com.netx.generics.R1.sql.Database;
import com.netx.generics.R1.sql.JdbcDriver;

public class TL_ConnectionDetails extends ComplexTypeLoader<Database> {

    public Database onLoad(Map<String, Object> values, PropertyDef pDef) {
        JdbcDriver driver = (JdbcDriver) values.get("driver");
        String server = (String) values.get("server");
        Integer port = (Integer) values.get("port");
        if (port == null) {
            port = driver.getDefaultPort();
        }
        String schema = (String) values.get("schema");
        String username = (String) values.get("username");
        String password = (String) values.get("password");
        return new Database(driver, server, port, schema, username, password);
    }

    public void onChange(Database arg, Map<String, Object> changes, PropertyDef pDef) {
    }
}
