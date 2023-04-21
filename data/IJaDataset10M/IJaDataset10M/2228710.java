package org.jmlspecs.models;

import java.math.BigInteger;

public class JMLObjectSequence<E> implements JMLCollection<E> {

    /** The list representing this sequence's elements, in order.
     */
    protected final JMLListObjectNode<E> theSeq;

    /** This sequence's length.
     */
    protected final BigInteger _length;

    public JMLObjectSequence() {
        theSeq = null;
        _length = BigInteger.ZERO;
    }

    /** Initialize this to be the sequence containing just the given element.
     *  @param e the element that is the first element in this sequence.
     *  @see #singleton
     */
    public JMLObjectSequence(E e) {
        theSeq = JMLListObjectNode.cons(e, null);
        _length = BigInteger.ONE;
    }

    protected JMLObjectSequence(JMLListObjectNode<E> ls, int len) {
        theSeq = ls;
        _length = BigInteger.valueOf(len);
    }

    /** The empty JMLObjectSequence.
     *  @see #JMLObjectSequence()
     */
    public static final JMLObjectSequence EMPTY = new JMLObjectSequence();

    public static <F> JMLObjectSequence<F> singleton(F e) {
        return new JMLObjectSequence<F>(e);
    }

    public static <F> JMLObjectSequence<F> convertFrom(F[] a) {
        JMLObjectSequence<F> ret = EMPTY;
        for (int i = a.length - 1; 0 <= i; i--) {
            ret = ret.insertFront(a[i]);
        }
        return ret;
    }

    public static <F> JMLObjectSequence<F> convertFrom(F[] a, int size) {
        JMLObjectSequence<F> ret = EMPTY;
        for (int i = size - 1; 0 <= i; i--) {
            ret = ret.insertFront(a[i]);
        }
        return ret;
    }

    public static <F> JMLObjectSequence<F> convertFrom(java.util.Collection<F> c) throws ClassCastException {
        JMLObjectSequence<F> ret = EMPTY;
        java.util.Iterator<F> celems = c.iterator();
        while (celems.hasNext()) {
            F o = celems.next();
            if (o == null) {
                ret = ret.insertBack(null);
            } else {
                ret = ret.insertBack(o);
            }
        }
        return ret;
    }

    public static <F> JMLObjectSequence<F> convertFrom(JMLCollection<F> c) throws ClassCastException {
        JMLObjectSequence<F> ret = EMPTY;
        JMLIterator<F> celems = c.iterator();
        while (celems.hasNext()) {
            F o = celems.next();
            if (o == null) {
                ret = ret.insertBack(null);
            } else {
                ret = ret.insertBack(o);
            }
        }
        return ret;
    }

    public E itemAt(int i) throws JMLSequenceException {
        if (i < 0 || i >= int_length()) {
            throw new JMLSequenceException("Index out of range.");
        } else {
            JMLListObjectNode<E> thisWalker = theSeq;
            int k = 0;
            for (; k < i; k++) {
                thisWalker = thisWalker.next;
            }
            return (thisWalker.head());
        }
    }

    public E get(int i) throws IndexOutOfBoundsException {
        try {
            E ret = itemAt(i);
            return ret;
        } catch (JMLSequenceException e) {
            IndexOutOfBoundsException e2 = new IndexOutOfBoundsException();
            e2.initCause(e);
            throw e2;
        }
    }

    public int int_size() {
        return _length.intValue();
    }

    public int int_length() {
        return _length.intValue();
    }

    public int count(E item) {
        JMLListObjectNode<E> ptr = this.theSeq;
        int cnt = 0;
        while (ptr != null) {
            if (ptr.headEquals(item)) {
                cnt++;
            }
            ptr = ptr.next;
        }
        return cnt;
    }

    public boolean has(Object elem) {
        return theSeq != null && theSeq.has(elem);
    }

