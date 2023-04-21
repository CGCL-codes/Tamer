package iaik.pkcs.pkcs11.objects;

import iaik.pkcs.pkcs11.Mechanism;
import iaik.pkcs.pkcs11.wrapper.Constants;
import iaik.pkcs.pkcs11.wrapper.Functions;

/**
 * Objects of this class represent a mechanism array attribute of a PKCS#11 object
 * as specified by PKCS#11. This attribute is available since
 * cryptoki version 2.20.
 *
 * @author <a href="mailto:Birgit.Haas@iaik.tugraz.at"> Birgit Haas </a>
 * @version 1.0
 * @invariants
 */
public class MechanismArrayAttribute extends Attribute {

    /**
   * Default constructor - only for internal use.
   */
    MechanismArrayAttribute() {
        super();
    }

    /**
   * Constructor taking the PKCS#11 type of the attribute.
   *
   * @param type The PKCS#11 type of this attribute; e.g.
   *             PKCS11Constants.CKA_VALUE.
   * @preconditions (type <> null)
   * @postconditions
   */
    public MechanismArrayAttribute(Long type) {
        super(type);
    }

    /**
   * Set the attributes of this mechanism attribute array by
   * specifying a Mechanism[].
   * Null, is also valid.
   * A call to this method sets the present flag to true.
   *
   * @param value The MechanismArrayAttribute value to set. May be null.
   * @preconditions
   * @postconditions
   */
    public void setMechanismAttributeArrayValue(Mechanism[] value) {
        long[] values = null;
        if (value != null) {
            values = new long[value.length];
            for (int i = 0; i < value.length; i++) {
                values[i] = value[i].getMechanismCode();
            }
        }
        ckAttribute_.pValue = values;
        present_ = true;
    }

    /**
   * Get the mechanism attribute array value of this attribute as Mechanism[].
   * Null, is also possible.
   *
   * @return The mechanism attribute array value of this attribute or null.
   * @preconditions
   * @postconditions
   */
    public Mechanism[] getMechanismAttributeArrayValue() {
        Mechanism[] mechanisms = null;
        if (ckAttribute_.pValue != null) {
            long[] values = (long[]) ckAttribute_.pValue;
            mechanisms = new Mechanism[values.length];
            for (int i = 0; i < values.length; i++) {
                Mechanism mechanism = new Mechanism(values[i]);
                mechanisms[i] = mechanism;
            }
        }
        return mechanisms;
    }

    /**
   * Get a string representation of the value of this attribute.
   *
   * @return A string representation of the value of this attribute.
   * @preconditions
   * @postconditions (result <> null)
   */
    protected String getValueString() {
        StringBuffer buffer = new StringBuffer(1024);
        Mechanism[] allowedMechanisms = getMechanismAttributeArrayValue();
        for (int i = 0; i < allowedMechanisms.length; i++) {
            buffer.append(Constants.NEWLINE);
            buffer.append(Constants.INDENT + Constants.INDENT + Constants.INDENT);
            buffer.append(allowedMechanisms[i].getName());
        }
        return buffer.toString();
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
        if (otherObject instanceof MechanismArrayAttribute) {
            MechanismArrayAttribute other = (MechanismArrayAttribute) otherObject;
            equal = (this == other) || (((this.present_ == false) && (other.present_ == false)) || (((this.present_ == true) && (other.present_ == true)) && ((this.sensitive_ == other.sensitive_) && Functions.equals((long[]) this.ckAttribute_.pValue, (long[]) other.ckAttribute_.pValue))));
        }
        return equal;
    }

    /**
   * The overriding of this method should ensure that the objects of this class
   * work correctly in a hashtable.
   *
   * @return The hash code of this object.
   * @preconditions
   * @postconditions
   */
    public int hashCode() {
        return (ckAttribute_.pValue != null) ? Functions.hashCode((long[]) ckAttribute_.pValue) : 0;
    }

    /**
   * Create a (deep) clone of this object.
   * The attributes in the CK_ATTRIBUTE[] need not be cloned, as they can't be set
   * separately.
   *
   * @return A clone of this object.
   * @preconditions
   * @postconditions (result <> null)
   *                 and (result instanceof MechanismAttributeArray)
   *                 and (result.equals(this))
   */
    public java.lang.Object clone() {
        MechanismArrayAttribute clone;
        clone = (MechanismArrayAttribute) super.clone();
        clone.ckAttribute_.pValue = ((long[]) this.ckAttribute_.pValue).clone();
        return clone;
    }
}
