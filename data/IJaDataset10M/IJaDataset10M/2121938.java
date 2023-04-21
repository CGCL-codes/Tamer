package codec.asn1;

import java.io.IOException;

/**
 * Represents the ASN.1 NULL type.
 *
 * @author Volker Roth
 * @author Stefan Endler
 * @version "$Id: ASN1Null.java,v 1.3 2007/08/11 18:21:50 sendler Exp $"
 */
public class ASN1Null extends ASN1AbstractType {

    public Object getValue() {
        return null;
    }

    public int getTag() {
        return ASN1.TAG_NULL;
    }

    public void encode(Encoder enc) throws ASN1Exception, IOException {
        enc.writeNull(this);
    }

    public void decode(Decoder dec) throws ASN1Exception, IOException {
        dec.readNull(this);
    }

    public String toString() {
        return "NULL";
    }
}
