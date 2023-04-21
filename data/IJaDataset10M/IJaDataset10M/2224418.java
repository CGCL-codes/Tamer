package com.xenoage.zong.text;

import static com.xenoage.kernel.Range.range;
import static com.xenoage.pdlib.PVector.pvec;
import static com.xenoage.zong.text.FormattedText.fText;
import static com.xenoage.zong.text.FormattedTextParagraph.fPara;
import static com.xenoage.zong.text.FormattedTextUtils.insert;
import static com.xenoage.zong.text.FormattedTextUtils.merge;
import static com.xenoage.zong.text.FormattedTextUtils.split;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import com.xenoage.kernel.Tuple2;
import com.xenoage.pdlib.PVector;
import com.xenoage.util.color.ColorInfo;
import com.xenoage.zong.core.text.Alignment;

/**
 * Test cases for the {@link FormattedTextUtils} class.
 * 
 * @author Andreas Wenger
 */
public class FormattedTextUtilsTest {

    @Test
    public void cleanTest() {
        FormattedText text = createText1Para();
        FormattedText textCleaned = FormattedTextUtils.clean(text);
        assertEquals(1, textCleaned.paragraphs.size());
        assertEquals(2, textCleaned.paragraphs.getFirst().elements.size());
        assertEquals(new FormattedTextString("Hallo ", text.paragraphs.getFirst().elements.get(0).getStyle()), textCleaned.paragraphs.getFirst().elements.get(0));
        assertEquals(new FormattedTextString("Andrea", text.paragraphs.getFirst().elements.get(1).getStyle()), textCleaned.paragraphs.getFirst().elements.get(1));
    }

    @Test
    public void mergeTest() {
        FormattedText text1 = createText1Para();
        FormattedText text2 = createText3Paras();
        assertEquals("Hallo AndreaHallo Andrea", merge(text1, text1).toString());
        assertEquals("Hallo AndreaFirst Line\nSecond Line and\na Third Line", merge(text1, text2).toString());
    }

    @Test
    public void splitTest1Para() {
        FormattedText text = createText1Para();
        Tuple2<FormattedText, FormattedText> textSplit = split(text, 7);
        assertEquals("Hallo A", textSplit.get1().toString());
        assertEquals("ndrea", textSplit.get2().toString());
        textSplit = split(text, 8);
        assertEquals("Hallo An", textSplit.get1().toString());
        assertEquals("drea", textSplit.get2().toString());
    }

    @Test
    public void splitTest3Paras() {
        FormattedText text = createText3Paras();
        Tuple2<FormattedText, FormattedText> textSplit = split(text, 10);
        assertEquals("First Line", textSplit.get1().toString());
        assertEquals("\nSecond Line and\na Third Line", textSplit.get2().toString());
        textSplit = split(text, 11);
        assertEquals("First Line\n", textSplit.get1().toString());
        assertEquals("Second Line and\na Third Line", textSplit.get2().toString());
        textSplit = split(text, 3);
        assertEquals("Fir", textSplit.get1().toString());
        assertEquals("st Line\nSecond Line and\na Third Line", textSplit.get2().toString());
        textSplit = split(text, 14);
        assertEquals("First Line\nSec", textSplit.get1().toString());
        assertEquals("ond Line and\na Third Line", textSplit.get2().toString());
        textSplit = split(text, 30);
        assertEquals("First Line\nSecond Line and\na T", textSplit.get1().toString());
        assertEquals("hird Line", textSplit.get2().toString());
        textSplit = split(text, 0);
        assertEquals("", textSplit.get1().toString());
        assertEquals("First Line\nSecond Line and\na Third Line", textSplit.get2().toString());
        textSplit = split(text, 39);
        assertEquals("First Line\nSecond Line and\na Third Line", textSplit.get1().toString());
        assertEquals("", textSplit.get2().toString());
    }

    @Test
    public void splitTestExceptions() {
        FormattedText text1 = createText1Para();
        for (int i : range(text1.toString().length() + 1)) split(text1, i);
        FormattedText text2 = createText3Paras();
        for (int i : range(text2.toString().length() + 1)) split(text2, i);
    }

    @Test
    public void insertElementTest1() {
        FormattedText text = createText1Para();
        FormattedTextString insertText = new FormattedTextString("ndi und A", text.paragraphs.getFirst().elements.get(1).getStyle());
        FormattedText textInsert = insert(text, 7, insertText);
        assertEquals(1, textInsert.paragraphs.size());
        assertEquals(2, textInsert.paragraphs.getFirst().elements.size());
        assertEquals(new FormattedTextString("Hallo ", text.paragraphs.getFirst().elements.get(0).getStyle()), textInsert.paragraphs.getFirst().elements.get(0));
        assertEquals(new FormattedTextString("Andi und Andrea", text.paragraphs.getFirst().elements.get(1).getStyle()), textInsert.paragraphs.getFirst().elements.get(1));
    }

    @Test
    public void insertElementTest2() {
        FormattedText text = createText1Para();
        FormattedTextString insertText = new FormattedTextString("di und An", text.paragraphs.getFirst().elements.get(0).getStyle());
        FormattedText textInsert = insert(text, 8, insertText);
        assertEquals(1, textInsert.paragraphs.size());
        assertEquals(4, textInsert.paragraphs.getFirst().elements.size());
        assertEquals(new FormattedTextString("Hallo ", text.paragraphs.getFirst().elements.get(0).getStyle()), textInsert.paragraphs.getFirst().elements.get(0));
        assertEquals(new FormattedTextString("An", text.paragraphs.getFirst().elements.get(1).getStyle()), textInsert.paragraphs.getFirst().elements.get(1));
        assertEquals(new FormattedTextString("di und An", text.paragraphs.getFirst().elements.get(0).getStyle()), textInsert.paragraphs.getFirst().elements.get(2));
        assertEquals(new FormattedTextString("drea", text.paragraphs.getFirst().elements.get(1).getStyle()), textInsert.paragraphs.getFirst().elements.get(3));
    }

    @Test
    public void insertTextTest() {
        FormattedText text = createText3Paras();
        FormattedText textInsert = insert(text, 14, text);
        assertEquals("First Line\nSecFirst Line\nSecond Line and\na Third Lineond Line and\na Third Line", textInsert.toString());
        assertEquals(5, textInsert.paragraphs.size());
    }

    private FormattedText createText1Para() {
        FormattedTextStyle style1 = new FormattedTextStyle(12);
        FormattedTextStyle style2 = new FormattedTextStyle(14, ColorInfo.blue);
        return fText(fPara(new PVector<FormattedTextElement>(new FormattedTextString("Hallo ", style1), new FormattedTextString("An", style2), new FormattedTextString("dr", style2), new FormattedTextString("ea", style2)), Alignment.Left));
    }

    private FormattedText createText3Paras() {
        FormattedTextStyle style1 = new FormattedTextStyle(12);
        FormattedTextStyle style2 = new FormattedTextStyle(14, ColorInfo.blue);
        return fText(pvec(fPara(new PVector<FormattedTextElement>(new FormattedTextString("First ", style1), new FormattedTextString("Line", style2)), Alignment.Left), fPara(new PVector<FormattedTextElement>(new FormattedTextString("Second Line and", style2)), Alignment.Left), fPara(new PVector<FormattedTextElement>(new FormattedTextString("a ", style2), new FormattedTextString("Third", style1), new FormattedTextString(" Line", style2)), Alignment.Left)));
    }
}
