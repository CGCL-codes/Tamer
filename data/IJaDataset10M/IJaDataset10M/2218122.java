package codec.examples.optionalFields;

import codec.asn1.ASN1Exception;
import codec.asn1.ASN1IA5String;
import codec.asn1.ASN1Integer;
import codec.asn1.ASN1Sequence;
import codec.asn1.DERDecoder;
import codec.asn1.DEREncoder;
import codec.asn1.Encoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class shows a sample implementation of an ASN.1 SEQUENCE type with an
 * OPTIONAL component.
 * <PRE>Person := SEQUENCE {
 *      age    INTEGER OPTIONAL,
 *     name    IA5String }</PRE>
 *
 * @author Alberto Sierra
 * @version "$Id: Person.java,v 1.3 2004/08/05 13:21:04 pebinger Exp $"
 */
public class Person extends ASN1Sequence {

    /**
     * DOCUMENT ME!
     */
    private ASN1Integer age_ = null;

    /**
     * DOCUMENT ME!
     */
    private ASN1IA5String name_ = null;

    /**
     * Constructor for encoding setting a value for the optional field.
     *
     * @param age DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public Person(int age, String name) {
        super(2);
        age_ = new ASN1Integer(age);
        name_ = new ASN1IA5String(name);
    }

    /**
     * Constructor for encoding leaving optional field empty.
     *
     * @param name DOCUMENT ME!
     */
    public Person(String name) {
        super(1);
        name_ = new ASN1IA5String(name);
    }

    /**
     * Constructor for decoding.
     */
    public Person() {
        super(2);
        age_ = new ASN1Integer();
        age_.setOptional(true);
        name_ = new ASN1IA5String();
        add(age_);
        add(name_);
    }

    /**
     * Add only the components that will be encoded to class.
     */
    protected void map() {
        clear();
        if (age_ != null) {
            add(age_);
        }
        add(name_);
    }

    /**
     * Override the encode(Encoder) method so that map() is called before
     * each call of this method.
     *
     * @param enc DOCUMENT ME!
     *
     * @throws ASN1Exception DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void encode(Encoder enc) throws ASN1Exception, IOException {
        map();
        super.encode(enc);
    }

    /**
     * Returns a byte array representing an encoded instance of this class.
     *
     * @return DOCUMENT ME!
     *
     * @throws ASN1Exception DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public byte[] getEncoded() throws ASN1Exception, IOException {
        ByteArrayOutputStream out;
        DEREncoder encoder;
        byte[] encodedAsn1Object;
        out = new ByteArrayOutputStream();
        encoder = new DEREncoder(out);
        this.encode(encoder);
        encodedAsn1Object = out.toByteArray();
        encoder.close();
        return encodedAsn1Object;
    }

    /**
     * Decodes the byte array passed as argument. The decoded values are
     * stored in the member variables of this class that represent the
     * components of the corresponding ASN.1 type.
     *
     * @param encodedData DOCUMENT ME!
     *
     * @throws ASN1Exception DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void decode(byte[] encodedData) throws ASN1Exception, IOException {
        ByteArrayInputStream in;
        DERDecoder decoder;
        in = new ByteArrayInputStream(encodedData);
        decoder = new DERDecoder(in);
        this.decode(decoder);
        decoder.close();
    }

    /**
     * Set and get methods.
     *
     * @param age DOCUMENT ME!
     */
    public void setAge(int age) {
        age_ = new ASN1Integer(age);
        age_.setOptional(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAge() {
        return age_.getBigInteger().intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        name_ = new ASN1IA5String(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name_.getString();
    }

    public void removeAge() {
        age_ = null;
    }

    /**
     * Override the toString() method so that map() is called before each
     * call of this method.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        map();
        return super.toString();
    }
}
