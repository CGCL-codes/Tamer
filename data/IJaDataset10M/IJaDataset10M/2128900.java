package org.jlambda.util;

import static org.jlambda.tuples.Tuple2.tuple;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import org.jlambda.Context.PlaceHolder;
import org.jlambda.functions.Fun0;
import org.jlambda.functions.Fun1;
import org.jlambda.functions.Fun2;
import org.jlambda.functions.Fun3;
import org.jlambda.list.List;
import org.jlambda.list.MemoizingList;
import org.jlambda.listFunctions.ListFun1;
import org.jlambda.listFunctions.ListFun2;
import org.jlambda.listFunctions.ListFun3;
import org.jlambda.tuples.Tuple2;

public final class Lists {

    /**
	 * Force a list to evaluate.  By default all lists are lazy but seq forces the evaluation.
	 * NOTE: Do not use seq(List) on infinite lists, they will not return, use seq(List, int).
	 * 
	 * @param list
	 * @return 
	 */
    public static class Seq<T> extends Fun1<Iterable<T>, Iterable<T>> {

        @Override
        public Iterable<T> apply(Iterable<T> p1) {
            drain(p1.iterator());
            return p1;
        }
    }

    private static final <T> void drain(Iterator<T> itr) {
        while (itr.hasNext()) {
            itr.next();
        }
    }

    /**
	 * When created on a MemoizingList it will iterate only the calculated items in the list
	 * 
	 * @author Chris
	 *
	 * @param <T>
	 */
    public static class CalculatedIterableProxy<T> implements Iterable<T> {

        MemoizingList<T> list = null;

        public CalculatedIterableProxy(MemoizingList<T> list) {
            this.list = list;
        }

        public Iterator iterator() {
            return list.calculatedIterator();
        }
    }

    /**
	 * Force a list to evaluate.  By default all lists are lazy but seq forces the evaluation.
	 * This version of seq is suitable for infinite lists, as the caller must supply an
	 * upper bound.
	 * 
	 * @param list
	 * @param max
	 * @return
	 */
    public static class SeqN<T> extends Fun2<Iterable<T>, Integer, Iterable<T>> {

        @Override
        public Iterable<T> apply(Iterable<T> list, Integer count) {
            Iterator itr = list.iterator();
            for (int i = 0; i < count && itr.hasNext(); i++) {
                itr.next();
            }
            return list;
        }
    }

    /**
	 * Force a list to evaluate.  By default all lists are lazy but seq forces the evaluation.
	 * NOTE: Do not use seq(List) on infinite lists, they will not return, use seq(List, int).
	 * 
	 * @param list
	 * @return 
	 */
    public static final <T> Iterable<T> seq(Iterable<T> list) {
        return (Iterable<T>) new Seq().apply(list);
    }

    /**
	 * Force a list to evaluate.  By default all lists are lazy but seq forces the evaluation.
	 * This version of seq is suitable for infinite lists, as the caller must supply an
	 * upper bound.
	 * 
	 * @param list
	 * @param max
	 * @return 
	 */
    public static final Iterable seqN(Iterable list, int max) {
        return (Iterable) new SeqN().apply(list, max);
    }

    /**
	 * Expects a list as its execution parameter, returns the list minus the first item
	 * 
	 * tail list n
	 * 
	 */
    public static final <T> Iterable<T> tail(Iterable<T> iterable) {
        return new Tail<T>().apply(iterable, 1);
    }

    public static final <T> Iterable<T> tail(Iterable<T> iterable, int n) {
        return new Tail<T>().apply(iterable, n);
    }

    public static class Tail<T> extends ListFun2<Iterable<T>, Integer, T> {

        Iterator<T> itr = null;

        public Tail() {
            setMemoized(false);
        }

        public T getNext() {
            if (itr.hasNext()) {
                return itr.next();
            } else {
                EOL = true;
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Iterable<T> p1, Integer n) {
            itr = p1.iterator();
            for (int i = 0; i < n; i++) {
                if (itr.hasNext()) {
                    itr.next();
                }
            }
        }
    }

    /**
	 * Always memoizes results
	 * 
	 * @author CTw
	 *
	 */
    public static class Memoize<T> extends ListFun1<Iterable<T>, T> {

        Iterator<T> itr = null;

        public Memoize() {
            setMemoized(true);
        }

        public T getNext() {
            if (itr.hasNext()) {
                return itr.next();
            } else {
                EOL = true;
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Iterable<T> p1) {
            itr = p1.iterator();
        }
    }

    public static final <T> Iterable<T> memoize(Iterable<T> itr) {
        return new Memoize<T>().apply(itr);
    }

    public static class Last<T> extends Fun1<MemoizingList<T>, T> {

