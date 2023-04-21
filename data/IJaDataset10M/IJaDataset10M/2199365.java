package org.apache.harmony.luni.tests.java.util;

import java.util.FormattableFlags;
import junit.framework.TestCase;

public class FormattableFlagsTest extends TestCase {

    /**
     * @test java.util.FormattableFlags ConstantFieldValues
     */
    public void test_ConstantFieldValues() {
        assertEquals(1, FormattableFlags.LEFT_JUSTIFY);
        assertEquals(2, FormattableFlags.UPPERCASE);
        assertEquals(4, FormattableFlags.ALTERNATE);
    }
}
