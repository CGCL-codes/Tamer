package net.sf.poormans.tool.filetransfer;

import static org.junit.Assert.*;
import net.sf.poormans.tool.connection.PathAnalyzer;
import org.junit.Before;
import org.junit.Test;

public class FileTransferTarget_Test_01 {

    private PathAnalyzer ftt;

    @Before
    public void setUp() throws Exception {
        ftt = new PathAnalyzer("a/b/c/name.txt");
    }

    @Test
    public final void testGetDir() {
        assertEquals(ftt.getDir(), "a/b/c");
    }

    @Test
    public final void testGetName() {
        assertEquals(ftt.getName(), "name.txt");
    }

    @Test
    public final void testGetPath() {
        assertEquals(ftt.getPath(), "a/b/c/name.txt");
    }
}
