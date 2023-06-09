package org.apache.harmony.luni.tests.java.io;

import java.io.File;
import org.apache.harmony.luni.internal.io.FileCanonPathCache;
import junit.framework.TestCase;

public class FileCanonPathCacheTest extends TestCase {

    private static int DEFAULT_TIMEOUT = 600000;

    @Override
    public void setUp() throws Exception {
        FileCanonPathCache.clear();
        FileCanonPathCache.setTimeout(DEFAULT_TIMEOUT);
    }

    public void testGetSet() throws Exception {
        File file1 = new File("test/hello~1");
        assertNull(FileCanonPathCache.get(file1.getAbsolutePath()));
        FileCanonPathCache.put(file1.getAbsolutePath(), file1.getCanonicalPath());
        assertEquals(file1.getCanonicalPath(), FileCanonPathCache.get(file1.getAbsolutePath()));
        File file2 = new File("test/world~1");
        assertNull(FileCanonPathCache.get(file2.getAbsolutePath()));
        FileCanonPathCache.put(file2.getAbsolutePath(), file2.getCanonicalPath());
        assertEquals(file2.getCanonicalPath(), FileCanonPathCache.get(file2.getAbsolutePath()));
        assertNull(FileCanonPathCache.get("notexist"));
    }

    public void testGetTimeout01() throws Exception {
        FileCanonPathCache.setTimeout(10);
        File file1 = new File("test/hello~1");
        assertNull(FileCanonPathCache.get(file1.getAbsolutePath()));
        FileCanonPathCache.put(file1.getAbsolutePath(), file1.getCanonicalPath());
        Thread.sleep(50);
        assertNull(FileCanonPathCache.get(file1.getAbsolutePath()));
    }

    public void testGetTimeout02() throws Exception {
        FileCanonPathCache.setTimeout(10);
        File file1 = new File("test/hello~1");
        assertNull(FileCanonPathCache.get(file1.getAbsolutePath()));
        FileCanonPathCache.put(file1.getAbsolutePath(), file1.getCanonicalPath());
        File file2 = new File("test/hello~2");
        assertNull(FileCanonPathCache.get(file2.getAbsolutePath()));
        FileCanonPathCache.put(file2.getAbsolutePath(), file2.getCanonicalPath());
        File file3 = new File("test/hello~3");
        assertNull(FileCanonPathCache.get(file3.getAbsolutePath()));
        FileCanonPathCache.put(file3.getAbsolutePath(), file3.getCanonicalPath());
        File file4 = new File("test/hello~4");
        assertNull(FileCanonPathCache.get(file4.getAbsolutePath()));
        FileCanonPathCache.put(file4.getAbsolutePath(), file4.getCanonicalPath());
        File file5 = new File("test/hello~5");
        assertNull(FileCanonPathCache.get(file5.getAbsolutePath()));
        FileCanonPathCache.put(file5.getAbsolutePath(), file5.getCanonicalPath());
        Thread.sleep(50);
        assertNull(FileCanonPathCache.get(file1.getAbsolutePath()));
        assertNull(FileCanonPathCache.get(file2.getAbsolutePath()));
        assertNull(FileCanonPathCache.get(file3.getAbsolutePath()));
        assertNull(FileCanonPathCache.get(file4.getAbsolutePath()));
        assertNull(FileCanonPathCache.get(file5.getAbsolutePath()));
    }

    public void testTimeout03() throws Exception {
        FileCanonPathCache.setTimeout(10);
        File file = new File("1");
        FileCanonPathCache.put(file.getAbsolutePath(), file.getAbsolutePath());
        file = new File("2");
        FileCanonPathCache.put(file.getAbsolutePath(), file.getAbsolutePath());
        file = new File("3");
        FileCanonPathCache.put(file.getAbsolutePath(), file.getAbsolutePath());
        Thread.sleep(100);
        FileCanonPathCache.get(file.getAbsolutePath());
        assertNull(FileCanonPathCache.get(new File("1").getAbsolutePath()));
        assertNull(FileCanonPathCache.get(new File("2").getAbsolutePath()));
    }

    public void testCacheFull() throws Exception {
        int cacheSize = FileCanonPathCache.CACHE_SIZE;
        File[] files = new File[cacheSize];
        for (int i = 0; i < cacheSize; ++i) {
            files[i] = new File("test/world" + i);
            FileCanonPathCache.put(files[i].getAbsolutePath(), files[i].getCanonicalPath());
        }
        for (int i = cacheSize; i < files.length; ++i) {
            assertEquals(files[i - cacheSize].getCanonicalPath(), FileCanonPathCache.get(files[i - cacheSize].getAbsolutePath()));
            files[i] = new File("test/world" + i);
            FileCanonPathCache.put(files[i].getAbsolutePath(), files[i].getCanonicalPath());
            assertEquals(files[i].getCanonicalPath(), FileCanonPathCache.get(files[i].getAbsolutePath()));
            assertNull(FileCanonPathCache.get(files[i - cacheSize].getAbsolutePath()));
        }
    }
}
