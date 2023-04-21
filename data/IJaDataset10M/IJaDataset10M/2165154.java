package org.jmlspecs.unfinished;

import java.util.Enumeration;

public class JMLEqualsToObjectRelationEnumerator implements JMLEnumeration, JMLValueType {

    /** An enumerator for the image pairs in this relation.
     */
    protected JMLEqualsToObjectRelationImageEnumerator imagePairEnum;

    /** The current key for pairs being enumerated.
     */
    protected Object key;

    /** An enumerator for the range elements that are related to the
     *  key that have not yet been returned.
     */
    protected JMLObjectSetEnumerator imageEnum;

    JMLEqualsToObjectRelationEnumerator(JMLEqualsToObjectRelation rel) {
        imagePairEnum = rel.imagePairs();
        if (imagePairEnum.hasMoreElements()) {
            JMLEqualsValuePair p = imagePairEnum.nextImagePair();
            key = p.key;
            imageEnum = ((JMLObjectSet) p.value).elements();
        } else {
            key = null;
            imageEnum = (new JMLObjectSet()).elements();
        }
    }

    protected JMLEqualsToObjectRelationEnumerator(JMLEqualsToObjectRelationImageEnumerator ipEnum, JMLObjectSetEnumerator iEnum, Object k) {
        imagePairEnum = (JMLEqualsToObjectRelationImageEnumerator) ipEnum.clone();
        imageEnum = (JMLObjectSetEnumerator) iEnum.clone();
        key = k;
    }

    public boolean hasMoreElements() {
        return (imagePairEnum.hasMoreElements() || imageEnum.hasMoreElements());
    }

    public Object nextElement() throws JMLNoSuchElementException {
        if (imageEnum.hasMoreElements()) {
            Object v = imageEnum.nextElement();
            return new JMLEqualsObjectPair(key, (Object) v);
        } else if (imagePairEnum.hasMoreElements()) {
            Object p = imagePairEnum.nextElement();
            JMLEqualsValuePair imagePair = (JMLEqualsValuePair) p;
            key = imagePair.key;
            imageEnum = ((JMLObjectSet) imagePair.value).elements();
            Object v = imageEnum.nextElement();
            return new JMLEqualsObjectPair(key, (Object) v);
        } else {
            throw new JMLNoSuchElementException();
        }
    }

    public JMLEqualsObjectPair nextPair() throws JMLNoSuchElementException {
        Object p = nextElement();
        JMLEqualsObjectPair aPair = (JMLEqualsObjectPair) p;
        return aPair;
    }

    /** Return a clone of this enumerator.
     */
    public Object clone() {
        return new JMLEqualsToObjectRelationEnumerator(imagePairEnum, imageEnum, key);
    }

    /** Return true just when this enumerator has the same state as
     *  the given argument.
     */
    public boolean equals(Object oth) {
        if (oth == null || !(oth instanceof JMLEqualsToObjectRelationEnumerator)) {
            return false;
        } else {
            JMLEqualsToObjectRelationEnumerator js = (JMLEqualsToObjectRelationEnumerator) oth;
            return abstractValue().equals(js.abstractValue());
        }
    }

    /** Return a hash code for this enumerator.
     */
    public int hashCode() {
        return abstractValue().hashCode();
    }

    /** Return the set of uniterated pairs from this enumerator.
     */
    protected JMLValueSet abstractValue() {
        JMLValueSet ret = new JMLValueSet();
        JMLEqualsToObjectRelationEnumerator enum2 = (JMLEqualsToObjectRelationEnumerator) clone();
        while (enum2.hasMoreElements()) {
            JMLEqualsObjectPair aPair = enum2.nextPair();
            ret = ret.insert(aPair);
        }
        return ret;
    }
}
