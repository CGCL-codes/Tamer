package tudresden.ocl.lib;

import java.util.*;
import org.ocl4java.ocl.types.OCLCollectionHelper;

/** A OclSequence is a ordered collection of elements that may contain
 *  duplicates. See documentation of OclCollection for more information.
 *
 *  <p>Indexing within an OclSequence begins with 1 for the first element.
 *
 *  @see OclCollection
 *  @author Frank Finger
 */
public class OclSequence extends OclCollection {

    /** public constructor for valid OclSequences; it should be considered to use
   *  Ocl.getOclSequenceFor(Object o) instead of calling this constructor
   *  directly
   *
   *  @see Ocl#getOclSequenceFor(Object o)
   */
    public OclSequence(List list) {
        super(list);
    }

    /** constructor for undefined OclSequence
   */
    public OclSequence(int dummy, String reason) {
        super(dummy, reason);
    }

    /** static factory method for an OclSequence containg no elements
   */
    public static OclSequence getEmptyOclSequence() {
        return new OclSequence(new ArrayList());
    }

    /** two OclSequences are equal if they contain the same elements in the
   *  same order
   */
    public OclBoolean isEqualTo(Object o) {
        if (!(o instanceof OclSequence)) {
            System.out.println("OclSequence isEqualTo() is called with a non-OclSequence parameter");
            return OclBoolean.FALSE;
        }
        OclSequence other = (OclSequence) o;
        if (isUndefined()) return new OclBoolean(0, getUndefinedReason());
        if (other.isUndefined()) return new OclBoolean(0, other.getUndefinedReason());
        Iterator i1 = this.collection.iterator();
        Iterator i2 = other.collection.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            if (!i1.next().equals(i2.next())) {
                return OclBoolean.FALSE;
            }
        }
        if (i1.hasNext() || i2.hasNext()) return OclBoolean.FALSE; else return OclBoolean.TRUE;
    }

    /** @return an OclSequence according to the shorthand notation for <CODE>collect</CODE>
   *
   *  @see OclCollection#getFeature(String name)
   */
    public OclRoot getFeature(final String name) {
        return OCLCollectionHelper.getFeatureAsSequence(this, name);
    }

    /** @return an instance of OclSequence
   *  @see OclCollection#select(OclIterator iter, OclBooleanEvaluatable eval)
   *  @see OclCollection#selectToList(OclIterator iter, OclBooleanEvaluatable eval)
   */
    public OclCollection select(OclIterator iter, OclBooleanEvaluatable eval) {
        if (isUndefined()) return this;
        List l = selectToList(iter, eval);
        if (l == null) return new OclSequence(0, "error in selectToList"); else return new OclSequence(l);
    }

    /** @return an instance of OclSequence
   *  @see OclCollection#collect(OclIterator iter, OclRootEvaluatable eval)
   *  @see OclCollection#collectToList(OclIterator iter, OclRootEvaluatable eval)
   */
    public OclCollection collect(OclIterator iter, OclRootEvaluatable eval) {
        if (isUndefined()) return this;
        List l = collectToList(iter, eval);
        if (l == null) return new OclSequence(0, "error in collectToList"); else return new OclSequence(l);
    }

    /** This method calls <CODE>union(OclSequence seq)</CODE> if the argument
   *  <CODE>col</CODE> is of type <CODE>OclSequence</CODE>.
   *  Return an undefined value, if not.
   *
   *  @see #union(OclSequence seq)
   */
    public OclCollection union(OclCollection col) {
        if (isUndefined()) return this;
        if (col.isUndefined()) return col;
        if (col instanceof OclSequence) return union((OclSequence) col); else return new OclSequence(0, "OclSequence union() called with non-OclSequence argument");
    }

    /** @return the OclSequence consisting of all elements of this OclSequence,
   *          followed by all elements of the OclSequence given as argument
   */
    public OclSequence union(OclSequence seq) {
        if (isUndefined()) return this;
        if (seq.isUndefined()) return seq;
        ArrayList list = new ArrayList(collection);
        list.addAll(seq.collection);
        return new OclSequence(list);
    }

    /** Ocl.STRICT_VALUE_TYPES determines whether the returned OclSequence is
   *  a newly constructed one, or if this OclSequence is changed appropriately
   *  and then returned. If the <CODE>java.util.Collection</CODE> backing this OclSequence
   *  is not a <CODE>java.util.List</CODE>, a new OclSequence is created
   *  independent of Ocl.STRICT_VALUE_TYPES.
   *
   *  @return the OclSequence consisting of all elements of this OclSequence,
   *          followes by the object given as argument
   */
    public OclSequence append(OclRoot obj) {
        if (isUndefined()) return this;
        if (obj.isUndefined()) return new OclSequence(0, obj.getUndefinedReason());
        if (Ocl.STRICT_VALUE_TYPES || !(collection instanceof List)) {
            ArrayList list = new ArrayList(collection);
            list.add(obj);
            return new OclSequence(list);
        } else {
            collection.add(obj);
            return this;
        }
    }

    /** Ocl.STRICT_VALUE_TYPES determines whether the returned OclSequence is
   *  a newly constructed one, or if this OclSequence is changed appropriately
   *  and then returned. If the <CODE>java.util.Collection</CODE> backing this OclSequence
   *  is not a <CODE>java.util.List</CODE>, a new OclSequence is created
   *  independent of Ocl.STRICT_VALUE_TYPES.
   *
   *  @return the OclSequence consisting of the object given as argument followed
   *          by all elements of this OclSequence
   */
    public OclSequence prepend(OclRoot obj) {
        if (isUndefined()) return this;
        if (obj.isUndefined()) return new OclSequence(0, obj.getUndefinedReason());
        if (Ocl.STRICT_VALUE_TYPES || !(collection instanceof List)) {
            ArrayList list = new ArrayList(collection.size() + 1);
            list.add(obj);
            list.addAll(collection);
            return new OclSequence(list);
        } else {
            ((List) collection).add(0, obj);
            return this;
        }
    }

    /** Ocl.STRICT_VALUE_TYPES determines whether the resulting OclSequence will
   *  be backed by the same java.lang.Collection as this OclSequence
   *
   *  @return the sub-sequence of this OclSequence starting at <CODE>lower</CODE>,
   *          up to and including <CODE>upper</CODE>; the first element of this
   *          sequence has the number 1
   *
   *  @param lower needs to be greater than or equal to 1 and less than or
   *         equal to <CODE>upper</CODE>
   *  @param upper needs to be less than or equal to the OclSequences
   *         <CODE>size()</CODE>
   *
   *  @see OclCollection#size()
   */
    public OclSequence subSequence(OclInteger lower, OclInteger upper) {
        if (isUndefined()) return this;
        if (lower.isUndefined()) return new OclSequence(0, lower.getUndefinedReason());
        if (upper.isUndefined()) return new OclSequence(0, upper.getUndefinedReason());
        try {
            List oldlist;
            if (!(collection instanceof List)) oldlist = new ArrayList(collection); else oldlist = (List) collection;
            List newlist = oldlist.subList(lower.getInt() - 1, upper.getInt());
            if (Ocl.STRICT_VALUE_TYPES) return new OclSequence(new ArrayList(newlist)); else return new OclSequence(newlist);
        } catch (IndexOutOfBoundsException e) {
            return new OclSequence(0, "illegal index in OclSequence substring(" + lower + ", " + upper + ")");
        }
    }

    /** @return the OclRoot object with the index <CODE>index</CODE>
   *
   *  @param index needs to be greater than 0 and less than or equal to
   *         <code>size()</code>
   */
    public OclRoot at(OclInteger index) {
        if (isUndefined()) return new OclAnyImpl(0, getUndefinedReason());
        if (index.isUndefined()) return new OclAnyImpl(0, index.getUndefinedReason());
        if (!(collection instanceof List)) collection = new ArrayList(collection);
        try {
            return (OclRoot) ((List) collection).get(index.getInt() - 1);
        } catch (IndexOutOfBoundsException e) {
            return new OclAnyImpl(0, "illegal index " + index + " in OclSequence at()");
        }
    }

    /** @return the first element of this OclSequence; this method is implemented to
   *          call <CODE>at( OclInteger<1> )</CODE>
   */
    public OclRoot first() {
        return at(new OclInteger(1));
    }

    /** @return the last element of this OclSequence; this method is implemented to
   *          call <CODE>at( size() )</CODE>
   */
    public OclRoot last() {
        return at(size());
    }

    /** @return an OclSequence containing all elements of this OclSequence,
   *          followed by the object given as parameter; this method is
   *          implemented to call <CODE>append(obj)</CODE>
   */
    public OclCollection including(OclRoot obj) {
        return append(obj);
    }

    /** @return an OclSequence containing all elements of this OclSequence that
   *          are not equal to <CODE>obj</CODE>, in
   *          the same order as in this OclSequence
   */
    public OclCollection excluding(OclRoot obj) {
        if (isUndefined()) return this;
        if (obj.isUndefined()) return new OclSequence(0, obj.getUndefinedReason());
        ArrayList list = new ArrayList(collection.size());
        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            try {
                if (!obj.isEqualTo(next).isTrue()) {
                    list.add(next);
                }
            } catch (OclException e) {
                list.add(next);
            }
        }
        return new OclSequence(list);
    }

    public String toString() {
        return "OclSequence" + super.toString();
    }
}
