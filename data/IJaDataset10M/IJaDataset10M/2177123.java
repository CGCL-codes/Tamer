package net.sf.jfling.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.sf.jfling.utils.FixedLengthUtils;

/**
 * $$Id: .java,v 1.0 ${date} PCL Exp $$ Copyright (c) 2008-2010 ING Groep N.V. All rights reserved. This software is the confidential and
 * proprietary information of ING Groep. You shall not disclose such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with ING.
 * 
 * @author Tom Rigole - SkillTeam
 * 
 */
public class FixedLengthUtilsTest {

    @Test
    public void testIsNull() {
        boolean result = FixedLengthUtils.isNull("000", 3, '0');
        assertTrue(result);
        result = FixedLengthUtils.isNull("000", 3, ' ');
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNullIllegalLength() {
        FixedLengthUtils.isNull("00", 3, '0');
    }

    @Test
    public void testNullValue() {
        String nullValue = FixedLengthUtils.nullValue(3, 'b');
        assertEquals("bbb", nullValue);
    }

    @Test
    public void testRemovePadding() {
        String input = "OOOOlahOlah";
        String output = FixedLengthUtils.removePadding(input, 'O');
        assertEquals("lahOlah", output);
        input = "lahOlah";
        output = FixedLengthUtils.removePadding(input, 'O');
        assertEquals("lahOlah", output);
    }
}
