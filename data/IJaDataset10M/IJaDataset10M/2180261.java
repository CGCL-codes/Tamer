package uk.ac.lkl.common.util.reflect.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import java.util.List;
import org.junit.Test;
import uk.ac.lkl.common.util.reflect.GenericClass;
import uk.ac.lkl.common.util.reflect.test.generics.*;

public class GenericClassTest {

    @Test
    public void testCreatingTwoSimpleInstances() {
        try {
            new GenericClass<Boolean>(Boolean.class);
            new GenericClass<Boolean>(Boolean.class);
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.INSTANCE_EXISTS_FOR_CLASS_TREE);
        }
    }

    @Test
    public void testSimpleName() {
        GenericClass<?> genericClass = GenericClass.getGeneric(TestClass2Type3.class);
        System.out.println(genericClass.getSimpleName());
        System.out.println(genericClass.getFullName());
    }

    @Test
    public void testDuplicateGenericClass() {
        try {
            new IntegerExpressionType();
            new IntegerExpressionType2();
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.INSTANCE_EXISTS_FOR_CLASS_TREE);
        }
    }

    @Test
    public void testDuplicateComplexGenericClass() {
        try {
            new TestClass2Type();
            new TestClass2Type2();
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.INSTANCE_EXISTS_FOR_CLASS_TREE);
        }
    }

    @Test
    public void testNonDirectSubClass() {
        try {
            new BigIntegerExpressionType();
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.NOT_A_DIRECT_SUBCLASS);
        }
    }

    @Test
    public void testBadGenericArgumentFormat() {
        try {
            new ArrayGenericType();
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.BAD_GENERIC_ARGUMENT_FORMAT);
        }
    }

    @Test
    public void testExtraConstructor() {
        try {
            new ExtraConstructorClass();
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.TOO_MANY_CONSTRUCTORS);
        }
    }

    @Test
    public void testMissingDefaultConstructor() {
        try {
            new MissingDefaultConstructorClass(3);
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.MISSING_DEFAULT_CONSTRUCTOR);
        }
    }

    @Test
    public void testMissingGenericParameter() {
        try {
            new MissingGenericType();
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.MISSING_GENERIC_ARGUMENT);
        }
    }

    @Test
    public void testPrivateDefaultConstructor() {
        try {
            GenericClass.getGeneric(PrivateDefaultConstructor.class);
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.INACCESSIBLE_DEFAULT_CONSTRUCTOR);
        }
    }

    @Test
    public void testProtectedDefaultConstructor() {
        try {
            GenericClass.getGeneric(ProtectedDefaultConstructor.class);
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.INACCESSIBLE_DEFAULT_CONSTRUCTOR);
        }
    }

    @Test
    public void testAbstractSubclass() {
        try {
            GenericClass.getGeneric(AbstractClass.class);
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.ABSTRACT_SUBCLASS);
        }
    }

    @Test
    public void testExceptionInConstructor() {
        try {
            GenericClass.getGeneric(ExceptionInConstructor.class);
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.CONSTRUCTOR_THROWS_EXCEPTION);
        }
    }

    @Test
    public void testExceptionInStaticInitializer() {
        try {
            GenericClass.getGeneric(ExceptionInStaticInitializer.class);
            fail();
        } catch (IllegalArgumentException e) {
            Throwable cause = e.getCause();
            assertSame(cause, GenericClass.INITIALIZATION_THROWS_EXCEPTION);
        }
    }

    @Test
    public void testCorrectClass() {
        GenericClass<?> genericClass = GenericClass.getGeneric(TestClass2Type4.class);
        System.out.println(genericClass.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildInstance() {
        GenericClass<List> genericClass = GenericClass.get(List.class, GenericClass.getSimple(Integer.class));
        GenericClass<List> genericClass2 = GenericClass.get(List.class, GenericClass.getSimple(Double.class));
        GenericClass<List> genericClass3 = GenericClass.get(List.class, GenericClass.getSimple(Integer.class));
        System.out.println(genericClass.getSimpleName());
        System.out.println(genericClass2.getSimpleName());
        System.out.println(genericClass3.getSimpleName());
    }
}
