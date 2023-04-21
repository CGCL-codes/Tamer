package org.datascooter.inface;

import java.util.List;

/**
 * Interface for provide a database connectors
 * 
 * @author nemo
 * 
 */
public interface IConnectorProvider extends IProvider<IContextConnector> {

    public List<String> getAvailableContexts();
}
