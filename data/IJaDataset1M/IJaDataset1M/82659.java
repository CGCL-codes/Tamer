package com.mainatom.utils;

import junit.framework.*;
import org.apache.commons.vfs.*;
import java.io.*;

public class FileScannerVFSTest extends TestCase {

    private String create1() throws IOException {
        File root = new File("temp/scanner");
        root.mkdirs();
        JpFileUtils.cleanDirectory(root);
        cr(root, "file1.txt");
        cr(root, "file2.txt");
        cr(root, "file3.dat");
        cr(root, ".svn");
        cr(root, "CVS");
        cr(root, ".svn/file1.txt");
        cr(root, "CVS/file2.txt");
        cr(root, "dir1/file11.txt");
        cr(root, "dir1/file12.txt");
        cr(root, "dir1/dir11/file111.txt");
        cr(root, "dir1/dir11/file112.txt");
        cr(root, "dir2/file21.txt");
        cr(root, "dir2/file22.txt");
        return VFS.getManager().resolveFile(root.getAbsolutePath()).toString();
    }

    private void cr(File root, String dir) throws IOException {
        File f = new File(root, dir);
        if (f.getName().indexOf('.') < 1) {
            f.mkdirs();
        } else {
            f.getParentFile().mkdirs();
            FileWriter wr = new FileWriter(f);
            wr.close();
        }
    }

    public void testDefault() throws Exception {
        FileScannerVFS x = new FileScannerVFS();
        x.setRootDir(create1());
        x.scan();
        assertEquals(x.getFiles().size(), 9);
        assertEquals(x.getFiles().get(0).getName().getBaseName(), "file1.txt");
        assertEquals(x.getDirs().size(), 3);
        assertEquals(x.getDirs().get(0).getName().getBaseName(), "dir1");
    }

    public void testNotRecursive() throws Exception {
        FileScannerVFS x = new FileScannerVFS();
        x.setRootDir(create1());
        x.setRecursive(false);
        x.scan();
        assertEquals(x.getFiles().size(), 3);
        assertEquals(x.getFiles().get(0).getName().getBaseName(), "file1.txt");
        assertEquals(x.getDirs().size(), 2);
        assertEquals(x.getDirs().get(0).getName().getBaseName(), "dir1");
    }

    public void testMaskFile() throws Exception {
        FileScannerVFS x = new FileScannerVFS();
        x.setRootDir(create1());
        x.getIncludeFile().add("*.txt");
        x.getExcludeFile().add("*1*.*");
        x.scan();
        assertEquals(x.getFiles().size(), 2);
        assertEquals(x.getFiles().get(0).getName().getBaseName(), "file2.txt");
        assertEquals(x.getDirs().size(), 3);
        assertEquals(x.getDirs().get(0).getName().getBaseName(), "dir1");
    }
}
