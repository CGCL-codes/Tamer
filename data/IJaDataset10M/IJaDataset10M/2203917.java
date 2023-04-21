package org.deri.iris.storage_old;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage_old.IRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * New relation which creates indexes on the fly. The relation will remember the
 * relations returned by the <code>indexOn(int[])</code> method, so no
 * additional computations need to be done, if the same index is requested
 * again.
 * </p>
 * <p>
 * <code>null</code> is not permitted by this relation, nor by its subsets.
 * </p>
 * <p>
 * $Id: IndexingOnTheFlyRelation.java,v 1.6 2007-10-19 07:37:18 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.6 $
 */
public class IndexingOnTheFlyRelation extends AbstractSet<ITuple> implements IRelation {

    /** Map storing all created indexes and the corresponding relaions to them. */
    private final Map<Integer, SortedSet<ITuple>> indexes = new HashMap<Integer, SortedSet<ITuple>>();

    /** The primary index. */
    private final SortedSet<ITuple> primary;

    /** The arity of the relation. */
    private final int arity;

    /**
	 * Creates a new empty relation object with a given arity.
	 * @param a the arity of the relation
	 * @throws IllegalArgumentException if the arity is negative
	 */
    IndexingOnTheFlyRelation(final int a) {
        if (a < 0) {
            throw new IllegalArgumentException("The arity must not be negative, but was " + a);
        }
        final int[] primaryI = new int[a];
        if (a > 0) {
            primaryI[0] = 1;
        }
        primary = new TreeSet<ITuple>(new TupleComparator(primaryI));
        indexes.clear();
        indexes.put(Arrays.hashCode(TupleComparator.indexComparisonOrder(primaryI)), primary);
        arity = a;
    }

    @Override
    public Iterator<ITuple> iterator() {
        return new ModifiableIterator<ITuple>(primary);
    }

    @Override
    public int size() {
        return primary.size();
    }

    @Override
    public boolean add(final ITuple t) {
        if (t == null) {
            throw new NullPointerException("The tuple to add must not be null");
        }
        if (t.size() != arity) {
            throw new IllegalArgumentException("The arity of the tuple must be " + arity + " but was " + t.size());
        }
        boolean changed = false;
        if (primary.add(t)) {
            changed = true;
            for (final SortedSet<ITuple> s : indexes.values()) {
                s.add(t);
            }
        }
        return changed;
    }

    /**
	 * Internal remove method. Removes the tuple from all relations.
	 * @param o the object to remove
	 * @return <code>true</code> if the collection was changed by this call,
	 * otherwise <code>false</code>
	 */
    private boolean intRemove(final Object o) {
        boolean changed = false;
        if (primary.remove(o)) {
            changed = true;
            for (final SortedSet<ITuple> s : indexes.values()) {
                s.remove(o);
            }
        }
        return changed;
    }

    public Comparator<? super ITuple> comparator() {
        return primary.comparator();
    }

    public ITuple first() {
        return primary.first();
    }

    public SortedSet<ITuple> headSet(ITuple to) {
        if (to == null) {
            throw new NullPointerException("The to tuple must not be null");
        }
        return new SubRelation(primary.headSet(to), null, to);
    }

    public ITuple last() {
        return primary.last();
    }

    public SortedSet<ITuple> subSet(ITuple from, ITuple to) {
        if ((from == null) || (to == null)) {
            throw new NullPointerException("The from and to tuple must not be null");
        }
        return new SubRelation(primary.subSet(from, to), from, to);
    }

    public SortedSet<ITuple> tailSet(ITuple from) {
        if (from == null) {
            throw new NullPointerException("The from tuple must not be null");
        }
        return new SubRelation(primary.tailSet(from), from, null);
    }

