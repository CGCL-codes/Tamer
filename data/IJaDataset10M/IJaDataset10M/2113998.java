package com.continuent.tungsten.router.config;

import java.util.HashMap;
import java.util.Map;
import com.continuent.tungsten.commons.cluster.resource.DataSource;
import com.continuent.tungsten.commons.cluster.resource.DataSourceRole;
import com.continuent.tungsten.commons.cluster.resource.ResourceType;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.config.cluster.RouterConfiguration;
import com.continuent.tungsten.router.AbstractRouterTestCase;

/**
 * Tests ability to read and store router configuration values.
 */
public class TestRouterConfiguration extends AbstractRouterTestCase {

    /**
     * Tests reading and writing of configurations. Failed operations generate
     * an exception.
     */
    public void testConfiguration() throws Exception {
        RouterConfiguration config = new RouterConfiguration(null);
        config.load();
        Map<String, Map<String, TungstenProperties>> originalServicesMap = config.getDataServicesMap();
        TungstenProperties newDs = new TungstenProperties();
        newDs.setString("name", "newds");
        newDs.setString("description", "a new datastore");
        newDs.setBoolean("isAvailable", true);
        newDs.setString("role", DataSourceRole.slave.toString());
        newDs.setString("driver", "com.mysql.jdbc.Driver");
        newDs.setInt("precedence", 1);
        newDs.setString("driver", "jdbc:mysql://localhost/test");
        config.storeResourceConfig("default", ResourceType.DATASOURCE, newDs);
        config.deleteResourceConfig("default", ResourceType.DATASOURCE, newDs.getString("name"));
        config.storeDataSourceConfig("default", originalServicesMap.get("default"));
    }

    private static Map<String, DataSource> getDsList(String serviceName, Map<String, Map<String, TungstenProperties>> servicesPropList) {
        Map<String, DataSource> newMap = new HashMap<String, DataSource>();
        Map<String, TungstenProperties> serviceProps = servicesPropList.get(serviceName);
        for (TungstenProperties props : serviceProps.values()) {
            newMap.put(props.getString(DataSource.NAME), new DataSource(props));
        }
        return newMap;
    }
}
