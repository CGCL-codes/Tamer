package de.objectcode.openk.soa.registry.api;

import java.util.Properties;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "database-jdbc")
public class DatabaseJdbcDescriptor extends DatabaseDescriptor {

    String driverClass;

    String jdbcUrl;

    String username;

    String password;

    @XmlAttribute(name = "driver", required = true)
    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @XmlAttribute(name = "url", required = true)
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @XmlAttribute(name = "username", required = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlAttribute(name = "password", required = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void toProperties(String prefix, Properties properties) {
        super.toProperties(prefix, properties);
        properties.setProperty(prefix + "driver", driverClass);
        properties.setProperty(prefix + "url", jdbcUrl);
        properties.setProperty(prefix + "username", username);
        properties.setProperty(prefix + "password", password);
    }
}
