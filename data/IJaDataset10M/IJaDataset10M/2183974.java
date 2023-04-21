package org.jmlspecs.unfinished;

import java.util.Enumeration;
import org.jmlspecs.lang.JMLIterator;

public class JMLValueToObjectRelation implements JMLCollection {

    /** The set of elements in the domain of this relation.
     */
    protected final JMLValueSet domain_;

    /** The set representing the image pairs in the relation.  The
     * elements of this set are JMLValueValuePairs, which are all
     * non-null.  Each such pair has a key which is an element in
     * domain_ and a value which is a JMLObjectSet containing all of
     * the elements that the key of the pair is related to.
     */
    protected final JMLValueSet imagePairSet_;

    /** The size (number of pairs) of this relation.
     */
    protected final int size_;

    public JMLValueToObjectRelation() {
        domain_ = new JMLValueSet();
        imagePairSet_ = new JMLValueSet();
        size_ = 0;
    }

    public JMLValueToObjectRelation(JMLType dv, Object rv) {
        size_ = 1;
        domain_ = new JMLValueSet(dv);
        JMLObjectSet img = new JMLObjectSet(rv);
        imagePairSet_ = new JMLValueSet(new JMLValueValuePair(dv, img));
    }

    public JMLValueToObjectRelation(JMLValueObjectPair pair) {
        this(pair.key, pair.value);
    }

    protected JMLValueToObjectRelation(JMLValueSet ipset, JMLValueSet dom, int sz) {
        domain_ = dom;
        imagePairSet_ = ipset;
        size_ = sz;
    }

    /** The empty JMLValueToObjectRelation.
     * @see #JMLValueToObjectRelation()
     */
    public static final JMLValueToObjectRelation EMPTY = new JMLValueToObjectRelation();

    public static JMLValueToObjectRelation singleton(JMLType dv, Object rv) {
        return new JMLValueToObjectRelation(dv, rv);
    }

    public static JMLValueToObjectRelation singleton(JMLValueObjectPair pair) {
        return singleton(pair.key, pair.value);
    }

    public boolean isaFunction() {
        return size_ == domain_.int_size();
    }

