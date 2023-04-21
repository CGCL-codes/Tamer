package com.gnizr.db.dao.folder;

import java.util.List;
import junit.framework.TestCase;

public class TestFoldersParser extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParse() throws Exception {
        String ps = "folder1,folder2, aaa folder5";
        FoldersParser parser = new FoldersParser(ps);
        List<String> fs = parser.getFolderNames();
        assertEquals(3, fs.size());
        assertEquals("aaa folder5", fs.get(2));
    }
}
