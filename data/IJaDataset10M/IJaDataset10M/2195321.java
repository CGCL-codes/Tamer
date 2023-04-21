package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.SchemaType;

public class XmlFloatRestriction extends JavaFloatHolderEx implements XmlFloat {

    public XmlFloatRestriction(SchemaType type, boolean complex) {
        super(type, complex);
    }
}
