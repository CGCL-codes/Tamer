package com.workplacesystems.queuj.utils.collections;

import java.util.Comparator;
import java.util.SortedMap;

/**
 *
 * @author  Dave
 */
public interface SortedBidiMap<K, V> extends SortedMap<K, V>, BidiMap<K, V> {

    Comparator<? super V> valueComparator();

    SortedBidiMap<K, V> subMapByValue(V fromValue, V toValue);

    SortedBidiMap<K, V> headMapByValue(V toValue);

    SortedBidiMap<K, V> tailMapByValue(V fromValue);

    V firstValue();

    K firstKeyByValue();

    V firstValueByValue();

    V lastValue();

    K lastKeyByValue();

    V lastValueByValue();
}
