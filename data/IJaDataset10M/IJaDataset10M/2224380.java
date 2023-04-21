package org.op4j.operators.intf.arrayofset;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.mapbuild.IMapBuilder;
import org.op4j.operators.intf.array.ILevel0ArrayOperator;
import org.op4j.operators.intf.arrayofarray.ILevel0ArrayOfArrayOperator;
import org.op4j.operators.intf.arrayoflist.ILevel0ArrayOfListOperator;
import org.op4j.operators.intf.arrayofmap.ILevel0ArrayOfMapOperator;
import org.op4j.operators.intf.generic.ILevel0GenericUniqOperator;
import org.op4j.operators.intf.listofarray.ILevel0ListOfArrayOperator;
import org.op4j.operators.intf.listoflist.ILevel0ListOfListOperator;
import org.op4j.operators.intf.listofmap.ILevel0ListOfMapOperator;
import org.op4j.operators.intf.listofset.ILevel0ListOfSetOperator;
import org.op4j.operators.intf.setofarray.ILevel0SetOfArrayOperator;
import org.op4j.operators.intf.setoflist.ILevel0SetOfListOperator;
import org.op4j.operators.intf.setofmap.ILevel0SetOfMapOperator;
import org.op4j.operators.intf.setofset.ILevel0SetOfSetOperator;
import org.op4j.operators.qualities.ICastableToArrayOfSetOperator;
import org.op4j.operators.qualities.IConvertibleOperator;
import org.op4j.operators.qualities.IConvertibleToArrayOfArrayOperator;
import org.op4j.operators.qualities.IConvertibleToArrayOfListOperator;
import org.op4j.operators.qualities.IConvertibleToArrayOfMapOperator;
import org.op4j.operators.qualities.IConvertibleToListOfArrayOperator;
import org.op4j.operators.qualities.IConvertibleToListOfListOperator;
import org.op4j.operators.qualities.IConvertibleToListOfMapOperator;
import org.op4j.operators.qualities.IConvertibleToListOfSetOperator;
import org.op4j.operators.qualities.IConvertibleToSetOfArrayOperator;
import org.op4j.operators.qualities.IConvertibleToSetOfListOperator;
import org.op4j.operators.qualities.IConvertibleToSetOfMapOperator;
import org.op4j.operators.qualities.IConvertibleToSetOfSetOperator;
import org.op4j.operators.qualities.IDistinguishableOperator;
import org.op4j.operators.qualities.IEvaluableOperator;
import org.op4j.operators.qualities.IExecutableOperator;
import org.op4j.operators.qualities.IFlattenableAsArrayOperator;
import org.op4j.operators.qualities.IGenerizableOperator;
import org.op4j.operators.qualities.IModifiableCollectionOperator;
import org.op4j.operators.qualities.INavigableCollectionOperator;
import org.op4j.operators.qualities.ISortableOperator;
import org.op4j.operators.qualities.IUniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ILevel0ArrayOfSetOperator<T> extends IUniqOperator<Set<T>[]>, INavigableCollectionOperator<Set<T>>, IDistinguishableOperator, ICastableToArrayOfSetOperator, ISortableOperator<Set<T>>, IFlattenableAsArrayOperator<T>, IModifiableCollectionOperator<Set<T>>, IGenerizableOperator<Set<T>[]>, IExecutableOperator<Set<T>[]>, IConvertibleOperator<Set<T>[]>, IEvaluableOperator<Set<T>[]>, IConvertibleToArrayOfArrayOperator<T>, IConvertibleToArrayOfListOperator<T>, IConvertibleToListOfArrayOperator<T>, IConvertibleToListOfListOperator<T>, IConvertibleToListOfSetOperator<T>, IConvertibleToSetOfArrayOperator<T>, IConvertibleToSetOfListOperator<T>, IConvertibleToSetOfSetOperator<T>, IConvertibleToArrayOfMapOperator<T>, IConvertibleToListOfMapOperator<T>, IConvertibleToSetOfMapOperator<T> {

    public ILevel1ArrayOfSetElementsOperator<T> forEach();

    public ILevel0ArrayOfSetOperator<T> distinct();

    public ILevel0ArrayOfSetOperator<T> sort();

    public ILevel0ArrayOfSetOperator<T> sort(final Comparator<? super Set<T>> comparator);

    public ILevel0ArrayOperator<T> flatten(final Type<? super T> type);

    public ILevel0ArrayOfSetOperator<T> add(final Set<T>... newElements);

    public ILevel0ArrayOfSetOperator<T> insert(final int position, final Set<T>... newElements);

    public ILevel0ArrayOfSetOperator<T> addAll(final Collection<Set<T>> collection);

    public ILevel0ArrayOfSetOperator<T> removeIndexes(final int... indices);

    public ILevel0ArrayOfSetOperator<T> removeEquals(final Set<T>... values);

    public ILevel0ArrayOfSetOperator<T> removeMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel0ArrayOfSetOperator<T> removeNotMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel0ArrayOfSetOperator<T> removeNullOrNotMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel0ArrayOfSetOperator<T> removeNotNullNotMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel0ArrayOfSetOperator<T> removeNotNullMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel0ArrayOfSetOperator<T> removeNullOrMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel0ArrayOfSetOperator<T> removeIndexesNot(final int... indices);

    public ILevel0ArrayOfSetOperator<T> removeNulls();

    public ILevel0ArrayOfArrayOperator<T> toArrayOfArray(final Type<T> of);

    public ILevel0ArrayOfListOperator<T> toArrayOfList();

    public ILevel0ListOfArrayOperator<T> toListOfArray(final Type<T> of);

    public ILevel0ListOfListOperator<T> toListOfList();

    public ILevel0ListOfSetOperator<T> toListOfSet();

    public ILevel0SetOfArrayOperator<T> toSetOfArray(final Type<T> of);

    public ILevel0SetOfListOperator<T> toSetOfList();

    public ILevel0SetOfSetOperator<T> toSetOfSet();

    public ILevel0ArrayOfMapOperator<T, T> toArrayOfMap();

    public <K> ILevel0ArrayOfMapOperator<K, T> toArrayOfMap(final IEvaluator<K, ? super T> keyEval);

    public <K, V> ILevel0ArrayOfMapOperator<K, V> toArrayOfMap(final IMapBuilder<K, V, ? super T> mapBuild);

    public ILevel0ListOfMapOperator<T, T> toListOfMap();

    public <K> ILevel0ListOfMapOperator<K, T> toListOfMap(final IEvaluator<K, ? super T> keyEval);

    public <K, V> ILevel0ListOfMapOperator<K, V> toListOfMap(final IMapBuilder<K, V, ? super T> mapBuild);

    public ILevel0SetOfMapOperator<T, T> toSetOfMap();

    public <K> ILevel0SetOfMapOperator<K, T> toSetOfMap(final IEvaluator<K, ? super T> keyEval);

    public <K, V> ILevel0SetOfMapOperator<K, V> toSetOfMap(final IMapBuilder<K, V, ? super T> mapBuild);

    public ILevel1ArrayOfSetElementsOperator<T> forEachIndex(final int... indices);

    public ILevel1ArrayOfSetElementsOperator<T> forEachMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel1ArrayOfSetElementsOperator<T> forEachNotMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel1ArrayOfSetElementsOperator<T> forEachNullOrNotMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel1ArrayOfSetElementsOperator<T> forEachNotNullNotMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel1ArrayOfSetElementsOperator<T> forEachNullOrMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel1ArrayOfSetElementsOperator<T> forEachNotNullMatching(final IEvaluator<Boolean, ? super Set<T>> eval);

    public ILevel1ArrayOfSetElementsOperator<T> forEachNull();

    public ILevel1ArrayOfSetElementsOperator<T> forEachIndexNot(final int... indices);

    public ILevel1ArrayOfSetElementsOperator<T> forEachNotNull();

    public ILevel0GenericUniqOperator<Set<T>[]> generic();

    public <X> ILevel0GenericUniqOperator<X> convert(final IConverter<X, ? super Set<T>[]> converter);

    public <X> ILevel0GenericUniqOperator<X> eval(final IEvaluator<X, ? super Set<T>[]> eval);

    public <X> ILevel0GenericUniqOperator<X> exec(final IFunction<X, ? super Set<T>[]> function);

    public <X> ILevel0ArrayOfSetOperator<X> asArrayOfSetOf(final Type<X> type);

    public ILevel0ArrayOfSetOperator<?> asArrayOfSetOfUnknown();
}
