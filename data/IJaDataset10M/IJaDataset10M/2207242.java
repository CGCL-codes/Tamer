package net.sourceforge.olympos.oaw.openoffice;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestHtmlConverter {

    @Test
    public void testDefault() throws Exception {
        String result = HtmlConverter.html2openoffice("default");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"uwmDefault\">default</text:span></text:p>", result);
    }

    @Test
    public void testBold() throws Exception {
        String result = HtmlConverter.html2openoffice("<b>bold</b>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"bold\">bold</text:span></text:p>", result);
    }

    @Test
    public void testItalic() throws Exception {
        String result = HtmlConverter.html2openoffice("<i>italic</i>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"italic\">italic</text:span></text:p>", result);
    }

    @Test
    public void testUnderline() throws Exception {
        String result = HtmlConverter.html2openoffice("<u>underline</u>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"underline\">underline</text:span></text:p>", result);
    }

    @Test
    public void testBoldItalic() throws Exception {
        String result = HtmlConverter.html2openoffice("<b><i>text</i></b>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalic\">text</text:span></text:p>", result);
    }

    @Test
    public void testItalicBold() throws Exception {
        String result = HtmlConverter.html2openoffice("<i><b>text</b></i>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalic\">text</text:span></text:p>", result);
    }

    @Test
    public void testBoldUnderline() throws Exception {
        String result = HtmlConverter.html2openoffice("<b><u>text</u></b>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testUnderlineBold() throws Exception {
        String result = HtmlConverter.html2openoffice("<u><b>text</b></u>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testItalicUnderline() throws Exception {
        String result = HtmlConverter.html2openoffice("<i><u>text</u></i>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"italicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testUnderlineItalic() throws Exception {
        String result = HtmlConverter.html2openoffice("<u><i>text</i></u>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"italicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testBoldItalicUnderline() throws Exception {
        String result = HtmlConverter.html2openoffice("<b><i><u>text</u></i></b>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testBoldUnderlineItalic() throws Exception {
        String result = HtmlConverter.html2openoffice("<b><u><i>text</i></u></b>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testItalicBoldUnderline() throws Exception {
        String result = HtmlConverter.html2openoffice("<i><b><u>text</u></b></i>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testItalicUnderlineBold() throws Exception {
        String result = HtmlConverter.html2openoffice("<i><u><b>text</b></u></i>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testUnderlineBoldItalic() throws Exception {
        String result = HtmlConverter.html2openoffice("<u><b><i>text</i></b></u>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testUnderlineItalicBold() throws Exception {
        String result = HtmlConverter.html2openoffice("<u><i><b>text</b></i></u>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldItalicUnderline\">text</text:span></text:p>", result);
    }

    @Test
    public void testBoldBeforeBold() throws Exception {
        String result = HtmlConverter.html2openoffice("<b>text1</b><b>text2</b>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"bold\">text1</text:span><text:span text:style-name=\"bold\">text2</text:span></text:p>", result);
    }

    @Test
    public void testBoldBeforeGapBeforeItalic() throws Exception {
        String result = HtmlConverter.html2openoffice("<b>text1</b>nix<i>text2</i>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"bold\">text1</text:span><text:span text:style-name=\"uwmDefault\">nix</text:span><text:span text:style-name=\"italic\">text2</text:span></text:p>", result);
    }

    @Test
    public void testBoldUnderlineBeforeGapBeforeItalicUnderlineBeforeGap() throws Exception {
        String result = HtmlConverter.html2openoffice("<b><u>text1</u></b>nix<i><u>text2</u></i>nix2");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"boldUnderline\">text1</text:span><text:span text:style-name=\"uwmDefault\">nix</text:span><text:span text:style-name=\"italicUnderline\">text2</text:span><text:span text:style-name=\"uwmDefault\">nix2</text:span></text:p>", result);
    }

    @Test
    public void testGapBeforeBoldUnderlineBeforeGapBeforeItalicUnderlineBeforeGap() throws Exception {
        String result = HtmlConverter.html2openoffice("nix0<b><u>text1</u></b>nix<i><u>text2</u></i>nix2");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"uwmDefault\">nix0</text:span><text:span text:style-name=\"boldUnderline\">text1</text:span><text:span text:style-name=\"uwmDefault\">nix</text:span><text:span text:style-name=\"italicUnderline\">text2</text:span><text:span text:style-name=\"uwmDefault\">nix2</text:span></text:p>", result);
    }

    @Test
    public void testUnorderedListDefault() throws Exception {
        String result = HtmlConverter.html2openoffice("<ul><li>text1</li><li>text2</li></ul>");
        assertEquals("<text:list xml:id=\"id001\" text:style-name=\"uwmUl\"><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">text1</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">text2</text:span></text:p></text:list-item></text:list>", result);
    }

    @Test
    public void testUnorderedListNested() throws Exception {
        String result = HtmlConverter.html2openoffice("<ul><li>1</li><li>2</li><li>3<ul><li>3.1</li><li>3.2</li><li><ul><li>3.3.1</li><li>3.3.2</li></ul></li></ul></li><li>4</li></ul>");
        assertEquals("<text:list xml:id=\"id001\" text:style-name=\"uwmUl\"><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">1</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">2</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">3</text:span></text:p><text:list xml:id=\"id001\" text:style-name=\"uwmUl\"><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">3.1</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">3.2</text:span></text:p></text:list-item><text:list-item><text:list xml:id=\"id001\" text:style-name=\"uwmUl\"><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">3.3.1</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">3.3.2</text:span></text:p></text:list-item></text:list></text:list-item></text:list></text:list-item><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">4</text:span></text:p></text:list-item></text:list>", result);
    }

    @Test
    public void testOrderedListDefault() throws Exception {
        String result = HtmlConverter.html2openoffice("<ol><li>text1</li><li>text2</li></ol>");
        assertEquals("<text:list xml:id=\"id001\" text:style-name=\"uwmOl\"><text:list-item text:start-value=\"1\"><text:p text:style-name=\"uwmOlP\"><text:span text:style-name=\"uwmDefault\">text1</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmOlP\"><text:span text:style-name=\"uwmDefault\">text2</text:span></text:p></text:list-item></text:list>", result);
    }

    @Test
    public void testOrderedListMultiple() throws Exception {
        String result = HtmlConverter.html2openoffice("<ol><li>text1</li><li>text2</li></ol><ol><li>text3</li><li>text4</li></ol>");
        assertEquals("<text:list xml:id=\"id001\" text:style-name=\"uwmOl\"><text:list-item text:start-value=\"1\"><text:p text:style-name=\"uwmOlP\"><text:span text:style-name=\"uwmDefault\">text1</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmOlP\"><text:span text:style-name=\"uwmDefault\">text2</text:span></text:p></text:list-item></text:list><text:list xml:id=\"id001\" text:style-name=\"uwmOl\"><text:list-item text:start-value=\"1\"><text:p text:style-name=\"uwmOlP\"><text:span text:style-name=\"uwmDefault\">text3</text:span></text:p></text:list-item><text:list-item><text:p text:style-name=\"uwmOlP\"><text:span text:style-name=\"uwmDefault\">text4</text:span></text:p></text:list-item></text:list>", result);
    }

    @Test
    public void testEmpty() throws Exception {
        String result = HtmlConverter.html2openoffice("");
        assertEquals("", result);
    }

    @Test
    public void testParagraph() throws Exception {
        String result = HtmlConverter.html2openoffice("<p> </p>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"></text:p>", result);
    }

    @Test
    public void testBr() throws Exception {
        String result = HtmlConverter.html2openoffice("<p><br></p>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"uwmDefault\"><text:line-break/></text:span></text:p>", result);
    }

    @Test
    public void testBrNl() throws Exception {
        String result = HtmlConverter.html2openoffice("<p><br>\n</p>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"uwmDefault\"><text:line-break/></text:span></text:p>", result);
    }

    @Test
    public void testWhitespace() throws Exception {
        String result = HtmlConverter.html2openoffice("<p>a<br> <ul> <li><p>b</p></li></ul></p>");
        assertEquals("<text:p text:style-name=\"uwmStandard\"><text:span text:style-name=\"uwmDefault\">a<text:line-break/></text:span></text:p><text:list xml:id=\"id001\" text:style-name=\"uwmUl\"><text:list-item><text:p text:style-name=\"uwmUlP\"><text:span text:style-name=\"uwmDefault\">b</text:span></text:p></text:list-item></text:list>", result);
    }
}
