package com.reeltwo.jumble.util;

import java.lang.reflect.Method;
import junit.framework.JUnit4TestAdapter;
import junit.framework.JUnit4TestCaseFacade;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Class containing several utility methods useful to Jumble.
 * 
 * @author Tin Pavlinic
 * @version $Revision: 689 $
 */
public class JumbleUtils {

    private JumbleUtils() {
    }

    /**
   * Determines whether a given class is a test class.
   * 
   * @param clazz the class to check.
   * @return true if the class is a test class, false otherwise.
   */
    public static boolean isTestClass(Class<?> clazz) {
        final boolean junit3 = isJUnit3TestClass(clazz);
        final boolean junit4 = isJUnit4TestClass(clazz);
        return junit3 || junit4;
    }

    /**
   * Checks if the given class is a JUnit 3 test class.
   * 
   * @param clazz the class to check.
   * @return if clazz is a test class, false otherwise.
   */
    public static boolean isJUnit3TestClass(Class<?> clazz) {
        Class<?> tmp = clazz;
        while (tmp != Object.class) {
            Class<?>[] intfc = tmp.getInterfaces();
            for (int i = 0; i < intfc.length; i++) {
                if (intfc[i].getName().equals("junit.framework.Test")) {
                    return true;
                }
            }
            tmp = tmp.getSuperclass();
        }
        return false;
    }

    /**
   * Determines if <code>clazz</code> is a JUnit 4 test class.
   * 
   * @param clazz the class to check.
   * @return true if the given class contains JUnit 4 test cases.
   */
    public static boolean isJUnit4TestClass(Class<?> clazz) {
        for (Method m : clazz.getMethods()) {
            if (m.getAnnotation(org.junit.Test.class) != null) {
                return true;
            }
        }
        return false;
    }

    /**
   * Gets whether assertions are currently enabled.
   * 
   * @return whether assertions are currently enabled
   * 
   */
    @SuppressWarnings(value = "all")
    public static boolean isAssertionsEnabled() {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        return assertionsEnabled;
    }

    /**
   * Gets the name of a test, (different method depending on underlying type).
   * 
   * @param t
   * @return test name
   */
    public static String getTestName(Test t) {
        if (t instanceof TestCase) {
            return ((TestCase) t).getName();
        }
        if (t instanceof JUnit4TestCaseFacade) {
            return ((JUnit4TestCaseFacade) t).getDescription().getDisplayName();
        }
        if (t instanceof JUnit4TestAdapter) {
            return ((JUnit4TestAdapter) t).getDescription().getDisplayName();
        }
        throw new ClassCastException(t.getClass().toString());
    }
}
