package org.subrecord.impl.connector;

import java.util.List;
import org.subrecord.ConnectorPickStrategy;
import org.subrecord.DataServerConnector;

public abstract class AbstractConnectorStrategy implements ConnectorPickStrategy {

    protected List<DataServerConnector> connectors;

    protected int replicationFactor;

    protected int nodeCount;

    public AbstractConnectorStrategy(List<DataServerConnector> connectors, int replicationFactor) {
        this.connectors = connectors;
        this.replicationFactor = replicationFactor;
        this.nodeCount = connectors.size();
    }
}
