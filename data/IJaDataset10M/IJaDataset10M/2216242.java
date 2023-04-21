package org.authorsite.email.util;

import org.authorsite.email.EmailFolder;
import junit.framework.TestCase;

public class MboxFileImporterTest extends TestCase {

    public void testSetupFolderParents() {
        MboxFileImporter i = new MboxFileImporter();
        String fileABC = "a-b-c";
        EmailFolder one = new EmailFolder();
        i.setupFolderParents(one, fileABC);
        assertEquals("/a/b/c", one.getPath());
        EmailFolder oneB = one.getParent();
        assertEquals("b", oneB.getName());
        EmailFolder oneA = oneB.getParent();
        assertEquals("a", oneA.getName());
        assertEquals(EmailFolder.ROOT, oneA.getParent());
        String fileABCD = "a-b-c-d";
        EmailFolder two = new EmailFolder();
        i.setupFolderParents(two, fileABCD);
        assertEquals("/a/b/c/d", two.getPath());
        String fileA = "a";
        EmailFolder three = new EmailFolder();
        i.setupFolderParents(three, fileA);
        assertEquals("/a", three.getPath());
    }

    public void testFileImport() throws Exception {
        MboxFileImporter i = new MboxFileImporter();
        i.processFile("email/mboxes/oldTest1", "jdbc:mysql://localhost:3306/authorsite?characterEncoding=utf8", "test", "test");
    }
}
