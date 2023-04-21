package org.helianto.document;

import static org.junit.Assert.assertEquals;
import org.helianto.core.Entity;
import org.helianto.core.Operator;
import org.helianto.document.base.AbstractCustomDocument;
import org.junit.Test;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class CustomDocumentTests {

    @Test
    public void custom() {
        Serializer serializer = new Serializer(new Entity(new Operator("DEFAULT"), "ALIAS"), "CODE");
        AbstractCustomDocument customDocument = new AbstractCustomDocument() {

            private static final long serialVersionUID = 1L;
        };
        customDocument.setSeries(serializer);
        assertEquals("CODE", customDocument.getInternalNumberKey());
    }
}
