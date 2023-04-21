package de.objectcode.openk.soa.registry.api;

import java.util.Properties;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "connector")
public class ConnectorDescriptor implements IPropertiesSource {

    String url;

    DatabaseDescriptor databaseDescriptor;

    @XmlAttribute(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElements({ @XmlElement(name = "jdbc", namespace = "http://objectcode.de/openk/registry", type = DatabaseJdbcDescriptor.class), @XmlElement(name = "datasource", namespace = "http://objectcode.de/openk/registry", type = DatabaseDataSourceDescriptor.class) })
    public DatabaseDescriptor getDatabaseDescriptor() {
        return databaseDescriptor;
    }

    public void setDatabaseDescriptor(DatabaseDescriptor databaseDescriptor) {
        this.databaseDescriptor = databaseDescriptor;
    }

    public void toProperties(String prefix, Properties properties) {
        properties.setProperty(prefix + "url", url);
        if (databaseDescriptor != null) {
            databaseDescriptor.toProperties(prefix + "db.", properties);
        }
    }
}
