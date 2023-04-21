package net.sf.bacchus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRtnEditor {

    private RtnEditor editor;

    @Before
    public void setUp() throws Exception {
        this.editor = new RtnEditor();
    }

    @Test
    public void testRtnPropertyEditorObject() {
        final Object source = new Object();
        final RtnEditor sourced = new RtnEditor(source);
        assertSame(source, sourced.getSource());
    }

    @Test
    public void testSetAsTextNull() {
        this.editor.setAsText(null);
        assertNull(this.editor.getValue());
    }

    @Test
    public void testSetAsTextEmpty() {
        this.editor.setAsText("");
        assertNull(this.editor.getValue());
    }

    @Test
    public void testSetAsTextWhitespace() {
        this.editor.setAsText(" ");
        assertNull(this.editor.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAsTextTooLong() {
        this.editor.setAsText("1234567890");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAsTextBadRtnFormat() {
        this.editor.setAsText("ABCDEFGH");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAsTextInvalid() {
        this.editor.setAsText("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAsTextShort() {
        this.editor.setAsText("1");
    }

    @Test
    public void testSetAsTextEight() {
        this.editor.setAsText("12345678");
        final Object value = this.editor.getValue();
        assertTrue(value instanceof Rtn);
        assertEquals(12345678, ((Rtn) value).getRtn());
    }

    @Test
    public void testSetAsTextNineValid() {
        this.editor.setAsText("123456780");
        final Object value = this.editor.getValue();
        assertTrue(value instanceof Rtn);
        assertEquals(12345678, ((Rtn) value).getRtn());
        assertEquals(0, ((Rtn) value).getCheck());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAsTextNineInvalid() {
        this.editor.setAsText("123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAsTextBadCheckFormat() {
        this.editor.setAsText("12345678A");
    }
}
