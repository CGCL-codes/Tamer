package org.jomper.test.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AddTest {

    private String x = "Buy";

    private String i = "Effective Java!";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addTest() {
        x = x + i;
    }

    @Test
    public void addTestPlus() {
        x += i;
        System.out.println('1' + '9');
    }

    @After
    public void tearDown() throws Exception {
    }
}
