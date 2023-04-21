package net.sf.xisemele.impl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import net.sf.xisemele.impl.AttributeImpl;
import net.sf.xisemele.impl.FormatterProvider;
import net.sf.xisemele.impl.ValueImpl;
import org.junit.Test;

/**
 * Casos de teste para a classe {@link AttributeImpl}.
 * 
 * @author Carlos Eduardo Coral.
 */
public class AttributeImplTest {

    @Test
    public void testNameValue() {
        FormatterProvider provider = createMock(FormatterProvider.class);
        ValueImpl value = new ValueImpl(null, provider, "20");
        AttributeImpl attribute = new AttributeImpl("test", value);
        assertEquals("name", "test", attribute.name());
        assertEquals("value", value, attribute.value());
    }

    /**
    * Testa os método {@link AttributeImpl#equals(Object)} e {@link AttributeImpl#hashCode()}.
    */
    @Test
    public void testEqualsHashCode() {
        FormatterProvider provider = createMock(FormatterProvider.class);
        assertEquals(new AttributeImpl("a", new ValueImpl(null, provider, "10")), new AttributeImpl("a", new ValueImpl(null, provider, "10")));
        assertEquals(new AttributeImpl("a", new ValueImpl(null, provider, "10")).hashCode(), new AttributeImpl("a", new ValueImpl(null, provider, "10")).hashCode());
        assertFalse(new AttributeImpl("a", new ValueImpl(null, provider, "10")).equals(new AttributeImpl("b", new ValueImpl(null, provider, "10")).hashCode()));
    }
}
