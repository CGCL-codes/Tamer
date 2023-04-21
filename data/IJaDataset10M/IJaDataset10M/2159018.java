package nl.flotsam.preon.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import junit.framework.TestCase;

public class ReplacingAnnotatedElementTest extends TestCase {

    public void testReplacement() {
        AnnotatedElement element = Test.class;
        assertEquals("bar", element.getAnnotation(Foo.class).value());
        element = new ReplacingAnnotatedElement(element, new Foo() {

            public String value() {
                return "foobar";
            }

            public Class<? extends Annotation> annotationType() {
                return Foo.class;
            }
        });
        assertEquals("foobar", element.getAnnotation(Foo.class).value());
    }

    @Foo("bar")
    private static class Test {
    }

    @Retention(RetentionPolicy.RUNTIME)
    private static @interface Foo {

        String value();
    }
}
