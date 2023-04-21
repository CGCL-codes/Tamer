package org.tripcom.query.client.RDF.types;

import java.util.Date;
import org.tripcom.query.client.RDF.ns.RDF;

/**
 * Representation of an XMLLiteral
 */
public class XMLLiteral extends org.tripcom.query.client.RDF.Literal {

    static final long serialVersionUID = -8702536956200993398L;

    public static String m_sTYPE_URI = RDF.XMLLITERAL;

    /**
     * Class constructor
     */
    public XMLLiteral(String sValue) {
        super(sValue, RDF._XMLLITERAL);
    }

    XMLLiteral(String string, Object valueObject) {
        this(string);
        setValueObject(valueObject);
    }
}
