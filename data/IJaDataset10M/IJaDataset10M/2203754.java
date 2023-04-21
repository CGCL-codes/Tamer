package iaik.pkcs.pkcs11.objects;

import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.wrapper.Constants;

/**
 * Objects of this class represent DSA private keys as specified by PKCS#11
 * v2.11.
 *
 * @author Karl Scheibelhofer
 * @version 1.0
 * @invariants (prime_ <> null)
 *             and (subprime_ <> null)
 *             and (base_ <> null)
 *             and (value_ <> null)
 */
public class DSAPrivateKey extends PrivateKey {

    /**
   * The prime (p) of this DSA key.
   */
    protected ByteArrayAttribute prime_;

    /**
   * The subprime (q) of this DSA key.
   */
    protected ByteArrayAttribute subprime_;

    /**
   * The base (g) of this DSA key.
   */
    protected ByteArrayAttribute base_;

    /**
   * The private value (x) of this DSA key.
   */
    protected ByteArrayAttribute value_;

    /**
   * Deafult Constructor.
   *
   * @preconditions
   * @postconditions
   */
    public DSAPrivateKey() {
        super();
        keyType_.setLongValue(KeyType.DSA);
    }

    /**
   * Called by getInstance to create an instance of a PKCS#11 DSA private key.
   *
   * @param session The session to use for reading attributes.
   *                This session must have the appropriate rights; i.e.
   *                it must be a user-session, if it is a private object.
   * @param objectHandle The object handle as given from the PKCS#111 module.
   * @exception TokenException If getting the attributes failed.
   * @preconditions (session <> null)
   * @postconditions
   */
    protected DSAPrivateKey(Session session, long objectHandle) throws TokenException {
        super(session, objectHandle);
        keyType_.setLongValue(KeyType.DSA);
    }

    /**
   * The getInstance method of the PrivateKey class uses this method to create
   * an instance of a PKCS#11 DSA private key.
   *
   * @param session The session to use for reading attributes.
   *                This session must have the appropriate rights; i.e.
   *                it must be a user-session, if it is a private object.
   * @param objectHandle The object handle as given from the PKCS#111 module.
   * @return The object representing the PKCS#11 object.
   *         The returned object can be casted to the
   *         according sub-class.
   * @exception TokenException If getting the attributes failed.
   * @preconditions (session <> null)
   * @postconditions (result <> null) 
   */
    public static Object getInstance(Session session, long objectHandle) throws TokenException {
        return new DSAPrivateKey(session, objectHandle);
    }

    /**
   * Put all attributes of the given object into the attributes table of this
   * object. This method is only static to be able to access invoke the
   * implementation of this method for each class separately (see use in
   * clone()).
   *
   * @param object The object to handle.
   * @preconditions (object <> null)
   * @postconditions
   */
    protected static void putAttributesInTable(DSAPrivateKey object) {
        if (object == null) {
            throw new NullPointerException("Argument \"object\" must not be null.");
        }
        object.attributeTable_.put(Attribute.PRIME, object.prime_);
        object.attributeTable_.put(Attribute.SUBPRIME, object.subprime_);
        object.attributeTable_.put(Attribute.BASE, object.base_);
        object.attributeTable_.put(Attribute.VALUE, object.value_);
    }

    /**
   * Allocates the attribute objects for this class and adds them to the
   * attribute table.
   *
   * @preconditions
   * @postconditions
   */
    protected void allocateAttributes() {
        super.allocateAttributes();
        prime_ = new ByteArrayAttribute(Attribute.PRIME);
        subprime_ = new ByteArrayAttribute(Attribute.SUBPRIME);
        base_ = new ByteArrayAttribute(Attribute.BASE);
        value_ = new ByteArrayAttribute(Attribute.VALUE);
        putAttributesInTable(this);
    }

    /**
   * Create a (deep) clone of this object.
   *
   * @return A clone of this object.
   * @preconditions
   * @postconditions (result <> null)
   *                 and (result instanceof DSAPrivateKey)
   *                 and (result.equals(this))
   */
    public java.lang.Object clone() {
        DSAPrivateKey clone = (DSAPrivateKey) super.clone();
        clone.prime_ = (ByteArrayAttribute) this.prime_.clone();
        clone.subprime_ = (ByteArrayAttribute) this.subprime_.clone();
        clone.base_ = (ByteArrayAttribute) this.base_.clone();
        clone.value_ = (ByteArrayAttribute) this.value_.clone();
        putAttributesInTable(clone);
        return clone;
    }

    /**
   * Compares all member variables of this object with the other object.
   * Returns only true, if all are equal in both objects.
   *
   * @param otherObject The other object to compare to.
   * @return True, if other is an instance of this class and all member
   *         variables of both objects are equal. False, otherwise.
   * @preconditions
   * @postconditions
   */
    public boolean equals(java.lang.Object otherObject) {
        boolean equal = false;
        if (otherObject instanceof DSAPrivateKey) {
            DSAPrivateKey other = (DSAPrivateKey) otherObject;
            equal = (this == other) || (super.equals(other) && this.prime_.equals(other.prime_) && this.subprime_.equals(other.subprime_) && this.base_.equals(other.base_) && this.value_.equals(other.value_));
        }
        return equal;
    }

    /**
   * Gets the prime attribute of this DSA key.
   *
   * @return The prime attribute.
   * @preconditions
   * @postconditions (result <> null)
   */
    public ByteArrayAttribute getPrime() {
        return prime_;
    }

    /**
   * Gets the subprime attribute of this DSA key.
   *
   * @return The subprime attribute.
   * @preconditions
   * @postconditions (result <> null)
   */
    public ByteArrayAttribute getSubprime() {
        return subprime_;
    }

    /**
   * Gets the base attribute of this DSA key.
   *
   * @return The base attribute.
   * @preconditions
   * @postconditions (result <> null)
   */
    public ByteArrayAttribute getBase() {
        return base_;
    }

    /**
   * Gets the value attribute of this DSA key.
   *
   * @return The value attribute.
   * @preconditions
   * @postconditions (result <> null)
   */
    public ByteArrayAttribute getValue() {
        return value_;
    }

    /**
   * Read the values of the attributes of this object from the token.
   *
   * @param session The session handle to use for reading attributes.
   *                This session must have the appropriate rights; i.e.
   *                it must be a user-session, if it is a private object.
   * @exception TokenException If getting the attributes failed.
   * @preconditions (session <> null)
   * @postconditions
   */
    public void readAttributes(Session session) throws TokenException {
        super.readAttributes(session);
        Object.getAttributeValues(session, objectHandle_, new Attribute[] { prime_, subprime_, base_ });
        Object.getAttributeValue(session, objectHandle_, value_);
    }

    /**
   * This method returns a string representation of the current object. The
   * output is only for debugging purposes and should not be used for other
   * purposes.
   *
   * @return A string presentation of this object for debugging output.
   * @preconditions
   * @postconditions (result <> null)
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer(1024);
        buffer.append(super.toString());
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("Prime (hex): ");
        buffer.append(prime_.toString());
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("Subprime (hex): ");
        buffer.append(subprime_.toString());
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("Base (hex): ");
        buffer.append(base_.toString());
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("Value (hex): ");
        buffer.append(value_.toString());
        return buffer.toString();
    }
}
