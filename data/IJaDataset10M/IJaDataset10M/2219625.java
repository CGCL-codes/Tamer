package org.middleheaven.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.lang.reflect.Method;
import java.util.LinkedList;
import org.junit.Test;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.collections.EnhancedCollection;

public class TestIntrospector {

    @Test
    public void testPropertyAcessor() {
        SomeBean sb = new SomeBean();
        PropertyAccessor pa = Introspector.of(SomeBean.class).inspect().properties().named("list").retrive();
        pa.setValue(sb, new LinkedList());
        pa.getValue(sb);
        pa = Introspector.of(SomeBean.class).inspect().properties().named("name").retrive();
        pa.setValue(sb, "A");
        try {
            pa.getValue(sb);
            assertFalse(true);
        } catch (WriteOnlyPropertyException e) {
        }
    }

    @Test
    public void testInterfaces() {
        Class<?>[] i = Introspector.of(SomeObject.class).getImplementedInterfaces();
        assertEquals(1, i.length);
        i = Introspector.of(SomeObject.class).getDeclaredInterfaces();
        assertEquals(1, i.length);
    }

    @Test
    public void testAnnotationsInParents() {
        EnhancedCollection<Method> all = Introspector.of(SomeObject.class).inspect().methods().notInheritFromObject().annotatedWith(Wire.class).retriveAll();
        assertFalse(all.isEmpty());
    }
}
