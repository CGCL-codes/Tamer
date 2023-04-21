package tudresden.ocl20.core.lib;

import java.util.*;

/** A OclSet is a collection that does not contain duplicates. See
 *  documentation of OclCollection further information.
 *
 *  @see OclCollection
 *  @author Frank Finger
 */
public class OclSet extends OclUnsortedCollection implements OclSubtractable {

    /** public constructor for valid OclSets; usually it is preferrably to use
   *  the methods <CODE>Ocl.getOclRepresentationFor(...)</CODE> to get
   *  instances of library classes */
    public OclSet(Set set) {
        super(set);
    }

    /** This constructor should only be used if the parameter is known to contain
   *  no duplicates, e.g. if it is the result of a select operation on a OclSet.
   */
    protected OclSet(List l) {
        super(l);
    }

    /** private constructor for undefined OclSet */
    public OclSet(int dummy, String reason) {
        super(dummy, reason);
    }

    /** static factory method for OclSet containing no elements
   */
    public static OclSet getEmptyOclSet() {
        return new OclSet(new HashSet());
    }

    /** two OclSets are equal if they contain the same elements
   */
    public OclBoolean isEqualTo(Object o) {
        if (!(o instanceof OclSet)) {
            System.out.println("OclSet isEqualTo() is called with a non-OclSet parameter");
            return OclBoolean.FALSE;
        }
        OclSet other = (OclSet) o;
        if (isUndefined()) return new OclBoolean(0, getUndefinedReason());
        if (other.isUndefined()) return new OclBoolean(0, other.getUndefinedReason());
        if (this.collection.containsAll(other.collection) && other.collection.containsAll(this.collection)) {
            return OclBoolean.TRUE;
        } else {
            return OclBoolean.FALSE;
        }
    }

    /** @return an instance of OclSet
   *  @see OclCollection#select(OclIterator iter, OclBooleanEvaluatable eval)
   *  @see OclCollection#selectToList(OclIterator iter, OclBooleanEvaluatable eval)
   */
    public OclCollection select(OclIterator iter, OclBooleanEvaluatable eval) {
        if (isUndefined()) return this;
        List list = selectToList(iter, eval);
        if (list == null) return new OclSet(0, "error in selectToList"); else return new OclSet(list);
    }

    /** @return an instance of OclBag
   *  @see OclCollection#collect(OclIterator iter, OclRootEvaluatable eval)
   *  @see OclCollection#collectToList(OclIterator iter, OclRootEvaluatable eval)
   */
    public OclCollection collect(OclIterator iter, OclRootEvaluatable eval) {
        if (isUndefined()) return this;
        List list = collectToList(iter, eval);
        if (list == null) return new OclSet(0, "error in collectToList"); else return new OclBag(list);
    }

    public OclCollection collectNested(OclIterator iter, OclRootEvaluatable eval) {
        if (isUndefined()) return this;
        List list = collectNestedToList(iter, eval);
        if (list == null) return new OclBag(0, "error in collectToList"); else return new OclBag(list);
    }

    /**
   *Removes one level of nesting.
   */
    public OclCollection flatten() {
        if (isUndefined()) return this;
        List list = flattenToList();
        if (list == null) return new OclSet(0, "error in flattenToList"); else return new OclSet(list);
    }

    /** This method tries to call the appropriate method of the same name (for
   *  OclSet or OclBag parameters) or, if there is no such method, handles this
   *  error by returning an undefined value.
   *
   *  @see OclCollection#union(OclCollection col)
   *  @see #union(OclBag col)
   *  @see #union(OclSet col)
   */
    public OclCollection union(OclCollection col) {
        if (isUndefined()) return this;
        if (col.isUndefined()) return col;
        if (col instanceof OclSet) return union((OclSet) col); else if (col instanceof OclBag) return union((OclBag) col); else return new OclSet(0, "tried to create union of OclSet and OclSequence");
    }

    /** The union of two OclSets is itself an OclSet again.
   */
    public OclSet union(OclSet set) {
        if (isUndefined()) return this;
        if (set.isUndefined()) return set;
        HashSet hs = new HashSet(collection);
        hs.addAll(set.collection);
        return new OclSet(hs);
    }

    /** The union of an OclSet and an OclBag is an OclBag.
   */
    public OclBag union(OclBag bag) {
        if (isUndefined()) return new OclBag(0, getUndefinedReason());
        if (bag.isUndefined()) return bag;
        ArrayList list = new ArrayList(collection.size() + bag.collection.size());
        list.addAll(collection);
        list.addAll(bag.collection);
        return new OclBag(list);
    }

