package de.uni_muenster.cs.sev.lethal.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAOps;

public class Combinator {

    /**
	 * Given a set of instances of type S, calculates the n-fold Cartesian
	 * product, represented as the set of all lists of cardinal number n.
	 * 
	 * @param <S> type of objects occurring in the set
	 * @param myset given set of objects
	 * @param n	 dimension of the Cartesian product
	 * @return n-fold Cartesian product of the given set (myset^n)
	 */
    public static <S> Set<List<S>> combine(Set<S> myset, int n) {
        HashSet<List<S>> ret = new HashSet<List<S>>();
        if (n == 0) {
            ret.add(new LinkedList<S>());
            return ret;
        } else if (n == 1) {
            for (S x : myset) {
                LinkedList<S> l = new LinkedList<S>();
                l.add(x);
                ret.add(l);
            }
            return ret;
        } else {
            Set<List<S>> rest = combine(myset, n - 1);
            for (S x : myset) {
                for (List<S> l : rest) {
                    l.add(0, x);
                    ret.add(new LinkedList<S>(l));
                    l.remove(0);
                }
            }
            return ret;
        }
    }

    /**
	 * Returns the Cartesian product of a given list of sets. <br>
	 * The idea is similar to the idea of combine, which is in fact 
	 * just a special case of a Cartesian product.
	 * 
	 * @param <S> type of the elements in the given sets
	 * @param sets list of sets to be set theoretically multiplied
	 * @return the Cartesian product of the given sets
	 */
    public static <S> Set<List<S>> cartesianProduct(List<Set<S>> sets) {
        HashSet<List<S>> ret = new HashSet<List<S>>();
        if (sets.size() == 0) {
            ret.add(new LinkedList<S>());
            return ret;
        } else {
            if (sets.size() == 1) {
                Set<S> A = sets.get(0);
                for (S x : A) {
                    LinkedList<S> l = new LinkedList<S>();
                    l.add(x);
                    ret.add(l);
                }
                return ret;
            } else {
                Set<S> A1 = sets.get(0);
                Set<List<S>> A2toN = cartesianProduct(sets.subList(1, sets.size()));
                for (S x : A1) {
                    for (List<S> l : A2toN) {
                        l.add(0, x);
                        ret.add(new LinkedList<S>(l));
                        l.remove(0);
                    }
                }
                return ret;
            }
        }
    }

    /**
	 * Computes the smallest set A of lists with the following properties:
	 * (i) for each l \in A: x \in A
	 * (ii) A contains all combinations of n elements of the union of the given set and {x} with property (i). <br>
	 * Idea: This set consists exactly of all lists (a_1,...,a_{j-1},x,a_{j+1},...,a_n) where
	 * the a_i are elements of the union of the given set and {x}. <br>
	 * Just used in {@link FTAOps#determinize}
	 * 
	 * @param <Q> type of elements
	 * @param set arbitrary set
	 * @param x arbitrary element 
	 * @param n STRICTLY POSITIVE number which describes the length of the lists to return
	 * @return the smallest set A of lists with the following properties: 
	 * (i) for each l \in A: x \in A, 
	 * (ii) A contains all combinations of n elements of (set \cup {x})
	 */
    public static <Q> Set<List<Q>> allListsContainingXCombine(Set<Q> set, Q x, int n) {
        Set<Q> cpySet = new HashSet<Q>(set);
        Set<List<Q>> ret = new HashSet<List<Q>>();
        cpySet.add(x);
        Set<List<Q>> foo = combine(cpySet, n - 1);
        for (List<Q> l : foo) {
            for (int pos = 0; pos < n; pos++) {
                l.add(pos, x);
                ret.add(new ArrayList<Q>(l));
                l.remove(pos);
            }
        }
        return ret;
    }
}
