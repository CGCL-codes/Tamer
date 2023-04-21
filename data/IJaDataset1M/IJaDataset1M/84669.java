package com.ecmdeveloper.plugin.model.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import com.ecmdeveloper.plugin.core.model.IConnection;
import com.ecmdeveloper.plugin.core.model.IObjectStoreItem;
import com.ecmdeveloper.plugin.core.model.IObjectStores;
import com.ecmdeveloper.plugin.core.model.tasks.AbstractTask;
import com.ecmdeveloper.plugin.core.model.tasks.IConnectConnectionTask;
import com.ecmdeveloper.plugin.core.model.tasks.TaskResult;
import com.ecmdeveloper.plugin.model.ObjectStore;

/**
 * @author ricardo.belfor
 *
 */
public class ConnectConnectionTask extends AbstractTask implements IConnectConnectionTask {

    private Object connectionName;

    private Map<String, IConnection> connections;

    private IObjectStores objectStores;

    private ArrayList<IObjectStoreItem> connectionObjectStores;

    @Override
    public Collection<IObjectStoreItem> getConnectionObjectStores() {
        return connectionObjectStores;
    }

    public ConnectConnectionTask(Object connectionName, Map<String, IConnection> connections, IObjectStores objectStores) {
        this.connectionName = connectionName;
        this.connections = connections;
        this.objectStores = objectStores;
    }

    @Override
    public Object call() throws Exception {
        connections.get(connectionName).connect();
        connectionObjectStores = new ArrayList<IObjectStoreItem>();
        for (IObjectStoreItem objectStoreItem : objectStores.getChildren()) {
            ObjectStore objectStore = (ObjectStore) objectStoreItem;
            if (objectStore.getConnection().getName().equals(connectionName)) {
                objectStore.connect();
                connectionObjectStores.add(objectStore);
            }
        }
        fireTaskCompleteEvent(TaskResult.COMPLETED);
        return null;
    }
}
