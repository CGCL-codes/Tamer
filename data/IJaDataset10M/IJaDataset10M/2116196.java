package org.ejbca.cvc;

import java.io.IOException;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;

/**
 * Represents Object Identifier. Parsing/encoding routines modified to
 * use the BC routines rather than manual ones (which were buggy).
 * 
 * @author Keijo Kurkinen, Swedish National Police Board
 *
 */
public class OIDField extends AbstractDataField {

    private static final long serialVersionUID = 5212215839749666908L;

    private String id;

    OIDField() {
        super(CVCTagEnum.OID);
    }

    /**
    * Constructs a new instance from a String (the oid value)
    * @param id
    */
    OIDField(String id) {
        this();
        this.id = id;
    }

    /**
    * Constructs a new instance by parsing DER-encoded data
    * @param data
    */
    OIDField(byte[] data) {
        this();
        this.id = DERObjectIdentifier.getInstance(new DEROctetString(data)).getId();
    }

    public String getValue() {
        return id;
    }

    @Override
    protected byte[] getEncoded() {
        DERObjectIdentifier o = new DERObjectIdentifier(id);
        try {
            byte[] a = o.getEncoded();
            byte[] b = new byte[a.length - 2];
            System.arraycopy(a, 2, b, 0, b.length);
            return b;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    @Override
    protected String valueAsText() {
        return id;
    }

    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof OIDField) {
            return id.equals(((OIDField) other).getValue());
        } else {
            return false;
        }
    }
}
