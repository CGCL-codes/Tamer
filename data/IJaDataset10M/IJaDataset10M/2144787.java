package org.avis.net.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.filter.codec.ProtocolCodecException;
import static org.avis.net.common.IO.getBytes;
import static org.avis.net.common.IO.putBytes;
import static org.avis.net.security.DualKeyScheme.checkProdOrCon;
import static org.avis.net.security.KeyScheme.schemeFor;

/**
 * A key collection used to secure notifications. A key collection
 * contains zero or more mappings from a key scheme ({@link KeyScheme})
 * to the keys registered for that scheme ({@link KeySet}).<p>
 *
 * Clients will generally only need to use the add/remove methods to
 * modify key sets. The {@link #match(Keys)} method implements the
 * logic to determine whether one key set matches another for the
 * purpose of notification delivery.<p>
 *
 * See also section 7.4 of the client protocol spec.
 * 
 * @author Matthew Phillips
 */
public class Keys {

    public static final int PRODUCER = DualKeyScheme.PRODUCER;

    public static final int CONSUMER = DualKeyScheme.CONSUMER;

    /**
   * An empty, immutable collection of keys.
   */
    public static final Keys EMPTY_KEYS = new EmptyKeys();

    private static final DualKeySet EMPTY_DUAL_KEYSET = new DualKeySet(true);

    private static final SingleKeySet EMPTY_SINGLE_KEYSET = new EmptySingleKeys();

    private Map<KeyScheme, KeySet> keySets;

    public Keys() {
        keySets = new HashMap<KeyScheme, KeySet>(4);
    }

    public Keys(Keys keys) {
        this();
        add(keys);
    }

    /**
   * True if no keys are in the collection.
   */
    public boolean isEmpty() {
        return keySets.isEmpty();
    }

    /**
   * Return the total number of keys in this key collection.
   */
    public int size() {
        if (isEmpty()) return 0;
        int size = 0;
        for (KeySet keyset : keySets.values()) size += keyset.size();
        return size;
    }

    /**
   * Add a key for single key scheme.
   *  
   * @param scheme The key scheme.
   * @param key The key to add.
   * 
   * @see #remove(SingleKeyScheme, Key)
   */
    public void add(SingleKeyScheme scheme, Key key) {
        newKeysetFor(scheme).add(key);
    }

    /**
   * Remove a key for single key scheme.
   *  
   * @param scheme The key scheme.
   * @param key The key to remove.
   * 
   * @see #add(SingleKeyScheme, Key)
   */
    public void remove(SingleKeyScheme scheme, Key key) throws IllegalArgumentException {
        KeySet keys = keySets.get(scheme);
        if (keys != null) {
            keys.remove(key);
            if (keys.isEmpty()) keySets.remove(scheme);
        }
    }

    /**
   * Add a key for dual key scheme.
   *  
   * @param scheme The key scheme.
   * @param prodOrCon One of {@link #PRODUCER} or {@link #CONSUMER}.
   * @param key The key to add.
   * 
   * @throws IllegalArgumentException if prodOrCon is not valid.
   * 
   * @see #remove(DualKeyScheme, int, Key)
   */
    public void add(DualKeyScheme scheme, int prodOrCon, Key key) throws IllegalArgumentException {
        checkProdOrCon(prodOrCon);
        DualKeySet keySet = (DualKeySet) newKeysetFor(scheme);
        keySet.keysFor(prodOrCon).add(key);
    }

    /**
   * Remove a key for dual key scheme.
   *  
   * @param scheme The key scheme.
   * @param prodOrCon One of {@link #PRODUCER} or {@link #CONSUMER}.
   * @param key The key to remove.
   * 
   * @throws IllegalArgumentException if prodOrCon is not valid.
   * 
   * @see #add(DualKeyScheme, int, Key)
   */
    public void remove(DualKeyScheme scheme, int prodOrCon, Key key) {
        checkProdOrCon(prodOrCon);
        DualKeySet keySet = (DualKeySet) keySets.get(scheme);
        if (keySet != null) {
            keySet.keysFor(prodOrCon).remove(key);
            if (keySet.isEmpty()) keySets.remove(scheme);
        }
    }

