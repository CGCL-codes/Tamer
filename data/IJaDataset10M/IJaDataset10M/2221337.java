package org.zodiak.document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import junit.framework.TestCase;

/**
 * This base class handles creation and deletion of objects to be tested
 * by subclasses.
 * 
 * @author Steven R. Farley
 */
public abstract class OpenDocumentTestBase extends TestCase {

    private static final String SAMPLE_TEXT_DOC = "/SampleText.odt";

    protected File docFile;

    protected OpenDocumentFile openDoc;

    public OpenDocumentTestBase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        docFile = copyTestDocument();
        openDoc = new OpenDocumentFile(docFile);
    }

    protected void tearDown() throws Exception {
        openDoc.close();
        docFile.delete();
    }

    /**
   * Copies the sample text document, embedded as a resource, to a temporary
   * file.
   */
    protected static File copyTestDocument() throws Exception {
        File file = File.createTempFile("zodiak-test-", null);
        InputStream in = OpenDocumentFileTest.class.getResourceAsStream(SAMPLE_TEXT_DOC);
        OutputStream out = new FileOutputStream(file);
        int b;
        while ((b = in.read()) != -1) out.write(b);
        in.close();
        out.close();
        return file;
    }
}
