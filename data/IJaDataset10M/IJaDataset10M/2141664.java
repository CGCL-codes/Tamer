package org.exist.management;

import org.exist.storage.BrokerPool;
import org.exist.storage.DBBroker;
import javax.management.openmbean.*;
import java.util.Iterator;
import java.util.Map;

public class Database implements DatabaseMBean {

    private static String[] itemNames = { "owner", "referenceCount" };

    private static String[] itemDescriptions = { "Name of the thread owning the broker", "Number of references held by the thread" };

    private static String[] indexNames = { "owner" };

    private BrokerPool pool;

    public Database(BrokerPool pool) {
        this.pool = pool;
    }

    public String getInstanceId() {
        return pool.getId();
    }

    public int getMaxBrokers() {
        return pool.getMax();
    }

    public int getAvailableBrokers() {
        return pool.available();
    }

    public int getActiveBrokers() {
        return pool.active();
    }

    public TabularData getActiveBrokersMap() {
        OpenType[] itemTypes = { SimpleType.STRING, SimpleType.INTEGER };
        try {
            CompositeType infoType = new CompositeType("brokerInfo", "Provides information on a broker instance.", itemNames, itemDescriptions, itemTypes);
            TabularType tabularType = new TabularType("activeBrokers", "Lists all threads currently using a broker instance", infoType, indexNames);
            TabularDataSupport data = new TabularDataSupport(tabularType);
            for (Iterator i = pool.getActiveBrokers().entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                Thread thread = (Thread) entry.getKey();
                DBBroker broker = (DBBroker) entry.getValue();
                Object[] itemValues = { thread.getName(), broker.getReferenceCount() };
                data.put(new CompositeDataSupport(infoType, itemNames, itemValues));
            }
            return data;
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getReservedMem() {
        return pool.getReservedMem();
    }

    public long getCacheMem() {
        return pool.getCacheManager().getTotalMem();
    }

    public long getCollectionCacheMem() {
        return pool.getCollectionCacheMgr().getMaxTotal();
    }
}