    /**
   * Add all keys in a collection.
   * 
   * @param keys The keys to add.
   * 
   * @see #remove(Keys)
   */
    public void add(Keys keys) {
        if (keys == this) throw new IllegalArgumentException("Cannot add key collection to itself");
        for (KeyScheme scheme : keys.keySets.keySet()) newKeysetFor(scheme).add(keys.keySets.get(scheme));
    }

    /**
   * Remove all keys in a collection.
   * 
   * @param keys The keys to remove.
   * 
   * @see #add(Keys)
   */
    public void remove(Keys keys) {
        if (keys == this) throw new IllegalArgumentException("Cannot remove key collection from itself");
        for (KeyScheme scheme : keys.keySets.keySet()) {
            KeySet myKeys = keySets.get(scheme);
            if (myKeys != null) {
                myKeys.remove(keys.keysetFor(scheme));
                if (myKeys.isEmpty()) keySets.remove(scheme);
            }
        }
    }

    /**
   * Create a new key collection with some keys added/removed. This
   * does not modify the current collection.
   * 
   * @param addKeys Keys to add.
   * @param removeKeys Keys to remove
   * 
   * @return A new key set with keys added remove. If both add/remove
   *         key sets are empty, this returns the current instance.
   */
    public Keys delta(Keys addKeys, Keys removeKeys) {
        if (addKeys.isEmpty() && removeKeys.isEmpty()) {
            return this;
        } else {
            Keys keys = new Keys(this);
            keys.add(addKeys);
            keys.remove(removeKeys);
            return keys;
        }
    }

    /**
   * Get the key set for a given scheme. This set should not be
   * modified.
   * 
   * @param scheme The scheme.
   * @return The key set for the scheme. Will be empty if no keys are
   *         defined for the scheme.
   * 
   * @see #keysetFor(DualKeyScheme)
   * @see #keysetFor(SingleKeyScheme)
   */
    public KeySet keysetFor(KeyScheme scheme) {
        KeySet keys = keySets.get(scheme);
        if (keys == null) return EMPTY_SINGLE_KEYSET; else return keys;
    }

    /**
   * Get the key set for a dual scheme. This set should not be
   * modified.
   * 
   * @param scheme The scheme.
   * @return The key set for the scheme. Will be empty if no keys are
   *         defined for the scheme.
   * 
   * @see #keysetFor(KeyScheme)
   * @see #keysetFor(SingleKeyScheme)
   */
    public DualKeySet keysetFor(DualKeyScheme scheme) {
        DualKeySet keys = (DualKeySet) keySets.get(scheme);
        if (keys == null) return EMPTY_DUAL_KEYSET; else return keys;
    }

    /**
   * Get the key set for a single scheme. This set should not be
   * modified.
   * 
   * @param scheme The scheme.
   * @return The key set for the scheme. Will be empty if no keys are
   *         defined for the scheme.
   *         
   * @see #keysetFor(KeyScheme)
   * @see #keysetFor(DualKeyScheme)
   */
    public SingleKeySet keysetFor(SingleKeyScheme scheme) {
        SingleKeySet keys = (SingleKeySet) keySets.get(scheme);
        if (keys == null) return EMPTY_SINGLE_KEYSET; else return keys;
    }

    /**
   * Lookup/create a key set for a scheme.
   */
    private KeySet newKeysetFor(KeyScheme scheme) {
        KeySet keys = keySets.get(scheme);
        if (keys == null) {
            keys = scheme.isDual() ? new DualKeySet() : new SingleKeySet();
            keySets.put(scheme, keys);
        }
        return keys;
    }

