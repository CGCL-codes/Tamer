package com.risertech.xdav.internal.webdav.xml.property;

import org.jdom.Element;
import com.risertech.xdav.http.type.EntityTag;
import com.risertech.xdav.internal.webdav.xml.WebDAVConverter;
import com.risertech.xdav.webdav.property.GetETag;

/**
 * Value:      entity-tag  ; defined in section 3.11 of [RFC2068]
 * 		<!ELEMENT getetag (#PCDATA) >
 * 
 * @author phil
 */
public class GetETagConverter extends WebDAVConverter {

    public GetETagConverter() {
        super(GetETag.class, "getetag");
    }

    @Override
    protected Element doCreateElement(Object object) {
        Element element = createElement();
        GetETag getETag = (GetETag) object;
        if (!getETag.isRequest()) {
            element.setText(((GetETag) object).getEntityTag().toString());
        }
        return element;
    }

    @Override
    protected Object doCreateObject(Element element) {
        String text = element.getText();
        String opaqueTag = text.substring(text.indexOf('"') + 1, text.length() - 1);
        EntityTag entityTag = new EntityTag(opaqueTag, text.startsWith("W/"));
        return new GetETag(entityTag);
    }
}
