package org.jmlspecs.unfinished;

import java.util.Enumeration;

public class JMLEqualsToValueRelationImageEnumerator implements JMLEnumeration, JMLValueType {

    /** An enumerator for the image pairs in this relation.
     */
    protected JMLValueSetEnumerator pairEnum;

    JMLEqualsToValueRelationImageEnumerator(JMLEqualsToValueRelation rel) {
        pairEnum = rel.imagePairSet_.elements();
    }

    protected JMLEqualsToValueRelationImageEnumerator(JMLValueSetEnumerator pEnum) {
        pairEnum = (JMLValueSetEnumerator) pEnum.clone();
    }

    public boolean hasMoreElements() {
        return pairEnum.hasMoreElements();
    }

    public Object nextElement() throws JMLNoSuchElementException {
        if (pairEnum.hasMoreElements()) {
            Object o = pairEnum.nextElement();
            return o;
        } else {
            throw new JMLNoSuchElementException();
        }
    }

    public JMLEqualsValuePair nextImagePair() throws JMLNoSuchElementException {
        Object p = nextElement();
        JMLEqualsValuePair aPair = (JMLEqualsValuePair) p;
        return aPair;
    }

    /** Return a clone of this enumerator.
     */
    public Object clone() {
        return new JMLEqualsToValueRelationImageEnumerator(pairEnum);
    }

    /** Return true just when this enumerator has the same state as
     *  the given argument.
     */
    public boolean equals(Object oth) {
        if (oth == null || !(oth instanceof JMLEqualsToValueRelationImageEnumerator)) {
            return false;
        } else {
            JMLEqualsToValueRelationImageEnumerator js = (JMLEqualsToValueRelationImageEnumerator) oth;
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
        JMLEqualsToValueRelationImageEnumerator enum2 = (JMLEqualsToValueRelationImageEnumerator) clone();
        while (enum2.hasMoreElements()) {
            JMLEqualsValuePair aPair = enum2.nextImagePair();
            ret = ret.insert(aPair);
        }
        return ret;
    }
}
