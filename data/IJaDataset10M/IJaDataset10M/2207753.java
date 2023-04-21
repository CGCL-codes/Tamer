package javax.swing.text;

import javax.swing.BasicSwingTestCase;
import javax.swing.SerializableTestCase;
import javax.swing.text.AbstractDocumentTest.DisAbstractedDocument;
import org.apache.harmony.x.swing.StringConstants;

public class AbstractDocument_SerializationTest extends SerializableTestCase {

    private AbstractDocument doc;

    private final String key = "key";

    private final String value = "value";

    private final String text = "text\n" + AbstractDocument_UpdateTest.RTL;

    @Override
    protected void setUp() throws Exception {
        toSave = doc = new DisAbstractedDocument(new GapContent());
        doc.insertString(0, text, null);
        doc.putProperty(key, value);
        doc.setDocumentFilter(new AbstractDocument_FilterTest.Filter());
        super.setUp();
    }

    @Override
    public void testSerializable() throws BadLocationException {
        AbstractDocument restored = (AbstractDocument) toLoad;
        assertEquals(text, restored.getText(0, restored.getLength()));
        assertEquals(value, restored.getProperty(key));
        assertEquals(Boolean.TRUE, restored.getProperty(BasicSwingTestCase.isHarmony() ? StringConstants.BIDI_PROPERTY : "i18n"));
        if (BasicSwingTestCase.isHarmony()) {
            AbstractDocument_ListenerTest listener = new AbstractDocument_ListenerTest();
            restored.addDocumentListener(listener);
            restored.addUndoableEditListener(listener);
            restored.insertString(0, "234", null);
            assertEquals("12345" + text, restored.getText(0, restored.getLength()));
            assertNotNull(listener.insert);
            assertNotNull(listener.undo);
            assertEquals(0, restored.getStartPosition().getOffset());
            assertEquals(restored.getLength() + 1, restored.getEndPosition().getOffset());
            restored.readLock();
            restored.readUnlock();
        }
    }
}
