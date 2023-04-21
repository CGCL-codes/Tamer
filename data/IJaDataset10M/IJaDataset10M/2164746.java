package codec.asn1;

/**
 * This class represents an ASN.1 T61String as described
 * in ITU-T Recommendation X.680. Note that no value checking
 * is performed!
 *
 * @author Markus Tak (by cut-and-paste ;-) )
 * @author Stefan Endler
 * @version "$Id: ASN1VisibleString.java,v 1.3 2007/08/11 18:21:50 sendler Exp $"
 */
public class ASN1VisibleString extends ASN1AbstractString {

    public ASN1VisibleString() {
        super();
    }

    /**
 * Creates an instance with the given string value.
 * No constraints can be set yet so none are checked.
 *
 * @param The string value.
  */
    public ASN1VisibleString(String s) {
        super(s);
    }

    public int getTag() {
        return ASN1.TAG_VISIBLESTRING;
    }
}
