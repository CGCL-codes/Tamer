package org.nakedobjects.plugins.sql.objectstore;

import java.util.Properties;
import org.nakedobjects.plugins.sql.objectstore.common.SqlIntegrationTestCommon;

public class MySqlTest extends SqlIntegrationTestCommon {

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("nakedobjects.persistence.sql.jdbc.driver", "com.mysql.jdbc.Driver");
        properties.put("nakedobjects.persistence.sql.jdbc.connection", "jdbc:mysql://abacus/noftest");
        properties.put("nakedobjects.persistence.sql.jdbc.user", "nof");
        properties.put("nakedobjects.persistence.sql.jdbc.password", "");
        return properties;
    }

    public String getPropertiesFilename() {
        return "mysql.properties";
    }
}
