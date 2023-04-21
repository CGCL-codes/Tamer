package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Method;
import org.hibernate.PropertyNotFoundException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.SimpleObject;

public class ValueTypePropertyAccessorGetValueHolder {

    private NakedPropertyAccessor accessor;

    @Before
    public void setUp() {
        accessor = new NakedPropertyAccessor();
    }

    @Ignore("need to convert, was originally written for the old value holder design (TextString, etc)")
    @Test
    public void happyCaseWhenPropertyExists() {
        Method m = accessor.getValueHolderMethod(SimpleObject.class, "string");
        assertEquals("getString", m.getName());
    }

    @Test(expected = PropertyNotFoundException.class)
    public void shouldThrowExceptionIfPropertyDoesNotExist() {
        accessor.getValueHolderMethod(SimpleObject.class, "missing");
    }
}
