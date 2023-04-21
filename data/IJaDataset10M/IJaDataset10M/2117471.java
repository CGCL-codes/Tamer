package com.volantis.mcs.eclipse.common.odom;

import com.volantis.devrep.repository.accessors.DefaultNamespaceAdapterFilter;
import com.volantis.xml.schema.W3CSchemata;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import java.io.IOException;

/**
 * Integratin test that ensures ODOMObservable objects perform validation
 * when they are modified.
 */
public class DefaultNamespacedODOMIntegrationValidationTestCase extends ODOMIntegrationValidationTestCase {

    protected Document createDocument() throws JDOMException, IOException {
        return createDocumentFromString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "<shiporder xmlns=\"" + getNamespace().getURI() + "\"" + " xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" " + "xsi:schemaLocation=\"" + getNamespace().getURI() + " " + getAbsoluteSchemaLocation() + "\" orderid='889923'>" + "<orderperson>John Smith</orderperson>" + "<shipto>" + "<name>Ola Nordmann</name>" + "<address>Langgt 23</address>" + "<city>4000 Stavanger</city>" + "<country>Norway</country>" + "</shipto>" + "<item>" + "<title>Empire Burlesque</title>" + "<note>Special Edition</note>" + "<quantity>1</quantity>" + "<price>10.90</price>" + "</item>" + "<item>" + "<title>Hide your heart</title>" + "<quantity>1</quantity>" + "<price>9.90</price>" + "</item>" + "</shiporder>");
    }

    protected Namespace getNamespace() {
        return MCSNamespace.LPDM;
    }

    protected SAXBuilder createSAXBuilder() {
        SAXBuilder builder = super.createSAXBuilder();
        builder.setXMLFilter(new DefaultNamespaceAdapterFilter(getNamespace().getPrefix()));
        return builder;
    }
}
