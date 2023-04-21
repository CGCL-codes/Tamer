package junit.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the class <code>PA</code>.
 */
public class PATest {

    /**
    * An instance of a test-subclass.
    */
    private Child child;

    /**
    * An instance of a test-subclass in a variable of type superclass.
    */
    private Parent childInParent;

    /**
    * an instance of a test-superclass.
    */
    private Parent parent;

    /**
    * Sets up the test-environment by instantiating the test-instances.
    * 
    * @see junit.framework.TestCase#setUp()
    */
    @Before
    public final void setUp() {
        this.parent = new Parent("Charlie");
        this.child = new Child("Charlie");
        this.childInParent = new Child("Charlie");
    }

    /**
    * Tears down the test-environment by deleting the test-instances.
    * 
    * @see junit.framework.TestCase#tearDown()
    */
    @After
    public final void tearDown() {
        this.parent = null;
        this.child = null;
        this.childInParent = null;
    }

    /**
    * Tests the method <code>getValue</code> with a non-existing field.
    * 
    * @see junit.extensions.PA#getValue(java.lang.Object, java.lang.String)
    */
    @Test
    public void testGetValueOnInvalidField() throws Exception {
        Throwable thrownException = null;
        try {
            PA.getValue(this.parent, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertEquals("noSuchField", thrownException.getCause().getMessage());
        try {
            PA.getValue(this.child, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertEquals("noSuchField", thrownException.getCause().getMessage());
        try {
            PA.getValue(this.childInParent, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertEquals("noSuchField", thrownException.getCause().getMessage());
        try {
            PA.getValue(Parent.class, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertEquals("noSuchField", thrownException.getCause().getMessage());
        try {
            PA.getValue(null, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(InvalidParameterException.class, thrownException.getCause().getClass());
        assertEquals("Can't get field on null object/class", thrownException.getCause().getMessage());
    }

    /**
    * Tests the method <code>instantiate</code>.
    * 
    * @see junit.extensions.PA#instantiate(java.lang.Class)
    */
    @Test
    public void testInstantiateOnInvalidParameters() throws Exception {
        Throwable thrownException = null;
        try {
            PA.instantiate(Parent.class, 21);
            fail("instantiating with wrong parameter type should throw Exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertEquals(Parent.class.getName() + ".<init>(java.lang.Integer)", thrownException.getCause().getMessage());
        try {
            PA.instantiate(Child.class, "Charlie", "Brown");
            fail("instantiating with wrong second parameter type should throw Exception");
        } catch (Exception e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertEquals(Child.class.getName() + ".<init>(java.lang.String, java.lang.String)", thrownException.getCause().getMessage());
        try {
            PA.instantiate(Child.class, new Class[] { String.class, String.class }, "Charlie", 8);
            fail("instantiating with unmatching parameter types should throw Exception");
        } catch (Exception e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertEquals(Child.class.getName() + ".<init>(java.lang.String, java.lang.String)", thrownException.getCause().getMessage());
        try {
            PA.instantiate(Child.class, new Class[] { String.class, Integer.class }, "Charlie", "Brown");
            fail("instantiating with unmatching parameter types should throw Exception");
        } catch (Exception e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        assertEquals("argument type mismatch", thrownException.getCause().getMessage());
        try {
            PA.instantiate(Child.class, new Class[] { String.class, Integer.class, String.class }, "Charlie", 8, "Brown");
            fail("instantiating with wrong parameter count should throw Exception");
        } catch (Exception e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertEquals(Child.class.getName() + ".<init>(java.lang.String, java.lang.Integer, java.lang.String)", thrownException.getCause().getMessage());
    }

    /**
    * Tests the constructor of PA.
    * 
    * @see junit.extensions.PA#instantiate(java.lang.Class)
    */
    @Test
    public void testInstantiationThrowsException() throws Exception {
        Throwable thrownException = null;
        try {
            PA.instantiate(PA.class);
            fail("Instantiating PA should throw Exception - you must have enabled assertions to run unit-tests");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(InvocationTargetException.class, thrownException.getCause().getClass());
    }

    /**
    * Tests the method <code>invokeMethod</code> on a non-existing method.
    * 
    * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
    @Test
    public void testInvokeMethodOnInvalidMethodName() throws Exception {
        Throwable thrownException = null;
        try {
            PA.invokeMethod(this.child, "getInt");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setInt)(", 5);
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "getInt)");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.parent, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.childInParent, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(Parent.class, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
    }

    /**
    * Tests the method <code>invokeMethod</code> with invalid arguments.
    * 
    * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
    @Test
    public void testInvokeMethodWithInvalidSignature() throws Exception {
        Throwable thrownException = null;
        try {
            PA.invokeMethod(this.child, "setName", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setName java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setName(java.lang.String", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setName(java.lang.SString)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
    }

    /**
    * Tests the method <code>invokeMethod</code> with invalid arguments.
    * 
    * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
    @Test
    public void testInvokeMethodWithInvalidArguments() throws Exception {
        Throwable thrownException = null;
        try {
            PA.invokeMethod(this.child, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setData(non.existing.package.NonExistingClass)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setData()");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.parent, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.childInParent, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setName(java.lang.String)", 2);
            fail("should throw IllegalArgumentException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setName(.String)", "Heribert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setName(string)", "Heribert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setName(NotAString)", "Heribert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setData(Integer)", 2);
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setInt(java.lang.String)", (Object[]) null);
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setInt(int)", "Herbert");
            fail("should throw IllegalArgumentException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(Parent.class, "setStaticInt(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchMethodException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setInt(int)", (Object[]) null);
            fail("should throw IllegalArgumentException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        assertNull(thrownException.getMessage());
    }

    /**
    * Tests the method <code>invokeMethod</code> with array of primitives instead of several arguments. This is not ok for several
    * reasons: a) downward compatibility - was not ok in the past (one had to use Object[]) b) this is the typical behaviour when using
    * varargs (Java doesn't autoconvert primitive arrays)
    * 
    * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
    @Test
    public void testInvokeMethodWithPrimitiveArrayInsteadOfSingleValues() throws Exception {
        Throwable thrownException = null;
        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new int[] { 5, 3 });
            fail("invoking method with an array of primitives instead of single primitives should raise exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Integer[] { 4, 3 });
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
    }

    /**
    * Tests the method <code>invokeMethod</code> with arrays of wrong length instead of several arguments.
    * 
    * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
    @Test
    public void testInvokeMethodWithArraysOfWrongLengthInsteadOfSingleValues() throws Exception {
        Throwable thrownException = null;
        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new int[] { 1 });
            fail("invoking method with array of wrong size should raise exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Integer[] { 2 });
            fail("invoking method with array of wrong size should raise exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Object[] { 3 });
            fail("invoking method with array of wrong size should raise exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
    }

    /**
    * Tests Autoboxing not supported
    * 
    * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
    @Test
    public void testAutoboxingNotSupported() throws Exception {
        Throwable thrownException = null;
        try {
            PA.invokeMethod(this.child, "setPrivateInts(int[])", new Integer[] { 1, 2 });
            fail("invoking method with single values instead of array as parameters should raise exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
    }

    /**
    * Tests the method <code>invokeMethod</code> with single values instead of an array.
    * 
    * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
    @Test
    public void testInvokeMethodWithSingleValuesInsteadOfArray() throws Exception {
        Throwable thrownException = null;
        try {
            PA.invokeMethod(this.child, "setPrivateInts(int[])", 1, 2);
            fail("invoking method with single values instead of array as parameters should raise exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", "Hello", "Bruno");
            fail("invoking method with single values instead of array as parameters should raise exception");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalArgumentException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
    }

    /**
    * Tests the method <code>setValue</code> with a static final field.
    * 
    * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
    @Test
    public void testSetValueOfStaticFinalField() throws Exception {
        Throwable thrownException = null;
        int previousValue = (Integer) PA.getValue(this.parent, "privateStaticFinalInt");
        assertTrue(previousValue != -3);
        try {
            PA.setValue(this.parent, "privateStaticFinalInt", -3);
            assertEquals(-3, PA.getValue(this.parent, "privateStaticFinalInt"));
            fail();
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalAccessException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.setValue(this.parent, "privateStaticFinalInt", previousValue);
            assertEquals(previousValue, PA.getValue(this.parent, "privateStaticFinalInt"));
            fail();
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalAccessException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
    }

    /**
    * Tests the method <code>setValue</code> with a static final object field.
    * 
    * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
    @Test
    public void testSetValueOfStaticFinalObjectField() throws Exception {
        String previousValue = (String) PA.getValue(this.parent, "privateStaticFinalString");
        assertTrue(previousValue != "Herbert");
        Throwable thrownException = null;
        try {
            PA.setValue(this.parent, "privateStaticFinalString", "Herbert");
            assertEquals("Herbert", PA.getValue(this.parent, "privateStaticFinalString"));
            fail();
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalAccessException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.setValue(this.parent, "privateStaticFinalString", previousValue);
            assertEquals(previousValue, PA.getValue(this.parent, "privateStaticFinalString"));
            fail();
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(IllegalAccessException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
    }

    /**
    * Tests the method <code>setValue</code> with a non-existing field.
    * 
    * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
    @Test
    public void testSetValueOnInvalidField() throws Exception {
        Throwable thrownException = null;
        try {
            PA.setValue(this.parent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.setValue(this.child, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.setValue(this.childInParent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
        try {
            PA.setValue(Parent.class, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            thrownException = e;
        }
        assertEquals(NoSuchFieldException.class, thrownException.getCause().getClass());
        assertNotNull(thrownException.getMessage());
    }
}
