package de.flingelli.latex;

import jancilla.encoding.EFileEncoding;
import jancilla.encoding.FileEncoding;
import java.io.File;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;

public class TestCaseLaTeXDocument {

    private final String emptyDocument = "\\documentclass[german,a4paper,10pt]{scrbook}\n" + "\\usepackage[latin1]{inputenc}\n\\usepackage{babel}\n\\usepackage{amsmath}\n\\begin{document}\n\n\\end{document}";

    private final String emptyDocumentTwoSide = "\\documentclass[german,a4paper,twoside,10pt]{scrbook}\n" + "\\usepackage[latin1]{inputenc}\n\\usepackage{babel}\n\\usepackage{amsmath}\n\\begin{document}\n\n\\end{document}";

    private final String headerDocument = "\\documentclass[german,a4paper,10pt]{scrbook}\n" + "\\usepackage[latin1]{inputenc}\n\\usepackage{babel}\n\\usepackage{amsmath}\n\\usepackage{longtable}\n\\begin{document}\n\n\\end{document}";

    private final String bodyDocument = "\\documentclass[german,a4paper,10pt]{scrbook}\n" + "\\usepackage[latin1]{inputenc}\n\\usepackage{babel}\n\\usepackage{amsmath}\n\\begin{document}\nHallo\n\n\\end{document}";

