package net.sf.xqz.tests.engine.testcases;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

public class AlwaysTrueTestCase extends TestCase {

    public AlwaysTrueTestCase(String name) {
        super(name);
    }

    public void testAlwaysTrue() {
        Assert.assertTrue(true);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
