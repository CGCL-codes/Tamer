package net.sf.jour.signature;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Vector;

/**
 * @author vlads
 * 
 */
class APICompareChangeHelper {

    List changes;

    static class ModifiersValue {

        private final int modifiers;

        public ModifiersValue(int modifiers) {
            this.modifiers = modifiers;
        }

        public boolean equals(Object obj) {
            if (obj instanceof ModifiersValue) {
                return modifiers == ((ModifiersValue) obj).modifiers;
            } else {
                return false;
            }
        }

        public String toString() {
            return Modifier.toString(modifiers) + " (" + modifiers + ")";
        }
    }

    APICompareChangeHelper() {
        changes = new Vector();
    }

    public void fail(String message) {
        changes.add(message);
    }

    public boolean assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
            return true;
        } else {
            return false;
        }
    }

    public boolean assertFalse(String message, boolean condition) {
        if (condition) {
            fail(message);
            return true;
        } else {
            return false;
        }
    }

    public boolean assertNull(String message, Object object) {
        return assertTrue(message, object == null);
    }

    public boolean assertNotNull(String message, Object object) {
        return assertTrue(message, object != null);
    }

    public boolean assertEquals(String message, int expected, int actual) {
        return assertEquals(message, new Integer(expected), new Integer(actual));
    }

    public boolean assertEquals(String message, boolean expected, boolean actual) {
        return assertEquals(message, new Boolean(expected), new Boolean(actual));
    }

    public boolean assertEquals(String message, Object expected, Object actual) {
        if (expected == null && actual == null) {
            return false;
        }
        if (expected != null && expected.equals(actual)) {
            return false;
        }
        failNotEquals(message, expected, actual);
        return true;
    }

    private void failNotEquals(String message, Object expected, Object actual) {
        fail(format(message, expected, actual));
    }

    String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }
        return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
    }
}
