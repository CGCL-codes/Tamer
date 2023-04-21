package jacky.lanlan.song.collection;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 
 * <DIV lang="en"></DIV> <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;

    protected final int maxCapacity;

    public LRUMap(int maxCapacity) {
        super(maxCapacity, 1.0f, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    public void writeTo(ObjectOutput out) throws IOException {
        out.writeInt(maxCapacity);
        int size = size();
        out.writeInt(size);
        for (Entry<K, V> e : entrySet()) {
            out.writeObject(e.getKey());
            out.writeObject(e.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> LRUMap<K, V> readFrom(ObjectInput in) throws IOException, ClassNotFoundException {
        int cap = in.readInt();
        final LRUMap<K, V> map = new LRUMap<K, V>(cap);
        final int size = in.readInt();
        for (int i = 0; i < size; i++) {
            K key = (K) in.readObject();
            V value = (V) in.readObject();
            map.put(key, value);
        }
        return map;
    }
}
