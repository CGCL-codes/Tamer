package org.berlin.lang.octane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collections;
import java.util.Stack;
import org.berlin.lang.octane.type.OBreak;
import org.berlin.lang.octane.type.ONumber;
import org.berlin.lang.octane.type.OString;
import org.berlin.lang.octane.type.OType;
import org.berlin.lang.octane.type.OWord;
import org.berlin.lang.octane.type.TypeConstants;

/**
 * 
 * @author bbrown
 * 
 */
@SuppressWarnings("unchecked")
public class OctaneLang {

    /**
     * 
     * @author bbrown
     * 
     */
    public static class ReaderException extends Exception {

        /**
         * Serial Version id. 
         */
        private static final long serialVersionUID = 1L;

        final int line;

        public ReaderException(int line, Throwable cause) {
            super(cause);
            this.line = line;
        }
    }

    /**
     * 
     * @param ch
     * @return
     */
    public static boolean isWhitespace(int ch) {
        return Character.isWhitespace(ch) || ch == '[' || ch == ']';
    }

    /**
     * 
     * @param r
     * @param ch
     * @throws IOException
     */
    public static void unread(final PushbackReader r, int ch) throws IOException {
        if (ch != -1) {
            r.unread(ch);
        }
    }

    /**
     * 
     * @param r
     * @param eofIsError
     * @param eofValue
     * @return
     * @throws Exception
     */
    public Object read(final PushbackReader r, final Stack<Integer> charStack, final boolean eofIsError, final Object eofValue) throws Exception {
        try {
            for (; ; ) {
                int ch = r.read();
                boolean hasWhiteSpace = false;
                while (isWhitespace(ch)) {
                    ch = r.read();
                    hasWhiteSpace = true;
                }
                if (hasWhiteSpace) {
                    charStack.push((int) TypeConstants.WHITESPACE);
                }
                if (TypeConstants.COMMENT_START == ch) {
                    while ((TypeConstants.COMMENT_END != ch) && (ch != TypeConstants.END)) {
                        ch = r.read();
                    }
                }
                if ((ch == TypeConstants.COMMENT_END) || isWhitespace(ch)) {
                    continue;
                }
                if (ch == TypeConstants.END) {
                    if (eofIsError) {
                        throw new Exception("EOF while reading");
                    }
                    return eofValue;
                } else if (Character.isLetterOrDigit(ch)) {
                    charStack.push(ch);
                } else if ((ch == TypeConstants.POINT) || (ch == TypeConstants.COMMA_BREAK) || (ch == TypeConstants.DOUBLE_QUOTE) || (ch == '_')) {
                    charStack.push(ch);
                } else {
                    System.out.println("INVALID INPUT ==>" + (char) ch);
                }
            }
        } catch (Exception e) {
            final LineNumberingPushbackReader rdr = (LineNumberingPushbackReader) r;
            throw new ReaderException(rdr.getLineNumber(), e);
        }
    }

    /**
     * Whitespace found.
     */
    public void readOnWhitespace(final Stack<Integer> charStack, final Stack<Integer> inputStack, final Stack<Integer> doubleRightStack, final Stack<OType> tokenStack) {
        boolean hasLetter = false;
        for (final int ci : inputStack) {
            if ((ci != TypeConstants.POINT) && (!Character.isDigit(ci))) {
                hasLetter = true;
            }
        }
        final boolean isNum = !hasLetter;
        if (isNum) {
            final StringBuilder buf = new StringBuilder(inputStack.size());
            for (final int rc : inputStack) {
                buf.append(String.valueOf(Character.digit(rc, 10)));
            }
            if (buf.length() != 0) {
                if (doubleRightStack.size() != 0) {
                    buf.append(TypeConstants.POINT);
                    for (final int rc : doubleRightStack) {
                        buf.append(String.valueOf(Character.digit(rc, 10)));
                    }
                }
                final double num = Double.valueOf(buf.toString());
                tokenStack.add(new ONumber(num));
            }
        } else {
            final StringBuilder buf = new StringBuilder(inputStack.size());
            for (final int rc : inputStack) {
                buf.append((char) rc);
            }
            if (buf.length() != 0) {
                tokenStack.add(new OWord(buf.toString()));
            }
        }
        clearReadStacks(inputStack, doubleRightStack);
    }

