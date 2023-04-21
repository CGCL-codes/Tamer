package net.sourceforge.purrpackage.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FileHelperTest extends Assert {

    FileHelper fileHelper = new FileHelper();

    @Test
    public void testRmdirRec() throws IOException {
        File d = new File(System.getProperty("java.io.tmpdir"), "net.sourceforge.purrpackage.reporting.FileHelperTest");
        d.mkdirs();
        File q = new File(d, "zot");
        q.createNewFile();
        assertTrue(q.exists());
        File x = new File(d, "uffda") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean delete() {
                super.delete();
                return false;
            }
        };
        assertTrue(x.createNewFile());
        FileHelper.RmRecResult r = fileHelper.rmRec(x);
        assertEquals(r.getFailures().size(), 1);
        fileHelper.rmRec(d);
        assertFalse(d.exists());
    }

    @Test
    public void testPathPossibleValid() {
        assertFalse(fileHelper.isPathPossiblyValid(null));
        assertFalse(fileHelper.isPathPossiblyValid(""));
        assertTrue(fileHelper.isPathPossiblyValid("?"));
    }

    @Test
    public void testBizarreCopyLogic() throws Exception {
        final AtomicBoolean copied = new AtomicBoolean(false);
        fileHelper = new FileHelper() {

            @Override
            public void copyFile(File a, File b) throws IOException {
                copied.set(true);
                super.copyFile(a, b);
            }
        };
        File d = new File(System.getProperty("java.io.tmpdir"));
        File src = new File(d, "FileHelperTestSrcTmp");
        src.mkdirs();
        File dest = new File(d, "FileHelperTestDestTmp");
        if (dest.exists()) {
            fileHelper.rmRec(dest);
        }
        dest.mkdirs();
        File a = new File(src, "a");
        FileOutputStream fos = new FileOutputStream(a);
        fos.write("zoinks".getBytes());
        fos.flush();
        fos.close();
        File b = new File(dest, "a");
        fileHelper.copyFileOrFileInDirectoryIfExists(null, "a", dest);
        assertFalse(copied.get());
        fileHelper.copyFileOrFileInDirectoryIfExists(src.getAbsolutePath(), "z", dest);
        assertFalse(copied.get());
        fileHelper.copyFileOrFileInDirectoryIfExists(src.getAbsolutePath(), "a", dest);
        assertTrue(copied.get());
        assertTrue(b.exists());
        assertTrue(b.delete());
        assertFalse(b.exists());
        fileHelper.copyFileOrFileInDirectoryIfExists(src.getAbsolutePath() + "/a", "a", dest);
        assertTrue(b.exists());
        fileHelper.rmRec(src);
        fileHelper.rmRec(dest);
    }
}
