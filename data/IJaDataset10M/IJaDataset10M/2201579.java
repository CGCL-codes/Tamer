package net.sf.staccatocommons.collections.stream;

import java.util.Map;
import net.sf.staccatocommons.collections.reduction.Reduction;
import net.sf.staccatocommons.defs.Applicable;
import net.sf.staccatocommons.defs.Applicable2;
import net.sf.staccatocommons.restrictions.check.NonNull;

/**
 * <strong>Warning: Experimental Interface. May change in the future in
 * incompatible ways</strong>
 * 
 * Loosely based on <a
 * href="http://code.google.com/p/cat-language/wiki/MapReduce">MapReduce</a>
 * 
 * @author flbulgarelli
 */
public interface Groupable<A> {

    /**
   * Groups elements by the given {@code groupingFunction}, and reduces each
   * group using the given {@code reduceFunction}.
   * <p>
   * The grouping is performed so that two elements {@code a} and {@code b} will
   * be put in the same group if and only if
   * {@code  groupFunction.apply(a).equals(groupFunction.apply(b)) }
   * </p>
   * <p>
   * This message is equivalent to
   * {@code groupOn(groupFunction, Functions.identity(), reduceFunction)}
   * </p>
   * 
   * @param <K>
   * @param groupFunction
   * @param reduceFunction
   * @return a Map with an entry for each group, where its key is the result of
   *         the grouping function, and the value is the result of the reduction
   *         of the elements for that group
   */
    @NonNull
    <K> Map<K, A> groupOn(Applicable<? super A, K> groupFunction, Applicable2<? super A, ? super A, A> reduceFunction);

    /**
   * Groups elements by the given {@code groupingFunction}, and reduces each
   * group using the given {@code mapFunction} and {@code reduceFunction}.
   * <p>
   * The grouping is performed so that two elements {@code a} and {@code b} will
   * be put in the same group if and only if
   * {@code  groupFunction.apply(a).equals(groupFunction.apply(b)) }
   * </p>
   * 
   * @param <K>
   * @param groupFunction
   * @param reduceFunction
   * @return a Map with an entry for each group, where its key is the result of
   *         the grouping function, and the value is the result of the reduction
   *         of the elements for that group
   */
    @NonNull
    <K, V> Map<K, V> groupOn(Applicable<? super A, K> groupFunction, Applicable<? super A, V> mapFunction, @NonNull Applicable2<? super V, ? super V, V> reduceFunction);

    /**
   * Groups elements by the given {@code groupingFunction}, and reduces each
   * group using the given {@code reduction}.
   * <p>
   * The grouping is performed so that two elements {@code a} and {@code b} will
   * be put in the same group if and only if
   * {@code  groupFunction.apply(a).equals(groupFunction.apply(b)) }
   * </p>
   * 
   * @param <K>
   * @param groupFunction
   * @param reduction
   * @return a Map with an entry for each group, where its key is the result of
   *         the grouping function, and the value is the result of the reduction
   *         of the elements for that group
   */
    @NonNull
    <K, V> Map<K, V> groupOn(Applicable<? super A, K> groupFunction, Reduction<A, V> reduction);
}
