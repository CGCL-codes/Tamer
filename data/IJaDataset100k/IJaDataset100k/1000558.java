package com.threerings.jpkg.debian;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PackageMaintainerTest {

    @Test
    public void testValidAddress() throws Exception {
        final PackageMaintainer maintainer = new PackageMaintainer("Test user", "test@test.com");
        assertEquals("Test user <test@test.com>", maintainer.getFieldValue());
    }

    @Test(expected = ControlDataInvalidException.class)
    public void testInvalidAddress() throws Exception {
        new PackageMaintainer("Test user", "broken@@brokencom");
    }

    @Test(expected = ControlDataInvalidException.class)
    public void testInvalidName() throws Exception {
        new PackageMaintainer("Test.user", "test@test.com");
    }
}
