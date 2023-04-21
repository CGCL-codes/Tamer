package edu.neu.ccs.demeterf.lib;

import java.util.Comparator;
import edu.neu.ccs.demeterf.ID;
import edu.neu.ccs.demeterf.Traversal;
import edu.neu.ccs.demeterf.control.Edge;
import edu.neu.ccs.demeterf.Control;

/** <p>Java Functional implementation of Red-Black Trees.  Use the static methods to
 *     create them, then insert/remove to modify them. Note that they are completely
 *     functional, so insert/remove return a new tree with or without the given element
 *     respectively </p>
 *  <p>The elements stored in the tree must implement <tt>Comparable<X></tt>, though
 *     insertion and deletion can accept a seperate <tt>Comparator</tt> if the
 *     implementation requires more than one, or you would rather use an external
 *     comparator.</p>
 */
public abstract class RBTree<X extends Comparable<X>> implements java.lang.Iterable<X> {

    static class CComp<X extends Comparable<X>> implements Comparator<X> {

        public int compare(X a, X b) {
            return a.compareTo(b);
        }
    }

    /** Is this RBTree a leaf? */
    public abstract boolean isLeaf();

    /** Returns a new RBTree that includes the given Comparable element */
    public RBTree<X> insert(X x) {
        return insert(x, new CComp<X>());
    }

    /** Returns a new RBTree that includes the given element using the given Comparator */
    public RBTree<X> insert(X x, Comparator<X> comp) {
        return ins(x, comp).makeBlack();
    }

    /** Returns a new RBTree that includes all the (Comparable) elements from the given RBTree */
    public RBTree<X> insertAll(RBTree<X> t) {
        return insertAll(t.toList());
    }

    /** Returns a new RBTree that includes all the (Comparable) elements from the given List */
    public RBTree<X> insertAll(List<X> lst) {
        return insertAll(lst, new CComp<X>());
    }

    /** Returns a new RBTree that includes all the elements from the given RBTree,
     *    using the given Comparator */
    public RBTree<X> insertAll(RBTree<X> t, Comparator<X> c) {
        return insertAll(t.toList(), c);
    }

    /** Returns a new RBTree that includes all the elements from the given List,
     *    using the given Comparator */
    public RBTree<X> insertAll(List<X> lst, Comparator<X> c) {
        if (lst.isEmpty()) return this;
        return insert(lst.top(), c).insertAll(lst.pop(), c);
    }

    /** Return this RBTree without the given (Comparable) element */
    public RBTree<X> remove(X x) {
        return remove(x, new CComp<X>());
    }

    /** Return this RBTree without the given element, using the given Comparator */
    public abstract RBTree<X> remove(X x, Comparator<X> comp);

    /** Return the number of elements in the RBTree */
    public abstract int size();

    abstract RBTree<X> del(X x, Comparator<X> c);

    abstract boolean isBlack();

    abstract boolean isRed();

    abstract RBNode<X> asNode();

    abstract RBTree<X> makeBlack();

    abstract RBTree<X> makeRed();

    abstract RBTree<X> ins(X x, Comparator<X> comp);

    /** Return this RBTree's predicessor element (Only valid on RBNodes) */
    public abstract X pred();

    /** Return this RBTree's successor element (Only valid on RBNodes) */
    public abstract X succ();

    /** Return this RBTree's minimum element (Only valid on RBNodes) */
    public abstract X min();

    /** Return this RBTree's maximum element (Only valid on RBNodes) */
    public abstract X max();

    boolean isBlackNode() {
        return !isLeaf() && isBlack();
    }

    boolean isRedNode() {
        return !isLeaf() && isRed();
    }

    /** Is the given (Comparable) element in this RBTree? */
    public boolean contains(X x) {
        return contains(x, new CComp<X>());
    }

    /** Is the given element in this RBTree, using the given Comparator? */
    public abstract boolean contains(X x, Comparator<X> comp);

    /** Are all the (Comparable) elements int the given RBTree contained in this RBTree? */
    public boolean containsAll(RBTree<X> x) {
        return containsAll(x, new CComp<X>());
    }

    /** Are all the elements int the given RBTree contained in this RBTree,
     *    using the given Comparator? */
    public abstract boolean containsAll(RBTree<X> x, Comparator<X> comp);

    /** Return the (Comparable) X in this RBTree that matches the given one */
    public X find(X x) {
        return find(x, new CComp<X>());
    }

    /** Return the X in this RBTree that matches the given one, using the given Comparator */
    public abstract X find(X x, Comparator<X> comp);

    /** Return a new RBTree without the given (Comparable) element */
    public RBTree<X> replace(X x) {
        return replace(x, new CComp<X>());
    }

    /** Return a new RBTree without the given element, using the given Comparator */
    public abstract RBTree<X> replace(X x, Comparator<X> comp);

