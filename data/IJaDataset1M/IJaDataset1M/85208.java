package com.phloc.ubl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Test class for class {@link UBL21DocumentTypes}.
 * 
 * @author philip
 */
public final class UBL21DocumentTypesTest {

    @Test
    public void testGetAllNamespaces() {
        assertNotNull(UBL21DocumentTypes.getAllNamespaces());
        assertNotNull(UBL21DocumentTypes.getAllLocalNames());
        assertEquals(UBL21DocumentTypes.getAllNamespaces().size(), UBL21DocumentTypes.getAllLocalNames().size());
        for (final String sNamespace : UBL21DocumentTypes.getAllNamespaces()) {
            assertNotNull(UBL21DocumentTypes.getDocumentTypeOfNamespace(sNamespace));
            assertNotNull(UBL21DocumentTypes.getImplementationClassOfNamespace(sNamespace));
            assertNotNull(UBL21DocumentTypes.getSchemaOfNamespace(sNamespace));
        }
        assertNull(UBL21DocumentTypes.getDocumentTypeOfNamespace("any"));
        assertNull(UBL21DocumentTypes.getImplementationClassOfNamespace("any"));
        assertNull(UBL21DocumentTypes.getSchemaOfNamespace("any"));
        assertNull(UBL21DocumentTypes.getDocumentTypeOfNamespace(null));
        assertNull(UBL21DocumentTypes.getImplementationClassOfNamespace(null));
        assertNull(UBL21DocumentTypes.getSchemaOfNamespace(null));
        for (final String sNamespace : UBL21DocumentTypes.getAllLocalNames()) {
            assertNotNull(UBL21DocumentTypes.getDocumentTypeOfLocalName(sNamespace));
            assertNotNull(UBL21DocumentTypes.getImplementationClassOfLocalName(sNamespace));
            assertNotNull(UBL21DocumentTypes.getSchemaOfLocalName(sNamespace));
        }
        assertNull(UBL21DocumentTypes.getDocumentTypeOfLocalName("any"));
        assertNull(UBL21DocumentTypes.getImplementationClassOfLocalName("any"));
        assertNull(UBL21DocumentTypes.getSchemaOfLocalName("any"));
        assertNull(UBL21DocumentTypes.getDocumentTypeOfLocalName(null));
        assertNull(UBL21DocumentTypes.getImplementationClassOfLocalName(null));
        assertNull(UBL21DocumentTypes.getSchemaOfLocalName(null));
    }

    @Test
    public void testGetSchemaOfImplementationClass() {
        assertNull(UBL21DocumentTypes.getSchemaOfImplementationClass(null));
        assertNull(UBL21DocumentTypes.getSchemaOfImplementationClass(String.class));
        for (final EUBL21DocumentType eDocType : EUBL21DocumentType.values()) assertSame(eDocType.getSchema(), UBL21DocumentTypes.getSchemaOfImplementationClass(eDocType.getImplementationClass()));
    }
}
