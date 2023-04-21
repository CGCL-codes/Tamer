package org.op4j.operators.intf.listofmap;

import java.util.List;
import java.util.Map;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.CastableToTypeOperator;
import org.op4j.operators.qualities.ExecutableOperator;
import org.op4j.operators.qualities.NavigatingMapEntryOperator;
import org.op4j.operators.qualities.ReplaceableIfNullOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectableOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level3ListOfMapElementsEntriesValueOperator<K, V, I> extends UniqOperator<List<Map<K, V>>, I>, NavigatingMapEntryOperator<I>, ExecutableOperator<V, I>, SelectableOperator<V, I>, ReplaceableOperator<V, I>, ReplaceableIfNullOperator<V, I>, CastableToTypeOperator<V, I> {

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifIndex(final int... indices);

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifTrue(final IEvaluator<Boolean, ? super V> eval);

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifFalse(final IEvaluator<Boolean, ? super V> eval);

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifNullOrFalse(final IEvaluator<Boolean, ? super V> eval);

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifNotNullAndFalse(final IEvaluator<Boolean, ? super V> eval);

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifNull();

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifNullOrTrue(final IEvaluator<Boolean, ? super V> eval);

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifIndexNot(final int... indices);

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifNotNull();

    public Level3ListOfMapElementsEntriesValueSelectedOperator<K, V, I> ifNotNullAndTrue(final IEvaluator<Boolean, ? super V> eval);

    public Level2ListOfMapElementsEntriesOperator<K, V, I> endOn();

    public Level3ListOfMapElementsEntriesValueOperator<K, V, I> replaceWith(final V replacement);

    public Level3ListOfMapElementsEntriesValueOperator<K, V, I> replaceIfNullWith(final V replacement);

    public <X> Level3ListOfMapElementsEntriesValueOperator<K, X, I> convert(final IConverter<X, ? super V> converter);

    public <X> Level3ListOfMapElementsEntriesValueOperator<K, X, I> eval(final IEvaluator<X, ? super V> eval);

    public <X> Level3ListOfMapElementsEntriesValueOperator<K, X, I> exec(final IFunction<X, ? super V> function);

    public <X> Level3ListOfMapElementsEntriesValueOperator<K, X, I> asType(final Type<X> type);

    public Level3ListOfMapElementsEntriesValueOperator<K, ?, I> asUnknown();
}