    public IRelation indexOn(final int[] idx) {
        if (idx == null) {
            throw new NullPointerException("The index must not be null");
        }
        if (idx.length != arity) {
            throw new IllegalArgumentException("The indexlength " + idx.length + " must match the arity of the relation " + arity);
        }
        final int orderHash = Arrays.hashCode(TupleComparator.indexComparisonOrder(idx));
        SortedSet<ITuple> s = indexes.get(orderHash);
        if (s == null) {
            s = new TreeSet<ITuple>(new TupleComparator(idx));
            s.addAll(primary);
            indexes.put(orderHash, s);
        }
        return new SubRelation(s);
    }

    public int getArity() {
        return arity;
    }

    /**
	 * <p>
	 * Compares two tuples according to a given set of indexes.
	 * </p>
	 * <p>
	 * $Id: IndexingOnTheFlyRelation.java,v 1.6 2007-10-19 07:37:18 poettler_ric Exp $
	 * </p>
	 * 
	 * @author Richard Pöttler (richard dot poettler at deri dot at)
	 * @version $Revision: 1.6 $
	 */
    private static class TupleComparator implements Comparator<ITuple> {

        /**
		 * Array holding the position and the priority of the indexes on which
		 * to sort.
		 */
        private final int[] indexOrder;

        /**
		 * <p>
		 * Creates a new comparator which sorts on a given set of positions in
		 * the tuple.
		 * </p>
		 * <p>
		 * The indedexes are specified as follows: All indexes you don't want to
		 * sort on are 0, the index you want to sort on mainly is 1, the second
		 * index (which will be taken into account if the terms at the given
		 * possition of the previous index are equal) is 2, and so on.
		 * </p>
		 * <p>
		 * e.g.: If you want a set of tuples first sorted on the third and then
		 * sorted on the first term you give an index of <code>[2,0,1]</code>.
		 * </p>
		 * <p>
		 * The index array will be normalized according to {@link
		 * #indexComparisonOrder(int[])
		 * indexComparisonOrder(int[])} before any comparisons will
		 * be done.
		 * </p>
		 * @param indexes the indexes on which to sort the tuples
		 * @see #indexComparisonOrder(int[])
		 */
        public TupleComparator(final int[] indexes) {
            if (indexes == null) {
                throw new NullPointerException("The indexes must not be null");
            }
            indexOrder = indexComparisonOrder(indexes);
        }

        /**
		 * <p>
		 * Returns the normalized order of the indexes on which the
		 * comparator will compare the terms of the tuples. This 
		 * method was excluded to make improve the way indexes are stored
		 * and accessed. E.g.: indexes like <code>[1, 2, 3, 4]</code>,
		 * <code>[1, 2, 3, 0]</code> and <code>[1, 2, 0, 0]</code> would
		 * produce a comparator which would compare on the terms at the
		 * indexes <code>[0, 1, 2, 3]</code>.
		 * </p>
		 * <p>
		 * To see how the submitted index should be constructed, look at
		 * {@link #TupleComparator(int[])
		 * TupleComparator(int[])}.
		 * </p>
		 * @param indexes the indexes on which to sort the tuples
		 * @return the normalized index order
		 * @throws NullPointerException if the index array is
		 * <code>null</code>
		 * @see #TupleComparator(int[])
		 */
        public static int[] indexComparisonOrder(final int[] indexes) {
            if (indexes == null) {
                throw new NullPointerException("The index array must not be null");
            }
            final int[] order = new int[indexes.length];
            if (indexes.length > 0) {
                final List<Integer> idx = new ArrayList<Integer>(indexes.length);
                for (final int i : indexes) {
                    idx.add(i);
                }
                int j = 0;
                for (int i = Math.max(1, Collections.min(idx)), max = Collections.max(idx); i <= max; i++) {
                    int pos = 0;
                    if ((pos = idx.indexOf(i)) > -1) {
                        order[j++] = pos;
                    }
                }
                for (int i = 0; j < indexes.length; i++) {
                    if (indexes[i] <= 0) {
                        order[j++] = i;
                    }
                }
            }
            return order;
        }

