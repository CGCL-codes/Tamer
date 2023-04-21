package org.openliberty.xmltooling.dst2_1.ref;

import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.openliberty.xmltooling.OpenLibertyHelpers;
import org.openliberty.xmltooling.dst2_1.DataResponseBaseType;
import org.openliberty.xmltooling.utility_2_0.ResponseType;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class QueryResponseMarshaller extends AbstractXMLObjectMarshaller {

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        QueryResponse obj = (QueryResponse) xmlObject;
        if (obj.getTimeStamp() != null) {
            domElement.setAttributeNS(null, DataResponseBaseType.ATT_TIME_STAMP, OpenLibertyHelpers.stringForDateTime(obj.getTimeStamp()));
        }
        if (obj.getItemRefId() != null) {
            domElement.setAttributeNS(null, ResponseType.ATT_ITEM_ID_REF, obj.getItemRefId());
        }
        Attr attr;
        for (Entry<QName, String> entry : obj.getUnknownAttributes().entrySet()) {
            attr = XMLHelper.constructAttribute(domElement.getOwnerDocument(), entry.getKey());
            attr.setValue(entry.getValue());
            domElement.setAttributeNodeNS(attr);
            if (Configuration.isIDAttribute(entry.getKey()) || obj.getUnknownAttributes().isIDAttribute(entry.getKey())) {
                attr.getOwnerElement().setIdAttributeNode(attr, true);
            }
        }
    }

    @Override
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
    }
}
