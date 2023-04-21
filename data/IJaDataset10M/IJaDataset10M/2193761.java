package org.klautor.client;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import jmunit.framework.cldc10.*;

/**
 * @author joey
 */
public class LoadedChooserTest extends TestCase {

    public LoadedChooserTest() {
        super(1, "LoadedChooserTest");
    }

    public void test(int testNumber) throws Throwable {
        switch(testNumber) {
            case 0:
                testCommandAction();
                break;
            default:
                break;
        }
    }

    /**
     * Test of testCommandAction method, of class LoadedChooser.
     */
    public void testCommandAction() throws AssertionFailedException {
        System.out.println("commandAction");
        LoadedChooser instance = null;
        Command c_1 = null;
        Displayable d_1 = null;
        instance.commandAction(c_1, d_1);
        fail("The test case is a prototype.");
    }
}
