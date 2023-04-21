package de.enough.polish.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import de.enough.polish.io.Externalizable;

/**
 * <p>Provides a list that may contain several duplicate keys</p>
 *
 * <p>Copyright Enough Software 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 * @param <K> type of keys; when you use the enough-polish-client-java5.jar you can parameterize the KeyValueList, e.g. KeyValueList&lt;Integer, String&gt; = new KeyValueList&lt;Integer, String&gt;(10); 
 * @param <V> type of values; when you use the enough-polish-client-java5.jar you can parameterize the KeyValueList, e.g. KeyValueList&lt;Integer, String&gt; = new KeyValueList&lt;Integer, String&gt;(10); 
 */
public class KeyValueList<K, V> implements Externalizable {

    public final ArrayList<K> keys;

    public final ArrayList<V> values;

    /**
	 * Creates an KeyValueList with the initial capacity of 10 and a growth factor of 75%
	 */
    public KeyValueList() {
        this(10, 75);
    }

    /**
	 * Creates an KeyValueList with the given initial capacity and a growth factor of 75%
	 * 
	 * @param initialCapacity the capacity of this list.
	 */
    public KeyValueList(int initialCapacity) {
        this(initialCapacity, 75);
    }

    /**
	 * Creates a new KeyValueList
	 * 
	 * @param initialCapacity the capacity of this list.
	 * @param growthFactor the factor in % for increasing the capacity 
	 * 								  when there's not enough room in this list anymore 
	 */
    public KeyValueList(int initialCapacity, int growthFactor) {
        this.keys = new ArrayList<K>(initialCapacity, growthFactor);
        this.values = new ArrayList<V>(initialCapacity, growthFactor);
    }

    /**
	 * Retrieves the current size of this array list.
	 *  
	 * @return the number of stored elements in this list.
	 */
    public int size() {
        return this.keys.size();
    }

    public boolean containsKey(K element) {
        return this.keys.contains(element);
    }

    public boolean containsValue(V element) {
        return this.values.contains(element);
    }

    public int indexOfKey(K element) {
        return this.keys.indexOf(element);
    }

    public int indexOfValue(V element) {
        return this.values.indexOf(element);
    }

    public K getKey(int index) {
        return this.keys.get(index);
    }

    public V getValue(int index) {
        return this.values.get(index);
    }

    public K remove(int index) {
        this.values.remove(index);
        return this.keys.remove(index);
    }

    public boolean remove(K element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        int index = indexOfKey(element);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    /**
	 * Removes all of the elements from this list. 
	 * The list will be empty after this call returns. 
	 */
    public void clear() {
        this.keys.clear();
        this.values.clear();
    }

    public void add(K key, V value) {
        this.keys.add(key);
        this.values.add(value);
    }

    public void add(int index, K key, V value) {
        this.keys.add(index, key);
        this.values.add(index, value);
    }

    public K set(int index, K key, V value) {
        this.values.set(index, value);
        return this.keys.set(index, key);
    }

    /**
	 * Retrieves the internal key array - use with care!
	 * This method allows to access stored objects without creating an intermediate
	 * array. You really should refrain from changing any elements in the returned array
	 * unless you are 110% sure about what you are doing. It is safe to cycle through this
	 * array to access it's elements, though. Note that some array positions might contain null.
	 * Also note that the internal array is changed whenever this list has to be increased.
	 * 
	 * @return the internal array
	 */
    public Object[] getInternalKeyArray() {
        return this.keys.getInternalArray();
    }

    /**
	 * Retrieves the internal value array - use with care!
	 * This method allows to access stored objects without creating an intermediate
	 * array. You really should refrain from changing any elements in the returned array
	 * unless you are 110% sure about what you are doing. It is safe to cycle through this
	 * array to access it's elements, though. Note that some array positions might contain null.
	 * Also note that the internal array is changed whenever this list has to be increased.
	 * 
	 * @return the internal array
	 */
    public Object[] getInternalValueArray() {
        return this.keys.getInternalArray();
    }

    public void read(DataInputStream in) throws IOException {
        this.keys.read(in);
        this.values.read(in);
    }

    public void write(DataOutputStream out) throws IOException {
        this.keys.write(out);
        this.values.write(out);
    }
}
