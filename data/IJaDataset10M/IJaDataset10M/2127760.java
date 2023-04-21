package rat.document.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import junit.framework.TestCase;
import rat.document.UnreadableArchiveException;

public class SingularFileDocumentTest extends TestCase {

    MonolithicFileDocument document;

    File file;

    protected void setUp() throws Exception {
        super.setUp();
        file = new File("src/test/elements/Source.java");
        document = new MonolithicFileDocument(file);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReadArchive() throws Exception {
        try {
            document.readArchive();
            fail("Source is not archive: Exception should have been thrown");
        } catch (UnreadableArchiveException e) {
        }
    }

    public void testReader() throws Exception {
        Reader reader = document.reader();
        assertNotNull("Reader should be returned", reader);
        assertEquals("First file line expected", "package elements;", new BufferedReader(reader).readLine());
    }

    public void testGetName() {
        final String name = document.getName();
        assertNotNull("Name is set", name);
        assertEquals("Name is filename", "src/test/elements/Source.java", name);
    }
}
