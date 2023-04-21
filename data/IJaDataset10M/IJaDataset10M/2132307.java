package org.xnap.commons.settings;

import junit.framework.TestCase;

/**
 * @author Steffen Pingel
 */
public class IntValidatorTest extends TestCase {

    public void testMinConstructor2() {
        IntValidator validator = new IntValidator(5);
        assertIllegalArgument(validator, 4);
        assertIllegalArgument(validator, 0);
        assertIllegalArgument(validator, -1);
        assertLegal(validator, 5);
        assertLegal(validator, 6);
        assertLegal(validator, Integer.MAX_VALUE);
    }

    public void testEmptyConstructor() {
        IntValidator v = new IntValidator();
        v.validate(Integer.MIN_VALUE + "");
        v.validate("0");
        v.validate(Integer.MAX_VALUE + "");
        assertIllegalArgument(v, "a");
    }

    public void testMinConstructor() {
        IntValidator v = new IntValidator(0);
        v.validate("0");
        v.validate(Integer.MAX_VALUE + "");
        assertIllegalArgument(v, "-1");
        assertIllegalArgument(v, Integer.MIN_VALUE + "");
    }

    public void testMinMaxConstructor() {
        IntValidator v = new IntValidator(0, 0);
        v.validate("0");
        assertIllegalArgument(v, "-1");
        assertIllegalArgument(v, "1");
        v = new IntValidator(-1, 1);
        v.validate("0");
        v.validate("-1");
        v.validate("1");
        assertIllegalArgument(v, "-2");
        assertIllegalArgument(v, "2");
    }

    public void testMinGreaterMaxConstructor() {
        try {
            IntValidator v = new IntValidator(1, 0);
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
        }
    }

    public void testNull() {
        IntValidator v = new IntValidator();
        assertIllegalArgument(v, null);
    }

    public void testString() {
        IntValidator v = new IntValidator();
        assertIllegalArgument(v, "a");
        assertIllegalArgument(v, "");
    }

    private void assertLegal(IntValidator validator, int value) {
        try {
            validator.validate(value + "");
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    private void assertIllegalArgument(IntValidator validator, int value) {
        assertIllegalArgument(validator, value + "");
    }

    private void assertIllegalArgument(IntValidator validator, String value) {
        try {
            validator.validate(value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
