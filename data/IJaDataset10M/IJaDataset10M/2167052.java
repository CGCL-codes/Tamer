package com.itzg.svn;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;

public class FileEntryTest {

    @Test
    public void testCtor() {
        File baseDir = new File("c:/tmp/baseDir");
        File fileWithin = new File(baseDir, "/A/B/C.txt");
        FileEntry entry = new FileEntry(baseDir, fileWithin);
        assertEquals("/A/B/C.txt", entry.getRelativePath());
    }
}
