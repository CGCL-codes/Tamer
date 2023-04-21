package org.jostraca.resource.test;

import org.jostraca.resource.PartLoader;
import org.jostraca.util.FileUtil;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.io.File;

/** Test cases for org.jostraca.resource.SimpleDataObject
 */
public class PartLoaderTest extends TestCase {

    public PartLoaderTest(String pName) {
        super(pName);
    }

    public static TestSuite suite() {
        return new TestSuite(PartLoaderTest.class);
    }

    public static void main(String[] pArgs) {
        TestRunner.run(suite());
    }

    public void testLoad() throws Exception {
        PartLoader pl = new PartLoader("<!--part-begin:", "-->", "<!--part-end:", "-->");
        File f = FileUtil.findFile("org/jostraca/resource/test/partloader.txt");
        String foo = pl.load(f, "foo");
        assertEquals(5, foo.length());
        String bar = pl.load(f, "bar");
        assertEquals(53, bar.length());
    }
}
