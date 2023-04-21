package org.obe.runtime.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.obe.client.api.repository.CompletionStrategyMetaData;
import org.obe.client.api.repository.RepositoryException;
import org.obe.engine.repository.AbstractRepository;
import org.obe.spi.runtime.CompletionStrategy;
import org.obe.spi.service.CompletionStrategyFactory;
import org.obe.spi.service.ServiceManager;

/**
 * Standard implementation of a work item assignment strategy factory.
 *
 * @author Adrian Price
 */
public class BasicCompletionStrategyFactory extends AbstractRepository implements CompletionStrategyFactory {

    private static final Log _logger = LogFactory.getLog(BasicCompletionStrategyFactory.class);

    public BasicCompletionStrategyFactory(ServiceManager svcMgr) {
        super(svcMgr, CompletionStrategyMetaData.class);
    }

    public void createStrategy(CompletionStrategyMetaData strategy) throws RepositoryException {
        createEntry(strategy);
    }

    public void deleteStrategy(String implClass) throws RepositoryException {
        deleteEntry(implClass);
    }

    public void updateStrategy(CompletionStrategyMetaData strategy) throws RepositoryException {
        updateEntry(strategy.getId(), strategy);
    }

    public CompletionStrategyMetaData[] findStrategyTypes(String locale) throws RepositoryException {
        return (CompletionStrategyMetaData[]) findObjectTypes();
    }

    public CompletionStrategyMetaData findStrategyType(String className, String locale) throws RepositoryException {
        return (CompletionStrategyMetaData) findObjectType(className);
    }

    public CompletionStrategyMetaData[] findStrategyMetaData() throws RepositoryException {
        return (CompletionStrategyMetaData[]) findMetaData();
    }

    public CompletionStrategyMetaData findStrategyMetaData(String strategy) throws RepositoryException {
        return (CompletionStrategyMetaData) findMetaData(strategy, true);
    }

    public CompletionStrategy findStrategy(String strategy) throws RepositoryException {
        return (CompletionStrategy) findInstance(strategy, true);
    }

    protected Log getLogger() {
        return _logger;
    }

    public String getServiceName() {
        return SERVICE_NAME;
    }
}
