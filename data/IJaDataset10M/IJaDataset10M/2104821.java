package test;

import org.junit.*;
import static org.junit.Assert.*;
import org.fest.swing.fixture.*;
import org.gjt.sp.jedit.testframework.Log;
import org.gjt.sp.jedit.SplitConfigParser;

public class SplitConfigParserTest {

    /**
     * Test the simple case where no user settings are applied. The parsed split
     * config string should be identical to the unparsed string.
     */
    @Test
    public void test1() {
        String splitConfig = "\"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/MiscUtilities.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/MiscUtilities.java\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/View.java\" buff \"view\" bufferset";
        SplitConfigParser parser = new SplitConfigParser(splitConfig);
        String after = parser.parse();
        assertEquals(splitConfig, after);
    }

    /**
     * Test the case where the user has elected to not restore files.
     */
    @Test
    public void test2() {
        String splitConfig = "\"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/MiscUtilities.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/MiscUtilities.java\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/View.java\" buff \"view\" bufferset";
        SplitConfigParser parser = new SplitConfigParser(splitConfig);
        parser.setIncludeFiles(false);
        String after = parser.parse();
        String shouldBe = "\"Untitled-1\" buffer \"view\" bufferset";
        assertEquals(shouldBe, after);
    }

    /**
     * Similar to test1, but this split configuration includes actual split
     * pane data. That data should not be lost.
     */
    @Test
    public void test3() {
        String splitConfig = "\"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 187 vertical \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 485 horizontal \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 384 vertical";
        SplitConfigParser parser = new SplitConfigParser(splitConfig);
        String after = parser.parse();
        assertEquals(splitConfig, after);
    }

    /**
     * Similar to test3, except in this test, the user has elected to not restore
     * files. The split data should not be lost.
     */
    @Test
    public void test4() {
        String splitConfig = "\"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 187 vertical \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 485 horizontal \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 384 vertical";
        SplitConfigParser parser = new SplitConfigParser(splitConfig);
        parser.setIncludeFiles(false);
        String after = parser.parse();
        String shouldBe = "\"Untitled-1\" buffer \"view\" bufferset \"Untitled-1\" buffer \"view\" bufferset 187 vertical \"Untitled-1\" buffer \"view\" bufferset 485 horizontal \"Untitled-1\" buffer \"view\" bufferset 384 vertical";
        assertEquals(shouldBe, after);
    }

    /**
     * In this test, both file names and split pane data are to be removed.
     */
    @Test
    public void test5() {
        String splitConfig = "\"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 187 vertical \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 485 horizontal \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"view\" bufferset 384 vertical";
        SplitConfigParser parser = new SplitConfigParser(splitConfig);
        parser.setIncludeFiles(false);
        parser.setIncludeSplits(false);
        String after = parser.parse();
        String shouldBe = "\"Untitled-1\" buffer \"view\" bufferset";
        assertEquals(shouldBe, after);
    }

    /**
     * This test checks that only remote files are removed from the split config
     * string. Note that 'sftp' won't work here because that would require that
     * the FTP plugin be installed, so I changed all the sftp references to ftp
     * so this test will pass.
     */
    @Test
    public void test6() {
        String splitConfig = "\"ftp://daleanson,dbconsole@web.sourceforge.net:22/home/groups/d/db/dbconsole/htdocs/README.TXT\" buffer \"/home/danson/.jedit/perspective.xml\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"ftp://daleanson,dbconsole@web.sourceforge.net:22/home/groups/d/db/dbconsole/htdocs/README.TXT\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset \"/home/danson/tmp/test/SplitConfigParserTest.java\" buffer \"/home/danson/.jedit/perspective.xml\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"ftp://daleanson,dbconsole@web.sourceforge.net:22/home/groups/d/db/dbconsole/htdocs/README.TXT\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset 128 vertical \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/.jedit/perspective.xml\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"ftp://daleanson,dbconsole@web.sourceforge.net:22/home/groups/d/db/dbconsole/htdocs/README.TXT\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset 565 horizontal \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buffer \"/home/danson/.jedit/perspective.xml\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"ftp://daleanson,dbconsole@web.sourceforge.net:22/home/groups/d/db/dbconsole/htdocs/README.TXT\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset 267 vertical";
        SplitConfigParser parser = new SplitConfigParser(splitConfig);
        parser.setIncludeRemoteFiles(false);
        String after = parser.parse();
        String shouldBe = "\"/home/danson/.jedit/perspective.xml\" buffer \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset \"/home/danson/tmp/test/SplitConfigParserTest.java\" buffer \"/home/danson/.jedit/perspective.xml\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset 128 vertical \"/home/danson/tmp/test/SplitConfigParser.java\" buffer \"/home/danson/.jedit/perspective.xml\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset 565 horizontal \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buffer \"/home/danson/.jedit/perspective.xml\" buff \"/home/danson/src/jedit/jEdit/org/gjt/sp/jedit/PerspectiveManager.java\" buff \"/home/danson/tmp/test/SplitConfigParser.java\" buff \"/home/danson/tmp/test/SplitConfigParserTest.java\" buff \"view\" bufferset 267 vertical";
        Log.log("   after: " + after);
        Log.log("expected: " + shouldBe);
        assertEquals(shouldBe, after);
    }
}
