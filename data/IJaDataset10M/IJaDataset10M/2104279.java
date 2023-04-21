package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.HashFunctions;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

/**
 * An open addressed hashing implementation for byte/byte primitive entries.
 *
 * Created: Sun Nov  4 08:56:06 2001
 *
 * @author Eric D. Friedman
 * @author Rob Eden
 * @author Jeff Randall
 * @version $Id: _K__V_Hash.template,v 1.1.2.6 2009/11/07 03:36:44 robeden Exp $
 */
public abstract class TByteByteHash extends TPrimitiveHash {

    static final long serialVersionUID = 1L;

    /** the set of bytes */
    public transient byte[] _set;

    /**
     * key that represents null
     *
     * NOTE: should not be modified after the Hash is created, but is
     *       not final because of Externalization
     *
     */
    protected byte no_entry_key;

    /**
     * value that represents null
     *
     * NOTE: should not be modified after the Hash is created, but is
     *       not final because of Externalization
     *
     */
    protected byte no_entry_value;

    /**
     * Creates a new <code>T#E#Hash</code> instance with the default
     * capacity and load factor.
     */
    public TByteByteHash() {
        super();
        no_entry_key = (byte) 0;
        no_entry_value = (byte) 0;
    }

    /**
     * Creates a new <code>T#E#Hash</code> instance whose capacity
     * is the next highest prime above <tt>initialCapacity + 1</tt>
     * unless that value is already prime.
     *
     * @param initialCapacity an <code>int</code> value
     */
    public TByteByteHash(int initialCapacity) {
        super(initialCapacity);
        no_entry_key = (byte) 0;
        no_entry_value = (byte) 0;
    }

    /**
     * Creates a new <code>TByteByteHash</code> instance with a prime
     * value at or near the specified capacity and load factor.
     *
     * @param initialCapacity used to find a prime capacity for the table.
     * @param loadFactor used to calculate the threshold over which
     * rehashing takes place.
     */
    public TByteByteHash(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        no_entry_key = (byte) 0;
        no_entry_value = (byte) 0;
    }

    /**
     * Creates a new <code>TByteByteHash</code> instance with a prime
     * value at or near the specified capacity and load factor.
     *
     * @param initialCapacity used to find a prime capacity for the table.
     * @param loadFactor used to calculate the threshold over which
     * rehashing takes place.
     * @param no_entry_value value that represents null
     */
    public TByteByteHash(int initialCapacity, float loadFactor, byte no_entry_key, byte no_entry_value) {
        super(initialCapacity, loadFactor);
        this.no_entry_key = no_entry_key;
        this.no_entry_value = no_entry_value;
    }

    /**
     * Returns the value that is used to represent null as a key. The default
     * value is generally zero, but can be changed during construction
     * of the collection.
     *
     * @return the value that represents null
     */
    public byte getNoEntryKey() {
        return no_entry_key;
    }

    /**
     * Returns the value that is used to represent null. The default
     * value is generally zero, but can be changed during construction
     * of the collection.
     *
     * @return the value that represents null
     */
    public byte getNoEntryValue() {
        return no_entry_value;
    }

    /**
     * initializes the hashtable to a prime capacity which is at least
     * <tt>initialCapacity + 1</tt>.
     *
     * @param initialCapacity an <code>int</code> value
     * @return the actual capacity chosen
     */
    protected int setUp(int initialCapacity) {
        int capacity;
        capacity = super.setUp(initialCapacity);
        _set = new byte[capacity];
        return capacity;
    }

    /**
     * Searches the set for <tt>val</tt>
     *
     * @param val an <code>byte</code> value
     * @return a <code>boolean</code> value
     */
    public boolean contains(byte val) {
        return index(val) >= 0;
    }

    /**
     * Executes <tt>procedure</tt> for each key in the map.
     *
     * @param procedure a <code>TByteProcedure</code> value
     * @return false if the loop over the set terminated because
     * the procedure returned false for some value.
     */
    public boolean forEach(TByteProcedure procedure) {
        byte[] states = _states;
        byte[] set = _set;
        for (int i = set.length; i-- > 0; ) {
            if (states[i] == FULL && !procedure.execute(set[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Releases the element currently stored at <tt>index</tt>.
     *
     * @param index an <code>int</code> value
     */
    protected void removeAt(int index) {
        _set[index] = no_entry_key;
        super.removeAt(index);
    }

    /**
     * Locates the index of <tt>val</tt>.
     *
     * @param key an <code>byte</code> value
     * @return the index of <tt>val</tt> or -1 if it isn't in the set.
     */
    protected int index(byte key) {
        int hash, probe, index, length;
        final byte[] states = _states;
        final byte[] set = _set;
        length = states.length;
        hash = HashFunctions.hash(key) & 0x7fffffff;
        index = hash % length;
        if (states[index] != FREE && (states[index] == REMOVED || set[index] != key)) {
            probe = 1 + (hash % (length - 2));
            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
            } while (states[index] != FREE && (states[index] == REMOVED || set[index] != key));
        }
        return states[index] == FREE ? -1 : index;
    }

    /**
     * Locates the index at which <tt>val</tt> can be inserted.  if
     * there is already a value equal()ing <tt>val</tt> in the set,
     * returns that value as a negative integer.
     *
     * @param key an <code>byte</code> value
     * @return an <code>int</code> value
     */
    protected int insertionIndex(byte key) {
        int hash, probe, index, length;
        final byte[] states = _states;
        final byte[] set = _set;
        length = states.length;
        hash = HashFunctions.hash(key) & 0x7fffffff;
        index = hash % length;
        if (states[index] == FREE) {
            return index;
        } else if (states[index] == FULL && set[index] == key) {
            return -index - 1;
        } else {
            probe = 1 + (hash % (length - 2));
            if (states[index] != REMOVED) {
                do {
                    index -= probe;
                    if (index < 0) {
                        index += length;
                    }
                } while (states[index] == FULL && set[index] != key);
            }
            if (states[index] == REMOVED) {
                int firstRemoved = index;
                while (states[index] != FREE && (states[index] == REMOVED || set[index] != key)) {
                    index -= probe;
                    if (index < 0) {
                        index += length;
                    }
                }
                return states[index] == FULL ? -index - 1 : firstRemoved;
            }
            return states[index] == FULL ? -index - 1 : index;
        }
    }

    /** {@inheritDoc} */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeByte(no_entry_key);
        out.writeByte(no_entry_value);
    }

    /** {@inheritDoc} */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        no_entry_key = in.readByte();
        no_entry_value = in.readByte();
    }
}
