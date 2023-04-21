package org.jmlspecs.models;

import java.util.Enumeration;

public class JMLEqualsToEqualsRelation<K, V> implements JMLCollection<JMLEqualsEqualsPair<K, V>> {

    /** The set of elements in the domain of this relation.
     */
    protected final JMLEqualsSet<K> domain_;

    /** The set representing the image pairs in the relation.  The
     * elements of this set are JMLEqualsValuePairs, which are all
     * non-null.  Each such pair has a key which is an element in
     * domain_ and a value which is a JMLEqualsSet containing all of
     * the elements that the key of the pair is related to.
     */
    protected final JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> imagePairSet_;

    /** The size (number of pairs) of this relation.
     */
    protected final int size_;

    public JMLEqualsToEqualsRelation() {
        domain_ = new JMLEqualsSet<K>();
        imagePairSet_ = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        size_ = 0;
    }

    public JMLEqualsToEqualsRelation(K dv, V rv) {
        size_ = 1;
        domain_ = new JMLEqualsSet<K>(dv);
        JMLEqualsSet<V> img = new JMLEqualsSet<V>(rv);
        imagePairSet_ = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(dv, img));
    }

    public JMLEqualsToEqualsRelation(JMLEqualsEqualsPair<K, V> pair) {
        this(pair.key, pair.value);
    }

    protected JMLEqualsToEqualsRelation(JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> ipset, JMLEqualsSet<K> dom, int sz) {
        domain_ = dom;
        imagePairSet_ = ipset;
        size_ = sz;
    }

    /** The empty JMLEqualsToEqualsRelation.
     * @see #JMLEqualsToEqualsRelation()
     */
    public static final JMLEqualsToEqualsRelation EMPTY = new JMLEqualsToEqualsRelation();

    public static <SK, SR> JMLEqualsToEqualsRelation<SK, SR> singleton(SK dv, SR rv) {
        return new JMLEqualsToEqualsRelation<SK, SR>(dv, rv);
    }

    public static <SK, SR> JMLEqualsToEqualsRelation<SK, SR> singleton(JMLEqualsEqualsPair<SK, SR> pair) {
        return singleton(pair.key, pair.value);
    }

    public boolean isaFunction() {
        return size_ == domain_.int_size();
    }

    public JMLEqualsSet<V> elementImage(K dv) {
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (imagePair.keyEquals(dv)) {
                JMLEqualsSet<V> res = imagePair.value;
                return res;
            }
        }
        return new JMLEqualsSet<V>();
    }

    public JMLEqualsSet<V> image(JMLEqualsSet<K> dom) {
        JMLEqualsSet<V> img = new JMLEqualsSet<V>();
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (dom.has(imagePair.key)) {
                JMLEqualsSet<V> ipv = imagePair.value;
                img = img.union(ipv);
            }
        }
        return img;
    }

    public JMLEqualsToEqualsRelation<V, K> inverse() {
        JMLEqualsToEqualsRelation<V, K> invRel = new JMLEqualsToEqualsRelation<V, K>();
        JMLEqualsToEqualsRelationEnumerator<K, V> assocEnum = this.associations();
        JMLEqualsEqualsPair<K, V> pair;
        while (assocEnum.hasMoreElements()) {
            pair = assocEnum.nextPair();
            invRel = invRel.add(pair.value, pair.key);
        }
        return invRel;
    }

    public JMLEqualsSet<K> inverseElementImage(V rv) {
        JMLEqualsSet<K> invImg = new JMLEqualsSet<K>();
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            JMLEqualsSet<V> img = imagePair.value;
            if (img.has(rv)) {
                invImg = invImg.insert(imagePair.key);
            }
        }
        return invImg;
    }

    public JMLEqualsSet<K> inverseImage(JMLEqualsSet<V> rng) {
        JMLEqualsSet<K> invImg = new JMLEqualsSet<K>();
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            JMLEqualsSet<V> img = imagePair.value;
            if (!img.intersection(rng).isEmpty()) {
                invImg = invImg.insert(imagePair.key);
            }
        }
        return invImg;
    }

    public boolean isDefinedAt(K dv) {
        return domain_.has(dv);
    }

    public boolean has(K dv, V rv) {
        return domain_.has(dv) && elementImage(dv).has(rv);
    }

    public boolean has(JMLEqualsEqualsPair<K, V> pair) {
        return has(pair.key, pair.value);
    }

    public boolean has(Object obj) {
        return obj != null && obj instanceof JMLEqualsEqualsPair && has((JMLEqualsEqualsPair<K, V>) obj);
    }

    public boolean isEmpty() {
        return size_ == 0;
    }

    public Object clone() {
        return new JMLEqualsToEqualsRelation<K, V>(imagePairSet_, domain_, size_);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JMLEqualsToEqualsRelation)) {
            return false;
        }
        JMLEqualsToEqualsRelation<K, V> rel = (JMLEqualsToEqualsRelation<K, V>) obj;
        if (size_ != rel.int_size()) {
            return false;
        }
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = imagePair.value;
            if (!img.equals(rel.elementImage(imagePair.key))) {
                return false;
            }
        }
        return true;
    }

    /** Return a hash code for this object.
     */
    public int hashCode() {
        return imagePairSet_.hashCode();
    }

    public JMLEqualsSet<K> domain() {
        return domain_;
    }

    public JMLEqualsSet<V> range() {
        JMLEqualsSet<V> rangeSet = new JMLEqualsSet<V>();
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = imagePair.value;
            rangeSet = rangeSet.union(img);
        }
        return rangeSet;
    }

    private static final String TOO_BIG_TO_INSERT = "Cannot insert into a Relation with Integer.MAX_VALUE elements.";

    public JMLEqualsToEqualsRelation<K, V> add(K dv, V rv) throws NullPointerException, IllegalStateException {
        if (rv == null) {
            throw new NullPointerException();
        }
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet;
        JMLEqualsSet<K> newDom;
        int newSize;
        JMLEqualsSet<V> img;
        if (!domain_.has(dv)) {
            if (size_ == Integer.MAX_VALUE) {
                throw new IllegalStateException(TOO_BIG_TO_INSERT);
            }
            newDom = domain_.insert(dv);
            newSize = size_ + 1;
            img = new JMLEqualsSet<V>(rv);
            newImagePairSet = imagePairSet_.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(dv, img));
        } else {
            newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
            newDom = domain_;
            newSize = 0;
            JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
            JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
            while (imagePairEnum.hasMoreElements()) {
                imagePair = imagePairEnum.nextImagePair();
                img = imagePair.value;
                if (imagePair.keyEquals(dv)) {
                    img = img.insert(rv);
                }
                int size_inc = img.int_size();
                if (newSize <= Integer.MAX_VALUE - size_inc) {
                    newSize = newSize + size_inc;
                } else {
                    throw new IllegalStateException(TOO_BIG_TO_INSERT);
                }
                newImagePairSet = newImagePairSet.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(imagePair.key, img));
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    public JMLEqualsToEqualsRelation<K, V> insert(JMLEqualsEqualsPair<K, V> pair) throws IllegalStateException {
        return add(pair.key, pair.value);
    }

    public JMLEqualsToEqualsRelation<K, V> removeFromDomain(K dv) {
        if (!domain_.has(dv)) {
            return (this);
        }
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        JMLEqualsSet<K> newDom = domain_.remove(dv);
        int newSize = 0;
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (!imagePair.keyEquals(dv)) {
                newImagePairSet = newImagePairSet.insert(imagePair);
                JMLEqualsSet<V> img = imagePair.value;
                newSize = newSize + img.int_size();
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    public JMLEqualsToEqualsRelation<K, V> remove(K dv, V rv) {
        if (!domain_.has(dv)) {
            return (this);
        }
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        JMLEqualsSet<K> newDom = domain_;
        int newSize = 0;
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = imagePair.value;
            int imgSize = img.int_size();
            if (imagePair.keyEquals(dv)) {
                img = img.remove(rv);
                imgSize = img.int_size();
                if (imgSize > 0) {
                    newImagePairSet = newImagePairSet.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(dv, img));
                    newSize = newSize + imgSize;
                } else {
                    newDom = newDom.remove(dv);
                }
            } else {
                newImagePairSet = newImagePairSet.insert(imagePair);
                newSize = newSize + imgSize;
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    public JMLEqualsToEqualsRelation<K, V> remove(JMLEqualsEqualsPair<K, V> pair) {
        return remove(pair.key, pair.value);
    }

    public <D extends JMLType> JMLValueToEqualsRelation<D, V> compose(JMLValueToEqualsRelation<D, K> othRel) {
        JMLValueSet<JMLValueValuePair<D, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLValueValuePair<D, JMLEqualsSet<V>>>();
        JMLValueSet<D> newDom = new JMLValueSet<D>();
        int newSize = 0;
        JMLValueToEqualsRelationImageEnumerator<D, K> imagePairEnum = othRel.imagePairs();
        JMLValueValuePair<D, JMLEqualsSet<K>> imagePair;
        JMLEqualsSet<K> img1;
        JMLEqualsSet<V> img2;
        int imgSize;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img1 = (JMLEqualsSet<K>) imagePair.value;
            img2 = this.image(img1);
            imgSize = img2.int_size();
            if (imgSize > 0) {
                newImagePairSet = newImagePairSet.insert(new JMLValueValuePair<D, JMLEqualsSet<V>>(imagePair.key, img2));
                newSize = newSize + imgSize;
                newDom = newDom.insert(imagePair.key);
            }
        }
        return new JMLValueToEqualsRelation<D, V>(newImagePairSet, newDom, newSize);
    }

    public <D> JMLObjectToEqualsRelation<D, V> compose(JMLObjectToEqualsRelation<D, K> othRel) {
        JMLValueSet<JMLObjectValuePair<D, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLObjectValuePair<D, JMLEqualsSet<V>>>();
        JMLObjectSet<D> newDom = new JMLObjectSet<D>();
        int newSize = 0;
        JMLObjectToEqualsRelationImageEnumerator<D, K> imagePairEnum = othRel.imagePairs();
        JMLObjectValuePair<D, JMLEqualsSet<K>> imagePair;
        JMLEqualsSet<K> img1;
        JMLEqualsSet<V> img2;
        int imgSize;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img1 = imagePair.value;
            img2 = this.image(img1);
            imgSize = img2.int_size();
            if (imgSize > 0) {
                newImagePairSet = newImagePairSet.insert(new JMLObjectValuePair<D, JMLEqualsSet<V>>(imagePair.key, img2));
                newSize = newSize + imgSize;
                newDom = newDom.insert(imagePair.key);
            }
        }
        return new JMLObjectToEqualsRelation<D, V>(newImagePairSet, newDom, newSize);
    }

    public JMLEqualsToEqualsRelation<K, V> union(JMLEqualsToEqualsRelation<K, V> othRel) throws IllegalStateException {
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        JMLEqualsSet<K> newDom = domain_;
        int newSize = 0;
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = imagePair.value;
            img = img.union(othRel.elementImage(imagePair.key));
            newImagePairSet = newImagePairSet.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(imagePair.key, img));
            int size_inc = img.int_size();
            if (newSize <= Integer.MAX_VALUE - size_inc) {
                newSize = newSize + size_inc;
            } else {
                throw new IllegalStateException(TOO_BIG_TO_UNION);
            }
        }
        JMLEqualsSet<K> diffDom = othRel.domain().difference(this.domain_);
        imagePairEnum = othRel.imagePairs();
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (diffDom.has(imagePair.key)) {
                newImagePairSet = newImagePairSet.insert(imagePair);
                newDom = newDom.insert(imagePair.key);
                img = imagePair.value;
                int size_inc = img.int_size();
                if (newSize <= Integer.MAX_VALUE - size_inc) {
                    newSize = newSize + size_inc;
                } else {
                    throw new IllegalStateException(TOO_BIG_TO_UNION);
                }
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    protected static final String TOO_BIG_TO_UNION = "Cannot make a union with more than Integer.MAX_VALUE elements.";

    public JMLEqualsToEqualsRelation<K, V> intersection(JMLEqualsToEqualsRelation<K, V> othRel) {
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        JMLEqualsSet<K> newDom = new JMLEqualsSet<K>();
        int newSize = 0;
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = imagePair.value;
            img = img.intersection(othRel.elementImage(imagePair.key));
            if (!img.isEmpty()) {
                newImagePairSet = newImagePairSet.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(imagePair.key, img));
                newDom = newDom.insert(imagePair.key);
                newSize = newSize + img.int_size();
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    public JMLEqualsToEqualsRelation<K, V> difference(JMLEqualsToEqualsRelation<K, V> othRel) {
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        JMLEqualsSet<K> newDom = new JMLEqualsSet<K>();
        int newSize = 0;
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = imagePair.value;
            img = img.difference(othRel.elementImage(imagePair.key));
            if (!img.isEmpty()) {
                newImagePairSet = newImagePairSet.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(imagePair.key, img));
                newDom = newDom.insert(imagePair.key);
                newSize = newSize + img.int_size();
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    public JMLEqualsToEqualsRelation<K, V> restrictDomainTo(JMLEqualsSet<K> dom) {
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        JMLEqualsSet<K> newDom = domain_.intersection(dom);
        int newSize = 0;
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (newDom.has(imagePair.key)) {
                newImagePairSet = newImagePairSet.insert(imagePair);
                img = imagePair.value;
                newSize = newSize + img.int_size();
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    public JMLEqualsToEqualsRelation<K, V> restrictRangeTo(JMLEqualsSet<V> rng) {
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
        JMLEqualsSet<K> newDom = new JMLEqualsSet<K>();
        int newSize = 0;
        JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
        JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
        JMLEqualsSet<V> img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = imagePair.value.intersection(rng);
            if (!img.isEmpty()) {
                newImagePairSet = newImagePairSet.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(imagePair.key, img));
                newDom = newDom.insert(imagePair.key);
                newSize = newSize + img.int_size();
            }
        }
        return new JMLEqualsToEqualsRelation<K, V>(newImagePairSet, newDom, newSize);
    }

    public String toString() {
        return toSet().toString();
    }

    public JMLEqualsToEqualsRelationEnumerator<K, V> associations() {
        return new JMLEqualsToEqualsRelationEnumerator<K, V>(this);
    }

    public JMLEqualsToEqualsRelationEnumerator<K, V> elements() {
        return associations();
    }

    public JMLIterator<JMLEqualsEqualsPair<K, V>> iterator() {
        return new JMLEnumerationToIterator<JMLEqualsEqualsPair<K, V>>(elements());
    }

    public JMLValueSet<JMLEqualsEqualsPair<K, V>> toSet() {
        JMLEqualsToEqualsRelationEnumerator<K, V> pairEnum = this.associations();
        JMLValueSet<JMLEqualsEqualsPair<K, V>> ret = new JMLValueSet<JMLEqualsEqualsPair<K, V>>();
        while (pairEnum.hasMoreElements()) {
            JMLEqualsEqualsPair<K, V> p = pairEnum.nextPair();
            ret = ret.insert(p);
        }
        return ret;
    }

    public JMLValueBag<JMLEqualsEqualsPair<K, V>> toBag() {
        JMLEqualsToEqualsRelationEnumerator<K, V> pairEnum = this.associations();
        JMLValueBag<JMLEqualsEqualsPair<K, V>> ret = new JMLValueBag<JMLEqualsEqualsPair<K, V>>();
        while (pairEnum.hasMoreElements()) {
            JMLEqualsEqualsPair<K, V> p = pairEnum.nextPair();
            ret = ret.insert(p);
        }
        return ret;
    }

    public JMLValueSequence<JMLEqualsEqualsPair<K, V>> toSequence() {
        JMLEqualsToEqualsRelationEnumerator<K, V> pairEnum = this.associations();
        JMLValueSequence<JMLEqualsEqualsPair<K, V>> ret = new JMLValueSequence<JMLEqualsEqualsPair<K, V>>();
        while (pairEnum.hasMoreElements()) {
            JMLEqualsEqualsPair<K, V> p = pairEnum.nextPair();
            ret = ret.insertFront(p);
        }
        return ret;
    }

    public JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> imagePairSet() {
        return imagePairSet_;
    }

    public JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairs() {
        return new JMLEqualsToEqualsRelationImageEnumerator<K, V>(this);
    }

    public JMLEqualsSetEnumerator<K> domainElements() {
        return domain_.elements();
    }

    public JMLEqualsSetEnumerator<V> rangeElements() {
        return range().elements();
    }

    public int int_size() {
        return size_;
    }

    public JMLEqualsToEqualsMap<K, V> toFunction() {
        JMLEqualsSet<K> newDom = domain_;
        int newSize = domain_.int_size();
        JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>> newImagePairSet = imagePairSet_;
        if (newSize != size_) {
            JMLEqualsToEqualsRelationImageEnumerator<K, V> imagePairEnum = this.imagePairs();
            newImagePairSet = new JMLValueSet<JMLEqualsValuePair<K, JMLEqualsSet<V>>>();
            JMLEqualsValuePair<K, JMLEqualsSet<V>> imagePair;
            JMLEqualsSet<V> img;
            while (imagePairEnum.hasMoreElements()) {
                imagePair = imagePairEnum.nextImagePair();
                img = imagePair.value;
                Enumeration<V> imgEnum = img.elements();
                V o = imgEnum.nextElement();
                img = new JMLEqualsSet<V>(o);
                newImagePairSet = newImagePairSet.insert(new JMLEqualsValuePair<K, JMLEqualsSet<V>>(imagePair.key, img));
            }
        }
        return new JMLEqualsToEqualsMap<K, V>(newImagePairSet, newDom, newSize);
    }
}