    public JMLObjectSet elementImage(JMLType dv) {
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (imagePair.keyEquals(dv)) {
                JMLObjectSet res = (JMLObjectSet) imagePair.value;
                return res;
            }
        }
        return new JMLObjectSet();
    }

    public JMLObjectSet image(JMLValueSet dom) {
        JMLObjectSet img = new JMLObjectSet();
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (dom.has(imagePair.key)) {
                JMLObjectSet ipv = (JMLObjectSet) imagePair.value;
                img = img.union(ipv);
            }
        }
        return img;
    }

    public JMLObjectToValueRelation inverse() {
        JMLObjectToValueRelation invRel = new JMLObjectToValueRelation();
        JMLValueToObjectRelationEnumerator assocEnum = this.associations();
        JMLValueObjectPair pair;
        while (assocEnum.hasMoreElements()) {
            pair = assocEnum.nextPair();
            invRel = invRel.add(pair.value, pair.key);
        }
        return invRel;
    }

    public JMLValueSet inverseElementImage(Object rv) {
        JMLValueSet invImg = new JMLValueSet();
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            JMLObjectSet img = (JMLObjectSet) imagePair.value;
            if (img.has(rv)) {
                invImg = invImg.insert(imagePair.key);
            }
        }
        return invImg;
    }

    public JMLValueSet inverseImage(JMLObjectSet rng) {
        JMLValueSet invImg = new JMLValueSet();
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            JMLObjectSet img = (JMLObjectSet) imagePair.value;
            if (!img.intersection(rng).isEmpty()) {
                invImg = invImg.insert(imagePair.key);
            }
        }
        return invImg;
    }

    public boolean isDefinedAt(JMLType dv) {
        return domain_.has(dv);
    }

    public boolean has(JMLType dv, Object rv) {
        return domain_.has(dv) && elementImage(dv).has(rv);
    }

    public boolean has(JMLValueObjectPair pair) {
        return has(pair.key, pair.value);
    }

    public boolean has(Object obj) {
        return obj != null && obj instanceof JMLValueObjectPair && has((JMLValueObjectPair) obj);
    }

    public boolean isEmpty() {
        return size_ == 0;
    }

    public Object clone() {
        return new JMLValueToObjectRelation(imagePairSet_, domain_, size_);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JMLValueToObjectRelation)) {
            return false;
        }
        JMLValueToObjectRelation rel = (JMLValueToObjectRelation) obj;
        if (size_ != rel.int_size()) {
            return false;
        }
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = (JMLObjectSet) imagePair.value;
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

    public JMLValueSet domain() {
        return domain_;
    }

    public JMLObjectSet range() {
        JMLObjectSet rangeSet = new JMLObjectSet();
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = (JMLObjectSet) imagePair.value;
            rangeSet = rangeSet.union(img);
        }
        return rangeSet;
    }

    private static final String TOO_BIG_TO_INSERT = "Cannot insert into a Relation with Integer.MAX_VALUE elements.";

    public JMLValueToObjectRelation add(JMLType dv, Object rv) throws NullPointerException, IllegalStateException {
        if (rv == null) {
            throw new NullPointerException();
        }
        JMLValueSet newImagePairSet;
        JMLValueSet newDom;
        int newSize;
        JMLObjectSet img;
        if (!domain_.has(dv)) {
            if (size_ == Integer.MAX_VALUE) {
                throw new IllegalStateException(TOO_BIG_TO_INSERT);
            }
            newDom = domain_.insert(dv);
            newSize = size_ + 1;
            img = new JMLObjectSet(rv);
            newImagePairSet = imagePairSet_.insert(new JMLValueValuePair(dv, img));
        } else {
            newImagePairSet = new JMLValueSet();
            newDom = domain_;
            newSize = 0;
            JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
            JMLValueValuePair imagePair;
            while (imagePairEnum.hasMoreElements()) {
                imagePair = imagePairEnum.nextImagePair();
                img = (JMLObjectSet) imagePair.value;
                if (imagePair.keyEquals(dv)) {
                    img = img.insert(rv);
                }
                int size_inc = img.int_size();
                if (newSize <= Integer.MAX_VALUE - size_inc) {
                    newSize = newSize + size_inc;
                } else {
                    throw new IllegalStateException(TOO_BIG_TO_INSERT);
                }
                newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(imagePair.key, img));
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLValueToObjectRelation insert(JMLValueObjectPair pair) throws IllegalStateException {
        return add(pair.key, pair.value);
    }

    public JMLValueToObjectRelation removeFromDomain(JMLType dv) {
        if (!domain_.has(dv)) {
            return (this);
        }
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = domain_.remove(dv);
        int newSize = 0;
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (!imagePair.keyEquals(dv)) {
                newImagePairSet = newImagePairSet.insert(imagePair);
                JMLObjectSet img = (JMLObjectSet) imagePair.value;
                newSize = newSize + img.int_size();
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLValueToObjectRelation remove(JMLType dv, Object rv) {
        if (!domain_.has(dv)) {
            return (this);
        }
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = domain_;
        int newSize = 0;
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = (JMLObjectSet) imagePair.value;
            int imgSize = img.int_size();
            if (imagePair.keyEquals(dv)) {
                img = img.remove(rv);
                imgSize = img.int_size();
                if (imgSize > 0) {
                    newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(dv, img));
                    newSize = newSize + imgSize;
                } else {
                    newDom = newDom.remove(dv);
                }
            } else {
                newImagePairSet = newImagePairSet.insert(imagePair);
                newSize = newSize + imgSize;
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLValueToObjectRelation remove(JMLValueObjectPair pair) {
        return remove(pair.key, pair.value);
    }

    public JMLValueToObjectRelation compose(JMLValueToValueRelation othRel) {
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = new JMLValueSet();
        int newSize = 0;
        JMLValueToValueRelationImageEnumerator imagePairEnum = othRel.imagePairs();
        JMLValueValuePair imagePair;
        JMLValueSet img1;
        JMLObjectSet img2;
        int imgSize;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img1 = (JMLValueSet) imagePair.value;
            img2 = this.image(img1);
            imgSize = img2.int_size();
            if (imgSize > 0) {
                newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(imagePair.key, img2));
                newSize = newSize + imgSize;
                newDom = newDom.insert(imagePair.key);
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLObjectToObjectRelation compose(JMLObjectToValueRelation othRel) {
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLObjectSet newDom = new JMLObjectSet();
        int newSize = 0;
        JMLObjectToValueRelationImageEnumerator imagePairEnum = othRel.imagePairs();
        JMLObjectValuePair imagePair;
        JMLValueSet img1;
        JMLObjectSet img2;
        int imgSize;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img1 = (JMLValueSet) imagePair.value;
            img2 = this.image(img1);
            imgSize = img2.int_size();
            if (imgSize > 0) {
                newImagePairSet = newImagePairSet.insert(new JMLObjectValuePair(imagePair.key, img2));
                newSize = newSize + imgSize;
                newDom = newDom.insert(imagePair.key);
            }
        }
        return new JMLObjectToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLValueToObjectRelation union(JMLValueToObjectRelation othRel) throws IllegalStateException {
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = domain_;
        int newSize = 0;
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = (JMLObjectSet) imagePair.value;
            img = img.union(othRel.elementImage(imagePair.key));
            newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(imagePair.key, img));
            int size_inc = img.int_size();
            if (newSize <= Integer.MAX_VALUE - size_inc) {
                newSize = newSize + size_inc;
            } else {
                throw new IllegalStateException(TOO_BIG_TO_UNION);
            }
        }
        JMLValueSet diffDom = othRel.domain().difference(this.domain_);
        imagePairEnum = othRel.imagePairs();
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (diffDom.has(imagePair.key)) {
                newImagePairSet = newImagePairSet.insert(imagePair);
                newDom = newDom.insert(imagePair.key);
                img = (JMLObjectSet) imagePair.value;
                int size_inc = img.int_size();
                if (newSize <= Integer.MAX_VALUE - size_inc) {
                    newSize = newSize + size_inc;
                } else {
                    throw new IllegalStateException(TOO_BIG_TO_UNION);
                }
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    protected static final String TOO_BIG_TO_UNION = "Cannot make a union with more than Integer.MAX_VALUE elements.";

    public JMLValueToObjectRelation intersection(JMLValueToObjectRelation othRel) {
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = new JMLValueSet();
        int newSize = 0;
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = (JMLObjectSet) imagePair.value;
            img = img.intersection(othRel.elementImage(imagePair.key));
            if (!img.isEmpty()) {
                newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(imagePair.key, img));
                newDom = newDom.insert(imagePair.key);
                newSize = newSize + img.int_size();
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLValueToObjectRelation difference(JMLValueToObjectRelation othRel) {
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = new JMLValueSet();
        int newSize = 0;
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = (JMLObjectSet) imagePair.value;
            img = img.difference(othRel.elementImage(imagePair.key));
            if (!img.isEmpty()) {
                newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(imagePair.key, img));
                newDom = newDom.insert(imagePair.key);
                newSize = newSize + img.int_size();
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLValueToObjectRelation restrictDomainTo(JMLValueSet dom) {
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = domain_.intersection(dom);
        int newSize = 0;
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            if (newDom.has(imagePair.key)) {
                newImagePairSet = newImagePairSet.insert(imagePair);
                img = (JMLObjectSet) imagePair.value;
                newSize = newSize + img.int_size();
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public JMLValueToObjectRelation restrictRangeTo(JMLObjectSet rng) {
        JMLValueSet newImagePairSet = new JMLValueSet();
        JMLValueSet newDom = new JMLValueSet();
        int newSize = 0;
        JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
        JMLValueValuePair imagePair;
        JMLObjectSet img;
        while (imagePairEnum.hasMoreElements()) {
            imagePair = imagePairEnum.nextImagePair();
            img = ((JMLObjectSet) imagePair.value).intersection(rng);
            if (!img.isEmpty()) {
                newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(imagePair.key, img));
                newDom = newDom.insert(imagePair.key);
                newSize = newSize + img.int_size();
            }
        }
        return new JMLValueToObjectRelation(newImagePairSet, newDom, newSize);
    }

    public String toString() {
        return toSet().toString();
    }

    public JMLValueToObjectRelationEnumerator associations() {
        return new JMLValueToObjectRelationEnumerator(this);
    }

    public JMLValueToObjectRelationEnumerator elements() {
        return associations();
    }

    public JMLIterator iterator() {
        return new JMLEnumerationToIterator(elements());
    }

    public JMLValueSet toSet() {
        JMLValueToObjectRelationEnumerator pairEnum = this.associations();
        JMLValueSet ret = new JMLValueSet();
        while (pairEnum.hasMoreElements()) {
            JMLValueObjectPair p = pairEnum.nextPair();
            ret = ret.insert(p);
        }
        return ret;
    }

    public JMLValueBag toBag() {
        JMLValueToObjectRelationEnumerator pairEnum = this.associations();
        JMLValueBag ret = new JMLValueBag();
        while (pairEnum.hasMoreElements()) {
            JMLValueObjectPair p = pairEnum.nextPair();
            ret = ret.insert(p);
        }
        return ret;
    }

    public JMLValueSequence toSequence() {
        JMLValueToObjectRelationEnumerator pairEnum = this.associations();
        JMLValueSequence ret = new JMLValueSequence();
        while (pairEnum.hasMoreElements()) {
            JMLValueObjectPair p = pairEnum.nextPair();
            ret = ret.insertFront(p);
        }
        return ret;
    }

    public JMLValueSet imagePairSet() {
        return imagePairSet_;
    }

    public JMLValueToObjectRelationImageEnumerator imagePairs() {
        return new JMLValueToObjectRelationImageEnumerator(this);
    }

    public JMLValueSetEnumerator domainElements() {
        return domain_.elements();
    }

    public JMLObjectSetEnumerator rangeElements() {
        return range().elements();
    }

    public int int_size() {
        return size_;
    }

    public JMLValueToObjectMap toFunction() {
        JMLValueSet newDom = domain_;
        int newSize = domain_.int_size();
        JMLValueSet newImagePairSet = imagePairSet_;
        if (newSize != size_) {
            JMLValueToObjectRelationImageEnumerator imagePairEnum = this.imagePairs();
            newImagePairSet = new JMLValueSet();
            JMLValueValuePair imagePair;
            JMLObjectSet img;
            while (imagePairEnum.hasMoreElements()) {
                imagePair = imagePairEnum.nextImagePair();
                img = (JMLObjectSet) imagePair.value;
                Enumeration imgEnum = img.elements();
                Object o = imgEnum.nextElement();
                if (o == null) {
                    img = new JMLObjectSet(null);
                } else {
                    Object rv = (Object) o;
                    img = new JMLObjectSet(rv);
                }
                newImagePairSet = newImagePairSet.insert(new JMLValueValuePair(imagePair.key, img));
            }
        }
        return new JMLValueToObjectMap(newImagePairSet, newDom, newSize);
    }
}
