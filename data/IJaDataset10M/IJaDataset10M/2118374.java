package theweb.i18n;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;
import junit.framework.TestCase;
import theweb.resources.ClasspathResourceLocation;

public class BundleTest extends TestCase {

    public void testLoad() {
        Bundle bundle = new Bundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        assertNotNull(bundle);
        assertEquals("значення", bundle.getProperty("simple"));
    }

    @SuppressWarnings("unchecked")
    public void testReload() throws Exception {
        Bundle bundle = new Bundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        assertEquals("значення", bundle.getProperty("simple"));
        Map<String, String> props = (Map<String, String>) readAccessibleField(bundle, "properties");
        props.clear();
        setAccessibleField(bundle, "lastModified", 12L);
        assertEquals("значення", bundle.getProperty("simple"));
    }

    private static Object readAccessibleField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        final Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }

    private static void setAccessibleField(Object obj, String name, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        final Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

    static class MockClassLoader extends ClassLoader {

        private boolean resourceKilled = false;

        public MockClassLoader(ClassLoader parent) {
            super(parent);
        }

        public void killResource() {
            resourceKilled = true;
        }

        @Override
        public URL getResource(String name) {
            if (resourceKilled) return null;
            return super.getResource(name);
        }
    }

    public void testReloadFileDeleted() throws Throwable {
        String name = "theweb/i18n/test_uk.properties";
        MockClassLoader classLoader = new MockClassLoader(Thread.currentThread().getContextClassLoader());
        ClassLoader prev = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            Bundle bundle = new Bundle(new ClasspathResourceLocation(name));
            assertEquals("значення", bundle.getProperty("simple"));
            classLoader.killResource();
            assertNull(bundle.getProperty("simple"));
        } finally {
            Thread.currentThread().setContextClassLoader(prev);
        }
    }

    public void testBom() throws Exception {
        Bundle bundle = new Bundle(new ClasspathResourceLocation("theweb/i18n/bom_uk.properties"));
        assertEquals("значення", bundle.getProperty("ключ"));
    }

    public void testTrim() throws Exception {
        Bundle bundle = new Bundle(new ClasspathResourceLocation("theweb/i18n/trim_uk.properties"));
        assertEquals("line1line2", bundle.getProperty("key1"));
        assertEquals("line1line2", bundle.getProperty("key2"));
        assertEquals("value3", bundle.getProperty("key3"));
        assertEquals("line1 line2", bundle.getProperty("key4"));
    }

    public void testMultiLine() throws Exception {
        Bundle bundle = new Bundle(new ClasspathResourceLocation("theweb/i18n/multiline_uk.properties"));
        assertEquals("line1line2", bundle.getProperty("key1"));
        assertEquals("value2", bundle.getProperty("key2"));
    }
}
