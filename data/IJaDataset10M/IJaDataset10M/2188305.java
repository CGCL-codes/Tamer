package org.op4j.operators.intf.listofmap;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.mapbuild.IMapBuilder;
import org.op4j.operators.intf.arrayofmap.ILevel0ArrayOfMapOperator;
import org.op4j.operators.intf.generic.ILevel0GenericUniqOperator;
import org.op4j.operators.intf.mapofmap.ILevel0MapOfMapOperator;
import org.op4j.operators.intf.setofmap.ILevel0SetOfMapOperator;
import org.op4j.operators.qualities.ICastableToListOfMapOperator;
import org.op4j.operators.qualities.IConvertibleOperator;
import org.op4j.operators.qualities.IConvertibleToArrayOfMapFromStructureOfMapOperator;
import org.op4j.operators.qualities.IConvertibleToMapOfMapFromStructureOfMapOperator;
import org.op4j.operators.qualities.IConvertibleToSetOfMapFromStructureOfMapOperator;
import org.op4j.operators.qualities.IDistinguishableOperator;
import org.op4j.operators.qualities.IEvaluableOperator;
import org.op4j.operators.qualities.IExecutableOperator;
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
public interface ILevel0ListOfMapOperator<K, V> extends IUniqOperator<List<Map<K, V>>>, INavigableCollectionOperator<Map<K, V>>, IDistinguishableOperator, ICastableToListOfMapOperator, ISortableOperator<Map<K, V>>, IExecutableOperator<List<Map<K, V>>>, IConvertibleToArrayOfMapFromStructureOfMapOperator<K, V>, IConvertibleToMapOfMapFromStructureOfMapOperator<K, V>, IConvertibleToSetOfMapFromStructureOfMapOperator<K, V>, IConvertibleOperator<List<Map<K, V>>>, IEvaluableOperator<List<Map<K, V>>>, IModifiableCollectionOperator<Map<K, V>>, IGenerizableOperator<List<Map<K, V>>> {

    public ILevel1ListOfMapElementsOperator<K, V> forEach();

    public ILevel0ListOfMapOperator<K, V> distinct();

    public ILevel0ListOfMapOperator<K, V> sort();

    public ILevel0ListOfMapOperator<K, V> sort(final Comparator<? super Map<K, V>> comparator);

    public ILevel0ListOfMapOperator<K, V> add(final Map<K, V>... newElements);

    public ILevel0ListOfMapOperator<K, V> insert(final int position, final Map<K, V>... newElements);

    public ILevel0ListOfMapOperator<K, V> addAll(final Collection<Map<K, V>> collection);

    public ILevel0ListOfMapOperator<K, V> removeIndexes(final int... indices);

    public ILevel0ListOfMapOperator<K, V> removeEquals(final Map<K, V>... values);

    public ILevel0ListOfMapOperator<K, V> removeMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel0ListOfMapOperator<K, V> removeNotMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel0ListOfMapOperator<K, V> removeNullOrNotMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel0ListOfMapOperator<K, V> removeNotNullNotMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel0ListOfMapOperator<K, V> removeNotNullMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel0ListOfMapOperator<K, V> removeNullOrMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel0ListOfMapOperator<K, V> removeIndexesNot(final int... indices);

    public ILevel0ListOfMapOperator<K, V> removeNulls();

    public ILevel1ListOfMapElementsOperator<K, V> forEachIndex(final int... indices);

    public ILevel1ListOfMapElementsOperator<K, V> forEachMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel1ListOfMapElementsOperator<K, V> forEachNotMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel1ListOfMapElementsOperator<K, V> forEachNullOrNotMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel1ListOfMapElementsOperator<K, V> forEachNotNullNotMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel1ListOfMapElementsOperator<K, V> forEachNullOrMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel1ListOfMapElementsOperator<K, V> forEachNotNullMatching(final IEvaluator<Boolean, ? super Map<K, V>> eval);

    public ILevel1ListOfMapElementsOperator<K, V> forEachNull();

    public ILevel1ListOfMapElementsOperator<K, V> forEachIndexNot(final int... indices);

    public ILevel1ListOfMapElementsOperator<K, V> forEachNotNull();

    public ILevel0GenericUniqOperator<List<Map<K, V>>> generic();

    public <X> ILevel0GenericUniqOperator<X> convert(final IConverter<X, ? super List<Map<K, V>>> converter);

    public <X> ILevel0GenericUniqOperator<X> eval(final IEvaluator<X, ? super List<Map<K, V>>> eval);

    public <X> ILevel0GenericUniqOperator<X> exec(final IFunction<X, ? super List<Map<K, V>>> function);

    public <X, Y> ILevel0ListOfMapOperator<X, Y> asListOfMapOf(final Type<X> keyType, final Type<Y> valueType);

    public ILevel0ListOfMapOperator<?, ?> asListOfMapOfUnknown();

    public ILevel0ArrayOfMapOperator<K, V> toArrayOfMap();

    public <K1> ILevel0MapOfMapOperator<K1, K, V> toMapOfMap(final IEvaluator<K1, ? super Map<K, V>> keyEval);

    public <K1, K2, V2> ILevel0MapOfMapOperator<K1, K2, V2> toMapOfMap(final IMapBuilder<K1, Map<K2, V2>, ? super Map<K, V>> mapBuild);

    public ILevel0SetOfMapOperator<K, V> toSetOfMap();
}
