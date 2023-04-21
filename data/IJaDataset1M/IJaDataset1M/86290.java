package org.conann.util;

import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void isEmpty() {
        assertFalse(StringUtils.isEmpty("some silly string"));
        assertFalse(StringUtils.isEmpty("1"));
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(" "));
        assertTrue(StringUtils.isEmpty("   "));
        assertTrue(StringUtils.isEmpty(null));
    }

    @Test
    public void uncapitalize() {
        Assert.assertEquals("someThing", StringUtils.uncapitalize("SomeThing"));
        Assert.assertEquals("someThing", StringUtils.uncapitalize("someThing"));
        Assert.assertEquals("1Thing", StringUtils.uncapitalize("1Thing"));
    }
}
