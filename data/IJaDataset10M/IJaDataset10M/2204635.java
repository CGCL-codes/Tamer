package org.liris.schemerger.core.dataset.network;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Damien Cram
 * 
 * @param <T>
 */
public class NetworkNodeAdapter<T extends Nodeable> {

    private Map<T, INetworkNode<T>> map = new TreeMap<T, INetworkNode<T>>();

    public INetworkNode<T> getNetworkNode(T v) {
        if (map.get(v) == null) map.put(v, new NetworkNode<T>(v));
        return map.get(v);
    }

    public Set<INetworkNode<T>> getNewNodeSet() {
        return new TreeSet<INetworkNode<T>>(map.values());
    }
}

;
