package com.sun.satsa.jcrmic.utils;

import java.io.*;

/**
 * IndentingWriter is a BufferedWriter subclass that supports automatic
 * indentation of lines of text written to the underlying Writer.
 *
 * Methods are provided for compact, convenient indenting, writing text,
 * and writing lines in various combinations.
 */
public class IndentingWriter extends BufferedWriter {

    /** true if the next character written is the first on a line */
    private boolean beginningOfLine = true;

    /** current number of spaces to prepend to lines */
    private int currentIndent = 0;

    /** number of spaces to change indent when indenting in or out */
    private int indentStep = 4;

    /**
     * Create a new IndentingWriter that writes indented text to the
     * given Writer.  Use the default indent step of four spaces.
     * @param out output stream
     */
    public IndentingWriter(Writer out) {
        super(out);
    }

    /**
     * Create a new IndentingWriter that writes indented text to the
     * given Writer and uses the supplied indent step.
     * @param out output stream
     * @param step indent step
     */
    public IndentingWriter(Writer out, int step) {
        this(out);
        if (indentStep < 0) throw new IllegalArgumentException("negative indent step");
        indentStep = step;
    }

    /**
     * Write a single character.
     * @param c the character
     * @throws IOException if I/O exception occurs
     */
    public void write(int c) throws IOException {
        checkWrite();
        super.write(c);
    }

    /**
     * Write a portion of an array of characters.
     * @param cbuf buffer
     * @param off offset
     * @param len length
     * @throws IOException if I/O exception occurs
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (len > 0) {
            checkWrite();
        }
        super.write(cbuf, off, len);
    }

    /**
     * Write a portion of a String.
     * @param s the string
     * @param off offset
     * @param len length
     * @throws IOException if I/O exception occurs
     */
    public void write(String s, int off, int len) throws IOException {
        if (len > 0) {
            checkWrite();
        }
        super.write(s, off, len);
    }

    /**
     * Write a line separator.  The next character written will be
     * preceded by an indent.
     * @throws IOException if I/O exception occurs
     */
    public void newLine() throws IOException {
        super.newLine();
        beginningOfLine = true;
    }

    /**
     * Check if an indent needs to be written before writing the next
     * character.
     *
     * The indent generation is optimized (and made consistent with
     * certain coding conventions) by condensing groups of eight spaces
     * into tab characters.
     * @throws IOException if I/O exception occurs
     */
    protected void checkWrite() throws IOException {
        if (beginningOfLine) {
            beginningOfLine = false;
            int i = currentIndent;
            while (i >= 8) {
                super.write('\t');
                i -= 8;
            }
            while (i > 0) {
                super.write(' ');
                --i;
            }
        }
    }

    /**
     * Increase the current indent by the indent step.
     */
    protected void indentIn() {
        currentIndent += indentStep;
    }

    /**
     * Decrease the current indent by the indent step.
     */
    protected void indentOut() {
        currentIndent -= indentStep;
        if (currentIndent < 0) currentIndent = 0;
    }

    /**
     * Indent in.
     */
    public void pI() {
        indentIn();
    }

    /**
     * Indent out.
     */
    public void pO() {
        indentOut();
    }

    /**
     * Write string.
     * @param s the string
     * @throws IOException if I/O exception occurs
     */
    public void p(String s) throws IOException {
        write(s);
    }

    /**
     * End current line.
     * @throws IOException if I/O exception occurs
     */
    public void pln() throws IOException {
        newLine();
    }

    /**
     * Write string; end current line.
     * @param s the string
     * @throws IOException if I/O exception occurs
     */
    public void pln(String s) throws IOException {
        p(s);
        pln();
    }

    /**
     * Write string; end current line; indent in.
     * @param s the string
     * @throws IOException if I/O exception occurs
     */
    public void plnI(String s) throws IOException {
        p(s);
        pln();
        pI();
    }

    /**
     * Indent out; write string.
     * @param s the string
     * @throws IOException if I/O exception occurs
     */
    public void pO(String s) throws IOException {
        pO();
        p(s);
    }

    /**
     * Indent out; write string; end current line.
     * @param s the string
     * @throws IOException if I/O exception occurs
     */
    public void pOln(String s) throws IOException {
        pO(s);
        pln();
    }

    /**
     * Indent out; write string; end current line; indent in.
     *
     * This method is useful for generating lines of code that both
     * end and begin nested blocks, like "} else {".
     * @param s the string
     * @throws IOException if I/O exception occurs
     */
    public void pOlnI(String s) throws IOException {
        pO(s);
        pln();
        pI();
    }
}
