package org.op4j.operators.impl.arrayofmap;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import org.javaruntype.type.Type;
import org.javaruntype.type.Types;
import org.op4j.exceptions.NonEmptyTargetException;
import org.op4j.functions.ArrayFuncs;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.converters.ToList;
import org.op4j.functions.converters.ToMap;
import org.op4j.functions.converters.ToSet;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.mapbuild.IMapBuilder;
import org.op4j.operations.Operation;
import org.op4j.operators.impl.AbstractOperatorImpl;
import org.op4j.operators.impl.generic.Level0GenericUniqOperatorImpl;
import org.op4j.operators.impl.listofmap.Level0ListOfMapOperatorImpl;
import org.op4j.operators.impl.mapofmap.Level0MapOfMapOperatorImpl;
import org.op4j.operators.impl.setofmap.Level0SetOfMapOperatorImpl;
import org.op4j.operators.intf.arrayofmap.Level0ArrayOfMapOperator;
import org.op4j.operators.intf.arrayofmap.Level0ArrayOfMapSelectedOperator;
import org.op4j.operators.intf.arrayofmap.Level1ArrayOfMapElementsOperator;
import org.op4j.operators.intf.generic.Level0GenericUniqOperator;
import org.op4j.operators.intf.listofmap.Level0ListOfMapOperator;
import org.op4j.operators.intf.mapofmap.Level0MapOfMapOperator;
import org.op4j.operators.intf.setofmap.Level0SetOfMapOperator;
import org.op4j.target.OperationChainingTarget;
import org.op4j.target.Target;
import org.op4j.target.Target.Normalization;
import org.op4j.util.NormalizationUtils;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public class Level0ArrayOfMapOperatorImpl<K, V, I> extends AbstractOperatorImpl implements Level0ArrayOfMapOperator<K, V, I> {

    public Level0ArrayOfMapOperatorImpl(final Target target) {
        super(target);
    }

    @SuppressWarnings("unchecked")
    public Level0ArrayOfMapOperator<K, V, I> add(final Map<K, V> newElement) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.Add<Map<K, V>>(NormalizationUtils.normalizeMap(newElement))));
    }

    public Level0ArrayOfMapOperator<K, V, I> addAll(final Map<K, V>... newElements) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.Add<Map<K, V>>(NormalizationUtils.normalizeMaps(newElements))));
    }

    @SuppressWarnings("unchecked")
    public Level0ArrayOfMapOperator<K, V, I> insert(final int position, final Map<K, V> newElement) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.Insert<Map<K, V>>(position, NormalizationUtils.normalizeMap(newElement))));
    }

    public Level0ArrayOfMapOperator<K, V, I> insertAll(final int position, final Map<K, V>... newElements) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.Insert<Map<K, V>>(position, NormalizationUtils.normalizeMaps(newElements))));
    }

    public Level0ArrayOfMapOperator<K, V, I> addAll(final Collection<Map<K, V>> collection) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.AddAll<Map<K, V>>(NormalizationUtils.normalizeMaps(collection))));
    }

    public Level0ArrayOfMapOperator<K, V, I> distinct() {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.Distinct<Map<K, V>>()));
    }

    public Level1ArrayOfMapElementsOperator<K, V, I> forEach() {
        return new Level1ArrayOfMapElementsOperatorImpl<K, V, I>(getTarget().iterate());
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllIndexes(final int... indices) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllIndexes<Map<K, V>>(indices)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllEqual(final Map<K, V>... values) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllEqual<Map<K, V>>(values)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllTrue(final IEvaluator<Boolean, ? super Map<K, V>> eval) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllTrue<Map<K, V>>(eval)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllFalse(final IEvaluator<Boolean, ? super Map<K, V>> eval) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllFalse<Map<K, V>>(eval)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllNullOrFalse(final IEvaluator<Boolean, ? super Map<K, V>> eval) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllNullOrFalse<Map<K, V>>(eval)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllNotNullAndFalse(final IEvaluator<Boolean, ? super Map<K, V>> eval) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllNotNullAndFalse<Map<K, V>>(eval)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllNullOrTrue(final IEvaluator<Boolean, ? super Map<K, V>> eval) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllNullOrTrue<Map<K, V>>(eval)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllNotNullAndTrue(final IEvaluator<Boolean, ? super Map<K, V>> eval) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllNotNullAndTrue<Map<K, V>>(eval)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllIndexesNot(final int... indices) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllIndexesNot<Map<K, V>>(indices)));
    }

    public Level0ArrayOfMapOperator<K, V, I> removeAllNull() {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.RemoveAllNull<Map<K, V>>()));
    }

    @SuppressWarnings("unchecked")
    public Level0ArrayOfMapOperator<K, V, I> sort() {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.Sort()));
    }

    public Level0ArrayOfMapOperator<K, V, I> sort(final Comparator<? super Map<K, V>> comparator) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().execute(new ArrayFuncs.SortByComparator<Map<K, V>>(comparator)));
    }

    @SuppressWarnings("unchecked")
    public Map<K, V>[] get() {
        return (Map<K, V>[]) getTarget().get();
    }

    public Level0GenericUniqOperator<Map<K, V>[], I> generic() {
        return new Level0GenericUniqOperatorImpl<Map<K, V>[], I>(getTarget());
    }

    public <X, Y> Level0ArrayOfMapOperator<X, Y, I> asArrayOfMapOf(final Type<X> keyType, final Type<Y> valueType) {
        return generic().asArrayOfMapOf(keyType, valueType);
    }

    public Level0ArrayOfMapOperator<?, ?, I> asArrayOfMapOfUnknown() {
        return asArrayOfMapOf(Types.OBJECT, Types.OBJECT);
    }

    public Level0ListOfMapOperator<K, V, I> toListOfMap() {
        return new Level0ListOfMapOperatorImpl<K, V, I>(getTarget().execute(new ToList.FromArray<Map<K, V>>()));
    }

    public <K1> Level0MapOfMapOperator<K1, K, V, I> toMapOfMap(final IEvaluator<K1, ? super Map<K, V>> keyEval) {
        return new Level0MapOfMapOperatorImpl<K1, K, V, I>(getTarget().execute(new ToMap.FromArrayByKeyEval<K1, Map<K, V>>(keyEval)));
    }

    public <K1, K2, V2> Level0MapOfMapOperator<K1, K2, V2, I> toMapOfMap(final IMapBuilder<K1, Map<K2, V2>, ? super Map<K, V>> mapBuild) {
        return new Level0MapOfMapOperatorImpl<K1, K2, V2, I>(getTarget().execute(new ToMap.FromArrayByMapBuilder<K1, Map<K2, V2>, Map<K, V>>(mapBuild)));
    }

    public Level0SetOfMapOperator<K, V, I> toSetOfMap() {
        return new Level0SetOfMapOperatorImpl<K, V, I>(getTarget().execute(new ToSet.FromArray<Map<K, V>>()));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifIndex(final int... indices) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectIndex(indices));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifIndexNot(final int... indices) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectIndexNot(indices));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifTrue(final IEvaluator<Boolean, ? super Map<K, V>[]> eval) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectMatching(eval));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifFalse(final IEvaluator<Boolean, ? super Map<K, V>[]> eval) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectNotMatching(eval));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifNotNull() {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectNotNull());
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifNotNullAndTrue(final IEvaluator<Boolean, ? super Map<K, V>[]> eval) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectNotNullAndMatching(eval));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifNotNullAndFalse(final IEvaluator<Boolean, ? super Map<K, V>[]> eval) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectNotNullAndNotMatching(eval));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifNull() {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectNull());
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifNullOrTrue(final IEvaluator<Boolean, ? super Map<K, V>[]> eval) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectNullOrMatching(eval));
    }

    public Level0ArrayOfMapSelectedOperator<K, V, I> ifNullOrFalse(final IEvaluator<Boolean, ? super Map<K, V>[]> eval) {
        return new Level0ArrayOfMapSelectedOperatorImpl<K, V, I>(getTarget().selectNullOrNotMatching(eval));
    }

    public <X, Y> Level0ArrayOfMapOperator<X, Y, I> convert(final IConverter<? extends Map<X, Y>[], ? super Map<K, V>[]> converter) {
        return new Level0ArrayOfMapOperatorImpl<X, Y, I>(getTarget().execute(converter, Normalization.ARRAY_OF_MAP));
    }

    public <X, Y> Level0ArrayOfMapOperator<X, Y, I> eval(final IEvaluator<? extends Map<X, Y>[], ? super Map<K, V>[]> eval) {
        return new Level0ArrayOfMapOperatorImpl<X, Y, I>(getTarget().execute(eval, Normalization.ARRAY_OF_MAP));
    }

    public <X, Y> Level0ArrayOfMapOperator<X, Y, I> exec(final IFunction<? extends Map<X, Y>[], ? super Map<K, V>[]> function) {
        return new Level0ArrayOfMapOperatorImpl<X, Y, I>(getTarget().execute(function, Normalization.ARRAY_OF_MAP));
    }

    public <X> Level0GenericUniqOperator<X, I> convert(final Type<X> resultType, final IConverter<? extends X, ? super Map<K, V>[]> converter) {
        return new Level0GenericUniqOperatorImpl<X, I>(getTarget().execute(converter, Normalization.NONE));
    }

    public <X> Level0GenericUniqOperator<X, I> eval(final Type<X> resultType, final IEvaluator<? extends X, ? super Map<K, V>[]> eval) {
        return new Level0GenericUniqOperatorImpl<X, I>(getTarget().execute(eval, Normalization.NONE));
    }

    public <X> Level0GenericUniqOperator<X, I> exec(final Type<X> resultType, final IFunction<? extends X, ? super Map<K, V>[]> function) {
        return new Level0GenericUniqOperatorImpl<X, I>(getTarget().execute(function, Normalization.NONE));
    }

    public Level0ArrayOfMapOperator<K, V, I> replaceWith(final Map<K, V>[] replacement) {
        return new Level0ArrayOfMapOperatorImpl<K, V, I>(getTarget().replaceWith(replacement));
    }

    public Level0ArrayOfMapOperator<K, V, I> replaceIfNullWith(final Map<K, V>[] replacement) {
        return ifNull().replaceWith(replacement).endIf();
    }

    public Operation<Map<K, V>[], I> createOperation() {
        final Target target = getTarget();
        if (!(target instanceof OperationChainingTarget)) {
            throw new NonEmptyTargetException();
        }
        final OperationChainingTarget ocTarget = (OperationChainingTarget) target;
        if (!ocTarget.isEmpty()) {
            throw new NonEmptyTargetException();
        }
        return new Operation<Map<K, V>[], I>(ocTarget);
    }
}
