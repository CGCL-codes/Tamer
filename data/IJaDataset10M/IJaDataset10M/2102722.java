package charismata.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionTypeList {

    static List<ConnectionType> connectionTypeList = new ArrayList();

    static Map<String, ConnectionType> connectionTypeMap = new HashMap();

    public static synchronized void addConnectionType(String connectionTypeClassName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class connectionTypeClass;
        try {
            connectionTypeClass = cl.loadClass(connectionTypeClassName);
            Object connectionTypeObj = connectionTypeClass.newInstance();
            ConnectionType connectionType = (ConnectionType) connectionTypeObj;
            addConnectionType(connectionType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addConnectionType(ConnectionType connectionType) {
        connectionTypeList.add(connectionType);
        connectionTypeMap.put(connectionType.getConnectionTypeName(), connectionType);
    }

    public static ConnectionType getConnectionType(String connectionTypeName) {
        return connectionTypeMap.get(connectionTypeName);
    }

    public static List<ConnectionType> getConnectionTypeList() {
        return connectionTypeList;
    }

    public static Map<String, ConnectionType> getConnectionTypeMap() {
        return connectionTypeMap;
    }
}