        @Override
        public T apply(MemoizingList<T> list) {
            int csize = 0;
            csize = list.getCurrentSize();
            if (csize > 0) {
                return list.get(csize - 1);
            }
            throw new NoSuchElementException("Calculated List size is " + csize);
        }
    }

    public static final <T> T last(MemoizingList<T> list) {
        return new Last<T>().apply(list);
    }

    private static int getLast(int size, int takelast) {
        if (size == 0) {
            return 0;
        }
        if (takelast > size) {
            takelast = size - 1;
        }
        return takelast;
    }

    public static class LastN<T> extends Fun2<MemoizingList<T>, Integer, Collection<T>> {

        @Override
        public Collection<T> apply(MemoizingList<T> list, Integer takelast) {
            int csize = 0;
            csize = list.getCurrentSize();
            takelast = getLast(csize, takelast);
            if (takelast == 0) throw new NoSuchElementException("Calculated List size is " + csize);
            return list.take(csize - takelast, csize);
        }
    }

    public static final <T> Collection<T> last(MemoizingList<T> list, int takelast) {
        return new LastN<T>().apply(list, takelast);
    }

    public static class LastC<T> extends Fun1<Collection<T>, T> {

        @Override
        public T apply(Collection<T> list) {
            int csize = 0;
            csize = list.size();
            if (csize > 0) {
                ArrayList<T> col = new ArrayList<T>(list);
                return col.get(csize - 1);
            }
            throw new NoSuchElementException("Collection size is " + csize);
        }
    }

    public static final <T> T last(Collection<T> list) {
        return new LastC<T>().apply(list);
    }

    public static class LastCN<T> extends Fun2<Collection<T>, Integer, Collection<T>> {

        @Override
        public Collection<T> apply(Collection<T> list, Integer takelast) {
            int csize = 0;
            csize = list.size();
            if (csize > 0) {
                ArrayList<T> col = new ArrayList<T>(list);
                return col.subList(col.size() - takelast, col.size());
            }
            throw new NoSuchElementException("Collection size is " + csize);
        }
    }

    public static final <T> Collection<T> last(Collection<T> list, int takelast) {
        return new LastCN<T>().apply(list, takelast);
    }

    /**
	 * Expects a list of lists as its parameter.  Iterates over each list,
	 * moving onto the next one. (does not memoize).
	 * 
	 * flatten list<list<T>> 
	 * 
	 */
    public static class Flatten<T> extends ListFun1<Iterable<Iterable<T>>, T> {

        Iterator<T> itr = null;

        Iterator<Iterable<T>> itritr = null;

        public Flatten() {
            setMemoized(false);
        }

        public T getNext() {
            if (itr.hasNext()) {
                return itr.next();
            } else {
                if (itritr.hasNext()) {
                    itr = itritr.next().iterator();
                    if (itr.hasNext()) {
                        return itr.next();
                    }
                }
                EOL();
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Iterable<Iterable<T>> p1) {
            itritr = p1.iterator();
            if (itritr.hasNext()) {
                itr = itritr.next().iterator();
            }
        }
    }

    public static <T> Iterable<T> flatten(Iterable<Iterable<T>> list) {
        return new Flatten<T>().apply(list);
    }

    /**
	 * joins two lists.  An iterator will iterate through the first list and then the second.  An infinite number of joins can be made.
	 * 
	 * join list<T> list<T> 
	 * 
	 */
    public static class Join<T> extends ListFun2<Iterable<T>, Iterable<T>, T> {

        Iterator<T> itr = null;

        Iterable<T> p2 = null;

        boolean p2d = false;

        public Join() {
            setMemoized(false);
        }

        public T getNext() {
            if (itr.hasNext()) {
                return itr.next();
            } else {
                if (!p2d) {
                    itr = p2.iterator();
                    p2d = true;
                    if (itr.hasNext()) {
                        return itr.next();
                    }
                }
                EOL();
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Iterable<T> p1, Iterable<T> p2) {
            itr = p1.iterator();
            this.p2 = p2;
        }
    }

    public static <T> Iterable<T> join(Iterable<T> list1, Iterable<T> list2) {
        return new Join<T>().apply(list1, list2);
    }

    /**
	 * Expects a list as its execution parameter, returns a list
	 * for as many items as taken.
	 * 
	 * take list n (pos)
	 * 
	 */
    public static class Take<T> extends ListFun3<Iterable<T>, Integer, Integer, T> {

        Iterator<T> itr = null;

        int count = 0;

        int pos = -1;

        public Take() {
            super();
            setMemoized(false);
        }

        int i = 0;

        public T getNext() {
            if (itr.hasNext() && i < count) {
                i++;
                return itr.next();
            } else {
                EOL();
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Iterable<T> p1, Integer p2, Integer p3) {
            itr = p1.iterator();
            this.count = p2;
            this.pos = p3;
            for (int i = 0; i < pos && itr.hasNext(); i++) {
                itr.next();
            }
        }
    }

