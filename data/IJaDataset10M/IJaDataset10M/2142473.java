package org.sourceforge.jemm.client.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sourceforge.jemm.client.Entity;
import org.sourceforge.jemm.client.classes.Shadowed;
import org.sourceforge.jemm.client.field.FieldKey;
import org.sourceforge.jemm.types.ID;

public class PrimitiveAttributeTest {

    Entity sEntity;

    PrimitiveAttribute accessor;

    @Before
    public void setUp() {
        Shadowed s = new Shadowed(new ID(1));
        sEntity = new Entity(s);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getFieldValid() {
        accessor = new PrimitiveAttribute(sEntity, new FieldKey(Shadowed.class, "a"));
        Object value = accessor.getField();
        assertEquals(0, value);
    }

    @Test
    public void getFieldInvalidButExists() {
        accessor = new PrimitiveAttribute(sEntity, new FieldKey(Shadowed.class, "obj"));
        Object value = accessor.getField();
        assertNull(value);
    }

    @Test(expected = RuntimeException.class)
    public void getFieldNonExistent() {
        accessor = new PrimitiveAttribute(sEntity, new FieldKey(Shadowed.class, "doesNotExist"));
        accessor.getField();
    }

    @Test
    public void setFieldExists() {
        accessor = new PrimitiveAttribute(sEntity, new FieldKey(Shadowed.class, "a"));
        accessor.set(2);
        assertEquals(2, accessor.getField());
    }

    @Test(expected = RuntimeException.class)
    public void setFieldNotExists() {
        accessor = new PrimitiveAttribute(sEntity, new FieldKey(Shadowed.class, "doesNotExist"));
        accessor.set(null);
    }
}
