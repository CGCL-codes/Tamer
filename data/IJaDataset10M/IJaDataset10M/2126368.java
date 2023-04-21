package org.op4j.operators.intf.setofmap;

import java.util.Map;
import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.NavigatingMapEntryOperator;
import org.op4j.operators.qualities.SelectableOperator;
import org.op4j.operators.qualities.UniqOperator;
import org.op4j.operators.qualities.ReplaceableOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level3SetOfMapElementsSelectedEntriesSelectedKeyOperator<K, V, I> extends UniqOperator<Set<Map<K, V>>, I>, NavigatingMapEntryOperator<I>, ExecutableSelectedOperator<K, I>, ReplaceableOperator<K, I>, SelectableOperator<K, I> {

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifIndex(final int... indices);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifTrue(final IEvaluator<Boolean, ? super K> eval);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifFalse(final IEvaluator<Boolean, ? super K> eval);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifNullOrFalse(final IEvaluator<Boolean, ? super K> eval);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifNotNullAndFalse(final IEvaluator<Boolean, ? super K> eval);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifNull();

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifNullOrTrue(final IEvaluator<Boolean, ? super K> eval);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifIndexNot(final int... indices);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifNotNull();

    public Level3SetOfMapElementsSelectedEntriesSelectedKeySelectedOperator<K, V, I> ifNotNullAndTrue(final IEvaluator<Boolean, ? super K> eval);

    public Level2SetOfMapElementsSelectedEntriesSelectedOperator<K, V, I> endOn();

    public Level3SetOfMapElementsSelectedEntriesSelectedKeyOperator<K, V, I> replaceWith(final K replacement);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeyOperator<K, V, I> exec(final IFunction<? extends K, ? super K> function);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeyOperator<K, V, I> eval(final IEvaluator<? extends K, ? super K> eval);

    public Level3SetOfMapElementsSelectedEntriesSelectedKeyOperator<K, V, I> convert(final IConverter<? extends K, ? super K> converter);
}
