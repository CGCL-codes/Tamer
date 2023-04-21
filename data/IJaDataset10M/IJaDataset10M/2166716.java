package org.junit.remote.internal.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.Test;

/**
 * @author  Steven Stallion
 * @version $Revision: 12 $
 */
public class AnnotationsTest {

    @Test
    public void isAnnotatedOnClass() {
        assert Annotations.isAnnotated(Mock1.class, MockAnnotation.class);
    }

    @Test
    public void isNotAnnotatedOnClass() {
        assert !Annotations.isAnnotated(Mock2.class, MockAnnotation.class);
    }

    @Test
    public void isAnnotatedOnMethod() throws NoSuchMethodException {
        Method method = Mock2.class.getMethod("method");
        assert Annotations.isAnnotated(method, MockAnnotation.class);
    }

    @Test
    public void isNotAnnotatedOnMethod() throws NoSuchMethodException {
        Method method = Mock1.class.getMethod("method");
        assert !Annotations.isAnnotated(method, MockAnnotation.class);
    }

    @Test
    public void getAnnotationOnClass() {
        assert Annotations.getAnnotation(Mock1.class, MockAnnotation.class) != null;
    }

    @Test
    public void getAnnotationOnClassWithoutAnnotation() {
        assert Annotations.getAnnotation(Mock2.class, MockAnnotation.class) == null;
    }

    @Test
    public void getAnnotationOnMethod() throws NoSuchMethodException {
        Method method = Mock2.class.getMethod("method");
        assert Annotations.getAnnotation(method, MockAnnotation.class) != null;
    }

    @Test
    public void getAnnotationOnMethodWithoutAnnotation() throws NoSuchMethodException {
        Method method = Mock1.class.getMethod("method");
        assert Annotations.getAnnotation(method, MockAnnotation.class) == null;
    }

    @Test
    public void requireAnnotationOnClass() {
        assert Annotations.requireAnnotation(Mock1.class, MockAnnotation.class) != null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void requireAnnotationOnClassFails() {
        Annotations.requireAnnotation(Mock2.class, MockAnnotation.class);
    }

    @Test
    public void requireAnnotationOnMethod() throws NoSuchMethodException {
        Method method = Mock2.class.getMethod("method");
        assert Annotations.requireAnnotation(method, MockAnnotation.class) != null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void requireAnnotationOnMethodFails() throws NoSuchMethodException {
        Method method = Mock1.class.getMethod("method");
        Annotations.requireAnnotation(method, MockAnnotation.class);
    }

    @Test
    public void findAnnotatedMethods() throws NoSuchMethodException {
        Method method = Mock2.class.getMethod("method");
        Set<Method> methods = Annotations.findAnnotatedMethods(Mock2.class, MockAnnotation.class);
        assert methods.contains(method);
    }

    @Test
    public void findAnnotatedMethodsWithParent() throws NoSuchMethodException {
        Method method = Mock3.class.getMethod("parent");
        Set<Method> methods = Annotations.findAnnotatedMethods(Mock1.class, MockAnnotation.class);
        assert methods.contains(method);
    }

    @Retention(RetentionPolicy.RUNTIME)
    private static @interface MockAnnotation {
    }

    @MockAnnotation
    private static class Mock1 extends Mock3 {

        @Override
        public void method() {
        }
    }

    private static class Mock2 {

        @MockAnnotation
        public void method() {
        }
    }

    private static class Mock3 {

        @MockAnnotation
        public void parent() {
        }

        public void method() {
        }
    }
}
