package net.sf.doolin.sdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.sf.doolin.sdo.support.DOClosedTypeException;
import net.sf.doolin.sdo.support.DefaultDataType;
import net.sf.doolin.sdo.support.DefaultProperty;
import org.junit.Test;

public class DefaultDataTypeTest {

    @Test(expected = DOClosedTypeException.class)
    public void closed() {
        DataSchema schema = mock(DataSchema.class);
        when(schema.toString()).thenReturn("MySchema");
        DefaultProperty<String> firstName = new DefaultProperty<String>("firstName", false, String.class, false, null);
        DefaultProperty<String> lastName = new DefaultProperty<String>("lastName", false, String.class, false, null);
        DefaultDataType type = new DefaultDataType(schema, "ThisType");
        type.addProperty(firstName);
        type.close();
        type.addProperty(lastName);
    }

    @Test
    public void subClass() {
        DataSchema schema = mock(DataSchema.class);
        when(schema.toString()).thenReturn("MySchema");
        DefaultDataType a = new DefaultDataType(schema, "A");
        DefaultDataType b = new DefaultDataType(schema, "B");
        b.setParentType(a);
        assertTrue(a.isSubClass(a));
        assertTrue(b.isSubClass(b));
        assertTrue(b.isSubClass(a));
        assertFalse(a.isSubClass(b));
    }

    @Test
    public void toStringTest() {
        DataSchema schema = mock(DataSchema.class);
        when(schema.toString()).thenReturn("MySchema");
        DefaultDataType type = new DefaultDataType(schema, "ThisType");
        assertEquals("MySchema::ThisType", type.toString());
    }
}
