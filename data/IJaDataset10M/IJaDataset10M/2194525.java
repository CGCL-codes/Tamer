package info.goldenorb.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p>The MMap class encapsulates the TreeMap class to provide 
 * addtional functionalities.</p>
 * 
 * <p>With this class is possible to get an ordered list of values.
 * In order to do this, you need fist to set an object that implements
 * the <code>java.util.Comparator</code> interface using the method
 * <code>setValueComparator(Comparator)</code>, and call the method 
 * <code>values().</code> If a comparator was not set using this 
 * method, the <code>java.util.TreeMap</code> implementation of the method 
 * <code>values()</code> will be used.</p>
 * 
 * <p>It's also possible to retrieve an ordered Set with the keys.
 * See the <code>Set keySet()</code> javadoc for more information.</p>
 */
public class MMap<K, V> extends TreeMap<K, V> implements SortedMap<K, V>, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 5287894253714652073L;

    private Comparator<V> valueComparator;

    /**
	 * Default constructor.
	 */
    public MMap() {
    }

    /**
	 * Constructor.
	 * @param c comparator.
	 */
    public MMap(final Comparator<? super K> c) {
        super(c);
    }

    /**
     * Constructor.
     * @param m list.
     */
    public MMap(final Map<? extends K, ? extends V> m) {
        super(m);
    }

    /**
     * Constructor.
     * @param m sorted list.
     */
    public MMap(final SortedMap<K, ? extends V> m) {
        super(m);
    }

    /**
     * @return comparator.
     */
    public Comparator<V> getValueComparator() {
        return valueComparator;
    }

    /**
	 * @param valueComparator sets the comparator.
	 */
    public void setValueComparator(final Comparator<V> valueComparator) {
        this.valueComparator = valueComparator;
    }

    /**
	 * <p>Retrieve an ordered collection of values given a comparator.</p>
	 * 
	 * <p>This comparator is set using the method 
	 * <code>setValueComparator(Comparator)</code>;</p>
	 * 
	 * <p>If a comparator was not set, the <code>java.util.TreeMap</code> 
	 * implementation of the method <code>values()</code> will be executed.</p>
	 * 
	 * @return An ordered <code>Collection</code>.
	 */
    @Override
    public Collection<V> values() {
        if (this.valueComparator == null) return super.values();
        final List<V> values = new ArrayList<V>(super.values());
        Collections.sort(values, valueComparator);
        return values;
    }

    private List<K> compare(final List<K> keys, final List<V> values, final int index) {
        final List<K> tmpKeys = keys;
        K tempKey = null;
        V tempValue = null;
        if (valueComparator.compare(values.get(index + 1), values.get(index)) == -1) {
            tempValue = values.get(index);
            values.set(index, values.get(index + 1));
            values.set(index + 1, tempValue);
            tempKey = keys.get(index);
            keys.set(index, keys.get(index + 1));
            keys.set(index + 1, tempKey);
        }
        return tmpKeys;
    }

    /**
	 * <p>This method returns an orderd <code>java.util.Set</code> using
	 * a <code>java.util.Comparator</code> implementation set using the
	 * method <code>setValueComparator(Comparator)</code>.</p>
	 * 
	 * <p>It's a set of keys, but the order is defined applying
	 * the <code>compare(K, V)</code> over the list of values.</p>
	 * 
	 * @return An ordered <code>Set</code>.
	 */
    @Override
    public Set<K> keySet() {
        if (this.valueComparator == null) return super.keySet();
        List<K> keys = new ArrayList<K>(super.keySet());
        final List<V> values = new ArrayList<V>(super.values());
        final int n = values.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                keys = compare(keys, values, j);
            }
        }
        return new LinkedHashSet<K>(keys);
    }
}
