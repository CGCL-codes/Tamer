package issrg.ac;

import iaik.asn1.*;
import iaik.asn1.structures.*;

/**
 * This is a default AttributeValue object used in the Attribute construct. You 
 * can derive from this
 * class and register your own AttributeValue decoders using 
 * Attribute.registerAttribute method, so they will be used 
 * automatically when
 * an AC is decoded.
 *
 * <p>When you create a subclass, don't forget to override isDecoded method, or 
 * derive your class
 * from the DistinctAttribute class (which simply overrides that method to 
 * return true). Alternatively,
 * your code can go through the vector of attribute values and replace the 
 * default AttributeValue objects
 * with descendants of this class that are aware of their semantics (and can 
 * represent the ASN1Object
 * of the value in a programmer-friendly way).
 *
 * @see Attribute#registerAttribute
 */
public class AttributeValue implements ASN1Type, Cloneable {

    /**
   * This is the data as an ASN1Object that represents the value of the 
   * attribute.
   */
    protected ASN1Object data = null;

    /**
   * Returns the raw ASN1Object of the attribute, if it has been taken from 
   * the BER-encoded AC. You
   * don't need to set it, so there is no method for that.
   *
   * <p>This value may not be meaningful in some subclasses of AttributeValue.
   */
    public ASN1Object getRawAttribute() {
        return data;
    }

    /**
   * This method tells whether the value has been truly decoded, or it is some
   * kind of placeholder and the actual value must be decoded by other means.
   * 
   * <p>This method should be overridden by the user-defined classes to return 
   * true, so when the
   * content of the set of attribute values is examined, the code would know if 
   * the class is aware
   * of its semantics.
   *
   * @return true, if the value has been decoded and semantically evaluated by
   *   the subclass; false otherwise; by default returns false, so override 
   *   this method
   */
    public boolean isDecoded() {
        return false;
    }

    protected AttributeValue() {
    }

    /**
   * This constructor builds an AttributeValue from ASN1Object of the single
   * value.
   *
   * @param data - the ASN1Object of this value
   */
    public AttributeValue(ASN1Object data) throws CodingException {
        decode(data);
    }

    /**
   * This method copies the given AttributeValue.
   *
   * @param av - the AttributeValue to copy
   */
    public AttributeValue(AttributeValue av) throws CodingException {
        data = iaik.asn1.DerCoder.decode(iaik.asn1.DerCoder.encode(av.data));
    }

    /**
   * This method returns the ASN1Object of this value.
   */
    public ASN1Object toASN1Object() throws CodingException {
        return data;
    }

    /**
   * This method decodes the single value of an Attribute. By default simply
   * keeps the raw encoding, so you need to override this method in subclasses.
   *
   * @param ao - the ASN1Object of the single Attribute value
   */
    public void decode(ASN1Object ao) throws CodingException {
        data = ao;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String ident) {
        return data.toString();
    }

    public Object clone() {
        try {
            return new AttributeValue(this);
        } catch (CodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
