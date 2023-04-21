package org.openliberty.xmltooling.dst2_1.ref;

import org.openliberty.xmltooling.OpenLibertyHelpers;
import org.openliberty.xmltooling.dst2_1.ResultQueryBaseType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

public class QueryItemMarshaller extends AbstractXMLObjectMarshaller {

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        QueryItem obj = (QueryItem) xmlObject;
        obj.getSelectQualifAttributes().marshallAttributes(domElement);
        obj.getPaginationAttributes().marshallAttributes(domElement);
        if (obj.getItemIDRef() != null) {
            domElement.setAttributeNS(null, ResultQueryBaseType.ATT_ITEM_ID_REF, obj.getItemIDRef());
        }
        if (obj.getContingency() != null) {
            domElement.setAttributeNS(null, ResultQueryBaseType.ATT_CONTINGENCY, OpenLibertyHelpers.stringFromBoolean(obj.getContingency(), null));
        }
        if (obj.getIncludeCommonAttributes() != null) {
            domElement.setAttributeNS(null, ResultQueryBaseType.ATT_INCLUDE_COMMON_ATTS, OpenLibertyHelpers.stringFromBoolean(obj.getIncludeCommonAttributes(), null));
        }
        if (obj.getChangedSince() != null) {
            domElement.setAttributeNS(null, ResultQueryBaseType.ATT_CHANGED_SINCE, OpenLibertyHelpers.stringForDateTime(obj.getChangedSince()));
        }
        if (obj.getItemID() != null) {
            domElement.setAttributeNS(null, ResultQueryBaseType.ATT_ITEM_ID, obj.getItemID());
        }
    }

    @Override
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
    }
}
