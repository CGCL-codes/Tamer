package cn.ac.ntarl.clb.utils;

import java.io.FileNotFoundException;
import cn.vlabs.clb.utils.UnCompress;
import junit.framework.TestCase;

public class UnCompressTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        util = new UnCompress("c:\\tmp");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testUnzipString() throws FileNotFoundException {
        util.unzip("c:\\tmp\\utils.zip");
    }

    private UnCompress util;
}
