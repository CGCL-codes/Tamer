package org.tripcom.query.client.RDF.types;

import org.tripcom.query.client.RDF.Resource;
import org.tripcom.query.client.RDF.ns.XSD;

/**
 * Representation of a normalized string
 */
public class NormalizedStringLiteral extends org.tripcom.query.client.RDF.types.StringLiteral {

    static final long serialVersionUID = -1985474533991561295L;

    public static String m_sTYPE_URI = XSD.NORMALIZEDSTRING;

    /**
     * Class constructor
     */
    public NormalizedStringLiteral(String sValue) {
        super(sValue, XSD._NORMALIZEDSTRING);
    }

    NormalizedStringLiteral(String value, Object valueObject) {
        this(value);
        setValueObject(valueObject);
    }

    /**
     * @param value
     * @param datatype
     */
    public NormalizedStringLiteral(String value, Resource datatype) {
        super(value, datatype);
    }

    protected Object createValueObject() throws TypeException {
        StringBuffer sb = new StringBuffer(getLabel());
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == 9 || sb.charAt(i) == 10 || sb.charAt(i) == 13 || sb.charAt(i) == '\f') {
                sb.setCharAt(i, ' ');
            }
        }
        return sb.toString();
    }
}