        public int compare(ITuple o1, ITuple o2) {
            if ((o1 == null) || (o2 == null)) {
                throw new NullPointerException("The tuples must not be null");
            }
            int res = 0;
            for (final int i : indexOrder) {
                final ITerm t1 = o1.get(i);
                final ITerm t2 = o2.get(i);
                if ((t1 == null) && (t2 != null)) {
                    return -1;
                } else if ((t1 != null) && (t2 == null)) {
                    return 1;
                } else if ((t1 != null) && (t2 != null) && ((res = t1.compareTo(t2)) != 0)) {
                    return res;
                }
            }
            return 0;
        }
    }

    /**
	 * <p>
	 * Proxy class to safely iterate through the collections of the
	 * <code>IndexingOnTheFlyRelation</code>. The calls to the
	 * <code>intRemove(...)</code> method are passed to the outer
	 * <code>SortedSet</code>.
	 * </p>
	 * <p>
	 * $Id: IndexingOnTheFlyRelation.java,v 1.6 2007-10-19 07:37:18 poettler_ric Exp $
	 * </p>
	 * @author Richard Pöttler (richard dot poettler at deri dot at)
	 * @version $Revision: 1.6 $
	 */
    private class ModifiableIterator<Type> implements Iterator<Type> {

        /** The inner iterator through which to iterate. */
        private Iterator<Type> i;

        /** The inner set from where to obtain the iterator. */
        private final SortedSet<Type> ss;

        /** The last element returned by this iterator. */
        private Type last;

        /**
		 * <p>
		 * Constructs a new iterator for the given set.
		 * </p>
		 * @param ss the set through which to iterate
		 * @throws NullPointerException if the set is <code>null</code>
		 */
        public ModifiableIterator(final SortedSet<Type> ss) {
            if (ss == null) {
                throw new NullPointerException("The set must not be null");
            }
            this.ss = ss;
            this.i = ss.iterator();
        }

        public boolean hasNext() {
            return i.hasNext();
        }

        public Type next() {
            return last = i.next();
        }

        public void remove() {
            IndexingOnTheFlyRelation.this.intRemove(last);
            i = ss.tailSet(last).iterator();
        }
    }

    /**
	 * <p>
	 * Proxyclass around subrelations gained by the
	 * <code>IndexingOnTheFlyRelation</code> or <code>SubRelation</code>.
	 * </p>
	 * <p>
	 * This class will pass all <code>add</code> and <code>remove</code>
	 * calls to the outer <code>IndexingOnTheFlyRelation</code>, so that it
	 * is save to modify it's subrelations.
	 * </p>
	 * <p>
	 * $Id: IndexingOnTheFlyRelation.java,v 1.6 2007-10-19 07:37:18 poettler_ric Exp $
	 * </p>
	 *
	 * @author Richard Pöttler (richard dot poettler at deri dot at)
	 * @version $Revision: 1.6 $
	 */
    private class SubRelation extends AbstractSet<ITuple> implements IRelation {

        /** The inner set holding the tuples. */
        private final SortedSet<ITuple> s;

        /** The from boundary of this subrelation, inclusive. */
        private final ITuple from;

        /** The to boundary of this subrelation, exclusive. */
        private final ITuple to;

        /**
		 * <p>
		 * Construcs a new subrelation with an upper and lower boundary.
		 * The submitted set must already be the subset which should be
		 * represented
		 * </p>
		 * @param s the subset which should be represented by this
		 * object
		 * @param from where this subrelation starts (inclusive),
		 * <code>null</code> if there is no upper boundary
		 * @param to there this subrelation goes (exclusive),
		 * <code>null</code> if there is no lower boundary
		 * @throws NullPointerException if the set is <code>null</code>
		 */
        public SubRelation(final SortedSet<ITuple> s, final ITuple from, final ITuple to) {
            if (s == null) {
                throw new NullPointerException("The set must not be null");
            }
            this.s = s;
            this.from = from;
            this.to = to;
        }

