package org.datascooter.cache;

import java.util.List;
import org.datascooter.impl.DataSnip;
import org.datascooter.inface.IDataManager;

public class DefaultDataCache extends AbstractDataCache<DataSnip, Object> {

    public DefaultDataCache() {
        super();
    }

    @Override
    public void start(IDataManager manager, List<CacheRegion> regionList) {
        this.dataManager = manager;
        if (regionList != null) {
            for (CacheRegion region : regionList) {
                setValue(region.dataSnip, dataManager.retrieveData(region.dataSnip));
            }
        }
    }

    @Override
    public Object getValue(DataSnip key) {
        Object value = delMap.get(key);
        if (value == null) {
            value = dataManager.retrieveData(key);
            checkSize();
            delMap.put(key, value);
        }
        return value;
    }
}
