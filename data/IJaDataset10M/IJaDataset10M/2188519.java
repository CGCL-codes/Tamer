package astcentric.editor.common.view.text;

import astcentric.editor.common.view.graphic.Rectangle;
import astcentric.editor.common.view.graphic.VisualContainerTestCase;
import astcentric.editor.common.view.graphic.context.Color;
import astcentric.editor.common.view.graphic.context.Font;
import astcentric.editor.common.view.graphic.context.FontStyle;
import astcentric.editor.common.view.graphic.context.MockGraphicsContext;
import astcentric.editor.common.view.text.DefaultTextElementFactory;
import astcentric.editor.common.view.text.TextElement;
import astcentric.editor.common.view.text.TextElementFactory;

public class DefaultTextElementFactoryTest extends VisualContainerTestCase {

    private static final Color COLOR1 = Color.create(128, 64, 32);

    private static final Color COLOR2 = Color.create(128, 64, 32);

    private static final Font FONT1 = Font.create("sf", FontStyle.BOLD, 1);

    private static final Font FONT2 = Font.create("sf", FontStyle.BOLD, 1);

    private TextElementFactory _factory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _factory = new DefaultTextElementFactory();
    }

    public void testCreate() {
        TextElement textElement = _factory.create("hello", FONT1, COLOR1, _context);
        assertEquals("hello", textElement.getText());
        assertSame(FONT1, textElement.getFont());
        assertSame(COLOR1, textElement.getColor());
        assertEquals(new Rectangle(0, -12, 5, 20), textElement.getShape());
    }

    public void testReCreateWithColor() {
        TextElement te1 = _factory.create("hello", FONT1, COLOR1, _context);
        _context = new MockGraphicsContext(_recorder);
        TextElement te2 = _factory.create("hel" + "lo", FONT2, COLOR2, _context);
        assertSame(te1, te2);
    }

    public void testReCreateWithoutColor() {
        TextElement te1 = _factory.create("hello", FONT1, null, _context);
        _context = new MockGraphicsContext(_recorder);
        TextElement te2 = _factory.create("hel" + "lo", FONT2, null, _context);
        assertSame(te1, te2);
    }
}