        /**
		 * <p>
		 * Construcs a new subrelation with no boundaries.
		 * The submitted set must already be the subset which should be
		 * represented
		 * </p>
		 * @param s the subset which should be represented by this
		 * object
		 * @throws NullPointerException if the set is <code>null</code>
		 */
        public SubRelation(final SortedSet<ITuple> s) {
            this(s, null, null);
        }

        public boolean add(final ITuple e) {
            if (e == null) {
                throw new NullPointerException("The element must not be null");
            }
            if (!inRange(e)) {
                throw new IllegalArgumentException("The element <" + e + ">couldn't be added, because it is out of range (from: <" + from + ">, to:<" + to + ">)");
            }
            return IndexingOnTheFlyRelation.this.add(e);
        }

        public int size() {
            return s.size();
        }

        public Iterator<ITuple> iterator() {
            return new ModifiableIterator<ITuple>(s);
        }

        public ITuple first() {
            return s.first();
        }

        public ITuple last() {
            return s.last();
        }

        public SortedSet<ITuple> headSet(final ITuple to) {
            if (to == null) {
                throw new NullPointerException("The tuple must not be null");
            }
            return new SubRelation(s.headSet(to), from, determineTo(to));
        }

        public SortedSet<ITuple> tailSet(final ITuple from) {
            if (from == null) {
                throw new NullPointerException("The tuple must not be null");
            }
            return new SubRelation(s.tailSet(from), determineFrom(from), to);
        }

        public SortedSet<ITuple> subSet(final ITuple from, final ITuple to) {
            if ((from == null) || (to == null)) {
                throw new NullPointerException("The tuples must not be null");
            }
            return new SubRelation(s.subSet(from, to), determineFrom(from), determineTo(to));
        }

        public Comparator<? super ITuple> comparator() {
            return s.comparator();
        }

        public int getArity() {
            return IndexingOnTheFlyRelation.this.arity;
        }

        public IRelation indexOn(final int[] idx) {
            final SortedSet<ITuple> temp = IndexingOnTheFlyRelation.this.indexOn(idx);
            if ((from != null) && (to != null)) {
                return new SubRelation(temp.subSet(from, to), from, to);
            } else if (from != null) {
                return new SubRelation(temp.tailSet(from), from, to);
            } else if (to != null) {
                return new SubRelation(temp.headSet(to), from, to);
            }
            return new SubRelation(temp);
        }

        /**
		 * Checks whether a specified element is in the range of this
		 * subset.
		 * @param e the element to check
		 * @return <code>true</code> if it is in the range, otherwise
		 * <code>false</code>
		 * @throws NullPointerException if the element is
		 * <code>null</code>
		 */
        private boolean inRange(final ITuple e) {
            if (e == null) {
                throw new NullPointerException("The element to check must not be null");
            }
            return ((from == null) || (comparator().compare(e, from) >= 0)) && ((to == null) || (comparator().compare(e, to) < 0));
        }

        /**
		 * Determines the most restrective from tuple out of the
		 * submitted one and the one of the actual subrelation.
		 * @param from tuple which should be compared with the from
		 * tuple of <code>this</code> subset.
		 * @return the most restrective tuple
		 * @throws NullPointerException if the submitted tuple is
		 * <code>null</code>
		 */
        private ITuple determineFrom(final ITuple from) {
            if (from == null) {
                throw new NullPointerException("The tuple must not be null");
            }
            if (this.from == null) {
                return from;
            }
            return s.comparator().compare(this.from, from) < 0 ? from : this.from;
        }

        /**
		 * Determines the most restrective to tuple out of the
		 * submitted one and the one of the actual subrelation.
		 * @param to tuple which should be compared with the to
		 * tuple of <code>this</code> subset.
		 * @return the most restrective tuple
		 * @throws NullPointerException if the submitted tuple is
		 * <code>null</code>
		 */
        private ITuple determineTo(final ITuple to) {
            if (to == null) {
                throw new NullPointerException("The tuple must not be null");
            }
            if (this.to == null) {
                return to;
            }
            return s.comparator().compare(this.to, to) > 0 ? to : this.to;
        }
    }
}
