package org.op4j.operators.impl.mapofarray;

import java.util.Map;
import org.javaruntype.type.Type;
import org.javaruntype.type.Types;
import org.op4j.functions.IFunction;
import org.op4j.operators.impl.AbstractOperatorImpl;
import org.op4j.operators.intf.mapofarray.Level1MapOfArrayEntriesOperator;
import org.op4j.operators.intf.mapofarray.Level2MapOfArrayEntriesKeyOperator;
import org.op4j.operators.intf.mapofarray.Level2MapOfArrayEntriesKeySelectedOperator;
import org.op4j.target.Target;
import org.op4j.target.Target.CastType;
import org.op4j.target.Target.Normalisation;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public final class Level2MapOfArrayEntriesKeyOperatorImpl<K, V> extends AbstractOperatorImpl implements Level2MapOfArrayEntriesKeyOperator<K, V> {

    private final Type<V> type;

    public Level2MapOfArrayEntriesKeyOperatorImpl(final Type<V> type, final Target target) {
        super(target);
        this.type = type;
    }

    public Level1MapOfArrayEntriesOperator<K, V> endOn() {
        return new Level1MapOfArrayEntriesOperatorImpl<K, V>(this.type, getTarget().endOn());
    }

    public <X> Level2MapOfArrayEntriesKeyOperator<X, V> asType(final Type<X> newType) {
        return new Level2MapOfArrayEntriesKeyOperatorImpl<X, V>(this.type, getTarget().cast(CastType.OBJECT, newType));
    }

    public Level2MapOfArrayEntriesKeyOperator<?, V> asUnknown() {
        return asType(Types.OBJECT);
    }

    public Map<K, V[]> get() {
        return endOn().endFor().get();
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifIndex(final int... indexes) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectIndex(indexes));
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifIndexNot(final int... indexes) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectIndexNot(indexes));
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifTrue(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectMatching(eval));
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifFalse(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotMatching(eval));
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifNotNull() {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNull());
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifNotNullAndTrue(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNullAndMatching(eval));
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifNotNullAndFalse(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNullAndNotMatching(eval));
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifNull() {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNull());
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifNullOrTrue(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNullOrMatching(eval));
    }

    public Level2MapOfArrayEntriesKeySelectedOperator<K, V> ifNullOrFalse(final IFunction<Boolean, ? super K> eval) {
        return new Level2MapOfArrayEntriesKeySelectedOperatorImpl<K, V>(this.type, getTarget().selectNullOrNotMatching(eval));
    }

    public <X> Level2MapOfArrayEntriesKeyOperator<X, V> execIfNotNull(final IFunction<X, ? super K> function) {
        return new Level2MapOfArrayEntriesKeyOperatorImpl<X, V>(this.type, getTarget().executeIfNotNull(function, Normalisation.NONE));
    }

    public <X> Level2MapOfArrayEntriesKeyOperator<X, V> exec(final IFunction<X, ? super K> function) {
        return new Level2MapOfArrayEntriesKeyOperatorImpl<X, V>(this.type, getTarget().execute(function, Normalisation.NONE));
    }

    public Level2MapOfArrayEntriesKeyOperator<K, V> replaceWith(final K replacement) {
        return new Level2MapOfArrayEntriesKeyOperatorImpl<K, V>(this.type, getTarget().replaceWith(replacement, Normalisation.NONE));
    }

    public Level2MapOfArrayEntriesKeyOperator<K, V> replaceIfNullWith(final K replacement) {
        return ifNull().replaceWith(replacement).endIf();
    }
}
