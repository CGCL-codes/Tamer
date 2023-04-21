package com.oneandone.sushi.io;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class OSTest {

    @Test
    public void os() {
        assertNotNull(OS.CURRENT);
    }
}
