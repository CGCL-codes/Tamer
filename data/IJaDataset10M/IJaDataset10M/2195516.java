package org.devyant.magicbeans.ui.swing;

import java.awt.Component;
import org.devyant.magicbeans.WrapperTestCase;

/**
 * StringComponentTest is a <b>cool</b> class.
 * 
 * @author ftavares
 * @version $Revision: 1.1 $ $Date: 2005/11/16 22:03:09 $ ($Author: ftavares $)
 * @since Jul 3, 2005 2:28:26 AM
 */
public class StringComponentTest extends WrapperTestCase {

    /**
     * Creates a new <code>StringComponentTest</code> instance.
     * @param name
     */
    public StringComponentTest(String name) {
        super(name);
    }

    /**
     * Tests the StringComponent
     * @throws Exception
     */
    public void testComponent() throws Exception {
        testComponent("Foo", SwingStringComponent.class, "Bar");
    }

    /**
     * @see org.devyant.magicbeans.AbstractTestCase#messWithComponent(java.awt.Component, java.lang.Object)
     */
    protected void messWithComponent(Component component, Object expected) {
        ((SwingStringComponent) component).setText((String) expected);
    }
}
