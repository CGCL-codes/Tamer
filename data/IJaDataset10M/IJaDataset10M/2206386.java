package net.sourceforge.jpp.test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CompilableSuite extends SuiteBase {

    public static final String TEST_PACKAGE = "net.sourceforge.jpp.test";

    public static final char PACKAGE_SEPARATOR = '.';

    private static final String[] EMPTY_STRING_ARRAY = {};

    public boolean compileClass(String... className) throws Exception {
        return CompilerHelper.getInstance().compileClass(className);
    }

    public boolean compileClass(String[] options, String... className) throws Exception {
        return CompilerHelper.getInstance().compileClass(options, className);
    }

    public Class<?> loadClass(String className) throws Exception {
        return CompilerHelper.getInstance().loadClass(className);
    }

    @SuppressWarnings("unchecked")
    protected void assertSimilar(Class expectedClass, Class actualClass) throws Exception {
        assertEquals(expectedClass.getModifiers(), actualClass.getModifiers());
        Method[] expectedMethods = expectedClass.getDeclaredMethods();
        for (int i = 0; i < expectedMethods.length; i++) {
            Method expectedMethod = expectedMethods[i];
            Method actualMethod = actualClass.getDeclaredMethod(expectedMethod.getName(), expectedMethod.getParameterTypes());
            assertSimilar(expectedClass, expectedMethod.getReturnType(), actualClass, actualMethod.getReturnType());
            assertEquals(maskModifiers(expectedMethod.getModifiers()), maskModifiers(actualMethod.getModifiers()));
        }
        Field[] expectedFields = expectedClass.getDeclaredFields();
        for (int i = 0; i < expectedFields.length; i++) {
            Field expectedField = expectedFields[i];
            Field actualField = actualClass.getDeclaredField(expectedField.getName());
            assertSimilar(expectedClass, expectedField.getType(), actualClass, actualField.getType());
            assertEquals(expectedField.getModifiers(), actualField.getModifiers());
        }
        assertEquals(expectedClass.getPackage(), actualClass.getPackage());
        assertEquals(expectedClass.getSuperclass(), actualClass.getSuperclass());
    }

    protected void assertSimilar(Class expectedClass, Class expectedType, Class actualClass, Class actualType) throws Exception {
        assertEquals(expectedClass.equals(expectedType) ? actualClass : expectedType, actualType);
    }

    protected int maskModifiers(int value) {
        return value & ~Modifier.TRANSIENT;
    }

    protected String[] sources(Collection<String> values) {
        return values.toArray(EMPTY_STRING_ARRAY);
    }

    protected String[] sources(String... values) {
        Set<String> ret = new HashSet<String>();
        for (String value : values) {
            if (value != null) {
                ret.add(value);
            }
        }
        return ret.toArray(EMPTY_STRING_ARRAY);
    }
}
