package org.identifylife.descriptlet.store.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dbarnier
 *
 */
public class SetUtils {

    public static <T> Set<T> fromCollection(Collection<T> coll) {
        return new HashSet<T>(coll);
    }

    /**
   * Set of all objects that are members of setA, or setB, or both.
   * 
   * Logical OR
   * 
   * @param <T>
   * @param setA
   * @param setB
   * @return
   */
    public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
        Set<T> result = new HashSet<T>(setA);
        result.addAll(setB);
        return result;
    }

    /**
   * Set of all objects that are members of both setA and setB.
   * @param <T>
   * @param setA
   * @param setB
   * @return
   */
    public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
        Set<T> result = new HashSet<T>(setA);
        result.retainAll(setB);
        return result;
    }

    /**
   * Set of all objects that are members of setA but not setB.
   * @param <T>
   * @param setA
   * @param setB
   * @return
   */
    public static <T> Set<T> complement(Set<T> setA, Set<T> setB) {
        Set<T> result = new HashSet<T>(setA);
        result.removeAll(setB);
        return result;
    }

    /**
   * Set of all objects that are members of setA but not setB.
   * note: synonym of complement.
   * 
   * @param <T>
   * @param setA
   * @param setB
   * @return
   */
    public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
        return complement(setA, setB);
    }

    /**
   * Set of all objects that are a member of exactly one of setA and setB.
   * (the set difference of the union and the intersection)
   * 
   * @param <T>
   * @param setA
   * @param setB
   * @return
   */
    public static <T> Set<T> symDifference(Set<T> setA, Set<T> setB) {
        return difference(union(setA, setB), intersection(setA, setB));
    }

    /**
   * SetA is a subset of setB if setB contains all members of setA.
   * 
   * @param <T>
   * @param setA
   * @param setB
   * @return
   */
    public static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
        return setB.containsAll(setA);
    }

    /**
   * SetA is a superset of setB if setA contains all members of setB.
   * 
   * @param <T>
   * @param setA
   * @param setB
   * @return
   */
    public static <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
        return setA.containsAll(setB);
    }
}
