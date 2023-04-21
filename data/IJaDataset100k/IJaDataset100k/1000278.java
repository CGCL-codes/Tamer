package org.dozer.classmap;

import org.dozer.AbstractDozerTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dmitry.buzdin
 */
public class ClassMapKeyFactoryTest extends AbstractDozerTest {

    private ClassMapKeyFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ClassMapKeyFactory();
    }

    @Test
    public void testCreateKey() {
        String key1 = factory.createKey(String.class, Long.class);
        String key2 = factory.createKey(String.class, Long.class);
        assertEquals(key1, key2);
    }

    @Test
    public void testCreateKey_Order() {
        String key1 = factory.createKey(String.class, Long.class);
        String key2 = factory.createKey(Long.class, String.class);
        assertNotSame(key1, key2);
        assertFalse(key1.equals(key2));
    }

    @Test
    public void testCreateKey_MapId() {
        String key1 = factory.createKey(String.class, Long.class, "id");
        String key2 = factory.createKey(String.class, Long.class);
        assertNotSame(key1, key2);
        assertFalse(key1.equals(key2));
    }

    @Test
    public void testCreateKey_MapIdNull() {
        String key = factory.createKey(String.class, Long.class, null);
        assertNotNull(key);
    }
}