    public void clearReadStacks(final Stack<Integer> instack, final Stack<Integer> rightstack) {
        instack.clear();
        rightstack.clear();
    }

    /**
     * 
     * @param pushbackReader
     */
    public void readCharStack(final LineNumberingPushbackReader pushbackReader) throws Exception {
        final Object EOF = new Object();
        final Stack<Integer> charStack = new Stack<Integer>();
        final Stack<Integer> inputStack = new Stack<Integer>();
        final Stack<Integer> doubleRightStack = new Stack<Integer>();
        final Stack<OType> tokenStack = new Stack<OType>();
        this.read(pushbackReader, charStack, false, EOF);
        System.out.println(">>> Parsing: Reading Stack");
        Collections.reverse(charStack);
        boolean hasRightVal = false;
        while (!charStack.isEmpty()) {
            final int chartok = charStack.pop();
            if (TypeConstants.WHITESPACE == chartok) {
                readOnWhitespace(charStack, inputStack, doubleRightStack, tokenStack);
                hasRightVal = false;
            } else if (TypeConstants.DOUBLE_QUOTE == chartok) {
                final StringBuilder buf = new StringBuilder();
                int tokforstr = -1;
                while ((TypeConstants.DOUBLE_QUOTE != tokforstr) && !charStack.isEmpty()) {
                    tokforstr = charStack.pop();
                    if (TypeConstants.DOUBLE_QUOTE != tokforstr) {
                        buf.append((char) tokforstr);
                    }
                }
                tokenStack.push(new OString(buf.toString()));
                clearReadStacks(inputStack, doubleRightStack);
            } else if (Character.isDigit(chartok)) {
                if (hasRightVal) {
                    doubleRightStack.push(chartok);
                } else {
                    inputStack.push(chartok);
                }
            } else if (TypeConstants.COMMA_BREAK == chartok) {
                tokenStack.push(new OBreak("<BREAK>"));
                clearReadStacks(inputStack, doubleRightStack);
            } else if (Character.isLetter(chartok) || ('_' == chartok)) {
                inputStack.push(chartok);
            } else if (TypeConstants.POINT == chartok) {
                hasRightVal = true;
                doubleRightStack.clear();
            } else {
                System.out.println(String.format("<OTHER CHARACTER  %s>", chartok));
            }
        }
        System.out.println(">>>>>>>>>>>> Printing Tokens in TOKEN STACK <<");
        Collections.reverse(tokenStack);
        final Stack<OType> historyTokenStack = (Stack<OType>) tokenStack.clone();
        for (final OType token : tokenStack) {
            System.out.println("$[token]" + token);
        }
        System.out.println(">>>>>>>>>>>> Parsing Tokens");
        final OParser parser = new OParser();
        parser.parse(tokenStack);
    }

    /**    
     * 
     * @param reader
     * @param path
     * @param filename
     */
    public void load(final Reader reader, final String path, final String filename) throws Exception {
        final LineNumberingPushbackReader pushbackReader = (reader instanceof LineNumberingPushbackReader) ? (LineNumberingPushbackReader) reader : new LineNumberingPushbackReader(reader);
        this.readCharStack(pushbackReader);
    }

    /**
     * 
     * @param filename
     * @throws Exception
     */
    public void loadFile(final String filename) throws Exception {
        final FileInputStream f = new FileInputStream(filename);
        try {
            final File file = new File(filename);
            load(new InputStreamReader(f, "UTF-8"), file.getAbsolutePath(), file.getName());
        } finally {
            f.close();
        }
    }

    /**
     * 
     * @param filename
     * @throws Exception
     */
    public void loadFileResource(final String resfilepath) throws Exception {
        final URL url = this.getClass().getResource(resfilepath);
        final InputStreamReader ins = new InputStreamReader(url.openStream());
        this.load(ins, String.valueOf(url), String.valueOf(url));
    }

    /**
     * 
     * @param args
     * @throws Exception
     */
    public static final void main(final String[] args) throws Exception {
        System.out.println(">>Running Octane Lang");
        final OctaneLang lang = new OctaneLang();
        lang.loadFile("src/org/berlin/planet/resources/octane/test1.oct");
    }
}
