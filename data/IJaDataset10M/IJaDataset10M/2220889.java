package org.op4j.operators.intf.mapofmap;

import java.util.Map;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectedOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level2MapOfMapEntriesSelectedKeySelectedOperator<K1, K2, V, I> extends UniqOperator<Map<K1, Map<K2, V>>, I>, ExecutableSelectedOperator<K1, I>, ReplaceableOperator<K1, I>, SelectedOperator<K1, I> {

    public Level2MapOfMapEntriesSelectedKeyOperator<K1, K2, V, I> endIf();

    public Level2MapOfMapEntriesSelectedKeySelectedOperator<K1, K2, V, I> replaceWith(final K1 replacement);

    public Level2MapOfMapEntriesSelectedKeySelectedOperator<K1, K2, V, I> exec(final IFunction<? extends K1, ? super K1> function);

    public Level2MapOfMapEntriesSelectedKeySelectedOperator<K1, K2, V, I> eval(final IEvaluator<? extends K1, ? super K1> eval);

    public Level2MapOfMapEntriesSelectedKeySelectedOperator<K1, K2, V, I> convert(final IConverter<? extends K1, ? super K1> converter);
}
