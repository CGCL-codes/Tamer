package net.sf.gumshoe.indexer;

import java.io.*;
import junit.framework.TestCase;

/**
 * @author Gabor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiskIndexerTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DiskIndexerTest.class);
    }

    /**
	 * @param name
	 */
    public DiskIndexerTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testIndexing() throws Exception {
        File indexDir = new File("src/test/testindex");
        File dataDir = new File("src/test/testdata");
        new DiskIndexer().index(indexDir, dataDir, true);
    }
}
