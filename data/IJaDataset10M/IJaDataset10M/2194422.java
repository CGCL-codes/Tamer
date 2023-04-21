package org.gvsig.xmlschema.writer;

import org.gvsig.xmlschema.som.IXSElementDeclaration;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class ElementWritingTest extends WriterBaseTest {

    private String elementName1 = "river";

    private String elementType1 = "river_type";

    public void readSchema() {
        IXSElementDeclaration element = getSchema().getElementDeclarationByName(getNamespaceURI(), elementName1);
        assertNotNull(element);
        assertEquals(element.getQName().getLocalPart(), elementName1);
        assertEquals(element.getTypeName(), getSchema().getTargetNamespacePrefix() + ":" + elementType1);
    }

    public void writeSchema() {
        getSchema().addElement(elementName1, elementType1);
    }
}
