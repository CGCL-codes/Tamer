package com.ctrcv.framework.persistence;

import java.io.ByteArrayInputStream;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import junit.framework.TestCase;

public class TestS3Persistence extends TestCase {

    S3FilePersistence persister = null;

    public static String TestFilePath = "testS3/TestS3Persistence/upload.txt";

    public static String TestFileDir = "/testS3";

    @BeforeTest(groups = { "functional" })
    public void setUp() throws Exception {
        persister = new S3FilePersistence("ctrcv-test", System.getProperty("AWS_KEY"), System.getProperty("AWS_SECRET_KEY"));
        persister.delete(TestFileDir);
    }

    @Test(groups = { "functional" })
    public void testS3Upload() throws PersistenceException {
        String content = "hello,world";
        persister.write(TestFilePath, new ByteArrayInputStream(content.getBytes()));
        String[] subs = persister.list(TestFileDir);
        assertTrue(subs.length > 0);
        String s = new String(persister.read(TestFilePath));
        assertEquals(content, s);
    }
}
