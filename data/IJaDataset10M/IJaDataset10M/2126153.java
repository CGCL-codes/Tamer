package org.datascooter.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datascooter.exception.DataScooterException;
import org.datascooter.inface.IConnectorProvider;
import org.datascooter.inface.IContextConnector;
import org.datascooter.inface.IXMLReader;

public class DSConnectorProvider extends AbstractProvider<IContextConnector> implements IConnectorProvider {

    private Map<String, IContextConnector> connectorMap = new HashMap<String, IContextConnector>();

    public DSConnectorProvider(IXMLReader... readerArray) {
        super(readerArray);
    }

    @Override
    public IContextConnector getItem(String contextId) {
        return connectorMap.get(contextId);
    }

    @Override
    protected void processRoot(IXMLReader element) {
        try {
            IContextConnector contextConnector = (IContextConnector) Class.forName(element.getAttribute("class")).getConstructor().newInstance();
            connectorMap.put(contextConnector.getContextId(), contextConnector);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "***Not found class in a source:  " + element.getNamespaceIdentifier(), e);
            throw new DataScooterException(e);
        }
    }

    @Override
    public List<String> getAvailableContexts() {
        List<String> result = new ArrayList<String>();
        for (IContextConnector connector : connectorMap.values()) {
            result.add(connector.getContextId());
        }
        return result;
    }
}
