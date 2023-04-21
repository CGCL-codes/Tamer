package org.apache.geronimo.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public abstract class ASN1Set extends DERObject {

    protected Vector<DEREncodable> set = new Vector<DEREncodable>();

    /**
	 * return an ASN1Set from the given object.
	 * 
	 * @param obj
	 *            the object we want converted.
	 * @exception IllegalArgumentException
	 *                if the object cannot be converted.
	 */
    public static ASN1Set getInstance(Object obj) {
        if (obj == null || obj instanceof ASN1Set) {
            return (ASN1Set) obj;
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }

    /**
	 * Return an ASN1 set from a tagged object. There is a special case here, if
	 * an object appears to have been explicitly tagged on reading but we were
	 * expecting it to be implictly tagged in the normal course of events it
	 * indicates that we lost the surrounding set - so we need to add it back
	 * (this will happen if the tagged object is a sequence that contains other
	 * sequences). If you are dealing with implicitly tagged sets you really
	 * <b>should</b> be using this method.
	 * 
	 * @param obj
	 *            the tagged object.
	 * @param explicit
	 *            true if the object is meant to be explicitly tagged false
	 *            otherwise.
	 * @exception IllegalArgumentException
	 *                if the tagged object cannot be converted.
	 */
    public static ASN1Set getInstance(ASN1TaggedObject obj, boolean explicit) {
        if (explicit) {
            if (!obj.isExplicit()) {
                throw new IllegalArgumentException("object implicit - explicit expected.");
            }
            return (ASN1Set) obj.getObject();
        } else {
            if (obj.isExplicit()) {
                ASN1Set set = new DERSet(obj.getObject());
                return set;
            } else {
                if (obj.getObject() instanceof ASN1Set) {
                    return (ASN1Set) obj.getObject();
                }
                ASN1EncodableVector v = new ASN1EncodableVector();
                if (obj.getObject() instanceof ASN1Sequence) {
                    ASN1Sequence s = (ASN1Sequence) obj.getObject();
                    Iterator<DEREncodable> e = s.getObjects();
                    while (e.hasNext()) {
                        v.add((DEREncodable) e.next());
                    }
                    return new DERSet(v, false);
                }
            }
        }
        throw new IllegalArgumentException("unknown object in getInstanceFromTagged");
    }

    public ASN1Set() {
    }

    public Iterator<DEREncodable> getObjects() {
        return set.iterator();
    }

    /**
	 * return the object at the set postion indicated by index.
	 * 
	 * @param index
	 *            the set number (starting at zero) of the object
	 * @return the object at the set postion indicated by index.
	 */
    public DEREncodable getObjectAt(int index) {
        return (DEREncodable) set.elementAt(index);
    }

    /**
	 * return the number of objects in this set.
	 * 
	 * @return the number of objects in this set.
	 */
    public int size() {
        return set.size();
    }

    public int hashCode() {
        Iterator<DEREncodable> e = this.getObjects();
        int hashCode = 0;
        while (e.hasNext()) {
            hashCode ^= e.next().hashCode();
        }
        return hashCode;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ASN1Set)) {
            return false;
        }
        ASN1Set other = (ASN1Set) o;
        if (this.size() != other.size()) {
            return false;
        }
        Iterator<DEREncodable> s1 = this.getObjects();
        Iterator<DEREncodable> s2 = other.getObjects();
        while (s1.hasNext()) {
            if (!s1.next().equals(s2.next())) {
                return false;
            }
        }
        return true;
    }

    /**
	 * return true if a <= b (arrays are assumed padded with zeros).
	 */
    private boolean lessThanOrEqual(byte[] a, byte[] b) {
        if (a.length <= b.length) {
            for (int i = 0; i != a.length; i++) {
                int l = a[i] & 0xff;
                int r = b[i] & 0xff;
                if (r > l) {
                    return true;
                } else if (l > r) {
                    return false;
                }
            }
            return true;
        } else {
            for (int i = 0; i != b.length; i++) {
                int l = a[i] & 0xff;
                int r = b[i] & 0xff;
                if (r > l) {
                    return true;
                } else if (l > r) {
                    return false;
                }
            }
            return false;
        }
    }

    private byte[] getEncoded(DEREncodable obj) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ASN1OutputStream aOut = new ASN1OutputStream(bOut);
        try {
            aOut.writeObject(obj);
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot encode object added to SET", e);
        }
        return bOut.toByteArray();
    }

    protected void sort() {
        if (set.size() > 1) {
            boolean swapped = true;
            while (swapped) {
                int index = 0;
                byte[] a = getEncoded((DEREncodable) set.elementAt(0));
                swapped = false;
                while (index != set.size() - 1) {
                    byte[] b = getEncoded((DEREncodable) set.elementAt(index + 1));
                    if (lessThanOrEqual(a, b)) {
                        a = b;
                    } else {
                        DEREncodable o = set.elementAt(index);
                        set.setElementAt(set.elementAt(index + 1), index);
                        set.setElementAt(o, index + 1);
                        swapped = true;
                    }
                    index++;
                }
            }
        }
    }

    protected void addObject(DEREncodable obj) {
        set.addElement(obj);
    }

    abstract void encode(DEROutputStream out) throws IOException;
}
