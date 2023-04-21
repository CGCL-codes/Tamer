package com.goodcodeisbeautiful.archtea.util;

import java.io.File;
import com.goodcodeisbeautiful.test.util.CommonTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author hata
 *
 */
public class ArchteaUtilTestCase extends CommonTestCase {

    public static Test suite() {
        return new TestSuite(ArchteaUtilTestCase.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        cleanWorkFiles();
    }

    public void testCopyResourceTo() throws Exception {
        String destFilePath = getWorkDir() + File.separator + "copy-resource.txt";
        File f = new File(destFilePath);
        assertFalse(f.exists());
        ArchteaUtil.copyResourceTo(this.getClass(), "sample-resource.txt", destFilePath);
        assertTrue(f.exists());
        assertEquals("sample-resource.txt", getContents("copy-resource.txt").trim());
    }

    public void testGetFilePathFromBaseFile() throws Exception {
        File dir = new File(getWorkDir() + File.separator + "workdir");
        assertTrue(dir.mkdirs());
        File f = new File(dir.getCanonicalPath() + File.separator + "foo.txt");
        String fooPath = ArchteaUtil.getFilePathFromBaseFile(f, ".." + File.separator + "bar.txt");
        File fooFile = new File(fooPath);
        assertTrue(fooFile.isAbsolute());
        assertEquals(getWorkDir() + File.separator + "bar.txt", fooFile.getCanonicalPath());
        String absolutePath = getWorkDir() + File.separator + "hoge.txt";
        String absPath = ArchteaUtil.getFilePathFromBaseFile(f, absolutePath);
        File absFile = new File(absPath);
        assertTrue(absFile.isAbsolute());
        assertEquals(absolutePath, absFile.getCanonicalPath());
    }

    public void testGetFilePathFromBaseFile_ForFolder() throws Exception {
        File f = new File(getWorkDir());
        assertTrue(f.mkdirs());
        String fooPath = ArchteaUtil.getFilePathFromBaseFile(f, "bar.txt");
        assertEquals("Check baseFile is a directory.", new File(getWorkDir() + File.separator + "bar.txt").getCanonicalPath(), new File(fooPath).getCanonicalPath());
    }

    public void testGetHtmlCharEncoding() {
        String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals("UTF-8", ArchteaUtil.getHtmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
        assertEquals("UTF-8", ArchteaUtil.getHtmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "<?xml version='1.0' encoding='utf-8' ?>";
        assertEquals("UTF-8", ArchteaUtil.getHtmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />";
        assertEquals("UTF-8", ArchteaUtil.getHtmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "<meta content='text/html; charset=UTF-8' http-equiv='Content-Type' />";
        assertEquals("UTF-8", ArchteaUtil.getHtmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "<meta content='text/html; charset=UTF-8' http-equiv='Content-Type'>";
        assertEquals("UTF-8", ArchteaUtil.getHtmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
        assertEquals("UTF-8", ArchteaUtil.getHtmlCharEncoding(sample.getBytes(), null).toUpperCase());
    }

    public void testGetXmlCharEncoding() {
        String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals("UTF-8", ArchteaUtil.getXmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
        assertEquals("UTF-8", ArchteaUtil.getXmlCharEncoding(sample.getBytes(), null).toUpperCase());
        sample = "  <?xml version=\'1.0\' ?>";
        assertEquals("UTF-8", ArchteaUtil.getXmlCharEncoding(sample.getBytes()).toUpperCase());
    }

    public void testGetNamespace() {
        assertEquals("nsvalue", ArchteaUtil.getXmlNamespace("foo", "<?xml version=\"1.0\"?> <foo xmlns=\"nsvalue\">".getBytes()));
        assertEquals("nsvalue", ArchteaUtil.getXmlNamespace("foo", "<?xml version=\"1.0\"?> <hoge:foo xmlns:hoge=\"nsvalue\">".getBytes()));
        assertEquals("nsvalue", ArchteaUtil.getXmlNamespace("foo", "<?xml version=\"1.0\"?> <hoge:foo xmlns=\"notgood\" xmlns:hoge=\"nsvalue\">".getBytes()));
        assertEquals("nsvalue", ArchteaUtil.getXmlNamespace("foo", "<?xml version=\"1.0\"?> <hoge:foo xmlns=\'notgood\' xmlns:hoge=\'nsvalue\'>".getBytes()));
    }
}