    public static final <T> Iterable<T> take(Iterable<T> itr, int count) {
        return new Take<T>().apply(itr, count, -1);
    }

    public static final <T> Iterable<T> take(Iterable<T> itr, int count, int pos) {
        return new Take<T>().apply(itr, count, pos);
    }

    public static final <T> T first(Iterable<T> itr) {
        return new Take<T>().apply(itr, 1, -1).iterator().next();
    }

    public static final <T> Fun1<Iterable<T>, T> firstF() {
        return new Fun1<Iterable<T>, T>() {

            @Override
            public T apply(Iterable<T> p1) {
                return first(p1);
            }
        };
    }

    /**
	 * Calls the function on each element of the list, creating a new list.
	 * Each item in the new list is in the same order as the old list items
	 * with f applied.
	 * 
	 * map deferred_function ITERABLE
	 * 
	 */
    public static class Map<A, T> extends ListFun2<Fun1<A, T>, Iterable<A>, T> {

        Iterator<A> itr = null;

        Fun1<A, T> f = null;

        public T getNext() {
            if (itr.hasNext()) {
                A o = itr.next();
                return f.apply(o);
            } else {
                EOL();
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Fun1<A, T> p1, Iterable<A> p2) {
            f = p1;
            itr = p2.iterator();
        }
    }

    /**
	 * Calls the function on each element of the list, creating a new list and calling remove on the first.
	 * This will always modify state, use with care.  It is also adviced not to use this function in a
	 * multithreaded way.
	 * 
	 * mapR deferred_function ITERABLE
	 * 
	 */
    public static class MapR<A, T> extends ListFun2<Fun1<A, T>, Iterable<A>, T> {

        Iterator<A> itr = null;

        Fun1<A, T> f = null;

        public T getNext() {
            if (itr.hasNext()) {
                A o = itr.next();
                itr.remove();
                return f.apply(o);
            } else {
                EOL();
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Fun1<A, T> p1, Iterable<A> p2) {
            f = p1;
            itr = p2.iterator();
        }
    }

    public static final <A, T> Iterable<T> mapr(Fun1<A, T> f, Iterable<A> itr) {
        return new MapR<A, T>().apply(f, itr);
    }

    public static final <A, T> Iterable<T> maprNonMem(Fun1<A, T> f, Iterable<A> itr) {
        MapR<A, T> map = new MapR<A, T>();
        map.setMemoized(false);
        return map.apply(f, itr);
    }

    public static final <A, T> Iterable<T> map(Fun1<A, T> f, Iterable<A> itr) {
        return new Map<A, T>().apply(f, itr);
    }

    public static final <A, T> Iterable<T> mapNonMem(Fun1<A, T> f, Iterable<A> itr) {
        Map<A, T> map = new Map<A, T>();
        map.setMemoized(false);
        return map.apply(f, itr);
    }

    /**
	 * Calls the function on each key->value pair of the list, creating a new list
	 * 
	 * mapm deferred_function map
	 * 
	 */
    public static class MapM<K, V, T> extends ListFun2<Fun2<K, V, T>, java.util.Map<K, V>, T> {

        Iterator<java.util.Map.Entry<K, V>> itr = null;

        Fun2<K, V, T> f = null;

        public T getNext() {
            if (itr.hasNext()) {
                java.util.Map.Entry<K, V> o = itr.next();
                return f.apply(o.getKey(), o.getValue());
            } else {
                EOL();
                return null;
            }
        }

        @Override
        protected void create(List<T> list, Fun2<K, V, T> p1, java.util.Map<K, V> p2) {
            itr = p2.entrySet().iterator();
            f = p1;
        }
    }

    public static final <K, V, T> Iterable<T> mapM(Fun2<K, V, T> f, java.util.Map<K, V> itr) {
        return new MapM<K, V, T>().apply(f, itr);
    }

    public static final <K, V, T> Iterable<T> mapMNonMem(Fun2<K, V, T> f, java.util.Map<K, V> itr) {
        MapM<K, V, T> mapper = new MapM<K, V, T>();
        mapper.setMemoized(false);
        return mapper.apply(f, itr);
    }

    /**
	 * Joins the two lists to create a new list with this function
	 * 
	 * zipWith deferred_function list list
	 * 
	 */
    public static final <A, B, R> Iterable<R> zipWith(Fun2<A, B, R> f, Iterable<A> a, Iterable<B> b) {
        return new ZipWith<A, B, R>().apply(f, a, b);
    }

    public static class ZipWith<A, B, R> extends ListFun3<Fun2<A, B, R>, Iterable<A>, Iterable<B>, R> {

        Fun2<A, B, R> f = null;

