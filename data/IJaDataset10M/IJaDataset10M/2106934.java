package org.tglman.jsdms.data.store.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentsIndex {

    private List<FragmentsIndexEntry> entries = new ArrayList<FragmentsIndexEntry>();

    private Map<String, FragmentsIndexEntry> cacheMap;

    public FragmentsIndexEntry getFragments(long ruid, long version) {
        String key = ruid + ":" + version;
        return getCacheMap().get(key);
    }

    private Map<String, FragmentsIndexEntry> getCacheMap() {
        if (cacheMap == null) {
            cacheMap = new HashMap<String, FragmentsIndexEntry>();
            for (FragmentsIndexEntry entry : entries) {
                String key = entry.getRuid() + ":" + entry.getVersion();
                cacheMap.put(key, entry);
            }
        }
        return cacheMap;
    }
}
