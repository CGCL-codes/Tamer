package org.op4j.operators.impl.mapofarray;

import java.util.Map;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.operators.impl.AbstractOperatorImpl;
import org.op4j.operators.intf.mapofarray.Level1MapOfArraySelectedEntriesSelectedOperator;
import org.op4j.operators.intf.mapofarray.Level2MapOfArraySelectedEntriesSelectedKeyOperator;
import org.op4j.operators.intf.mapofarray.Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator;
import org.op4j.target.Target;
import org.op4j.target.Target.Normalisation;

public final class Level2MapOfArraySelectedEntriesSelectedKeyOperatorImpl<K, V> extends AbstractOperatorImpl implements Level2MapOfArraySelectedEntriesSelectedKeyOperator<K, V> {

    private final Type<V> type;

    public Level2MapOfArraySelectedEntriesSelectedKeyOperatorImpl(final Type<V> type, final Target target) {
        super(target);
        this.type = type;
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifIndex(final int... indexes) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectIndex(indexes));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifTrue(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectMatching(eval));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifFalse(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotMatching(eval));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifNullOrFalse(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNullOrNotMatching(eval));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifNotNullAndFalse(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNullAndNotMatching(eval));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifNull() {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNull());
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifNullOrTrue(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNullOrMatching(eval));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifIndexNot(final int... indexes) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectIndexNot(indexes));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifNotNull() {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNull());
    }

    public Level2MapOfArraySelectedEntriesSelectedKeySelectedOperator<K, V> ifNotNullAndTrue(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArraySelectedEntriesSelectedKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNullAndMatching(eval));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeyOperator<K, V> execIfNotNull(final IFunction<? extends K, ? super K> function) {
        return new Level2MapOfArraySelectedEntriesSelectedKeyOperatorImpl<K, V>(this.type, getTarget().executeIfNotNull(function, Normalisation.NONE));
    }

    public Level1MapOfArraySelectedEntriesSelectedOperator<K, V> endOn() {
        return new Level1MapOfArraySelectedEntriesSelectedOperatorImpl<K, V>(this.type, getTarget().endOn());
    }

    public Level2MapOfArraySelectedEntriesSelectedKeyOperator<K, V> exec(final IFunction<? extends K, ? super K> function) {
        return new Level2MapOfArraySelectedEntriesSelectedKeyOperatorImpl<K, V>(this.type, getTarget().execute(function, Normalisation.NONE));
    }

    public Level2MapOfArraySelectedEntriesSelectedKeyOperator<K, V> replaceWith(final K replacement) {
        return new Level2MapOfArraySelectedEntriesSelectedKeyOperatorImpl<K, V>(this.type, getTarget().replaceWith(replacement, Normalisation.NONE));
    }

    public Map<K, V[]> get() {
        return endOn().get();
    }
}
