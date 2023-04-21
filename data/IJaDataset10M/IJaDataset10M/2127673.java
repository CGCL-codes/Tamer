package org.jlense.help;

import java.net.URL;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Program to test help set merging.
 * 
 * @author ted stockwell [emorning@sourceforge.net] 
 **/
public class ShowHelpTest extends TestCase {

    HelpSet _hsMaster;

    HelpSet _hsJLedger;

    /**
     * Launches this test using the JLense Eclipse-based TestRunner application.
     * The method merely delegates the call to the official Eclipse launcher,
     * org.eclipse.core.launcher.Main. 
     */
    public static void main(String[] args) {
        if (args == null) args = new String[0];
        String[] nargs = new String[args.length + 3];
        System.arraycopy(args, 0, nargs, 0, args.length);
        nargs[nargs.length - 3] = "-application";
        nargs[nargs.length - 2] = "org.jlense.test.TestRunner";
        nargs[nargs.length - 1] = ShowHelpTest.class.getName();
        org.eclipse.core.launcher.Main.main(nargs);
    }

    public static Test suite() {
        return new TestSuite(ShowHelpTest.class);
    }

    public ShowHelpTest(String name) {
        super(name);
    }

    /**
     * Loads all the help sets
     */
    protected void setUp() throws Exception {
        _hsMaster = loadHS("Master");
        _hsJLedger = loadHS("JLedger");
    }

    protected HelpSet loadHS(String name) throws Exception {
        ClassLoader cl = this.getClass().getClassLoader();
        URL urlHS = HelpSet.findHelpSet(cl, name);
        if (urlHS == null) fail("help set now found:" + name);
        return new HelpSet(cl, urlHS);
    }

    protected void tearDown() throws Exception {
    }

    /**
     * shows the merged help sets
     **/
    public void testHelpSetMerge() throws Exception {
        HelpSet master = loadHS("Master");
        HelpSet merge = loadHS("JLedger");
        master.add(merge);
        HelpBroker hb = master.createHelpBroker();
        hb.setDisplayed(true);
    }
}
