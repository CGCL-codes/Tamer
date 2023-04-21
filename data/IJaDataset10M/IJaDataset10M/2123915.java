package com.evaserver.rof;

import junit.framework.TestCase;
import com.evaserver.rof.script.ClientReference;
import com.evaserver.rof.script.Window;

/**
 *
 *
 * @author Max Antoni
 * @version $Revision: 36 $
 */
public class TypeMapTest extends TestCase {

    private WindowManager windowManager = WindowManagerFactory.newInstance();

    public static class TestRemote {

        public String getFoo() {
            return "Dummy";
        }
    }

    public void testTypeMap() {
        Window window = windowManager.createWindow();
        ClientReference ref = window.newInstanceWithRemote("TypeMap", new TestRemote());
        ref.call("render");
        assertEquals("H1", window.execute("document.documentElement.firstChild.nextSibling.nextSibling.tagName"));
    }
}