        Iterator<A> l1 = null;

        Iterator<B> l2 = null;

        public R getNext() {
            if (l1.hasNext() && l2.hasNext()) {
                A ol1 = l1.next();
                B ol2 = l2.next();
                return f.apply(ol1, ol2);
            }
            EOL();
            return null;
        }

        @Override
        protected void create(List<R> list, Fun2<A, B, R> p1, Iterable<A> p2, Iterable<B> p3) {
            f = p1;
            l1 = p2.iterator();
            l2 = p3.iterator();
        }
    }

    ;

    /**
	 * Joins the two lists to create a new list of Tuple pairs.  The third parameter can be used curried away
	 * but allows the resulting tuple list to be a based from this tuple declaration.
	 * 
	 * zip list list Fun2<A,B,C>
	 * 
	 */
    public static class Zip<A, B, C> extends ListFun3<Iterable<A>, Iterable<B>, Fun2<A, B, C>, C> {

        Iterator<A> l1 = null;

        Iterator<B> l2 = null;

        Fun2<A, B, C> decl = null;

        @Override
        protected void create(List<C> list, Iterable<A> p1, Iterable<B> p2, Fun2<A, B, C> decl) {
            l1 = p1.iterator();
            l2 = p2.iterator();
            this.decl = decl;
        }

        public C getNext() {
            if (l1.hasNext() && l2.hasNext()) {
                A ol1 = l1.next();
                B ol2 = l2.next();
                return decl.apply(ol1, ol2);
            } else {
                EOL();
                return null;
            }
        }
    }

    ;

    /**
	 * @see zip
	 * @param l1
	 * @param l2
	 * @return
	 */
    public static <A, B> Iterable<Tuple2<A, B>> zip(Iterable<A> l1, Iterable<B> l2) {
        return new Zip<A, B, Tuple2<A, B>>().apply(l1, l2, new Tuple2<A, B>(null, null));
    }

    public static <A, B, C> Iterable<C> zip(Iterable<A> l1, Iterable<B> l2, Fun2<A, B, C> decl) {
        return new Zip<A, B, C>().apply(l1, l2, decl);
    }

    /**
	 * list must be a list of tuples of equal size, returns a tuple with seperate arrays for each element of the tuple list 
	 * 
	 * unzip [('a','b'),('c','d')] = ( ['a', 'c'], ['b','d'] )
	 * 
	 * unzip list 
	 * 
	 */
    public static class Unzip<A, B> extends Fun1<Iterable<Tuple2<A, B>>, Tuple2<Iterable<A>, Iterable<B>>> {

        @Override
        public Tuple2<Iterable<A>, Iterable<B>> apply(Iterable<Tuple2<A, B>> p1) {
            Iterator<Tuple2<A, B>> l1 = p1.iterator();
            ArrayList<A> one = new ArrayList<A>();
            ArrayList<B> two = new ArrayList<B>();
            while (l1.hasNext()) {
                Tuple2<A, B> ol1 = l1.next();
                one.add(ol1.get1());
                two.add(ol1.get2());
            }
            return new Tuple2<Iterable<A>, Iterable<B>>(one, two);
        }
    }

    ;

    /**
	 * @see unzip
	 * @param l1
	 * @param l2
	 * @return
	 */
    public static final <A, B> Tuple2<Iterable<A>, Iterable<B>> unzip(Iterable<Tuple2<A, B>> tuplelist) {
        return new Unzip<A, B>().apply(tuplelist);
    }

    /**
	 * Returns true if both lists are identical.
	 * 
	 * equals ITERABLE ITERABLE
	 * 
	 */
    public static class Equals<A, T> extends Fun3<Iterable<A>, Iterable<T>, Fun2<A, T, Boolean>, Boolean> {

        @Override
        public Boolean apply(Iterable<A> p1, Iterable<T> p2, Fun2<A, T, Boolean> f) {
            Iterator<A> l1 = p1.iterator();
            Iterator<T> l2 = p2.iterator();
            while (l1.hasNext() && l2.hasNext()) {
                A ol1 = l1.next();
                T ol2 = l2.next();
                if (ol1 != null) {
                    if (f != null) {
                        if (!f.apply(ol1, ol2)) {
                            return false;
                        }
                    } else if (!ol1.equals(ol2)) {
                        return false;
                    }
                }
            }
            if (l1.hasNext() != l2.hasNext()) return false;
            return true;
        }
    }

    ;

    public static final <A, T> Boolean equals(Iterable<A> p1, Iterable<T> p2) {
        return new Equals<A, T>().apply(p1, p2, null);
    }

    public static final <A, T> Boolean equals(Iterable<A> p1, Iterable<T> p2, Fun2<A, T, Boolean> f) {
        return new Equals<A, T>().apply(p1, p2, f);
    }

