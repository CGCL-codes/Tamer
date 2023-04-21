package br.gov.component.demoiselle.crud.converter;

import javax.faces.component.UIComponent;
import junit.framework.TestCase;
import org.easymock.classextension.EasyMock;

public class NullableStringConverterTest extends TestCase {

    private NullableStringConverter nl = null;

    public void setUp() {
        nl = new NullableStringConverter();
    }

    public void testGetAsObject() {
        UIComponent component = EasyMock.createMock(UIComponent.class);
        Object ret = nl.getAsObject(null, component, null);
        assertNull(ret);
    }

    public void testGetAsStringNull() {
        String ret = nl.getAsString(null, null, null);
        assertNull(ret);
    }

    public void testGetAsStringNotNull() {
        String ret = nl.getAsString(null, null, "test");
        assertEquals("test", ret);
    }
}
