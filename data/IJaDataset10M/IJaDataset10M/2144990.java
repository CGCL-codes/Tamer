package ch.skyguide.tools.requirement.hmi.openoffice;

import java.io.IOException;
import junit.framework.TestCase;
import ch.skyguide.tools.requirement.io.IoHelper;

public class OpenOfficeWriterDocumentTest extends TestCase {

    private OpenOfficeWriterDocument doc;

    private OpenOfficeContext ctx;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ctx = OpenOfficeManager.getContext();
    }

    @Override
    protected void tearDown() throws Exception {
        if (doc != null) {
            unload();
        }
        super.tearDown();
    }

    public void testIsEmpty() throws OpenOfficeException, IOException {
        load("empty.doc");
        assertTrue(doc.isEmpty());
    }

    public void testIsEmpty_TextOnly() throws OpenOfficeException, IOException {
        load("text.doc");
        assertFalse(doc.isEmpty());
    }

    public void testIsEmpty_GraphicsOnly() throws OpenOfficeException, IOException {
        for (int i : new int[] { 1, 2, 3, 4, 6 }) {
            load("5.1." + i + ".doc");
            assertFalse(doc.isEmpty());
            unload();
        }
    }

    private void load(String _suffix) throws OpenOfficeException, IOException {
        doc = OpenOfficeWriterDocument.loadDocument(ctx, IoHelper.readFully(getClass().getClassLoader().getResourceAsStream(getClass().getName().replace('.', '/') + "_data/" + _suffix)));
    }

    private void unload() {
        doc.dispose();
        doc = null;
    }
}
