package java.lang;

import java.io.Serializable;
import junit.framework.TestCase;

/**
 * tested class: java.lang.Class
 * tested method: isAssignableFrom
 */
public class ClassTestIsAssignableFrom extends TestCase {

    /**
     * if argument is null an NullPoinerException should be thrown.
     */
    public void test1() {
        try {
            getClass().isAssignableFrom(null);
        } catch (NullPointerException e) {
            return;
        }
        fail("NullPointerException exception expected");
    }

    /**
     * checks identity primitive  conversion.
     */
    public void test2() {
        assertTrue("Assert 0:", Integer.TYPE.isAssignableFrom(int.class));
        assertTrue("Assert 1:", int.class.isAssignableFrom(Integer.TYPE));
    }

    /**
     * checks widening refernce conversion for primitive type.
     */
    public void test3() {
        assertFalse(Object.class.isAssignableFrom(Character.TYPE));
    }

    /**
     * Classes that represent primitive types aren't assignable from
     * corresponding wrapper types as well as vice versa.  
     */
    public void test4() {
        assertFalse("Assert 0:", Integer.class.isAssignableFrom(Integer.TYPE));
        assertFalse("Assert 1:", Integer.TYPE.isAssignableFrom(Integer.class));
    }

    /**
     * The Serializable interface is super interface of the Boolean class. So it
     * should be assignable from the Boolean class. But not vice versa. 
     */
    public void test5() {
        assertTrue("Assert 0:", Serializable.class.isAssignableFrom(Boolean.class));
        assertFalse("Assert 1:", Boolean.class.isAssignableFrom(Serializable.class));
    }

    /**
     * Each array has the Object class as its super class.
     */
    public void test6() {
        assertTrue(Object.class.isAssignableFrom(new int[0].getClass()));
    }

    /**
     * The isAssignable() method should not perform widening primitive
     * conversion.
     */
    public void test7() {
        assertFalse("Assert 0:", double.class.isAssignableFrom(int.class));
        assertFalse("Assert 1:", int.class.isAssignableFrom(double.class));
    }

    /**
     * if a class represents array the isAssignable() method should work with
     * array's components. But this method should not perform widening primitive
     * conversion. 
     */
    public void test8() {
        assertFalse("Assert 0:", new double[0].getClass().isAssignableFrom(new int[0].getClass()));
        assertFalse("Assert 1:", new int[0].getClass().isAssignableFrom(new double[0].getClass()));
    }

    /**
     * if a class represents array the isAssignable() method should work with
     * array's components. So the Object[] class is assignable from the String[]
     * class as well as the Object class is assignable from the String class.
     */
    public void test9() {
        assertTrue(new Object[0].getClass().isAssignableFrom(new String[0].getClass()));
    }

    /**
     * The Object class is assignable from any reference type. 
     */
    public void test10() {
        assertTrue(Object.class.isAssignableFrom(getClass()));
    }
}
