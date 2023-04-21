package org.mushroomdb.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.LinkedHashSet;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

class SyntaxDocument extends DefaultStyledDocument {

    private DefaultStyledDocument doc;

    private Element rootElement;

    private boolean multiLineComment;

    private MutableAttributeSet normal;

    private MutableAttributeSet keyword;

    private MutableAttributeSet operator;

    private MutableAttributeSet comment;

    private MutableAttributeSet quote;

    private LinkedHashSet keywords, operators;

    public SyntaxDocument() {
        doc = this;
        rootElement = doc.getDefaultRootElement();
        putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
        normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.black);
        comment = new SimpleAttributeSet();
        StyleConstants.setForeground(comment, Color.gray);
        StyleConstants.setItalic(comment, true);
        keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, Color.blue);
        operator = new SimpleAttributeSet();
        StyleConstants.setForeground(operator, Color.orange);
        quote = new SimpleAttributeSet();
        StyleConstants.setForeground(quote, Color.red);
        keywords = new LinkedHashSet();
        keywords.add("select");
        keywords.add("create");
        keywords.add("insert");
        keywords.add("update");
        keywords.add("delete");
        keywords.add("from");
        keywords.add("where");
        keywords.add("order by");
        keywords.add("int");
        keywords.add("char");
        keywords.add("table");
        keywords.add("into");
        keywords.add("values");
        keywords.add("set");
        keywords.add("begin");
        keywords.add("commit");
        keywords.add("rollback");
        keywords.add("transaction");
        keywords.add("user");
        operators = new LinkedHashSet();
        operators.add("*");
        operators.add("=");
        operators.add("!=");
        operators.add("<");
        operators.add(">");
        operators.add("<=");
        operators.add(">=");
        operators.add("and");
        operators.add("or");
        operators.add("not");
        operators.add("null");
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if (str.equals("{")) str = addMatchingBrace(offset);
        super.insertString(offset, str, a);
        processChangedLines(offset, str.length());
    }

    public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);
        processChangedLines(offset, 0);
    }

    public void processChangedLines(int offset, int length) throws BadLocationException {
        String content = doc.getText(0, doc.getLength());
        int startLine = rootElement.getElementIndex(offset);
        int endLine = rootElement.getElementIndex(offset + length);
        setMultiLineComment(commentLinesBefore(content, startLine));
        for (int i = startLine; i <= endLine; i++) {
            applyHighlighting(content, i);
        }
        if (isMultiLineComment()) commentLinesAfter(content, endLine); else highlightLinesAfter(content, endLine);
    }

    private boolean commentLinesBefore(String content, int line) {
        int offset = rootElement.getElement(line).getStartOffset();
        int startDelimiter = lastIndexOf(content, getStartDelimiter(), offset - 2);
        if (startDelimiter < 0) return false;
        int endDelimiter = indexOf(content, getEndDelimiter(), startDelimiter);
        if (endDelimiter < offset & endDelimiter != -1) return false;
        doc.setCharacterAttributes(startDelimiter, offset - startDelimiter + 1, comment, false);
        return true;
    }

    private void commentLinesAfter(String content, int line) {
        int offset = rootElement.getElement(line).getEndOffset();
        int endDelimiter = indexOf(content, getEndDelimiter(), offset);
        if (endDelimiter < 0) return;
        int startDelimiter = lastIndexOf(content, getStartDelimiter(), endDelimiter);
        if (startDelimiter < 0 || startDelimiter <= offset) {
            doc.setCharacterAttributes(offset, endDelimiter - offset + 1, comment, false);
        }
    }

    private void highlightLinesAfter(String content, int line) throws BadLocationException {
        int offset = rootElement.getElement(line).getEndOffset();
        int startDelimiter = indexOf(content, getStartDelimiter(), offset);
        int endDelimiter = indexOf(content, getEndDelimiter(), offset);
        if (startDelimiter < 0) startDelimiter = content.length();
        if (endDelimiter < 0) endDelimiter = content.length();
        int delimiter = Math.min(startDelimiter, endDelimiter);
        if (delimiter < offset) return;
        int endLine = rootElement.getElementIndex(delimiter);
        for (int i = line + 1; i < endLine; i++) {
            Element branch = rootElement.getElement(i);
            Element leaf = doc.getCharacterElement(branch.getStartOffset());
            AttributeSet as = leaf.getAttributes();
            if (as.isEqual(comment)) applyHighlighting(content, i);
        }
    }

    private void applyHighlighting(String content, int line) throws BadLocationException {
        int startOffset = rootElement.getElement(line).getStartOffset();
        int endOffset = rootElement.getElement(line).getEndOffset() - 1;
        int lineLength = endOffset - startOffset;
        int contentLength = content.length();
        if (endOffset >= contentLength) endOffset = contentLength - 1;
        if (endingMultiLineComment(content, startOffset, endOffset) || isMultiLineComment() || startingMultiLineComment(content, startOffset, endOffset)) {
            doc.setCharacterAttributes(startOffset, endOffset - startOffset + 1, comment, false);
            return;
        }
        doc.setCharacterAttributes(startOffset, lineLength, normal, true);
        int index = content.indexOf(getSingleLineDelimiter(), startOffset);
        if ((index > -1) && (index < endOffset)) {
            doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
            endOffset = index - 1;
        }
        checkForTokens(content, startOffset, endOffset);
    }

    private boolean startingMultiLineComment(String content, int startOffset, int endOffset) throws BadLocationException {
        int index = indexOf(content, getStartDelimiter(), startOffset);
        if ((index < 0) || (index > endOffset)) return false; else {
            setMultiLineComment(true);
            return true;
        }
    }

    private boolean endingMultiLineComment(String content, int startOffset, int endOffset) throws BadLocationException {
        int index = indexOf(content, getEndDelimiter(), startOffset);
        if ((index < 0) || (index > endOffset)) return false; else {
            setMultiLineComment(false);
            return true;
        }
    }

    private boolean isMultiLineComment() {
        return multiLineComment;
    }

    private void setMultiLineComment(boolean value) {
        multiLineComment = value;
    }

    private void checkForTokens(String content, int startOffset, int endOffset) {
        while (startOffset <= endOffset) {
            while (isDelimiter(content.substring(startOffset, startOffset + 1))) {
                if (startOffset < endOffset) startOffset++; else return;
            }
            if (isQuoteDelimiter(content.substring(startOffset, startOffset + 1))) startOffset = getQuoteToken(content, startOffset, endOffset); else startOffset = getOtherToken(content, startOffset, endOffset);
        }
    }

    private int getQuoteToken(String content, int startOffset, int endOffset) {
        String quoteDelimiter = content.substring(startOffset, startOffset + 1);
        String escapeString = getEscapeString(quoteDelimiter);
        int index;
        int endOfQuote = startOffset;
        index = content.indexOf(escapeString, endOfQuote + 1);
        while ((index > -1) && (index < endOffset)) {
            endOfQuote = index + 1;
            index = content.indexOf(escapeString, endOfQuote);
        }
        index = content.indexOf(quoteDelimiter, endOfQuote + 1);
        if ((index < 0) || (index > endOffset)) endOfQuote = endOffset; else endOfQuote = index;
        doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quote, false);
        return endOfQuote + 1;
    }

    private int getOtherToken(String content, int startOffset, int endOffset) {
        int endOfToken = startOffset + 1;
        while (endOfToken <= endOffset) {
            if (isDelimiter(content.substring(endOfToken, endOfToken + 1))) break;
            endOfToken++;
        }
        String token = content.substring(startOffset, endOfToken);
        if (isKeyword(token)) {
            doc.setCharacterAttributes(startOffset, endOfToken - startOffset, keyword, false);
        } else if (isOperator(token)) {
            doc.setCharacterAttributes(startOffset, endOfToken - startOffset, operator, false);
        }
        return endOfToken + 1;
    }

    private int indexOf(String content, String needle, int offset) {
        int index;
        while ((index = content.indexOf(needle, offset)) != -1) {
            String text = getLine(content, index).trim();
            if (text.startsWith(needle) || text.endsWith(needle)) break; else offset = index + 1;
        }
        return index;
    }

    private int lastIndexOf(String content, String needle, int offset) {
        int index;
        while ((index = content.lastIndexOf(needle, offset)) != -1) {
            String text = getLine(content, index).trim();
            if (text.startsWith(needle) || text.endsWith(needle)) break; else offset = index - 1;
        }
        return index;
    }

    private String getLine(String content, int offset) {
        int line = rootElement.getElementIndex(offset);
        Element lineElement = rootElement.getElement(line);
        int start = lineElement.getStartOffset();
        int end = lineElement.getEndOffset();
        return content.substring(start, end - 1);
    }

    protected boolean isDelimiter(String character) {
        String operands = ";:{}()[]+-/%<=>!&|^~*";
        if (Character.isWhitespace(character.charAt(0)) || operands.indexOf(character) != -1) return true; else return false;
    }

    protected boolean isQuoteDelimiter(String character) {
        String quoteDelimiters = "\"'";
        if (quoteDelimiters.indexOf(character) < 0) return false; else return true;
    }

    protected boolean isKeyword(String token) {
        return keywords.contains(token.toLowerCase());
    }

    protected boolean isOperator(String token) {
        return operators.contains(token.toLowerCase());
    }

    protected String getStartDelimiter() {
        return "/*";
    }

    protected String getEndDelimiter() {
        return "*/";
    }

    protected String getSingleLineDelimiter() {
        return "//";
    }

    protected String getEscapeString(String quoteDelimiter) {
        return "\\" + quoteDelimiter;
    }

    protected String addMatchingBrace(int offset) throws BadLocationException {
        StringBuffer whiteSpace = new StringBuffer();
        int line = rootElement.getElementIndex(offset);
        int i = rootElement.getElement(line).getStartOffset();
        while (true) {
            String temp = doc.getText(i, 1);
            if (temp.equals(" ") || temp.equals("\t")) {
                whiteSpace.append(temp);
                i++;
            } else break;
        }
        return "{\n" + whiteSpace.toString() + "\t\n" + whiteSpace.toString() + "}";
    }

    public static void main(String a[]) {
        EditorKit editorKit = new StyledEditorKit() {

            public Document createDefaultDocument() {
                return new SyntaxDocument();
            }
        };
        final JEditorPane edit = new JEditorPane();
        edit.setEditorKitForContentType("text/java", editorKit);
        edit.setContentType("text/java");
        JButton button = new JButton("Load SyntaxDocument.java");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    FileInputStream fis = new FileInputStream("SyntaxDocument.java");
                    edit.read(fis, null);
                    edit.requestFocus();
                } catch (Exception e2) {
                }
            }
        });
        JFrame frame = new JFrame("Syntax Highlighting");
        frame.getContentPane().add(new JScrollPane(edit));
        frame.getContentPane().add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setVisible(true);
    }
}
