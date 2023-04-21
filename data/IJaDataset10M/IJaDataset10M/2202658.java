package iaik.pkcs.pkcs11;

import iaik.pkcs.pkcs11.wrapper.CK_VERSION;

/**
 * Objects of this class represent a version. This consists of a major and a
 * minor version number.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 1.0
 * @invariants
 */
public class Version implements Cloneable {

    /**
   * The major version number.
   */
    protected byte major_;

    /**
   * The minor version number.
   */
    protected byte minor_;

    /**
   * Constructor for internal use only.
   *
   * @preconditions
   * @postconditions
   */
    protected Version() {
    }

    /**
   * Constructor taking a CK_VERSION object.
   *
   * @param ckVersion A CK_VERSION object.
   * @preconditions (ckVersion <> null)
   * @postconditions
   */
    protected Version(CK_VERSION ckVersion) {
        if (ckVersion == null) {
            throw new NullPointerException("Argument \"ckVersion\" must not be null.");
        }
        major_ = ckVersion.major;
        minor_ = ckVersion.minor;
    }

    /**
   * Create a (deep) clone of this object.
   *
   * @return A clone of this object.
   * @preconditions
   * @postconditions (result <> null)
   *                 and (result instanceof Version)
   *                 and (result.equals(this))
   */
    public java.lang.Object clone() {
        Version clone;
        try {
            clone = (Version) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new TokenRuntimeException("An unexpected clone exception occurred.", ex);
        }
        return clone;
    }

    /**
   * Get the major version number.
   *
   * @return The major version number.
   * @preconditions
   * @postconditions
   */
    public byte getMajor() {
        return major_;
    }

    /**
   * Get the minor version number.
   *
   * @return The minor version number.
   * @preconditions
   * @postconditions
   */
    public byte getMinor() {
        return minor_;
    }

    /**
   * Returns the string representation of this object.
   *
   * @return the string representation of this object
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(major_ & 0xff);
        buffer.append('.');
        if (minor_ < 10) {
            buffer.append('0');
        }
        buffer.append(minor_ & 0xff);
        return buffer.toString();
    }

    /**
   * Compares major and minor version number of this objects with the other
   * object. Returns only true, if both are equal in both objects.
   *
   * @param otherObject The other Version object.
   * @return True, if other is an instance of Info and all member variables of
   *         both objects are equal. False, otherwise.
   * @preconditions
   * @postconditions
   */
    public boolean equals(java.lang.Object otherObject) {
        boolean equal = false;
        if (otherObject instanceof Version) {
            Version other = (Version) otherObject;
            equal = (this == other) || ((this.major_ == other.major_) && (this.minor_ == other.minor_));
        }
        return equal;
    }

    /**
   * The overriding of this method should ensure that the objects of this class
   * work correctly in a hashtable.
   *
   * @return The hash code of this object. Gained from the slotID_, state_ and
   *         deviceError_.
   * @preconditions
   * @postconditions
   */
    public int hashCode() {
        return major_ ^ minor_;
    }
}
