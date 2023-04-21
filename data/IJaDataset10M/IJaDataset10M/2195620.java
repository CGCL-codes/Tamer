package org.op4j.operators.intf.listofmap;

import java.util.Map;
import java.util.List;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.SelectedOperator;
import org.op4j.operators.qualities.UniqOperator;
import org.op4j.operators.qualities.ReplaceableOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level3ListOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> extends UniqOperator<List<Map<K, V>>, I>, ExecutableSelectedOperator<V, I>, ReplaceableOperator<V, I>, SelectedOperator<V, I> {

    public Level3ListOfMapElementsSelectedEntriesValueOperator<K, V, I> endIf();

    public Level3ListOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> convert(final IConverter<? extends V, ? super V> converter);

    public Level3ListOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> eval(final IEvaluator<? extends V, ? super V> eval);

    public Level3ListOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> replaceWith(final V replacement);

    public Level3ListOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> exec(final IFunction<? extends V, ? super V> function);
}
