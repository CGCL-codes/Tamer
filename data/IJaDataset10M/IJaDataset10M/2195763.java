package ru.susu.algebra.chartable;

import ru.susu.algebra.partition.Partition;

/**
 * @author akargapolov
 * @since: 26.02.2009
 */
public interface IPermutationsGroupCharTable<V> extends IGroupCharTable<V> {

    V getCharacter(Partition p1, Partition p2);

    void setCharacter(Partition p1, Partition p2, V value);
}
