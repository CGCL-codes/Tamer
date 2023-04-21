package org.jmlspecs.models;

public class JMLEqualsToEqualsRelationEnumerator<K, V> implements JMLEnumeration<JMLEqualsEqualsPair<K, V>>, JMLValueType {

    /** An enumerator for the image pairs in this relation.
     */
    protected JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum;

    /** The current key for pairs being enumerated.
     */
    protected K key;

    /** An enumerator for the range elements that are related to the
     *  key that have not yet been returned.
     */
    protected JMLEqualsSetEnumerator<V> imageEnum;

    JMLEqualsToEqualsRelationEnumerator(JMLEqualsToEqualsRelation<K, V> rel) {
        imagePairEnum = rel.imagePairs();
        if (imagePairEnum.hasMoreElements()) {
            JMLEqualsValuePair<K, JMLEqualsSet<V>> p = imagePairEnum.nextImagePair();
            key = p.key;
            imageEnum = p.value.elements();
        } else {
            key = null;
            imageEnum = (new JMLEqualsSet<V>()).elements();
        }
    }

    protected JMLEqualsToEqualsRelationEnumerator(JMLEqualsToEqualsRelationImageEnumerator<K, V> ipEnum, JMLEqualsSetEnumerator<V> iEnum, K k) {
        imagePairEnum = (JMLEqualsToEqualsRelationImageEnumerator<K, V>) ipEnum.clone();
        imageEnum = (JMLEqualsSetEnumerator<V>) iEnum.clone();
        key = k;
    }

    public boolean hasMoreElements() {
        return (imagePairEnum.hasMoreElements() || imageEnum.hasMoreElements());
    }

    public JMLEqualsEqualsPair<K, V> nextElement() throws JMLNoSuchElementException {
        if (imageEnum.hasMoreElements()) {
            V v = imageEnum.nextElement();
            return new JMLEqualsEqualsPair<K, V>(key, v);
        } else if (imagePairEnum.hasMoreElements()) {
            JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair = imagePairEnum.nextElement();
            key = imagePair.key;
            imageEnum = imagePair.value.elements();
            V v = imageEnum.nextElement();
            return new JMLEqualsEqualsPair<K, V>(key, v);
        } else {
            throw new JMLNoSuchElementException();
        }
    }

    public JMLEqualsEqualsPair<K, V> nextPair() throws JMLNoSuchElementException {
        JMLEqualsEqualsPair<K, V> aPair = nextElement();
        return aPair;
    }

    /** Return a clone of this enumerator.
     */
    public Object clone() {
        return new JMLEqualsToEqualsRelationEnumerator<K, V>(imagePairEnum, imageEnum, key);
    }

    /** Return true just when this enumerator has the same state as
     *  the given argument.
     */
    public boolean equals(Object oth) {
        if (oth == null || !(oth instanceof JMLEqualsToEqualsRelationEnumerator)) {
            return false;
        } else {
            JMLEqualsToEqualsRelationEnumerator<K, V> js = (JMLEqualsToEqualsRelationEnumerator<K, V>) oth;
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
    protected JMLValueSet<JMLEqualsEqualsPair<K, V>> abstractValue() {
        JMLValueSet<JMLEqualsEqualsPair<K, V>> ret = new JMLValueSet<JMLEqualsEqualsPair<K, V>>();
        JMLEqualsToEqualsRelationEnumerator<K, V> enum2 = (JMLEqualsToEqualsRelationEnumerator<K, V>) clone();
        while (enum2.hasMoreElements()) {
            JMLEqualsEqualsPair<K, V> aPair = enum2.nextPair();
            ret = ret.insert(aPair);
        }
        return ret;
    }
}
