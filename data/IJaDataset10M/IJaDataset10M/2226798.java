package org.devyant.magicbeans.ui.swing;

import java.awt.Component;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import org.devyant.magicbeans.WrapperTestCase;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.ClassMatcher;

/**
 * FileComponentTest is a <b>cool</b> class.
 * 
 * @author ftavares
 * @version $Revision: 1.1 $ $Date: 2005/11/16 22:03:09 $ ($Author: ftavares $)
 * @since Jul 4, 2005 5:13:23 AM
 */
public class FileComponentTest extends WrapperTestCase {

    /**
     * Creates a new <code>FileComponentTest</code> instance.
     * @param testName
     */
    public FileComponentTest(String testName) {
        super(testName);
    }

    /**
     * Tests the SwingFileComponent
     * @throws Exception
     */
    public void testComponent() throws Exception {
        testComponent(new File("src/conf/magic.properties"), SwingFileComponent.class, new File("src/conf/magic.xml").getAbsoluteFile());
    }

    /**
     * @throws MultipleComponentsFoundException 
     * @throws ComponentNotFoundException 
     * @see org.devyant.magicbeans.AbstractTestCase#messWithComponent(java.awt.Component, java.lang.Object)
     */
    protected void messWithComponent(Component component, Object expected) throws ComponentNotFoundException, MultipleComponentsFoundException {
        final Component button = getFinder().find(new Matcher() {

            public boolean matches(Component c) {
                return AbstractButton.class.isAssignableFrom(c.getClass()) && c.getParent().getClass() == SwingFileComponent.class;
            }
        });
        tester.actionClick(button);
        final JFileChooser fileChooser = (JFileChooser) getFinder().find(null, new ClassMatcher(JFileChooser.class));
        fileChooser.setSelectedFile(((File) expected));
        fileChooser.approveSelection();
    }
}
