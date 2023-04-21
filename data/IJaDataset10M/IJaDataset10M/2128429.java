package org.apache.hadoop.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A cached implementation of DNSToSwitchMapping that takes an
 * raw DNSToSwitchMapping and stores the resolved network location in 
 * a cache. The following calls to a resolved network location
 * will get its location from the cache. 
 *
 */
public class CachedDNSToSwitchMapping implements DNSToSwitchMapping {

    private Map<String, String> cache = new TreeMap<String, String>();

    protected DNSToSwitchMapping rawMapping;

    public CachedDNSToSwitchMapping(DNSToSwitchMapping rawMapping) {
        this.rawMapping = rawMapping;
    }

    public List<String> resolve(List<String> names) {
        names = NetUtils.normalizeHostNames(names);
        List<String> result = new ArrayList<String>(names.size());
        if (names.isEmpty()) {
            return result;
        }
        List<String> unCachedHosts = new ArrayList<String>(names.size());
        for (String name : names) {
            if (cache.get(name) == null) {
                unCachedHosts.add(name);
            }
        }
        List<String> rNames = rawMapping.resolve(unCachedHosts);
        if (rNames != null) {
            for (int i = 0; i < unCachedHosts.size(); i++) {
                cache.put(unCachedHosts.get(i), rNames.get(i));
            }
        }
        for (String name : names) {
            String networkLocation = cache.get(name);
            if (networkLocation != null) {
                result.add(networkLocation);
            } else {
                return null;
            }
        }
        return result;
    }
}