    /**
	 * Returns a list from the function parameters
	 * 
	 * add a b -> a + b
	 * 
	 * inc a -> add a 1  ( inc = curry(add, _, 1)
	 * 
	 * list ...
	 * 
	 */
    public static class ListGen<T> extends Fun1<T[], ArrayList<T>> {

        @Override
        public ArrayList<T> apply(T[] p1) {
            ArrayList<T> result = new ArrayList<T>();
            Collections.addAll(result, p1);
            return result;
        }
    }

    ;

    public static final <T> ArrayList<T> list(T... args) {
        return (ArrayList<T>) new ListGen<T>().apply(args);
    }

    public static final <T> ArrayList<T> list(Iterable<T> arg) {
        ArrayList<T> list = new ArrayList<T>();
        for (T t : arg) {
            list.add(t);
        }
        return list;
    }

    public static final <T> ArrayList<T> listA(T[] arg) {
        ArrayList<T> list = new ArrayList<T>();
        for (T t : arg) {
            list.add(t);
        }
        return list;
    }

    /**
	 * Forms an infinite list of values calculated by applying the second parameter to 
	 * the function as a seed.  Then further list elements are derived by applying the result
	 * to the function.
	 * 
	 * iterate.create( function , seed)
	 * 
	 */
    public static class Iterate<T> extends ListFun2<Fun1<T, T>, T, T> {

        Fun1<T, T> f = null;

        T prev = null;

        public T getNext() {
            prev = f.apply(prev);
            return prev;
        }

        @Override
        protected void create(List<T> list, Fun1<T, T> p1, T p2) {
            f = p1;
            prev = p2;
            if (list.isMemoized()) {
                ((MemoizingList) list).append(prev);
            }
        }
    }

    ;

    public static final <T> Iterable<T> iterate(Fun1<T, T> func, T start) {
        return new Iterate<T>().apply(func, start);
    }

    /**
	 * This version calls func on start, so the first value is not x but [f(x),f(f(x)) etc] 
	 * 
	 */
    public static final <T> Iterable<T> iterate1(Fun1<T, T> func, T start) {
        return new Iterate<T>().apply(func, func.apply(start));
    }

    /**
	 * Forms a list by taking from the supplied list items that match the predicate.
	 * 
	 * Note if the function returns null, this signifies the end of the list
	 * 
	 * filter.create( predicate , ITERABLE)
	 * 
	 */
    public static class Filter<T> extends ListFun2<Fun1<T, Boolean>, Iterable<T>, T> {

        Iterator<T> itr = null;

        Fun1<T, Boolean> f = null;

        public Filter() {
            setMemoized(false);
        }

        public T getNext() {
            while (itr.hasNext()) {
                T res = itr.next();
                Boolean pred = (Boolean) f.apply(res);
                if (pred != null && pred.booleanValue()) {
                    return res;
                } else if (pred == null) {
                    EOL = true;
                    return null;
                }
            }
            EOL = true;
            return null;
        }

        @Override
        protected void create(List<T> list, Fun1<T, Boolean> p1, Iterable<T> p2) {
            f = p1;
            itr = p2.iterator();
        }
    }

    public static final <T> Iterable<T> filter(Fun1<T, Boolean> f, Iterable<T> iterable) {
        return new Filter<T>().apply(f, iterable);
    }

    /**
	 * An infinite list of items each with the object X.  If X is a Function (must be deferred, and no arguments) then it will
	 * be executed for each new item in the list.
	 * 
	 * NOTE use curry when functions require arguments.
	 * 
	 * repeat.create( X)
	 * 
	 */
    public static class Repeat<T> extends ListFun2<T, Fun0<T>, T> {

        T o = null;

        Fun0<T> f = null;

        public Repeat() {
            setMemoized(false);
        }

        public T getNext() {
            if (f != null) {
                return f.apply();
            }
            return o;
        }

        @Override
        protected void create(List<T> list, T p1, Fun0<T> p2) {
            o = p1;
            f = p2;
        }
    }

    public static final <T> Iterable<T> repeat(T value) {
        return new Repeat<T>().apply(value, null);
    }

    public static final <T> Iterable<T> repeat(T value, Fun0<T> f) {
        return new Repeat<T>().apply(value, f);
    }

    /**
	 * An infinite list formed by cycling over the list (must be finite)
	 * 
	 * The optional second parameter produces a finite number of iterations.
	 * 
	 * cycle Iterable [n]
	 * 
	 */
    public static class Cycle<T> extends ListFun2<Iterable<T>, Integer, T> {

        LinkedList<T> base = new LinkedList<T>();

        Iterator<T> itr = null;

        int max = -1;

        int i = 0;

