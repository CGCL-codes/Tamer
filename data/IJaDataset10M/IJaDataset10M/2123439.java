package iaik.pkcs.pkcs11.parameters;

import iaik.pkcs.pkcs11.Mechanism;
import iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS;
import iaik.pkcs.pkcs11.wrapper.Constants;
import iaik.pkcs.pkcs11.wrapper.Functions;
import iaik.pkcs.pkcs11.wrapper.PKCS11Constants;

/**
 * This class encapsulates parameters for the Mechanism.RSA_PKCS_OAEP.
 *
 * @author Karl Scheibelhofer
 * @version 1.0
 * @invariants (source_ == SourceType.Empty)
 *             or (source_ == SourceType.DataSpecified)
 */
public class RSAPkcsOaepParameters extends RSAPkcsParameters {

    /**
   * This interface defines the available source types as defined by
   * PKCS#11: CKZ_DATA_SPECIFIED.
   *
   * @author Karl Scheibelhofer
   * @version 1.0
   * @invariants
   */
    public interface SourceType {

        /**
     * The indentifier for empty parameter. This is not defined explicitely in
     * the PKCS#11 v2.11 standard but in the text.
     */
        public static final long EMPTY = 0L;

        /**
     * The indentifier for CKZ_DATA_SPECIFIED.
     */
        public static final long DATA_SPECIFIED = PKCS11Constants.CKZ_DATA_SPECIFIED;
    }

    /**
   * The source of the encoding parameter.
   */
    protected long source_;

    /**
   * The data used as the input for the encoding parameter source.
   */
    protected byte[] sourceData_;

    /**
   * Create a new RSAPkcsOaepParameters object with the given attributes.
   *
   * @param hashAlgorithm The message digest algorithm used to calculate the
   *                      digest of the encoding parameter.
   * @param maskGenerationFunction The mask to apply to the encoded block. One
   *                               of the constants defined in the
   *                               MessageGenerationFunctionType interface.
   * @param source The source of the encoding parameter. One of the constants
   *               defined in the SourceType interface.
   * @param sourceData The data used as the input for the encoding parameter
   *                   source.
   * @preconditions (hashAlgorithm <> null)
   *                and (maskGenerationFunction == MessageGenerationFunctionType.Sha1)
   *                and ((source == SourceType.Empty)
   *                     or (source == SourceType.DataSpecified))
   * @postconditions
   */
    public RSAPkcsOaepParameters(Mechanism hashAlgorithm, long maskGenerationFunction, long source, byte[] sourceData) {
        super(hashAlgorithm, maskGenerationFunction);
        if ((source != SourceType.EMPTY) && (source != SourceType.DATA_SPECIFIED)) {
            throw new IllegalArgumentException("Illegal value for argument\"source\": " + Functions.toHexString(source));
        }
        source_ = source;
        sourceData_ = sourceData;
    }

    /**
   * Create a (deep) clone of this object.
   *
   * @return A clone of this object.
   * @preconditions
   * @postconditions (result <> null)
   *                 and (result instanceof RSAPkcsOaepParameters)
   *                 and (result.equals(this))
   */
    public java.lang.Object clone() {
        RSAPkcsOaepParameters clone = (RSAPkcsOaepParameters) super.clone();
        clone.sourceData_ = (byte[]) this.sourceData_.clone();
        return clone;
    }

    /**
   * Get this parameters object as an object of the CK_RSA_PKCS_OAEP_PARAMS
   * class.
   *
   * @return This object as a CK_RSA_PKCS_OAEP_PARAMS object.
   * @preconditions
   * @postconditions (result <> null)
   */
    public Object getPKCS11ParamsObject() {
        CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();
        params.hashAlg = hashAlgorithm_.getMechanismCode();
        params.mgf = maskGenerationFunction_;
        params.source = source_;
        params.pSourceData = sourceData_;
        return params;
    }

    /**
   * Get the source of the encoding parameter.
   *
   * @return The source of the encoding parameter.
   * @preconditions
   * @postconditions
   */
    public long getSource() {
        return source_;
    }

    /**
   * Get the data used as the input for the encoding parameter source.
   *
   * @return The data used as the input for the encoding parameter source.
   * @preconditions
   * @postconditions
   */
    public byte[] getSourceData() {
        return sourceData_;
    }

    /**
   * Set the source of the encoding parameter. One of the constants defined in
   * the SourceType interface.
   *
   * @param source The source of the encoding parameter.
   * @preconditions ((source == SourceType.Empty)
   *                 or (source == SourceType.DataSpecified))
   * @postconditions
   */
    public void setSource(long source) {
        if ((source != SourceType.EMPTY) && (source != SourceType.DATA_SPECIFIED)) {
            throw new IllegalArgumentException("Illegal value for argument\"source\": " + Functions.toHexString(source));
        }
        source_ = source;
    }

    /**
   * Set the data used as the input for the encoding parameter source.
   *
   * @param sourceData The data used as the input for the encoding parameter
   *                   source.
   * @preconditions
   * @postconditions
   */
    public void setSourceData(byte[] sourceData) {
        sourceData_ = sourceData;
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
        buffer.append("Source: ");
        if (source_ == SourceType.EMPTY) {
            buffer.append("Empty");
        } else if (source_ == SourceType.DATA_SPECIFIED) {
            buffer.append("Data Specified");
        } else {
            buffer.append("<unknown>");
        }
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("Source Data (hex): ");
        buffer.append(Functions.toHexString(sourceData_));
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
        if (otherObject instanceof RSAPkcsOaepParameters) {
            RSAPkcsOaepParameters other = (RSAPkcsOaepParameters) otherObject;
            equal = (this == other) || (super.equals(other) && (this.source_ == other.source_) && Functions.equals(this.sourceData_, other.sourceData_));
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
        return super.hashCode() ^ ((int) source_) ^ Functions.hashCode(sourceData_);
    }
}
