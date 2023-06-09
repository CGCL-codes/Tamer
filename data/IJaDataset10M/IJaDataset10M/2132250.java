package com.ibm.wala.ipa.callgraph.propagation;

import java.util.Collection;
import com.ibm.wala.analysis.pointers.HeapGraph;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.util.intset.OrdinalSet;
import com.ibm.wala.util.intset.OrdinalSetMapping;

/**
 * Abstract definition of pointer analysis
 */
public interface PointerAnalysis {

    /**
   * @param key representative of an equivalence class of pointers
   * @return Set of InstanceKey, representing the instance abstractions that define
   * the points-to set computed for the pointer key
   */
    OrdinalSet<InstanceKey> getPointsToSet(PointerKey key);

    /**
   * @return an Object that determines how to model abstract locations in the heap.
   */
    HeapModel getHeapModel();

    /**
   * @return a graph view of the pointer analysis solution
   */
    HeapGraph getHeapGraph();

    /**
   * @return the bijection between InstanceKey <=> Integer that defines the
   * interpretation of points-to-sets.
   */
    OrdinalSetMapping<InstanceKey> getInstanceKeyMapping();

    /**
   * @return all pointer keys known
   */
    Iterable<PointerKey> getPointerKeys();

    /**
   * @return all instance keys known
   */
    Collection<InstanceKey> getInstanceKeys();

    /**
   * did the pointer analysis use a type filter for a given points-to set?
   * (this is ugly).
   */
    boolean isFiltered(PointerKey pk);

    public IClassHierarchy getClassHierarchy();
}