    /** @return an OclSet containing all elements found both in this OclSet and
   *          in the OclBag given as parameter
   */
    public OclSet intersection(OclBag bag) {
        if (isUndefined()) return this;
        if (bag.isUndefined()) return new OclSet(0, bag.getUndefinedReason());
        Set set = intersection(bag.collection);
        return new OclSet(set);
    }

    public OclSet intersection(OclSet set) {
        if (isUndefined()) return this;
        if (set.isUndefined()) return set;
        Set ret = intersection(set.collection);
        return new OclSet(ret);
    }

    /** This method is called by the public methods <CODE>intersection(OclBag) </CODE>
   *  and <CODE>intersection(OclSet)</CODE>. It is assumed that this OclSet is not
   *  undefined.
   *
   *  @param col must not be <CODE>null</CODE> and should contain only
   *         objects of type <CODE>OclRoot</CODE>
   *
   *  @return a java.lang.Set containing all elements found in both the backing
   *          java.lang.Collection of this OclSet and the argument
   */
    protected Set intersection(Collection col) {
        Iterator iter = collection.iterator();
        HashSet set = new HashSet(collection.size() * 2);
        while (iter.hasNext()) {
            Object elem = (OclRoot) iter.next();
            if (col.contains(elem)) {
                set.add(elem);
            }
        }
        return set;
    }

    /** STRICT_VALUE_TYPES determines whether the changes of the returned
   *  collection affect this collection, and if the returned is actually
   *  different from this collection.
   *
   *  @return an OclSet containing all elements of this set, plus the argument
   *
   *  @see OclCollection#STRICT_VALUE_TYPES
   */
    public OclSet including(OclRoot obj) {
        if (isUndefined()) return this;
        if (obj.isUndefined()) return new OclSet(0, obj.getUndefinedReason());
        HashSet hs = new HashSet(collection);
        hs.add(obj);
        return new OclSet(hs);
    }

    /** STRICT_VALUE_TYPES determines whether the changes of the returned
   *  collection affect this collection, and if the returned is actually
   *  different from this collection.
   *
   *  @return an OclSet containing all elements of this OclSet that are not
   *          present in the argument set
   *
   *  @param s needs to be an OclSet, undefined value is returned
   */
    public OclSubtractable subtract(OclSubtractable s) {
        if (isUndefined()) return this;
        if (s.isUndefined()) return s;
        try {
            OclSet set = (OclSet) s;
            HashSet ret = new HashSet(collection);
            Iterator iter = set.collection.iterator();
            while (iter.hasNext()) ret.remove(iter.next());
            return new OclSet(ret);
        } catch (ClassCastException ex) {
            return new OclSet(0, "OclSet subtract called with non-OclSet argument");
        }
    }

    /** STRICT_VALUE_TYPES determines whether the changes of the returned
   *  collection affect this collection, and if the returned is actually
   *  different from this collection. If this OclSet is not backed by a
   *  java.util.HashSet, a new OclSet is created even if STRICT_VALUE_TYPES
   *  is set to <CODE>false</CODE>.
   *
   *  @return the OclSet containing all elements found in this OclSet or the
   *          argument, but not in both
   *
   *  @see OclCollection#STRICT_VALUE_TYPES
   */
    public OclSet symmetricDifference(OclSet set) {
        if (isUndefined()) return this;
        if (set.isUndefined()) return set;
        boolean bCreateCopy = !(collection instanceof HashSet);
        HashSet ret;
        HashSet other = new HashSet(set.collection);
        other.removeAll(collection);
        if (bCreateCopy) ret = new HashSet(collection); else ret = (HashSet) collection;
        ret.removeAll(set.collection);
        ret.addAll(other);
        if (bCreateCopy) return new OclSet(ret); else return this;
    }

    /** STRICT_VALUE_TYPES determines whether the changes of the returned
   *  collection affect this collection, and if the returned is actually
   *  different from this collection. If this OclSet is not backed by a
   *  java.util.HashSet, a new OclSet is created even if STRICT_VALUE_TYPES
   *  is set to <CODE>false</CODE>.
   *
   *  @return an OclSet containing all elements of this OclSet but the argument
   *
   *  @see OclCollection#STRICT_VALUE_TYPES
   */
    public OclSet excluding(OclRoot obj) {
        if (isUndefined()) return this;
        if (obj.isUndefined()) return new OclSet(0, obj.getUndefinedReason());
        boolean bCreateCopy = !(collection instanceof HashSet);
        HashSet ret;
        if (bCreateCopy) ret = new HashSet(collection); else ret = (HashSet) collection;
        ret.remove(obj);
        if (bCreateCopy) return new OclSet(ret); else return this;
    }

    public String toString() {
        return "OclSet" + super.toString();
    }
}
