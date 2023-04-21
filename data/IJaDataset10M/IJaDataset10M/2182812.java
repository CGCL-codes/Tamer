package org.openliberty.xmltooling.pp;

import org.openliberty.xmltooling.dst2_1.DSTURIMarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

public class MsgTechnologyMarshaller extends DSTURIMarshaller {

    public MsgTechnologyMarshaller() {
        super();
    }

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        super.marshallAttributes(xmlObject, domElement);
        MsgTechnology ppObject = (MsgTechnology) xmlObject;
        if (ppObject.getMsgLimit() != null) {
            domElement.setAttributeNS(null, MsgTechnology.ATT_MSG_LIMIT, ppObject.getMsgLimit().toString());
        }
    }
}
