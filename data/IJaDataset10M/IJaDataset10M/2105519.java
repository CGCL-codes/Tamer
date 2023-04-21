package org.cleartk.classifier.feature.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * <br>
 * Copyright (c) 2010, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philip Ogren
 * 
 */
public class NumericTypeUtilTest {

    @Test
    public void testContainsDigits() throws Exception {
        assertTrue(NumericTypeUtil.containsDigits("1"));
        assertTrue(NumericTypeUtil.containsDigits("1aa1"));
        assertTrue(NumericTypeUtil.containsDigits("11234"));
        assertTrue(NumericTypeUtil.containsDigits("oo0"));
        assertTrue(NumericTypeUtil.containsDigits("5-!@#$!@#$"));
        assertTrue(NumericTypeUtil.containsDigits("!@#$!@#$4"));
        assertTrue(NumericTypeUtil.containsDigits("    asdf asd 4 asdff aasdf "));
        assertFalse(NumericTypeUtil.containsDigits("l"));
        assertFalse(NumericTypeUtil.containsDigits("aa"));
        assertFalse(NumericTypeUtil.containsDigits("asdfasdf"));
        assertFalse(NumericTypeUtil.containsDigits("ooO"));
        assertFalse(NumericTypeUtil.containsDigits("!@#$!@#$"));
        assertFalse(NumericTypeUtil.containsDigits("    asdf asd asdff aasdf "));
        assertFalse(NumericTypeUtil.containsDigits(""));
    }

    @Test
    public void testIsDigits() throws Exception {
        assertTrue(NumericTypeUtil.isDigits("1"));
        assertFalse(NumericTypeUtil.isDigits("1aa1"));
        assertTrue(NumericTypeUtil.isDigits("11234"));
        assertFalse(NumericTypeUtil.isDigits("oo0"));
        assertFalse(NumericTypeUtil.isDigits("5-!@#$!@#$"));
        assertFalse(NumericTypeUtil.isDigits("!@#$!@#$4"));
        assertFalse(NumericTypeUtil.isDigits("    asdf asd 4 asdff aasdf "));
        assertTrue(NumericTypeUtil.isDigits("0123456789"));
    }
}
