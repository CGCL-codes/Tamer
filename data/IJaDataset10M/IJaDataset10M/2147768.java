package org.op4j.operators.impl.mapofarray;

import java.util.Map;
import org.javaruntype.type.Type;
import org.javaruntype.type.Types;
import org.op4j.functions.IFunction;
import org.op4j.operators.impl.AbstractOperatorImpl;
import org.op4j.operators.intf.mapofarray.Level2MapOfArrayEntriesValueOperator;
import org.op4j.operators.intf.mapofarray.Level3MapOfArrayEntriesValueElementsOperator;
import org.op4j.operators.intf.mapofarray.Level3MapOfArrayEntriesValueElementsSelectedOperator;
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
public final class Level3MapOfArrayEntriesValueElementsOperatorImpl<K, V> extends AbstractOperatorImpl implements Level3MapOfArrayEntriesValueElementsOperator<K, V> {

    private final Type<V> type;

    public Level3MapOfArrayEntriesValueElementsOperatorImpl(final Type<V> type, final Target target) {
        super(target);
        this.type = type;
    }

    public Level2MapOfArrayEntriesValueOperator<K, V> endFor() {
        return new Level2MapOfArrayEntriesValueOperatorImpl<K, V>(this.type, getTarget().endIterate(this.type.getRawClass()));
    }

    public <X> Level3MapOfArrayEntriesValueElementsOperator<K, X> asType(final Type<X> elementType) {
        return new Level3MapOfArrayEntriesValueElementsOperatorImpl<K, X>(elementType, getTarget().cast(CastType.OBJECT, elementType));
    }

    public Level3MapOfArrayEntriesValueElementsOperator<K, ?> asUnknown() {
        return asType(Types.OBJECT);
    }

    public Map<K, V[]> get() {
        return endFor().endOn().endFor().get();
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifIndex(final int... indexes) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectIndex(indexes));
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifIndexNot(final int... indexes) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectIndexNot(indexes));
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifTrue(final IFunction<Boolean, ? super V> eval) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectMatching(eval));
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifFalse(final IFunction<Boolean, ? super V> eval) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectNotMatching(eval));
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifNotNull() {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNull());
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifNotNullAndTrue(final IFunction<Boolean, ? super V> eval) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNullAndMatching(eval));
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifNotNullAndFalse(final IFunction<Boolean, ? super V> eval) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectNotNullAndNotMatching(eval));
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifNull() {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectNull());
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifNullOrTrue(final IFunction<Boolean, ? super V> eval) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectNullOrMatching(eval));
    }

    public Level3MapOfArrayEntriesValueElementsSelectedOperator<K, V> ifNullOrFalse(final IFunction<Boolean, ? super V> eval) {
        return new Level3MapOfArrayEntriesValueElementsSelectedOperatorImpl<K, V>(this.type, getTarget().selectNullOrNotMatching(eval));
    }

    public Level3MapOfArrayEntriesValueElementsOperator<K, V> execIfNotNull(final IFunction<? extends V, ? super V> function) {
        return new Level3MapOfArrayEntriesValueElementsOperatorImpl<K, V>(this.type, getTarget().executeIfNotNull(function, Normalisation.NONE));
    }

    public Level3MapOfArrayEntriesValueElementsOperator<K, V> exec(final IFunction<? extends V, ? super V> function) {
        return new Level3MapOfArrayEntriesValueElementsOperatorImpl<K, V>(this.type, getTarget().execute(function, Normalisation.NONE));
    }

    public <X> Level3MapOfArrayEntriesValueElementsOperator<K, X> exec(final Type<X> valueType, final IFunction<X, ? super V> function) {
        return new Level3MapOfArrayEntriesValueElementsOperatorImpl<K, X>(valueType, getTarget().execute(function, Normalisation.NONE));
    }

    public Level3MapOfArrayEntriesValueElementsOperator<K, V> replaceWith(final V replacement) {
        return new Level3MapOfArrayEntriesValueElementsOperatorImpl<K, V>(this.type, getTarget().replaceWith(replacement, Normalisation.NONE));
    }

    public Level3MapOfArrayEntriesValueElementsOperator<K, V> replaceIfNullWith(final V replacement) {
        return ifNull().replaceWith(replacement).endIf();
    }
}