    @Test
    public void testToString() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.toString();
        Assert.assertTrue(emptyDocument.equals(result));
    }

    @Test
    public void testToStringTwoSide() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.TWOSIDE);
        String result = document.toString();
        Assert.assertTrue(emptyDocumentTwoSide.equals(result));
    }

    @Test
    public void testAddHeaderElement() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        document.addHeaderElement("\\usepackage{longtable}");
        String result = document.toString();
        Assert.assertTrue(headerDocument.equals(result));
    }

    @Test
    public void testAddDocumentElement() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        document.addDocumentElement("Hallo");
        String result = document.toString();
        Assert.assertTrue(bodyDocument.equals(result));
    }

    @Test
    public void testWriteToFile() throws IOException {
        File file = File.createTempFile("tmpLaTeX", ".tex");
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.BOOK, ESide.ONESIDE);
        try {
            document.writeToFile(file.getAbsolutePath());
        } catch (IOException e) {
            Assert.fail();
        }
        file.delete();
    }

    @Test
    public void testEncodingOfWrittenFile() throws IOException {
        File file = File.createTempFile("tmpLaTeX", ".tex");
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.BOOK, ESide.ONESIDE);
        document.addDocumentElement("\\section{Überschrift}");
        try {
            document.writeToFile(file.getAbsolutePath());
            EFileEncoding encoding = FileEncoding.getEncoding(file);
            Assert.assertTrue(EFileEncoding.ISO_8859_1 == encoding);
        } catch (IOException e) {
            Assert.fail();
        }
        file.delete();
    }

    @Test
    public void testReplaceElements() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceElements(" - Test - ");
        Assert.assertEquals(" -- Test -- ", result);
    }

    @Test
    public void testReplaceInvertedCommasGerman() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceInvertedCommas("\"Test\"");
        Assert.assertEquals("\\glqq Test\\grqq ", result);
    }

    @Test
    public void testReplaceInvertedCommasGermanBlank() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.GERMAN, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceInvertedCommas("\"Test\" ");
        Assert.assertEquals("\\glqq Test\\grqq\\ ", result);
    }

    @Test
    public void testReplaceInvertedCommasEnglish() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceInvertedCommas("\"Test\" ");
        Assert.assertEquals("\\grqq Test\\grqq\\ ", result);
    }

    @Test
    public void testReplaceItemization() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceItemization("Without Items\n\n* Item 1\n* Item 2\n\nWithout Items.");
        Assert.assertTrue(result.startsWith("Without Items"));
        Assert.assertTrue(result.contains("\\begin{itemize}\n"));
        Assert.assertTrue(result.endsWith("Without Items."));
    }

    @Test
    public void testReplaceItemizationWithEndingItemization() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceItemization("* Item 1\n* Item 2\n");
        Assert.assertEquals("\\begin{itemize}\n\\item Item 1\n\\item Item 2\n\\end{itemize}\n", result);
    }

    @Test
    public void testReplaceTwoItemization() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceItemization("Without Items\n* Item 1\n* Item 2\n\nWithout Items.\n* Item 3\n\nEnde");
        Assert.assertEquals("Without Items\n\\begin{itemize}\n\\item Item 1\n\\item Item 2\n\\end{itemize}\n\nWithout Items." + "\n\\begin{itemize}\n\\item Item 3\n\\end{itemize}\n\nEnde", result);
    }

    @Test
    public void testSubsubitems() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceItemization("Normaler Text\n\n* Item 1\n+ Item 1.1\n+ Item 1.2\n+ Item 1.3\n* " + " Item 2\n+ Item 2.1\n\nWeiterer Text");
        Assert.assertEquals("Normaler Text\n\n\\begin{itemize}\n\\item Item 1\n\\begin{itemize}\n\\item Item 1.1\n" + "\\item Item 1.2\n\\item Item 1.3\n\\end{itemize}\n\\item Item 2\n" + "\\begin{itemize}\n\\item Item 2.1\n\\end{itemize}\n\\end{itemize}\n\nWeiterer Text", result);
    }

    @Test
    public void testSubSubSubitems() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        String result = document.replaceItemization("Normaler Text\n\n* Item 1\n+ Item 1.1\n~ Item 1.1.1\n~ Item 1.1.2\n+ " + " Item 1.2\n* Item 2\nWeiterer Text");
        Assert.assertEquals("Normaler Text\n\n\\begin{itemize}\n\\item Item 1\n\\begin{itemize}\n\\item Item 1.1\n" + "\\begin{itemize}\n\\item Item 1.1.1\n\\item Item 1.1.2\n\\end{itemize}\n" + "\\item Item 1.2\n\\end{itemize}\n\\item Item 2\n\\end{itemize}\nWeiterer Text", result);
    }

    @Test
    public void testStartsWith() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertTrue(document.startsWith('*', "* Item"));
        Assert.assertTrue(document.startsWith('*', "  * Item"));
        Assert.assertTrue(document.startsWith('+', "   + Item"));
        Assert.assertFalse(document.startsWith('*', "+ Item"));
    }

    @Test
    public void getTextWithoutPrefix() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertEquals(document.getTextWithoutPrefix('*', "* Item"), "Item");
        Assert.assertEquals(document.getTextWithoutPrefix('*', "  * Item"), "Item");
        Assert.assertEquals(document.getTextWithoutPrefix('+', "   + Item"), "Item");
        Assert.assertEquals(document.getTextWithoutPrefix('*', "+ Item"), "+ Item");
    }

    @Test
    public void testReplaceDotsWithBlank() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertEquals("Test \\dots\\ .", document.replaceDots("Test ... ."));
    }

    @Test
    public void testReplaceDots() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertEquals("Test \\dots", document.replaceDots("Test ..."));
    }

    @Test
    public void testReplaceGreaterWithBlanks() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertEquals("2 \\textgreater\\ 1", document.replaceGreaterAndLesser("2 > 1"));
    }

    @Test
    public void testReplaceGreater() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertEquals("2\\textgreater 1", document.replaceGreaterAndLesser("2>1"));
    }

    @Test
    public void testReplaceLessWithBlanks() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertEquals("1 \\textless\\ 2", document.replaceGreaterAndLesser("1 < 2"));
    }

    @Test
    public void testReplaceLess() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        Assert.assertEquals("1\\textless 2", document.replaceGreaterAndLesser("1<2"));
    }

    @Test
    public void testItemizeEnd() {
        LaTeXDocument document = new LaTeXDocument(ELanguage.ENGLISH, EPaperSize.A4PAPER, EDocumentClass.SCRBOOK, ESide.ONESIDE);
        StringBuffer buffer = new StringBuffer();
        buffer.append("* Was können wir verbessern?\n");
        buffer.append("  + Asterisk selbst implementieren\n");
        buffer.append("  + Tasks ausführlicher besprechen\n");
        buffer.append("  + Eher Worst Case Schätzungen für unbekannte Aufgaben");
        String result = document.replaceItemization(buffer.toString());
        Assert.assertEquals("\\begin{itemize}\n\\item Was können wir verbessern?\n\\begin{itemize}\n" + "\\item Asterisk selbst implementieren\n\\item Tasks " + "ausführlicher besprechen\n\\item Eher Worst Case Schätzungen " + "für unbekannte Aufgaben\n\\end{itemize}\n\\end{itemize}\n", result);
    }
}