    /**
   * Test whether a given key collection matches this one for the
   * purpose of notification delivery.
   * 
   * @param keys The producer keys to match against this (consumer)
   *          key collection.
   * @return True if a consumer using this key collection could
   *         receive a notification from a producer with the given
   *         producer key collection.
   */
    public boolean match(Keys keys) {
        if (isEmpty() || keys.isEmpty()) return false;
        for (Entry<KeyScheme, KeySet> entry : keys.keySets.entrySet()) {
            KeyScheme scheme = entry.getKey();
            KeySet keyset = entry.getValue();
            if (keySets.containsKey(scheme) && scheme.match(keyset, keySets.get(scheme))) {
                return true;
            }
        }
        return false;
    }

    public void encode(ByteBuffer out) {
        out.putInt(keySets.size());
        for (Entry<KeyScheme, KeySet> entry : keySets.entrySet()) {
            KeyScheme scheme = entry.getKey();
            KeySet keySet = entry.getValue();
            out.putInt(scheme.id);
            if (scheme.isDual()) {
                DualKeySet dualKeySet = (DualKeySet) keySet;
                out.putInt(2);
                encodeKeys(out, dualKeySet.producerKeys);
                encodeKeys(out, dualKeySet.consumerKeys);
            } else {
                out.putInt(1);
                encodeKeys(out, (SingleKeySet) keySet);
            }
        }
    }

    public static Keys decode(ByteBuffer in) throws ProtocolCodecException {
        int length = in.getInt();
        if (length == 0) return EMPTY_KEYS;
        try {
            Keys keys = new Keys();
            for (; length > 0; length--) {
                KeyScheme scheme = schemeFor(in.getInt());
                int keySetCount = in.getInt();
                if (scheme.isDual()) {
                    if (keySetCount != 2) throw new ProtocolCodecException("Dual key scheme with " + keySetCount + " key sets");
                    DualKeySet keyset = (DualKeySet) keys.newKeysetFor(scheme);
                    decodeKeys(in, keyset.producerKeys);
                    decodeKeys(in, keyset.consumerKeys);
                } else {
                    if (keySetCount != 1) throw new ProtocolCodecException("Single key scheme with " + keySetCount + " key sets");
                    decodeKeys(in, (SingleKeySet) keys.newKeysetFor(scheme));
                }
            }
            return keys;
        } catch (IllegalArgumentException ex) {
            throw new ProtocolCodecException(ex);
        }
    }

    private static void encodeKeys(ByteBuffer out, Set<Key> keys) {
        out.putInt(keys.size());
        for (Key key : keys) putBytes(out, key.data);
    }

    private static void decodeKeys(ByteBuffer in, Set<Key> keys) {
        for (int keysetCount = in.getInt(); keysetCount > 0; keysetCount--) keys.add(new Key(getBytes(in)));
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Keys && equals((Keys) object);
    }

    public boolean equals(Keys keys) {
        if (keySets.size() != keys.keySets.size()) return false;
        for (KeyScheme scheme : keys.keySets.keySet()) {
            if (!keysetFor(scheme).equals(keys.keysetFor(scheme))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (KeyScheme scheme : keySets.keySet()) hash ^= 1 << scheme.id;
        return hash;
    }

    static class EmptySingleKeys extends SingleKeySet {

        public boolean add(Key key) throws IllegalArgumentException {
            throw new UnsupportedOperationException();
        }

        public void add(KeySet keys) throws IllegalArgumentException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Key key) throws IllegalArgumentException, UnsupportedOperationException {
            return false;
        }

        public void remove(KeySet keys) throws IllegalArgumentException {
        }
    }

    static class EmptyKeys extends Keys {

        @Override
        public void add(Keys keys) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(Keys keys) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(SingleKeyScheme scheme, Key key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(SingleKeyScheme scheme, Key key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(DualKeyScheme scheme, int prodOrCon, Key key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(DualKeyScheme scheme, int prodOrCon, Key key) {
            throw new UnsupportedOperationException();
        }
    }
}
