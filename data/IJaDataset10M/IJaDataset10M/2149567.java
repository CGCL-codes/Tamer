package com.google.devtools.build.wireless.testing.java.injector;

import com.google.devtools.build.wireless.testing.java.injector.InclusionSelector;
import junit.framework.TestCase;

/**
 * @author Michele Sama
 *
 */
public class BaseSelectorTest extends TestCase {

    private InclusionSelector selector;

    /**
   * @param name
   */
    public BaseSelectorTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        selector = new InclusionSelector(false);
    }

    /**
   * Test method for 
   * {@link InclusionSelector#getMostSpecificAction(String)}
   * .
   */
    public void testGetMostSpecificAction() {
        selector.put("com.google", Boolean.FALSE);
        selector.put("com.google.foo", Boolean.TRUE);
        selector.put("com.google.foo.Bar", Boolean.FALSE);
        assertTrue("com.google.asd", !selector.getMostSpecificAction("com.google.asd"));
        assertTrue("com.google.foo.asd", selector.getMostSpecificAction("com.google.foo.asd"));
        assertTrue("com.google.foo.Bar", !selector.getMostSpecificAction("com.google.foo.Bar"));
    }
}
