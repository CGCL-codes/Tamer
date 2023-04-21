package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.SchemaType;

public class XmlDecimalRestriction extends JavaDecimalHolderEx implements XmlDecimal {

    public XmlDecimalRestriction(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