    public boolean containsAll(java.util.Collection<E> c) {
        java.util.Iterator<E> celems = c.iterator();
        while (celems.hasNext()) {
            E o = celems.next();
            if (!has(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPrefix(JMLObjectSequence<E> s2) {
        return int_length() <= s2.int_length() && (theSeq == null || theSeq.isPrefixOf(s2.theSeq));
    }

    public boolean isProperPrefix(JMLObjectSequence<E> s2) {
        return int_length() != s2.int_length() && isPrefix(s2);
    }

    public boolean isSuffix(JMLObjectSequence<E> s2) {
        if (int_length() > s2.int_length()) {
            return false;
        } else if (int_length() == 0) {
            return true;
        }
        JMLListObjectNode<E> suffix = s2.theSeq.removePrefix(s2.int_length() - int_length());
        return theSeq.equals(suffix);
    }

    public boolean isProperSuffix(JMLObjectSequence<E> s2) {
        return int_length() != s2.int_length() && isSuffix(s2);
    }

    public boolean equals(Object obj) {
        return (obj != null && obj instanceof JMLObjectSequence) && (int_length() == ((JMLObjectSequence<E>) obj).int_length()) && isPrefix((JMLObjectSequence<E>) obj);
    }

    /** Return a hash code for this object.
     */
    public int hashCode() {
        return (theSeq == null ? 0 : theSeq.hashCode());
    }

    public boolean isEmpty() {
        return theSeq == null;
    }

    public int indexOf(E item) throws JMLSequenceException {
        if (theSeq == null) {
            throw new JMLSequenceException(ITEM_PREFIX + item + IS_NOT_FOUND);
        }
        int idx = theSeq.indexOf(item);
        if (idx == -1) {
            throw new JMLSequenceException(ITEM_PREFIX + item + IS_NOT_FOUND);
        } else {
            return idx;
        }
    }

    private static final String ITEM_PREFIX = "item ";

    private static final String IS_NOT_FOUND = " is not in this sequence.";

    public E first() throws JMLSequenceException {
        if (theSeq == null) {
            throw new JMLSequenceException("Tried first() on empty sequence.");
        } else {
            return (theSeq.head());
        }
    }

    public E last() throws JMLSequenceException {
        if (theSeq == null) {
            throw new JMLSequenceException("Tried last() on empty sequence.");
        } else {
            return theSeq.last();
        }
    }

    public boolean isSubsequence(JMLObjectSequence<E> s2) {
        JMLListObjectNode<E> walker = s2.theSeq;
        for (int walkerLen = s2.int_length(); int_length() <= walkerLen; walkerLen--) {
            if (theSeq == null || theSeq.isPrefixOf(walker)) {
                return true;
            }
            walker = walker.next;
        }
        return false;
    }

    public boolean isProperSubsequence(JMLObjectSequence<E> s2) {
        return int_length() < s2.int_length() && isSubsequence(s2);
    }

    public boolean isSupersequence(JMLObjectSequence<E> s2) {
        return s2.isSubsequence(this);
    }

    public boolean isProperSupersequence(JMLObjectSequence<E> s2) {
        return s2.isProperSubsequence(this);
    }

    public boolean isInsertionInto(JMLObjectSequence<E> s2, E elem) {
        if (int_length() != s2.int_length() + 1) {
            return false;
        }
        JMLListObjectNode<E> walker = theSeq;
        JMLListObjectNode<E> s2walker = s2.theSeq;
        for (int lenRemaining = int_length(); lenRemaining > 0; lenRemaining--) {
            if (walker.headEquals(elem)) {
                if ((walker.next == null && s2walker == null) || (walker.next != null && walker.next.equals(s2walker))) {
                    return true;
                }
            }
            if (s2walker == null || !s2walker.headEquals(walker.head())) {
                return false;
            }
            walker = walker.next;
            s2walker = s2walker.next;
        }
        return false;
    }

    public boolean isDeletionFrom(JMLObjectSequence<E> s2, E elem) {
        return s2.isInsertionInto(this, elem);
    }

    public Object clone() {
        return this;
    }

    public JMLObjectSequence<E> prefix(int n) throws JMLSequenceException {
        if (n < 0 || n > int_length()) {
            throw new JMLSequenceException("Invalid parameter to prefix() with n = " + n + "\n" + "   when sequence length = " + int_length());
        } else {
            if (n == 0) {
                return new JMLObjectSequence<E>();
            } else {
                JMLListObjectNode<E> pfx_list = theSeq.prefix(n);
                return new JMLObjectSequence<E>(pfx_list, n);
            }
        }
    }

    public JMLObjectSequence<E> removePrefix(int n) throws JMLSequenceException {
        if (n < 0 || n > int_length()) {
            throw new JMLSequenceException("Invalid parameter to removePrefix() " + "with n = " + n + "\n" + "   when sequence length = " + int_length());
        } else {
            if (n == 0) {
                return this;
            } else {
                JMLListObjectNode<E> pfx_list = theSeq.removePrefix(n);
                return new JMLObjectSequence<E>(pfx_list, int_length() - n);
            }
        }
    }

    public JMLObjectSequence<E> concat(JMLObjectSequence<E> s2) {
        if (theSeq == null) {
            return s2;
        } else if (s2.theSeq == null) {
            return this;
        } else {
            JMLListObjectNode<E> new_list = theSeq.concat(s2.theSeq);
            return new JMLObjectSequence<E>(new_list, int_length() + s2.int_length());
        }
    }

    public JMLObjectSequence<E> reverse() {
        if (theSeq == null) {
            return this;
        } else {
            JMLListObjectNode<E> r = theSeq.reverse();
            return new JMLObjectSequence<E>(r, int_length());
        }
    }

    public JMLObjectSequence<E> removeItemAt(int index) throws JMLSequenceException {
        if (0 <= index && index < int_length()) {
            JMLListObjectNode<E> new_list = theSeq.removeItemAt(index);
            return new JMLObjectSequence<E>(new_list, int_length() - 1);
        } else {
            throw new JMLSequenceException("Invalid parameter to removeItemAt() " + "with index = " + index + "\n" + "   when sequence length = " + int_length());
        }
    }

    public JMLObjectSequence<E> replaceItemAt(int index, E item) throws JMLSequenceException {
        if (0 <= index && index < int_length()) {
            JMLListObjectNode<E> new_list = theSeq.replaceItemAt(index, item);
            return new JMLObjectSequence<E>(new_list, int_length());
        } else {
            throw new JMLSequenceException("Invalid parameter to replaceItemAt() " + "with index = " + index + "\n" + "   when sequence length = " + int_length());
        }
    }

    public JMLObjectSequence<E> header() throws JMLSequenceException {
        if (theSeq == null) {
            throw new JMLSequenceException("Tried header() on empty sequence.");
        } else {
            JMLListObjectNode<E> new_list = theSeq.removeLast();
            return new JMLObjectSequence<E>(new_list, int_length() - 1);
        }
    }

    public JMLObjectSequence<E> trailer() throws JMLSequenceException {
        if (theSeq == null) {
            throw new JMLSequenceException("Tried trailer() on empty sequence.");
        } else {
            JMLListObjectNode<E> new_list = theSeq.next;
            return new JMLObjectSequence<E>(new_list, int_length() - 1);
        }
    }

    public JMLObjectSequence<E> insertAfterIndex(int afterThisOne, E item) throws JMLSequenceException, IllegalStateException {
        if (afterThisOne < -1 || afterThisOne >= int_length()) {
            throw new JMLSequenceException("Invalid parameter to " + "insertAfterIndex() " + "with afterThisOne = " + afterThisOne + "\n" + "   when sequence length = " + int_length());
        } else if (int_length() < Integer.MAX_VALUE) {
            return insertBeforeIndex(afterThisOne + 1, item);
        } else {
            throw new IllegalStateException(TOO_BIG_TO_INSERT);
        }
    }

    private static final String TOO_BIG_TO_INSERT = "Cannot insert into a sequence with Integer.MAX_VALUE elements.";

    public JMLObjectSequence<E> insertBeforeIndex(int beforeThisOne, E item) throws JMLSequenceException, IllegalStateException {
        if (beforeThisOne < 0 || beforeThisOne > int_length()) {
            throw new JMLSequenceException("Invalid parameter to insertBeforeIndex()" + " with beforeThisOne = " + beforeThisOne + "\n" + "   when sequence length = " + int_length());
        } else if (int_length() < Integer.MAX_VALUE) {
            if (theSeq == null) {
                return new JMLObjectSequence<E>(item);
            } else {
                JMLListObjectNode<E> new_list = theSeq.insertBefore(beforeThisOne, item);
                return new JMLObjectSequence<E>(new_list, int_length() + 1);
            }
        } else {
            throw new IllegalStateException(TOO_BIG_TO_INSERT);
        }
    }

    public JMLObjectSequence<E> insertBack(E item) throws IllegalStateException {
        if (theSeq == null) {
            return new JMLObjectSequence<E>(item);
        } else if (int_length() < Integer.MAX_VALUE) {
            return new JMLObjectSequence<E>(theSeq.append(item), int_length() + 1);
        } else {
            throw new IllegalStateException(TOO_BIG_TO_INSERT);
        }
    }

    public JMLObjectSequence<E> insertFront(E item) throws IllegalStateException {
        if (theSeq == null) {
            return new JMLObjectSequence<E>(item);
        } else if (int_length() < Integer.MAX_VALUE) {
            return new JMLObjectSequence<E>(JMLListObjectNode.cons(item, theSeq), int_length() + 1);
        } else {
            throw new IllegalStateException(TOO_BIG_TO_INSERT);
        }
    }

    public JMLObjectSequence<E> subsequence(int from, int to) throws JMLSequenceException {
        if (from < 0 || from > to || to > int_length()) {
            throw new JMLSequenceException("Invalid parameters to " + "subsequence() with from = " + from + " and to = " + to + "\n" + "   " + "when sequence length = " + int_length());
        } else {
            if (theSeq == null) {
                return this;
            } else {
                JMLListObjectNode<E> removedPrefix = theSeq.removePrefix(from);
                if (removedPrefix == null) {
                    return new JMLObjectSequence<E>();
                } else {
                    JMLListObjectNode<E> new_list = removedPrefix.prefix(to - from);
                    return new JMLObjectSequence<E>(new_list, to - from);
                }
            }
        }
    }

    public JMLObjectBag<E> toBag() {
        JMLObjectBag<E> ret = new JMLObjectBag<E>();
        JMLIterator<E> elems = iterator();
        while (elems.hasNext()) {
            E o = elems.next();
            E e = (o == null ? null : o);
            ret = ret.insert(e);
        }
        return ret;
    }

    public JMLObjectSet<E> toSet() {
        JMLObjectSet<E> ret = new JMLObjectSet<E>();
        JMLIterator<E> elems = iterator();
        while (elems.hasNext()) {
            E o = elems.next();
            E e = (o == null ? null : o);
            ret = ret.insert(e);
        }
        return ret;
    }

    public Object[] toArray() {
        Object[] ret = new Object[int_length()];
        JMLIterator<E> elems = iterator();
        int i = 0;
        while (elems.hasNext()) {
            E o = elems.next();
            if (o == null) {
                ret[i] = null;
            } else {
                E e = o;
                ret[i] = e;
            }
            i++;
        }
        return ret;
    }

    public JMLObjectSequenceEnumerator<E> elements() {
        JMLObjectSequenceEnumerator<E> retValue = new JMLObjectSequenceEnumerator<E>(this);
        return retValue;
    }

    public JMLIterator<E> iterator() {
        return new JMLEnumerationToIterator<E>(elements());
    }

    public String toString() {
        String newStr = "(<";
        JMLListObjectNode<E> seqWalker = theSeq;
        boolean first = true;
        while (seqWalker != null) {
            if (!first) {
                newStr = newStr + ", ";
            }
            newStr = newStr + seqWalker.val;
            first = false;
            seqWalker = seqWalker.next;
        }
        return (newStr + ">)");
    }
}
