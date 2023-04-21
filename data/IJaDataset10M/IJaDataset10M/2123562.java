package org.op4j.operators.intf.map;

import java.util.Map;
import org.op4j.functions.IFunction;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.NavigatingMapEntryOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectableMapEntryComponentOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ILevel2MapEntriesSelectedKeyOperator<I, K, V> extends UniqOperator<Map<K, V>>, NavigatingMapEntryOperator, ExecutableSelectedOperator<K>, ReplaceableOperator<K>, SelectableMapEntryComponentOperator<K> {

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifTrue(final IFunction<? super K, Boolean> eval);

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifFalse(final IFunction<? super K, Boolean> eval);

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifNullOrFalse(final IFunction<? super K, Boolean> eval);

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifNotNullAndFalse(final IFunction<? super K, Boolean> eval);

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifNull();

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifNullOrTrue(final IFunction<? super K, Boolean> eval);

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifNotNull();

    public ILevel2MapEntriesSelectedKeySelectedOperator<I, K, V> ifNotNullAndTrue(final IFunction<? super K, Boolean> eval);

    public ILevel1MapEntriesSelectedOperator<I, K, V> endOn();

    public ILevel2MapEntriesSelectedKeyOperator<I, K, V> replaceWith(final K replacement);

    public ILevel2MapEntriesSelectedKeyOperator<I, K, V> exec(final IFunction<? super K, ? extends K> function);
}
