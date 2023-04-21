package com.novosec.pkix.asn1.crmf;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;

/**
 * ASN.1 structure DER En/DeCoder.
 *
 * <pre>
 *      AttributeTypeAndValue ::= SEQUENCE {
 *                            type OBJECT IDENTIFIER,
 *                            value ANY DEFINED BY type }
 * </pre>
 */
public class AttributeTypeAndValue implements DEREncodable {

    private DERObjectIdentifier type;

    private DEREncodable value;

    public static AttributeTypeAndValue getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static AttributeTypeAndValue getInstance(Object obj) {
        if (obj instanceof AttributeTypeAndValue) {
            return (AttributeTypeAndValue) obj;
        }
        if (obj instanceof DERObjectIdentifier) {
            return new AttributeTypeAndValue((DERObjectIdentifier) obj);
        }
        if (obj instanceof String) {
            return new AttributeTypeAndValue((String) obj);
        }
        if (obj instanceof ASN1Sequence) {
            return new AttributeTypeAndValue((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory");
    }

    public AttributeTypeAndValue(DERObjectIdentifier type) {
        this.type = type;
    }

    public AttributeTypeAndValue(String type) {
        this.type = new DERObjectIdentifier(type);
    }

    public AttributeTypeAndValue(DERObjectIdentifier type, DEREncodable value) {
        this.type = type;
        this.value = value;
    }

    public AttributeTypeAndValue(ASN1Sequence seq) {
        type = (DERObjectIdentifier) seq.getObjectAt(0);
        value = seq.getObjectAt(1);
    }

    public DERObjectIdentifier getObjectId() {
        return type;
    }

    public DEREncodable getParameters() {
        return value;
    }

    public DERObject getDERObject() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(type);
        v.add(value);
        return new DERSequence(v);
    }

    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof AttributeTypeAndValue)) {
            return false;
        }
        AttributeTypeAndValue other = (AttributeTypeAndValue) o;
        if (!this.getObjectId().equals(other.getObjectId())) {
            return false;
        }
        if (this.getParameters() == null && other.getParameters() == null) {
            return true;
        }
        if (this.getParameters() == null || other.getParameters() == null) {
            return false;
        }
        ByteArrayOutputStream b1Out = new ByteArrayOutputStream();
        ByteArrayOutputStream b2Out = new ByteArrayOutputStream();
        DEROutputStream d1Out = new DEROutputStream(b1Out);
        DEROutputStream d2Out = new DEROutputStream(b2Out);
        try {
            d1Out.writeObject(this.getParameters());
            d2Out.writeObject(other.getParameters());
            byte[] b1 = b1Out.toByteArray();
            byte[] b2 = b2Out.toByteArray();
            if (b1.length != b2.length) {
                return false;
            }
            for (int i = 0; i != b1.length; i++) {
                if (b1[i] != b2[i]) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
