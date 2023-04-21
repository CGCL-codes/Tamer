package com.teaminabox.eclipse.wiki.renderer;

public final class TwikiContentRendererTest extends AbstractContentRendererTest {

    public TwikiContentRendererTest(String name) {
        super(name);
    }

    public void testNumberedOrderedList() {
        String markup = "   1. first\n   1. second\n      1. second, first\n   1. third";
        String expected = "<ol type=\"1\"><li>first</li><li>second</li><ol type=\"1\"><li>second, first</li></ol><li>third</li></ol>";
        assertRenderedContains(markup, expected);
    }

    protected AbstractContentRenderer getRenderer() {
        return new TwikiBrowserContentRenderer();
    }

    public void testUpperCaseOrderedList() {
        String markup = "   A. first\n   A. second\n      A. second, first\n   A. third";
        String expected = "<ol type=\"A\"><li>first</li><li>second</li><ol type=\"A\"><li>second, first</li></ol><li>third</li></ol>";
        assertRenderedContains(markup, expected);
    }

    public void testLowerCaseOrderedList() {
        String markup = "   a. first\n   a. second\n      a. second, first\n   a. third";
        String expected = "<ol type=\"a\"><li>first</li><li>second</li><ol type=\"a\"><li>second, first</li></ol><li>third</li></ol>";
        assertRenderedContains(markup, expected);
    }

    public void testUpperRomanOrderedList() {
        String markup = "   I. first\n   I. second\n      I. second, first\n   I. third";
        String expected = "<ol type=\"I\"><li>first</li><li>second</li><ol type=\"I\"><li>second, first</li></ol><li>third</li></ol>";
        assertRenderedContains(markup, expected);
    }

    public void testLowerCaseRomanOrderedList() {
        String markup = "   i. first\n   i. second\n      i. second, first\n   i. third";
        String expected = "<ol type=\"i\"><li>first</li><li>second</li><ol type=\"i\"><li>second, first</li></ol><li>third</li></ol>";
        assertRenderedContains(markup, expected);
    }

    public void testEmptyHeader() {
        String markup = "---++";
        String expected = "<h2>---++</h2>";
        assertRenderedContains(markup, expected);
    }

    public void testBold() {
        String markup = "*foo*";
        String expected = "<strong>foo</strong>";
        assertRenderedContains(markup, expected);
    }

    public void testMultiBold() {
        String markup = "*foo*bar*";
        String expected = "<strong>foo*bar</strong>";
        assertRenderedContains(markup, expected);
    }

    public void testUnderscore() {
        AbstractContentRenderer renderer = getRenderer();
        assertEquals("single underscore", "_foo", renderer.processTags("_foo"));
    }

    public void testItalic() {
        String markup = "_foo bar_";
        String expected = "<em>foo bar</em>";
        assertRenderedContains(markup, expected);
    }

    public void testMultiItalic() {
        String markup = "_foo bar_blah_";
        String expected = "<em>foo bar_blah</em>";
        assertRenderedContains(markup, expected);
    }

    public void testBoldItalic() {
        String markup = "__foo__";
        String expected = "<strong><em>foo</em></strong>";
        assertRenderedContains(markup, expected);
    }

    public void testBoldCode() {
        String markup = "==foo==";
        String expected = "<code><b>foo</b></code>";
        assertRenderedContains(markup, expected);
    }

    public void testMultiBoldCode() {
        String markup = "==foo==bar==";
        String expected = "<code><b>foo==bar</b></code>";
        assertRenderedContains(markup, expected);
    }
}
