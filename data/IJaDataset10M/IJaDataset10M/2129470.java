package com.germinus.xpression.cms.educative;

import javax.jcr.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

interface JCRToXMLPropertiesConverter {

    void convertBinaryProperty(Property property, Element element, Document document);
}
