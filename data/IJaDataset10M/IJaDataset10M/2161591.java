package net.teqlo.db.sleepycat;

import net.teqlo.TeqloException;
import net.teqlo.db.impl.ServiceLookupData;
import net.teqlo.db.impl.ServiceLookupDataCache;

public class SleepycatServiceLookupDataCache extends ServiceLookupDataCache {

    private SleepycatXmlDatabase database;

    public SleepycatServiceLookupDataCache(SleepycatXmlDatabase database) throws TeqloException {
        super();
        this.database = database;
    }

    public ServiceLookupData fetch(String fqn, @SuppressWarnings("unused") Object fetchData) throws TeqloException {
        return database.fetchServiceLookupData(fqn);
    }
}
