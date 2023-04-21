package gloodb.file.storage.intransaction;

import gloodb.storage.StorageConfiguration;
import gloodb.storage.StorageFactory;
import gloodb.storage.StorageInterceptor;
import gloodb.storage.StorageStrategy;

/**
 * This storage implementation strategy stores all object in the transaction log.
 * 
 */
public class InTransactionStorageStrategy implements StorageStrategy {

    private final StorageFactory factory = new InTransactionStorageFactory();

    private final StorageConfiguration configuration;

    private final StorageInterceptor interceptor;

    public InTransactionStorageStrategy(String nameSpace, StorageInterceptor interceptor) {
        super();
        this.configuration = new InTransactionStorageConfiguration(nameSpace);
        this.interceptor = interceptor;
    }

    /**
     * Configuration getter. No configuration required. It always returns null.
     * @return Always null
     */
    public StorageConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public StorageFactory getFactory() {
        return factory;
    }

    @Override
    public StorageInterceptor getInterceptor() {
        return interceptor;
    }
}
