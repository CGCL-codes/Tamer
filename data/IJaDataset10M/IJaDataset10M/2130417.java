package com.evaserver.rof.ext;

import junit.framework.TestCase;
import com.evaserver.rof.script.Window;
import com.evaserver.rof.test.ScriptTester;

/**
 *
 *
 * @author Max Antoni
 * @version $Revision: 176 $
 */
public class ExcludedExtScriptTesterTest extends TestCase {

    public void testExt30() {
        verifyVersion("3.0");
    }

    public void testExt311() {
        verifyVersion("3.1.1");
    }

    public void testExt321() {
        verifyVersion("3.2.1");
    }

    private void verifyVersion(String inVersion) {
        ScriptTester scriptTester = new ScriptTester();
        scriptTester.setClass(getClass());
        scriptTester.setLibraries(new String[] { "ext-base-" + inVersion, "ext-all-" + inVersion });
        Window window = scriptTester.createWindow();
        assertEquals(inVersion, scriptTester.eval(window, "Ext.version"));
    }
}
