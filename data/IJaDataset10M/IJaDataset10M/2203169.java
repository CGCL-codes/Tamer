package org.apache.ws.jaxme.generator.types;

import org.apache.ws.jaxme.generator.sg.SGFactory;
import org.apache.ws.jaxme.generator.sg.SchemaSG;
import org.apache.ws.jaxme.generator.sg.SimpleTypeSG;
import org.apache.ws.jaxme.impl.DatatypeConverterImpl;
import org.apache.ws.jaxme.js.TypedValue;
import org.apache.ws.jaxme.js.impl.TypedValueImpl;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.parser.impl.LocSAXException;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class UnsignedIntSG extends LongSG {

    /** <p>Creates a new instance of UnsignedIntSG.</p>
   */
    public UnsignedIntSG(SGFactory pFactory, SchemaSG pSchema, XSType pType) throws SAXException {
        super(pFactory, pSchema, pType);
    }

    protected boolean isUnsigned() {
        return true;
    }

    protected String getDatatypeName() {
        return "UnsignedInt";
    }

    public TypedValue getCastFromString(SimpleTypeSG pController, String pValue) throws SAXException {
        try {
            return new TypedValueImpl(new Long(new DatatypeConverterImpl().parseUnsignedInt(pValue)) + "l", LONG_TYPE);
        } catch (NumberFormatException e) {
            throw new LocSAXException("Failed to convert string value to unsigned integer: " + pValue, getLocator());
        }
    }
}
