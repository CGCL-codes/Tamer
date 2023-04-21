package org.josef.test.science.math;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.science.math.CBoolean;
import org.junit.Test;

/**
 * JUnit test class for class {@link CBoolean}.
 * @author Kees Schotanus
 * @version 2.0 $Revision: 2840 $
 */
@Status(stage = PRODUCTION)
@Review(by = "Kees Schotanus", at = "2011-09-07", reason = "Initial review")
public final class CBooleanTest {

    /**
     * Tests {@link CBoolean#parseBoolean(String)} with true values.
     */
    @Test
    public void testParseBooleanWithTrueValues() {
        assertTrue("true=true", CBoolean.parseBoolean("true"));
        assertTrue("True=true", CBoolean.parseBoolean("True"));
        assertTrue("TRUE=true", CBoolean.parseBoolean("TRUE"));
        assertTrue("TRUE=true", CBoolean.parseBoolean("TRUE"));
        assertTrue("1=true", CBoolean.parseBoolean("1"));
    }

    /**
     * Tests {@link CBoolean#parseBoolean(String)} with false values.
     */
    @Test
    public void testParseBooleanWithFalseValues() {
        assertFalse("false=false", CBoolean.parseBoolean("false"));
        assertFalse("False=false", CBoolean.parseBoolean("False"));
        assertFalse("FALSE=false", CBoolean.parseBoolean("FALSE"));
        assertFalse("0=false", CBoolean.parseBoolean("0"));
    }

    /**
     * Tests {@link CBoolean#parseBoolean(String)} with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanWithNullValue() {
        CBoolean.parseBoolean(null);
    }

    /**
     * Tests {@link CBoolean#parseBoolean(String)} with empty value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanWithEmptyValue() {
        CBoolean.parseBoolean("");
    }

    /**
     * Tests {@link CBoolean#parseBoolean(String)} with illegal value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanWithIllegalValue() {
        CBoolean.parseBoolean("yep");
    }
}
