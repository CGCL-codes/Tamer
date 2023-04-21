package org.xngr.service.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;

/**
 * A simple text printer class.
 *
 * @version	$Revision: 1.2 $, $Date: 2007/06/28 13:14:09 $
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class TextPrinter {

    private static TextPrinter printer = null;

    private boolean printLineNumbers = false;

    private boolean wrapText = false;

    private boolean printHeader = false;

    private Font font = null;

    private PageFormat pageFormat = null;

    private Book book = null;

    private String title = null;

    private String[] text = null;

    private int wrapOffset = 0;

    private int tabSize = 4;

    /**
	 * returns the one text-printer object
	 */
    public static TextPrinter getPrinter() {
        if (printer == null) {
            printer = new TextPrinter();
        }
        return printer;
    }

    /**
	 * Constructor, sets the initial values.
	 */
    private TextPrinter() {
        pageFormat = new PageFormat();
        font = new Font("Courier", Font.PLAIN, 10);
    }

    /**
	 * Prints a document!
	 *
	 * @param document the document to print.
	 */
    public void print(PlainDocument document) throws PrinterException {
        print(document, "", 4);
    }

    /**
	 * Prints a document!
	 *
	 * @param document the document to print.
	 * @param title the document title.
	 * @param softabs wether to have soft-tabs.
	 * @param tabsize the size of the tabs.
	 */
    public void print(PlainDocument document, String title, int tabSize) throws PrinterException {
        Element root = document.getDefaultRootElement();
        String lines[] = new String[root.getElementCount()];
        Segment segment = new Segment();
        for (int i = 0; i < lines.length; i++) {
            Element line = root.getElement(i);
            try {
                document.getText(line.getStartOffset(), line.getEndOffset() - line.getStartOffset(), segment);
                lines[i] = segment.toString();
            } catch (BadLocationException ble) {
            }
        }
        print(lines, title, tabSize);
    }

    /**
	 * Prints an array of Strings!
	 *
	 * @param text the array of strings.
	 */
    public void print(String[] text) throws PrinterException {
        print(text, "", 4);
    }

    /**
	 * Prints an array of Strings!
	 *
	 * @param text the array of strings.
	 * @param title the document title.
	 * @param tabsize the size of the tabs.
	 */
    public void print(String[] text, String title, int tabSize) throws PrinterException {
        this.tabSize = tabSize;
        this.title = title;
        this.text = text;
        this.book = new Book();
        printTextArray();
    }

    /**
	 * Setup the printer!
	 */
    public void setup() {
        PrinterJob job = PrinterJob.getPrinterJob();
        pageFormat = job.pageDialog(pageFormat);
        pageFormat = job.validatePage(pageFormat);
    }

    private void printTextArray() throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        pageFormat = job.validatePage(pageFormat);
        text = removeEOLChar();
        if (printLineNumbers) {
            text = addLineNumbers();
        }
        if (wrapText == true) {
            text = wrapText();
        }
        book = pageinateText();
        job.setPageable(book);
        if (job.printDialog()) {
            job.print();
        }
    }

    private String[] removeEOLChar() {
        String temp1 = null;
        String temp2 = null;
        String temp3 = null;
        int lineCount = text.length;
        String[] newText = new String[lineCount];
        int offset = 0;
        for (int i = 0; i < lineCount; i++) {
            if (text[i].length() == 1) {
                newText[i] = " ";
            } else {
                temp1 = text[i].substring(text[i].length() - 2, text[i].length() - 1);
                temp2 = text[i].substring(text[i].length() - 1, text[i].length());
                if (temp1.compareTo("\r") == 0 || temp1.compareTo("\n") == 0) {
                    offset = 2;
                } else if (temp2.compareTo("\r") == 0 || temp2.compareTo("\n") == 0) {
                    offset = 1;
                } else {
                    offset = 0;
                }
                temp3 = text[i].substring(0, text[i].length() - offset);
                StringBuffer temp4 = new StringBuffer();
                int length = temp3.length();
                for (int j = 0; j < length; j++) {
                    if ("\t".equals(temp3.substring(j, j + 1)) == true) {
                        int numSpaces = temp4.length() % tabSize;
                        if (numSpaces == 0) {
                            numSpaces = tabSize;
                        }
                        for (int x = 0; x < numSpaces; x++) {
                            temp4.append(" ");
                        }
                    } else {
                        temp4.append(temp3.substring(j, j + 1));
                    }
                }
                newText[i] = temp4.toString();
            }
        }
        return newText;
    }

    /**
	 * Adds line numbers to the beginning of each line.
	 */
    private String[] addLineNumbers() {
        int numLines = text.length;
        int totalNumSpaces = 0;
        String temp = null;
        String[] newText = new String[numLines];
        Integer lines = new Integer(numLines);
        temp = lines.toString();
        totalNumSpaces = temp.length();
        wrapOffset = totalNumSpaces + 3;
        for (int i = 0; i < numLines; i++) {
            StringBuffer num = new StringBuffer();
            num.append(i + 1);
            int numLen = num.length();
            StringBuffer lineNum = new StringBuffer();
            for (int j = 0; j < (totalNumSpaces - numLen); j++) {
                lineNum.append(' ');
            }
            lineNum.append(num.toString());
            newText[i] = lineNum.toString() + ".  " + text[i];
        }
        return newText;
    }

    /**
	* Creates a new array of lines that all fit the width of the page.
	*/
    private String[] wrapText() {
        String currentLine = null;
        List<String> temp = new ArrayList<String>();
        int lineCount = text.length;
        int newLineCount = 0;
        StringBuffer wrapSpaces = new StringBuffer("");
        int i = 0;
        PageFormat pgfmt = pageFormat;
        Font pageFont = font;
        double pageWidth = pgfmt.getImageableWidth();
        for (i = 0; i < wrapOffset; i++) {
            wrapSpaces.append(' ');
        }
        for (i = 0; i < lineCount; i++) {
            currentLine = text[i];
            while (pageFont.getStringBounds(currentLine, new FontRenderContext(pageFont.getTransform(), false, false)).getWidth() > pageWidth) {
                int numChars = (int) (currentLine.length() * pageWidth / pageFont.getStringBounds(currentLine, new FontRenderContext(pageFont.getTransform(), false, false)).getWidth());
                temp.add(currentLine.substring(0, numChars));
                currentLine = wrapSpaces.toString() + currentLine.substring(numChars, currentLine.length());
            }
            temp.add(currentLine);
        }
        newLineCount = temp.size();
        String[] newText = new String[newLineCount];
        for (int j = 0; j < newLineCount; j++) {
            newText[j] = temp.get(j);
        }
        return newText;
    }

    /**
	* The pagination method, Paginate the text onto Printable page objects
	*/
    private Book pageinateText() {
        Book book = new Book();
        int linesPerPage = 0;
        int currentLine = 0;
        int pageNum = 0;
        PageFormat format = pageFormat;
        Font pageFont = font;
        int height = (int) format.getImageableHeight();
        int pages = 0;
        linesPerPage = height / (pageFont.getSize() + 2);
        pages = text.length / linesPerPage;
        String[] pageText;
        String readString;
        convertUnprintables();
        if (printHeader == true) {
            linesPerPage = linesPerPage - 2;
        }
        while (pageNum <= pages) {
            pageText = new String[linesPerPage];
            for (int x = 0; x < linesPerPage; x++) {
                try {
                    readString = text[currentLine];
                } catch (ArrayIndexOutOfBoundsException e) {
                    readString = " ";
                }
                pageText[x] = readString;
                currentLine++;
            }
            pageNum++;
            book.append(new Page(pageText, pageNum), format);
        }
        return book;
    }

    /**
	 * Converts unprintable things to a space.  stops some errors.
	 */
    private void convertUnprintables() {
        String tempString;
        int i = text.length;
        while (i > 0) {
            i--;
            tempString = text[i];
            if (tempString == null || "".equals(tempString)) {
                text[i] = " ";
            }
        }
    }

    /**
	 * Enable/Disable printing of line numbers.
	 *
	 * @param enable enable the printing of line numbers.
	 */
    public void setPrintLineNumber(boolean enable) {
        printLineNumbers = enable;
    }

    /**
	 * Find out if printing of line numbers has been enabled.
	 *
	 * @return true when the printing of linenumbers has been enabled.
	 */
    public boolean isPrintLineNumber() {
        return printLineNumbers;
    }

    /**
	 * Enable/Disable wrapping of text.
	 *
	 * @param enable enable the wrapping of text.
	 */
    public void setWrapText(boolean enable) {
        wrapText = enable;
    }

    /**
 	 * Find out if wrapping of text has been enabled.
	 *
	 * @return true when the wrapping of text has been enabled.
	 */
    public boolean isWrapText() {
        return wrapText;
    }

    /**
	 * Enable/Disable printing of the header.
	 * 
	 * @param enable enable the printing of the header.
	 */
    public void setPrintHeader(boolean enable) {
        printHeader = enable;
    }

    /**
	* Find out if printing of the header has been enabled.
	*
	* @return true when the printing of the header has been enabled.
	*/
    public boolean isPrintHeader() {
        return printHeader;
    }

    /**
	 * Set the printing font.
	 * 
	 * @param font the printing font.
	 */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
	 * Get the printing font.
	 *
	 * @return the printing font.
	 */
    public Font getFont() {
        return font;
    }

    /**
	 * An inner class that defines one page of text based
	 * on data about the PageFormat etc. from the book defined
	 * in the parent class
	 */
    class Page implements Printable {

        private String[] pageText;

        private int pageNumber = 0;

        Page(String[] text, int pageNum) {
            this.pageText = text;
            this.pageNumber = pageNum;
        }

        /**
		 * Defines the Printable print method, for printing a Page
		 */
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            int pos;
            int posOffset = 1;
            Font pageFont = font;
            if (printHeader == true) {
                StringBuffer pageNumText = new StringBuffer();
                pageNumText.append("Page ");
                pageNumText.append(pageNumber);
                double margin = (pageFormat.getWidth() - pageFormat.getImageableWidth()) / 2;
                graphics.setFont(font);
                graphics.setColor(Color.black);
                pos = (int) pageFormat.getImageableY() + (font.getSize() + 2);
                graphics.drawString(title, (int) pageFormat.getImageableX(), pos);
                int xPos = (int) (pageFormat.getWidth() - margin - graphics.getFontMetrics().stringWidth(pageNumText.toString()));
                graphics.drawString(pageNumText.toString(), xPos, pos);
                posOffset = 3;
            }
            graphics.setFont(pageFont);
            graphics.setColor(Color.black);
            for (int x = 0; x < (pageText.length); x++) {
                pos = (int) pageFormat.getImageableY() + (pageFont.getSize() + 2) * (x + posOffset);
                graphics.drawString(this.pageText[x], (int) pageFormat.getImageableX(), pos);
            }
            return Printable.PAGE_EXISTS;
        }
    }

    /**
	* An inner class that defines one section of printable text.
	* This allows the flexability to assign different fonts to
	* individual words or phrases (i.e. for headers/footers or
	* Syntax highlighting (pretty print).
	*/
    class PrintableText {

        private Font font;

        private boolean newLine = true;

        private String text;

        PrintableText() {
        }

        PrintableText(String text, Font font, boolean newLine) {
            this.text = text;
            this.font = font;
            this.newLine = newLine;
        }

        String getText() {
            return text;
        }

        void setText(String text) {
            this.text = text;
        }

        Font getFont() {
            return font;
        }

        void setFont(Font font) {
            this.font = font;
        }

        boolean isNewLine() {
            return newLine;
        }

        void setNewLine(boolean newLine) {
            this.newLine = newLine;
        }
    }
}