        @Override
        protected void create(List<T> list, Iterable<T> p1, Integer p2) {
            Iterator<T> sitr = p1.iterator();
            while (sitr.hasNext()) {
                base.add(sitr.next());
            }
            if (p2 != null) {
                max = p2;
            }
            itr = base.iterator();
        }

        public T getNext() {
            if (itr.hasNext()) {
                return itr.next();
            } else {
                i++;
                if (max > 0) {
                    if (i >= max) {
                        EOL();
                        return null;
                    }
                }
                itr = base.iterator();
                if (itr.hasNext()) {
                    return itr.next();
                } else {
                    EOL();
                    return null;
                }
            }
        }
    }

    ;

    public static final <T> Iterable<T> cycle(Iterable<T> i) {
        return new Cycle<T>().apply(i, null);
    }

    public static final <T> Iterable<T> cycle(Iterable<T> i, int count) {
        return new Cycle<T>().apply(i, count);
    }

    /**
	 * When the first parameter is true the second parameter is returned.  
	 * 
	 * If the first parameter is false or null then an null is returned.
	 * 
	 * nullIfFalse true true -> true 
	 * nullIfFalse false true -> null
	 * nullIfFalse false false -> null
	 * nullIfFalse true false -> false
	 * nullIfFalse null -> null
	 * 
	 * nullIfFalse Boolean Boolean
	 */
    public static Fun2<Boolean, Boolean, Boolean> nullIfFalse = new Fun2<Boolean, Boolean, Boolean>() {

        public Boolean apply(Boolean l, Boolean r) {
            if (l != null) {
                if (l.equals(true)) return r;
            }
            return null;
        }
    };

    /**
	 * foldl takes a starting Object and passes it to the deferred function (two params) with the first 
	 * item in the list as the second argument.  The result of this is taken with the next item in the list and passed to the function.
	 * This repeats until all items are processed and that result is returned. 
	 * 
	 *  e.g. foldl (add, list(1,2,3,4,5), 0) == 15
	 * 
	 * foldl Deferred ITERABLE Object
	 */
    public static class Foldl<A, R> extends Fun3<Fun2<A, R, R>, Iterable<A>, R, R> {

        @Override
        public R apply(Fun2<A, R, R> f, Iterable<A> param, R param3) {
            R r = param3;
            Iterator<A> itr = param.iterator();
            while (itr.hasNext()) {
                A element = (A) itr.next();
                r = f.apply(element, r);
            }
            return r;
        }
    }

    public static final <A, R> R foldl(Fun2<A, R, R> f, Iterable<A> param, R param3) {
        return new Foldl<A, R>().apply(f, param, param3);
    }

    /**
	 * A finite list of items each with the object X.  If X is a Function (must be deferred, and no arguments) then it will
	 * be executed for each new item in the list.  The maximum number of items is specified by N
	 * 
	 * NOTE use curry when functions require arguments.
	 * 
	 * replicate N X
	 * 
	 */
    public static class Replicate<T> extends ListFun3<Integer, T, Fun0<T>, T> {

        T value = null;

        int max = 0;

        int pos = 0;

        Fun0<T> f = null;

        public T getNext() {
            pos++;
            if (pos > max) {
                EOL = true;
                return null;
            }
            if (f != null) {
                return f.apply();
            }
            return value;
        }

        @Override
        protected void create(List<T> list, Integer p1, T p2, Fun0<T> p3) {
            max = p1;
            value = p2;
            f = p3;
        }
    }

    public static final <T> Iterable<T> replicate(int count, T val) {
        return new Replicate<T>().apply(count, val, null);
    }

    public static final <T> Iterable<T> replicate(int count, T val, Fun0<T> f) {
        return new Replicate<T>().apply(count, val, f);
    }

    /**
	 * Forms a list by taking from the supplied list until the function condition is no longer met
	 * 
	 * takeWhile function Iterable
	 * 
	 */
    public static class TakeWhile<T> extends ListFun2<Fun1<T, Boolean>, Iterable<T>, T> {

        Fun1<T, Boolean> f1 = null;

        Iterator<T> itr = null;

        public T getNext() {
            if (!itr.hasNext()) {
                EOL = true;
                return null;
            }
            T next = itr.next();
            if (f1.apply(next)) return next;
            EOL = true;
            return null;
        }

        @Override
        protected void create(List<T> list, Fun1<T, Boolean> p1, Iterable<T> p2) {
            f1 = p1;
            itr = p2.iterator();
        }
    }

    ;

    public static final <T> Iterable<T> takeWhile(Fun1<T, Boolean> pred, Iterable<T> theList) {
        return new TakeWhile<T>().apply(pred, theList);
    }

