package it.aton.proj.dem.storage.impl.pmap;

import it.aton.proj.dem.storage.service.StorageFactory;
import it.aton.proj.dem.storage.service.Store;
import java.io.IOException;

public class StorageFactoryImpl implements StorageFactory {

    private FMap persistentMap;

    public StorageFactoryImpl(String dir, long snapshotInterval) throws IOException, ClassNotFoundException {
        persistentMap = new FMap(dir, snapshotInterval);
    }

    public Store getConfigStore(String domain) {
        return new StoreImpl("C" + domain, persistentMap);
    }

    public Store getConfigStore(String domain, ClassLoader classLoader) {
        return new StoreImpl("C" + domain, classLoader, persistentMap);
    }

    public Store getPropertyStore(String domain) {
        return new StoreImpl("P" + domain, persistentMap);
    }

    public Store getPropertyStore(String domain, ClassLoader classLoader) {
        return new StoreImpl("P" + domain, classLoader, persistentMap);
    }

    public void close() {
        persistentMap.close();
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }
}
