package org.op4j.operators.impl.op.map;

import java.util.Map;
import org.op4j.functions.IFunction;
import org.op4j.operators.impl.AbstractOperator;
import org.op4j.operators.intf.map.ILevel2MapSelectedEntriesValueOperator;
import org.op4j.operators.qualities.UniqOpOperator;
import org.op4j.target.Target;
import org.op4j.target.Target.Normalisation;

public final class Level2MapSelectedEntriesValueOperator<I, K, V> extends AbstractOperator implements UniqOpOperator<I, Map<K, V>>, ILevel2MapSelectedEntriesValueOperator<I, K, V> {

    public Level2MapSelectedEntriesValueOperator(final Target target) {
        super(target);
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifTrue(final IFunction<? super V, Boolean> eval) {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectMatching(eval));
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifFalse(final IFunction<? super V, Boolean> eval) {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectNotMatching(eval));
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifNull() {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectNull());
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifNotNull() {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectNotNull());
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifNullOrFalse(final IFunction<? super V, Boolean> eval) {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectNullOrNotMatching(eval));
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifNotNullAndFalse(final IFunction<? super V, Boolean> eval) {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectNotNullAndNotMatching(eval));
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifNullOrTrue(final IFunction<? super V, Boolean> eval) {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectNullOrMatching(eval));
    }

    public Level2MapSelectedEntriesValueSelectedOperator<I, K, V> ifNotNullAndTrue(final IFunction<? super V, Boolean> eval) {
        return new Level2MapSelectedEntriesValueSelectedOperator<I, K, V>(getTarget().selectNotNullAndMatching(eval));
    }

    public Level1MapSelectedEntriesOperator<I, K, V> endOn() {
        return new Level1MapSelectedEntriesOperator<I, K, V>(getTarget().endOn());
    }

    public Level2MapSelectedEntriesValueOperator<I, K, V> exec(final IFunction<? super V, ? extends V> function) {
        return new Level2MapSelectedEntriesValueOperator<I, K, V>(getTarget().execute(function, Normalisation.NONE));
    }

    public Level2MapSelectedEntriesValueOperator<I, K, V> replaceWith(final V replacement) {
        return new Level2MapSelectedEntriesValueOperator<I, K, V>(getTarget().replaceWith(replacement, Normalisation.NONE));
    }

    public Map<K, V> get() {
        return endOn().get();
    }
}
