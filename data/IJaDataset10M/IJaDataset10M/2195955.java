package gleam.util.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaBean class to represent a map as a list of entries. This is
 * necessary to be able to pass Map-valued data over the wire.
 * 
 * @param <K> the key type of the map
 * @param <V> the value type of the map
 */
public class MapWrapper<K, V> {

    private List<MapEntry<K, V>> entries;

    /**
   * No-argument constructor to support JavaBean-style creation.
   */
    public MapWrapper() {
    }

    public MapWrapper(int initialSize) {
        entries = new ArrayList<MapEntry<K, V>>(initialSize);
    }

    public List<MapEntry<K, V>> getEntries() {
        return entries;
    }

    public void setEntries(List<MapEntry<K, V>> entries) {
        this.entries = entries;
    }

    /**
   * Add a single entry to this wrapper.
   */
    public void addEntry(K key, V value) {
        if (entries == null) {
            entries = new ArrayList<MapEntry<K, V>>();
        }
        MapEntry<K, V> entry = new MapEntry<K, V>();
        entry.setKey(key);
        entry.setValue(value);
        entries.add(entry);
    }

    /**
   * Returns a MapWrapper wrapping the given map.
   * 
   * @param source the source map.
   * @return <code>null</code> if source is <code>null</code>,
   *         otherwise a <code>MapWrapper</code> containing the same
   *         mappings as the source map.
   */
    public static <KT, VT> MapWrapper<KT, VT> wrap(Map<KT, VT> source) {
        if (source == null) {
            return null;
        }
        MapWrapper<KT, VT> wrapper = new MapWrapper<KT, VT>(source.size());
        for (Map.Entry<KT, VT> mapEntry : source.entrySet()) {
            wrapper.addEntry(mapEntry.getKey(), mapEntry.getValue());
        }
        return wrapper;
    }

    /**
   * Returns a map containing the same entries as the given wrapper.
   * This is implemented as a static method so it can handle null
   * wrappers.
   * 
   * @param wrapper the MapWrapper to unwrap
   * @return <code>null</code> if wrapper is <code>null</code>,
   *         otherwise a {@link HashMap} containing the same mappings as
   *         the wrapper.
   */
    public static <KT, VT> Map<KT, VT> unwrap(MapWrapper<KT, VT> wrapper) {
        if (wrapper == null) {
            return null;
        }
        int size = (wrapper.entries == null) ? 0 : wrapper.entries.size();
        Map<KT, VT> map = new HashMap<KT, VT>(size);
        if (size > 0) {
            for (MapEntry<KT, VT> entry : wrapper.entries) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }
}
