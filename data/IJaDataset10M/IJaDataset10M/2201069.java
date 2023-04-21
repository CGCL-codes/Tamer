package net.jadoth.collections;

import static net.jadoth.Jadoth.ref;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.jadoth.Jadoth;
import net.jadoth.collections.hashing.HashCollection;
import net.jadoth.collections.hashing.HashCollection.Analysis;
import net.jadoth.collections.types.XMap;
import net.jadoth.lang.reference._intReference;
import net.jadoth.util.KeyValue;
import net.jadoth.util.chars.VarChar;

/**
 * @author Thomas M�nz
 *
 */
public abstract class MapEntry<K, V> extends ChainEntry<K, V> implements XMap.Entry<K, V>, java.util.Map.Entry<K, V> {

    @SuppressWarnings("cast")
    public static <K, V, C extends HashCollection<?>> Analysis<C> analyzeSlots(final C hashCollection, final MapEntry<K, V>[] slots) {
        final HashMap<Integer, _intReference> distribution = new HashMap<Integer, _intReference>();
        int emptySlotCount = 0;
        for (MapEntry<K, V> entry : slots) {
            if (entry == null) {
                emptySlotCount++;
                continue;
            }
            int chainLength = 1;
            for (entry = entry.link; entry != null; entry = entry.link) {
                chainLength++;
            }
            final _intReference count = distribution.get(chainLength);
            if (count == null) {
                distribution.put(chainLength, ref(1));
            } else {
                count.value++;
            }
        }
        distribution.put(0, ref(emptySlotCount));
        final int distRange = distribution.size();
        final LimitList<KeyValue<Integer, Integer>> result = new LimitList<KeyValue<Integer, Integer>>(distRange);
        int shortestEntryChainLength = Integer.MAX_VALUE;
        int longestEntryChainLength = 0;
        for (final Map.Entry<Integer, _intReference> e : distribution.entrySet()) {
            final int chainLength = e.getKey();
            if (chainLength > 0) {
                if (chainLength < shortestEntryChainLength) {
                    shortestEntryChainLength = chainLength;
                } else if (chainLength > longestEntryChainLength) {
                    longestEntryChainLength = chainLength;
                }
            }
            result.add(Jadoth.keyValue(e.getKey(), e.getValue().value));
        }
        JaSort.valueSort((KeyValue<Integer, Integer>[]) result.internalGetStorageArray(), new Comparator<KeyValue<Integer, Integer>>() {

            @Override
            public int compare(final KeyValue<Integer, Integer> o1, final KeyValue<Integer, Integer> o2) {
                return o1.key().intValue() - o2.key().intValue();
            }
        });
        return new HashCollection.Analysis<C>(hashCollection, hashCollection.size(), hashCollection.hashDensity(), slots.length, shortestEntryChainLength, longestEntryChainLength, distRange, result.immure());
    }

    final int hash;

    MapEntry<K, V> link;

    protected MapEntry(final int hash, final MapEntry<K, V> link) {
        super();
        this.hash = hash;
        this.link = link;
    }

    @Override
    public String toString() {
        return new VarChar().append('(').append(this.key()).append('=').append(this.value()).append(')').toString();
    }

    @Deprecated
    @Override
    public abstract K getKey();

    @Deprecated
    @Override
    public abstract V getValue();

    protected abstract void set(K key, V value);
}
