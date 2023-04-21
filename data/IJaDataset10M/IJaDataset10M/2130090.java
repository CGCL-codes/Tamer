package org.unitilsnew.core;

import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Ducheyne
 */
public class TestClassHasAnnotationTest {

    private TestClass testClass;

    @Before
    public void initialize() throws Exception {
        testClass = new TestClass(MyClass.class);
    }

    @Test
    public void hasAnnotation() {
        boolean result = testClass.hasAnnotation(MyAnnotation1.class);
        assertTrue(result);
    }

    @Test
    public void hasAnnotationOnSuperClass() {
        boolean result = testClass.hasAnnotation(MyAnnotation2.class);
        assertTrue(result);
    }

    @Test
    public void annotationNotFound() {
        boolean result = testClass.hasAnnotation(Target.class);
        assertFalse(result);
    }

    @Retention(RUNTIME)
    private @interface MyAnnotation1 {
    }

    @Retention(RUNTIME)
    private @interface MyAnnotation2 {
    }

    @MyAnnotation2
    private static class SuperClass {
    }

    @MyAnnotation1
    private static class MyClass extends SuperClass {
    }
}
