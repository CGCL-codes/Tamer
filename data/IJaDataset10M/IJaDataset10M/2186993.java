package net.sf.javadc.tasks;

import junit.framework.TestCase;
import net.sf.javadc.config.ConstantSettings;

/**
 * @author Timo Westk�mper
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ClientTaskFactoryTest extends TestCase {

    /**
     * Constructor for ClientTaskFactoryTest.
     * 
     * @param arg0
     */
    public ClientTaskFactoryTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testClassCreation() throws Exception {
        Class cl = null;
        try {
            cl = Class.forName(ConstantSettings.CLIENTTASKFACTORY_PREFIX + "ILock" + ConstantSettings.CLIENTTASKFACTORY_POSTFIX);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        assertTrue(cl != null);
    }
}
