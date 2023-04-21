package iaik.pkcs.pkcs11.parameters;

import iaik.pkcs.pkcs11.wrapper.CK_X9_42_DH1_DERIVE_PARAMS;
import iaik.pkcs.pkcs11.wrapper.Constants;
import iaik.pkcs.pkcs11.wrapper.Functions;

/**
 * This abstract class encapsulates parameters for the X9.42 DH mechanism
 * Mechanism.X9_42_DH_DERIVE.
 *
 * @author Karl Scheibelhofer
 * @version 1.0
 * @invariants
 */
public class X942DH1KeyDerivationParameters extends DHKeyDerivationParameters {

    /**
   * The data shared between the two parties.
   */
    protected byte[] otherInfo_;

    /**
   * Create a new X942DH1KeyDerivationParameters object with the given attributes.
   *
   * @param keyDerivationFunction The key derivation function used on the shared
   *                              secret value.
   *                              One of the values defined in
   *                              KeyDerivationFunctionType.
   * @param otherInfo The data shared between the two parties.
   * @param publicData The other partie's public key value.
   * @preconditions ((keyDerivationFunction == KeyDerivationFunctionType.NULL)
   *                 or (keyDerivationFunction == KeyDerivationFunctionType.SHA1_KDF)
   *                 or (keyDerivationFunction == KeyDerivationFunctionType.SHA1_KDF_ASN1)
   *                 or (keyDerivationFunction == KeyDerivationFunctionType.SHA1_KDF_CONCATENATE))
   *                and (publicData <> null)
   * @postconditions
   */
    public X942DH1KeyDerivationParameters(long keyDerivationFunction, byte[] otherInfo, byte[] publicData) {
        super(keyDerivationFunction, publicData);
        otherInfo_ = otherInfo;
    }

    /**
   * Create a (deep) clone of this object.
   *
   * @return A clone of this object.
   * @preconditions
   * @postconditions (result <> null)
   *                 and (result instanceof X942DH1KeyDerivationParameters)
   *                 and (result.equals(this))
   */
    public java.lang.Object clone() {
        X942DH1KeyDerivationParameters clone = (X942DH1KeyDerivationParameters) super.clone();
        clone.otherInfo_ = (byte[]) this.otherInfo_.clone();
        return clone;
    }

    /**
   * Get this parameters object as an object of the CK_X9_42_DH1_DERIVE_PARAMS
   * class.
   *
   * @return This object as a CK_X9_42_DH1_DERIVE_PARAMS object.
   * @preconditions
   * @postconditions (result <> null)
   */
    public Object getPKCS11ParamsObject() {
        CK_X9_42_DH1_DERIVE_PARAMS params = new CK_X9_42_DH1_DERIVE_PARAMS();
        params.kdf = keyDerivationFunction_;
        params.pOtherInfo = otherInfo_;
        params.pPublicData = publicData_;
        return params;
    }

    /**
   * Get the data shared between the two parties.
   *
   * @return The data shared between the two parties.
   * @preconditions
   * @postconditions
   */
    public byte[] getOtherInfo() {
        return otherInfo_;
    }

    /**
   * Set the data shared between the two parties.
   *
   * @param otherInfo The data shared between the two parties.
   * @preconditions (otherInfo <> null)
   * @postconditions
   */
    public void setOtherInfo(byte[] otherInfo) {
        otherInfo_ = otherInfo;
    }

    /**
   * Returns the string representation of this object. Do not parse data from
   * this string, it is for debugging only.
   *
   * @return A string representation of this object.
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("Other Info: ");
        buffer.append(Functions.toHexString(otherInfo_));
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
        if (otherObject instanceof X942DH1KeyDerivationParameters) {
            X942DH1KeyDerivationParameters other = (X942DH1KeyDerivationParameters) otherObject;
            equal = (this == other) || (super.equals(other) && Functions.equals(this.otherInfo_, other.otherInfo_));
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
        return super.hashCode() ^ Functions.hashCode(otherInfo_);
    }
}