    /**
	 * Iterates over the list of functions tuples until one returns true.
	 * 
	 * Each function_tuple should match (function p, function result), when p o equals true, result o is returned.
	 * 
	 * NOTE to just return a value, use the Logic.value function
	 * 
	 * callUntil ITERABLE<function tuple> l, Object o
	 * 
	 * if no arguments match it returns null
	 * 
	 */
    public static class CallUntil<A, B> extends Fun2<Iterable<Tuple2<Fun1<A, Boolean>, Fun1<A, B>>>, A, B> {

        ArrayList<Fun1<A, Boolean>> preds = null;

        ArrayList<Fun1<A, B>> funcs = null;

        int size = 0;

        @Override
        public Fun1<A, B> curry(Iterable<Tuple2<Fun1<A, Boolean>, Fun1<A, B>>> p1, PlaceHolder ph2) {
            Iterator<Tuple2<Fun1<A, Boolean>, Fun1<A, B>>> itr = p1.iterator();
            preds = new ArrayList<Fun1<A, Boolean>>(20);
            funcs = new ArrayList<Fun1<A, B>>(20);
            while (itr.hasNext()) {
                Tuple2<Fun1<A, Boolean>, Fun1<A, B>> tuple = itr.next();
                preds.add(tuple.get1());
                funcs.add(tuple.get2());
            }
            size = preds.size();
            return super.curry(p1, ph2);
        }

        @Override
        public B apply(Iterable<Tuple2<Fun1<A, Boolean>, Fun1<A, B>>> p1, A p2) {
            if (preds == null) {
                Iterator<Tuple2<Fun1<A, Boolean>, Fun1<A, B>>> itr = p1.iterator();
                while (itr.hasNext()) {
                    Tuple2<Fun1<A, Boolean>, Fun1<A, B>> tuple = itr.next();
                    Fun1<A, Boolean> p = tuple.get1();
                    Fun1<A, B> r = tuple.get2();
                    Boolean res = (Boolean) p.apply(p2);
                    if (res != null && res.booleanValue()) {
                        return r.apply(p2);
                    }
                }
            } else {
                for (int i = 0; i < size; i++) {
                    Fun1<A, Boolean> pred = preds.get(i);
                    Boolean res = (Boolean) pred.apply(p2);
                    if (res != null && res.booleanValue()) {
                        return funcs.get(i).apply(p2);
                    }
                }
            }
            return null;
        }
    }

    ;

    public static final <A, B> B callUntil(Iterable<Tuple2<Fun1<A, Boolean>, Fun1<A, B>>> p1, A p2) {
        return new CallUntil<A, B>().apply(p1, p2);
    }

    /**
	 * Helper class for from.
	 * @author TGDTWCH1
	 *
	 */
    public static class To {

        final double base;

        public To(int base) {
            this.base = base;
        }

        public Iterable<Number> to(int upper) {
            return take(iterate(Math.inc, base), upper);
        }

        public Iterable<Number> toNonMemoized(int upper) {
            Iterate<Number> itr = new Iterate<Number>();
            itr.setMemoized(false);
            return take(itr.apply(Math.inc, base), upper);
        }

        public Iterable<Number> downTo(int lower) {
            return take(iterate(Math.dec, base), (int) (base - lower));
        }
    }

    /**
	 * Helper function for simple 1..100 syntax:
	 * 
	 * from(1).to(100)
	 * 
	 * @param i
	 * @return
	 */
    public static final To from(int i) {
        return new To(i);
    }

    public static class ArrayIterable<T> implements Iterable<T> {

        final T[] array;

        public ArrayIterable(T[] array) {
            this.array = array;
        }

        private class itr implements Iterator<T> {

            int pos = 0;

            int size = array.length;

            public boolean hasNext() {
                if (pos < size) {
                    return true;
                }
                return false;
            }

            public T next() {
                return array[pos++];
            }

            public void remove() {
            }
        }

        public Iterator<T> iterator() {
            return new itr();
        }
    }

    /** 
	 * turns an array into an interable 
	 * 
	 * @param <E>
	 * @param array
	 * @return
	 */
    public static final <E> Iterable<E> itr(E[] array) {
        return new ArrayIterable<E>(array);
    }

    public static class ReverseIterator<T> implements Iterator<T> {

        final ListIterator<T> itr;

        public ReverseIterator(java.util.List<T> list) {
            itr = list.listIterator(list.size());
        }

        public boolean hasNext() {
            if (itr.hasPrevious()) {
                return true;
            }
            return false;
        }

        public T next() {
            return itr.previous();
        }

        public void remove() {
        }
    }

    /**
	 * Reverses an Iterable.  If the iterator is a java.util.List, then the resulting list
	 * will be lazy.  
	 * 
	 * NOTE the list is a lamda.list.List then only the calulated reverse list 
	 * will be used, so a non-infinite list must be seq'd to reverse the whole of it.
	 * 
	 * If the Iterable is only a Collection then the full collection will be traversed.
	 * 
	 * reverse Iterable
	 * 
	 */
    public static class Reverse<T> extends ListFun1<Iterable<T>, T> {

