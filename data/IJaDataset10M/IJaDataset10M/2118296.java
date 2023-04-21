package org.garret.ptl.startup.compression;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.io.ByteArrayInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;

/**
 * Tests the logic of lconfig detection and codepack config/resource merging in Configuration class.
 *
 * @author Andrey Subbotin
 */
@Ignore
public class TestCompression {

    public void test_js() throws Exception {
        String js = "var x=/*comment*/ 'abc';//comment2";
        byte[] data = new JsResourceCompressor().compress(new ByteArrayInputStream(js.getBytes("UTF-8")));
        String result = new String(data, "UTF-8");
        System.err.println("RES=" + result);
        Assert.assertEquals(result, "\nvar x='abc';");
    }

    public void test_css() throws Exception {
        String js = "cls {\n color:red; /*skip*/ left:expr('2+\"aa\"'); right:expr(\"'2+'aa'\")}";
        byte[] data = new CssResourceCompressor().compress(new ByteArrayInputStream(js.getBytes("UTF-8")));
        String result = new String(data, "UTF-8");
        System.err.println("RES=" + result);
        Assert.assertEquals(result, "cls{color:red;left:expr('2+\"aa\"');right:expr(\"'2+'aa'\")}\n");
    }
}
