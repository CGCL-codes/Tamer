package org.sopera.metadata;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.InputStream;
import org.sopera.exception.CorruptedSourceException;
import org.sopera.metadata.impl.WsdlComponent;
import org.sopera.util.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OperationBuilderTests {

    public static final String SAMPLE_SCHEMA = "org/sopera/util/business.xsd";

    private Element elementDeclaration;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testMessageSpec() throws CorruptedSourceException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(SAMPLE_SCHEMA);
        Document document = DomUtils.docFromStream(is);
        elementDeclaration = DomUtils.getFirstChildNamed(WsdlComponent.XSD_NS, "element", document.getDocumentElement());
        Operation operation = OperationBuilder.operation().withRequestSchema(elementDeclaration).build();
    }
}
