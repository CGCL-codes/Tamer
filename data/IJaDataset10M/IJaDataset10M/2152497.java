package com.gargoylesoftware.base.testing;

import com.gargoylesoftware.base.util.DetailedNullPointerException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.AssertionFailedError;

/**
 * Utility methods dealing with JUnit testing.
 *
 * @version $Revision: 1.6 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class TestUtil {

    private TestUtil() {
    }

    /**
     * Serialize the specified object to a stream and then deserialize
     * it again.  This tests the following things:
     * <ol>
     * <li>Ensures that the original and reconstructed objects
     *     are equal.
     * <li>The object can be serialized
     * </ol>
     *
     * @param object The object to test
     * @param checkEquality True if the original and copy should be
     *        equal according to the semantics of the equals() method.
     * @return The copy.
     * @throws IOException If an error occcurs during serialization.
     */
    public static Object testSerialization(final Object object, final boolean checkEquality) throws IOException {
        assertNotNull("object", object);
        final Object copy = copyBySerialization(object);
        if (checkEquality) {
            checkEquality(object, copy);
        }
        return copy;
    }

    /**
     * Same as testSerialization(object, true).  Provided as a convenience as
     * the equality check is usually wanted.
     * @param object The object to test
     * @return The copy.
     * @throws IOException If an error occcurs during serialization.
     */
    public static Object testSerialization(final Object object) throws IOException {
        return testSerialization(object, true);
    }

    /**
     * Copy an object by serializing it into a buffer and then deserializing
     * it again.
     *
     * @param object The original.
     * @return The copy.
     * @throws IOException If an error occcurs during serialization.
     */
    public static Object copyBySerialization(final Object object) throws IOException {
        assertNotNull("object", object);
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            final byte[] b = bos.toByteArray();
            final ByteArrayInputStream bis = new ByteArrayInputStream(b);
            final ObjectInputStream ois = new ObjectInputStream(bis);
            final Object copy = ois.readObject();
            return copy;
        } catch (final ClassNotFoundException e) {
            throw new NoClassDefFoundError("Class not found: " + e.getMessage());
        }
    }

    /**
     * The the clone() method on an object.
     * @param object The original object to clone.
     * @param checkEquality True if the original and copy are to be compared
     * for equality after the clone().
     * @throws IllegalAccessException If we do not have authority to call the
     * clone() method.
     * @throws InvocationTargetException If an exception is thrown during
     * the processing of the clone() method
     */
    public static void testClone(final Object object, final boolean checkEquality) throws IllegalAccessException, InvocationTargetException {
        if (object instanceof Cloneable == false) {
            throw new AssertionFailedError("Object is not cloneable");
        }
        Method cloneMethod;
        try {
            cloneMethod = object.getClass().getDeclaredMethod("clone", new Class[0]);
        } catch (final NoSuchMethodException e) {
            throw new AssertionFailedError("Object does not have a public clone() method");
        }
        final Object copy = cloneMethod.invoke(object, new Object[0]);
        if (checkEquality) {
            checkEquality(object, copy);
        }
    }

    /**
     * Assert that the two objects are equal
     * @param original The original object
     * @param copy The object to compare against
     */
    private static void checkEquality(final Object original, final Object copy) {
        if (copy.equals(original) == false) {
            throw new AssertionFailedError("Objects are different: original=[" + original + "] copy=[" + copy + "]");
        }
        if (copy.hashCode() != original.hashCode()) {
            throw new AssertionFailedError("Hashcodes are different: original=[" + original.hashCode() + "] copy=[" + copy.hashCode() + "]");
        }
    }

    /**
     * Assert that a and b appear equal.  See {@link #appearsEqual(Object,Object)}
     * for an explanation of "appears"
     *
     * @param message The message to display if the assert fails.
     * @param a The first object to compare
     * @param b The second object to compare
     */
    public static void assertAppearsEqual(final String message, final Object a, final Object b) {
        if (appearsEqual(a, b) == false) {
            throw new AssertionFailedError(message);
        }
    }

    /**
     * Assert that a and b do not appear equal.  See {@link #appearsEqual(Object,Object)}
     * for an explanation of "appears"
     *
     * @param message The message to display if the assert fails.
     * @param a The first object to compare
     * @param b The second object to compare
     */
    public static void assertAppearsNotEqual(final String message, final Object a, final Object b) {
        if (appearsEqual(a, b) == true) {
            throw new AssertionFailedError(message);
        }
    }

    /**
     * Return true if the two objects appear to be equal.  Some objects cannot
     * be compared for equality because they don't implement either the equals
     * method or the Comparable interface.  An example would be the Event objects
     * used by AWT and Swing.<p>
     *
     * This method will attempt to determine if the two objects are equal by
     * calling all the public accessor methods on the objects and performing
     * equals checks on the results.<p>
     *
     * If an exception is thrown during the invocation of any of the getXX()
     * methods then that method will be ignored for the purpose of considering
     * equality.
     *
     * @param a The first object to be compared
     * @param b The second object to be compared
     * @return True if the two objects appear to be the same.
     */
    public static boolean appearsEqual(final Object a, final Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        final Class clazz = a.getClass();
        if (b.getClass() != clazz) {
            return false;
        }
        final Object noArgs[] = new Object[0];
        final Method getMethods[] = clazz.getDeclaredMethods();
        for (int i = 0; i < getMethods.length; i++) {
            final Method method = getMethods[i];
            if (method.getParameterTypes().length == 0) {
                try {
                    if (isEqual(method.invoke(a, noArgs), method.invoke(b, noArgs)) == false) {
                        return false;
                    }
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                } catch (final InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private static boolean isEqual(final Object a, final Object b) {
        final boolean isEqual;
        if (a == null && b == null) {
            isEqual = true;
        } else if (a == null || b == null) {
            isEqual = false;
        } else {
            isEqual = a.equals(b);
        }
        return isEqual;
    }

    /**
     * Verify that the specified value is not null.  If it is then throw an exception
     *
     * @param fieldName The name of the field to check
     * @param fieldValue The value of the field to check
     * @exception DetailedNullPointerException If fieldValue is null
     */
    public static final void assertNotNull(final String fieldName, final Object fieldValue) throws DetailedNullPointerException {
        if (fieldValue == null) {
            throw new DetailedNullPointerException(fieldName);
        }
    }
}
