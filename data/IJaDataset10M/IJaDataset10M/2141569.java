package org.tagunit.test;

import junit.framework.TestCase;
import org.tagunit.DefaultTestContextContainer;
import org.tagunit.taginfo.TagInfo;

/**
 * Test cases for the InterfaceTestPackage class.
 *
 * @author      Simon Brown
 */
public class InterfaceTestPackageTest extends TestCase {

    private TagInfo tagInfo;

    public void setUp() {
        tagInfo = new TagInfo();
        tagInfo.setTagHandlerClass("org.tagunit.tagext.assertion.AssertAttributeTag");
    }

    /**
   * Tests asserting that the interface test package works.
   */
    public void testInterfaces() {
        InterfaceTestPackage testPackage = new InterfaceTestPackage(tagInfo, "javax.servlet.jsp.tagext.Tag");
        DefaultTestContextContainer context = new DefaultTestContextContainer();
        testPackage.executeTest(context);
        assertEquals(true, context.getResult());
        testPackage = new InterfaceTestPackage(tagInfo, "org.tagunit.tagext.assertion.AssertAttributeTag");
        context = new DefaultTestContextContainer();
        testPackage.executeTest(context);
        assertEquals(true, context.getResult());
        testPackage = new InterfaceTestPackage(tagInfo, "javax.swing.JFrame");
        context = new DefaultTestContextContainer();
        testPackage.executeTest(context);
        assertEquals(false, context.getResult());
    }
}
