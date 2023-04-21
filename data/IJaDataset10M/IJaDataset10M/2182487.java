package nl.hoepsch.util;

import static org.testng.Assert.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import nl.hoepsch.json.marshaller.beans.Person;
import org.testng.annotations.Test;

public class ClassUtilTest {

    private final List<Class<?>> primitives = new ArrayList<Class<?>>();

    private final ClassUtil classUtil = new ClassUtil();

    public ClassUtilTest() {
        primitives.add(short.class);
        primitives.add(Short.class);
        primitives.add(int.class);
        primitives.add(Integer.class);
        primitives.add(long.class);
        primitives.add(Long.class);
        primitives.add(float.class);
        primitives.add(Float.class);
        primitives.add(double.class);
        primitives.add(Double.class);
        primitives.add(byte.class);
        primitives.add(Byte.class);
        primitives.add(char.class);
        primitives.add(Character.class);
        primitives.add(boolean.class);
        primitives.add(Boolean.class);
        primitives.add(String.class);
    }

    @Test
    public void testIsPrimitive() {
        for (Class<?> cls : primitives) {
            assertTrue(classUtil.isPrimitive(cls));
        }
    }

    @Test
    public void testGetters() throws Exception {
        Map<String, Method> methods = classUtil.getGetters(Person.class);
        assertEquals(methods.size(), 8);
        Set<Entry<String, Method>> entries = methods.entrySet();
        for (Entry<String, Method> entry : entries) {
            String property = entry.getKey();
            Method method = entry.getValue();
            assertEquals(classUtil.getPropertyName(method), property);
            assertEquals(classUtil.getGetter(Person.class, property), method);
        }
    }

    @Test
    public void testSetters() throws Exception {
        Map<String, Method> methods = classUtil.getSetters(Person.class);
        assertEquals(methods.size(), 6);
        Set<Entry<String, Method>> entries = methods.entrySet();
        for (Entry<String, Method> entry : entries) {
            String property = entry.getKey();
            Method method = entry.getValue();
            assertEquals(classUtil.getPropertyName(method), property);
            assertEquals(classUtil.getSetter(Person.class, property), method);
        }
    }
}
