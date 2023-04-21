package codec.asn1;

/**
 * Represents an ASN.1 SEQUENCE type as specified in ITU-T
 * Recommendation X.680.
 *
 * @author Volker Roth
 * @author Stefan Endler
 * @version "$Id: ASN1Sequence.java,v 1.3 2007/08/11 18:21:50 sendler Exp $"
 */
public class ASN1Sequence extends ASN1AbstractCollection {

    public ASN1Sequence() {
        super();
    }

    /**
      * Creates an instance with the given capacity.
      *
      * @param capacity The capacity.
      */
    public ASN1Sequence(int capacity) {
        super(capacity);
    }

    /**
      * Returns the {@link ASN1#TAG_SEQUENCE SEQUENCE} tag.
      *
      * @return The {@link ASN1#TAG_SEQUENCE SEQUENCE} tag.
      */
    public int getTag() {
        return ASN1.TAG_SEQUENCE;
    }
}