        Iterator<T> itr = null;

        @Override
        protected void create(List<T> list, Iterable<T> itra) {
            if (itra instanceof java.util.List) {
                itr = new ReverseIterator((java.util.List) itra);
            } else if (itra instanceof MemoizingList) {
                itr = new ReverseIterator<T>(((MemoizingList) itra).getList());
            } else {
                LinkedList base = new LinkedList();
                Iterator sitr = itra.iterator();
                while (sitr.hasNext()) {
                    Object element = (Object) sitr.next();
                    base.add(element);
                }
                itr = new ReverseIterator(base);
            }
        }

        public T getNext() {
            if (itr.hasNext()) {
                return itr.next();
            } else {
                EOL();
                return null;
            }
        }
    }

    ;

    public static final <E> Iterable<E> reverse(Iterable<E> itr) {
        return new Reverse<E>().apply(itr);
    }

    /**
	 * Constructs a set from variable args
	 * 
	 * @param objects
	 * @return
	 */
    public static final <T> HashSet<T> set(T... objects) {
        HashSet<T> set = new HashSet<T>();
        for (T object : objects) {
            set.add(object);
        }
        return set;
    }

    /**
	 * Constructs an insertion ordered set from variable args
	 * 
	 * @param objects
	 * @return
	 */
    public static final <T> LinkedHashSet<T> lset(T... objects) {
        LinkedHashSet<T> set = new LinkedHashSet();
        for (T object : objects) {
            set.add(object);
        }
        return set;
    }

    /**
	 * Constructs an ordered set from variable args
	 * 
	 * @param objects
	 * @return
	 */
    public static final <T> TreeSet<T> oset(T... objects) {
        TreeSet<T> set = new TreeSet();
        for (T object : objects) {
            set.add(object);
        }
        return set;
    }

    /**
	 * Constructs an ordered set from Iterable args
	 * 
	 * @param objects
	 * @return
	 */
    public static final <T> TreeSet<T> oset(Iterable<T> objects) {
        TreeSet<T> set = new TreeSet();
        for (T object : objects) {
            set.add(object);
        }
        return set;
    }

    /**
	 * A list that creates tuples of position and T 
	 */
    public static final class Pos<T> extends ListFun1<Iterable<T>, Tuple2<Number, T>> {

        Iterator<T> itr;

        @Override
        protected void create(List<Tuple2<Number, T>> list, Iterable<T> p1) {
            itr = p1.iterator();
        }

        long pos = 0;

        public Tuple2<Number, T> getNext() {
            if (!itr.hasNext()) {
                EOL();
                return null;
            }
            return tuple((Number) pos++, itr.next());
        }
    }

    ;

    public static final <T> Iterable<Tuple2<Number, T>> pos(Iterable<T> itr) {
        return new Pos<T>().apply(itr);
    }

    /**
	 * Returns the length of the iterable.  If the iterable is a memoizing list and
	 * has fully calculated then it returns currentSize.
	 * 
	 */
    public static final class Length<T, B extends Iterable<T>> extends Fun1<B, Number> {

        @Override
        public Number apply(B iterable) {
            int size = 0;
            if (iterable instanceof List) {
                List list = (List) iterable;
                if (list.getDecl().isEOL() && list.isMemoized()) {
                    return ((MemoizingList) list).getCurrentSize();
                }
            }
            Iterator itr = iterable.iterator();
            while (itr.hasNext()) {
                itr.next();
                size++;
            }
            return size;
        }
    }

    ;

    public static final <T> Length<T, Set<T>> lenSet() {
        return new Length<T, Set<T>>();
    }

    public static final <T, B extends Iterable<T>> Number length(B len) {
        return new Length<T, B>().apply(len);
    }

    public static final class FillListArray<A, B> extends Fun3<Fun1<A, B>, Iterable<A>, B[], B[]> {

        @Override
        public B[] apply(Fun1<A, B> f, Iterable<A> p1, B[] p2) {
            Iterator<A> itr = p1.iterator();
            int i = 0;
            while (itr.hasNext()) {
                A element = (A) itr.next();
                p2[i++] = f.apply(element);
            }
            return p2;
        }
    }

    public static final class FillArray<A> extends Fun2<Iterable<A>, Class<A>, A[]> {

        @Override
        public A[] apply(Iterable<A> p1, Class<A> clazz) {
            int size = length(p1).intValue();
            Iterator<A> itr = p1.iterator();
            A[] objs = (A[]) Array.newInstance(clazz, size);
            int i = 0;
            while (itr.hasNext()) {
                A element = (A) itr.next();
                objs[i++] = element;
            }
            return objs;
        }
    }
}
