package net.leegorous.jsc;

import java.io.File;
import org.apache.commons.io.FileUtils;

/**
 * @author leegorous
 * 
 */
public class JsFileTest extends JavaScriptFileTestSupport {

    /**
	 * @throws java.lang.Exception
	 */
    public void setUp() throws Exception {
    }

    public void testSynced() throws Exception {
        File file = new File(getFileName("/scripts/a.js"));
        JsFile js = new JsFile();
        js.setFile(file);
        js.refresh();
        assertTrue(js.synced());
        FileUtils.touch(file);
        assertTrue(!js.synced());
    }
}