    /** Return all the elements in thei RBTree as a List in order */
    public abstract List<X> toList();

    static <X extends Comparable<X>> RBTree<X> balance(X dat, RBTree<X> l, RBTree<X> r) {
        if (l.isRedNode() && r.isRedNode()) return node(red(), dat, l.makeBlack(), r.makeBlack());
        if (l.isRedNode()) {
            RBNode<X> L = l.asNode();
            if (L.left.isRedNode()) {
                RBNode<X> LL = L.left.asNode();
                return node(red(), L.data, node(black(), LL.data, LL.left, LL.right), node(black(), dat, L.right, r));
            }
            if (L.right.isRedNode()) {
                RBNode<X> LR = L.right.asNode();
                return node(red(), LR.data, node(black(), L.data, L.left, LR.left), node(black(), dat, LR.right, r));
            }
        }
        if (r.isRedNode()) {
            RBNode<X> R = r.asNode();
            if (R.left.isRedNode()) {
                RBNode<X> RL = R.left.asNode();
                return node(red(), RL.data, node(black(), dat, l, RL.left), node(black(), R.data, RL.right, R.right));
            }
            if (R.right.isRedNode()) {
                RBNode<X> RR = R.right.asNode();
                return node(red(), R.data, node(black(), dat, l, R.left), node(black(), RR.data, RR.left, RR.right));
            }
        }
        return node(black(), dat, l, r);
    }

    protected static <X extends Comparable<X>> RBTree<X> balleft(RBTree<X> l, X y, RBTree<X> r) {
        if (l.isRedNode()) return node(red(), y, l.makeBlack(), r);
        if (r.isBlackNode()) return balance(y, l, r.makeRed());
        RBNode<X> right = r.asNode();
        RBNode<X> rtlt = right.left.asNode();
        return node(red(), rtlt.data, node(black(), y, l, rtlt.left), balance(right.data, rtlt.right, right.right.makeRed()));
    }

    protected static <X extends Comparable<X>> RBTree<X> balright(RBTree<X> l, X y, RBTree<X> r) {
        if (r.isRedNode()) return node(red(), y, l, r.makeBlack());
        if (l.isBlackNode()) return balance(y, l.makeRed(), r);
        RBNode<X> left = l.asNode();
        RBNode<X> ltrt = left.right.asNode();
        return node(red(), ltrt.data, balance(left.data, left.left.makeRed(), ltrt.left), node(black(), y, ltrt.right, r));
    }

    protected static <X extends Comparable<X>> RBTree<X> append(RBTree<X> l, RBTree<X> r) {
        if (l.isLeaf()) return r;
        if (r.isLeaf()) return l;
        RBNode<X> left = l.asNode(), right = r.asNode();
        if (left.color.equals(right.color)) {
            RBColor c = left.color;
            RBTree<X> rtlt = append(left.right, right.left);
            if (rtlt.isRedNode()) {
                RBNode<X> rln = rtlt.asNode();
                return node(red(), rln.data, node(c, left.data, left.left, rln.left), node(c, right.data, rln.right, right.right));
            }
            if (c.isRed()) return node(red(), left.data, left.left, node(red(), right.data, rtlt, right.right));
            return balleft(left.left, left.data, node(black(), right.data, rtlt, right.right));
        }
        if (right.isRed()) return node(red(), right.data, append(left, right.left), right.right);
        return node(red(), left.data, left.left, append(left.right, right));
    }

    /** Return the RBColor "<b>Black</b>" */
    public static RBColor black() {
        return RBColor.black();
    }

    /** Return the RBColor "<b color="red">Red</b>" */
    public static RBColor red() {
        return RBColor.red();
    }

    /** Create an RBTree containing the given elements */
    public static <X extends Comparable<X>> RBTree<X> create(X... xs) {
        return create(List.create(xs));
    }

    /** Create an RBTree containing the given elements */
    public static <X extends Comparable<X>> RBTree<X> create(List<X> xs) {
        return create((Iterable<X>) xs);
    }

    /** Create an RBTree containing the given elements, using the given Comparator */
    public static <X extends Comparable<X>> RBTree<X> create(Iterable<X> xs) {
        RBTree<X> t = new RBLeaf<X>();
        for (X x : xs) t = t.insert(x);
        return t;
    }

    /** Create a new RBLeaf */
    public static <X extends Comparable<X>> RBLeaf<X> leaf() {
        return new RBLeaf<X>();
    }

    /** Create a new RBNode */
    public static <X extends Comparable<X>> RBNode<X> node(RBColor c, X d, RBTree<X> l, RBTree<X> r) {
        return new RBNode<X>(c, d, l, r);
    }

    /** Return an Interator for the elements in this list, in Comparator order. */
    public java.util.Iterator<X> iterator() {
        return toList().iterator();
    }

    public abstract int hashCode();

    public abstract boolean equals(Object o);
}
