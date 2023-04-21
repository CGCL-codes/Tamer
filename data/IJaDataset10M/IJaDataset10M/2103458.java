package org.op4j.operators.impl.setofmap;

import java.util.Map;
import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operations.Operation;
import org.op4j.operators.impl.AbstractOperatorImpl;
import org.op4j.operators.intf.setofmap.Level2SetOfMapElementsSelectedEntriesOperator;
import org.op4j.operators.intf.setofmap.Level3SetOfMapElementsSelectedEntriesValueOperator;
import org.op4j.operators.intf.setofmap.Level3SetOfMapElementsSelectedEntriesValueSelectedOperator;
import org.op4j.target.Target;
import org.op4j.target.Target.Normalization;
import org.op4j.target.Target.Structure;

public class Level3SetOfMapElementsSelectedEntriesValueOperatorImpl<K, V, I> extends AbstractOperatorImpl implements Level3SetOfMapElementsSelectedEntriesValueOperator<K, V, I> {

    public Level3SetOfMapElementsSelectedEntriesValueOperatorImpl(final Target target) {
        super(target);
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifIndex(final int... indices) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectIndex(indices));
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifTrue(final IEvaluator<Boolean, ? super V> eval) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectMatching(eval));
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifFalse(final IEvaluator<Boolean, ? super V> eval) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectNotMatching(eval));
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifNullOrFalse(final IEvaluator<Boolean, ? super V> eval) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectNullOrNotMatching(eval));
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifNotNullAndFalse(final IEvaluator<Boolean, ? super V> eval) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectNotNullAndNotMatching(eval));
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifNull() {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectNull());
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifNullOrTrue(final IEvaluator<Boolean, ? super V> eval) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectNullOrMatching(eval));
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifIndexNot(final int... indices) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectIndexNot(indices));
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifNotNull() {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectNotNull());
    }

    public Level3SetOfMapElementsSelectedEntriesValueSelectedOperator<K, V, I> ifNotNullAndTrue(final IEvaluator<Boolean, ? super V> eval) {
        return new Level3SetOfMapElementsSelectedEntriesValueSelectedOperatorImpl<K, V, I>(getTarget().selectNotNullAndMatching(eval));
    }

    public Level3SetOfMapElementsSelectedEntriesValueOperator<K, V, I> eval(final IEvaluator<? extends V, ? super V> eval) {
        return new Level3SetOfMapElementsSelectedEntriesValueOperatorImpl<K, V, I>(getTarget().execute(eval, Normalization.NONE));
    }

    public Level2SetOfMapElementsSelectedEntriesOperator<K, V, I> endOn() {
        return new Level2SetOfMapElementsSelectedEntriesOperatorImpl<K, V, I>(getTarget().endIterate(Structure.MAP_ENTRY, null));
    }

    public Level3SetOfMapElementsSelectedEntriesValueOperator<K, V, I> exec(final IFunction<? extends V, ? super V> function) {
        return new Level3SetOfMapElementsSelectedEntriesValueOperatorImpl<K, V, I>(getTarget().execute(function, Normalization.NONE));
    }

    public Level3SetOfMapElementsSelectedEntriesValueOperator<K, V, I> replaceWith(final V replacement) {
        return new Level3SetOfMapElementsSelectedEntriesValueOperatorImpl<K, V, I>(getTarget().replaceWith(replacement));
    }

    public Level3SetOfMapElementsSelectedEntriesValueOperator<K, V, I> convert(final IConverter<? extends V, ? super V> converter) {
        return new Level3SetOfMapElementsSelectedEntriesValueOperatorImpl<K, V, I>(getTarget().execute(converter, Normalization.NONE));
    }

    public Set<Map<K, V>> get() {
        return endOn().get();
    }

    public Operation<Set<Map<K, V>>, I> createOperation() {
        return endOn().createOperation();
    }
}
