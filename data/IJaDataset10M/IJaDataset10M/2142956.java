package ac.hiu.j314.elmve;

import java.io.*;

public class ElmStreamTokenizer extends StreamTokenizer {

    public ElmStreamTokenizer(Reader r) {
        super(r);
        initSyntax();
    }

    public String nextString() throws IOException {
        nextToken();
        return sval;
    }

    public int nextInt() throws IOException {
        nextToken();
        Integer i = new Integer(sval);
        return i.intValue();
    }

    public double nextDouble() throws IOException {
        nextToken();
        Double d = new Double(sval);
        return d.doubleValue();
    }

    public float nextFloat() throws IOException {
        nextToken();
        Float f = new Float(sval);
        return f.floatValue();
    }

    public boolean nextBoolean() throws IOException {
        nextToken();
        Boolean b = new Boolean(sval);
        return b.booleanValue();
    }

    public boolean hasMoreTokens() {
        try {
            nextToken();
            if (ttype != TT_EOF) {
                pushBack();
                return true;
            } else {
                pushBack();
                return false;
            }
        } catch (IOException e) {
            System.out.println("ElmStreamTokenizer.hasMoreToken().---???");
            e.printStackTrace();
        }
        return false;
    }

    public void initSyntax() {
        resetSyntax();
        wordChars('A', 'Z');
        wordChars('a', 'z');
        wordChars(' ', 'ÿ');
        wordChars('0', '9');
        wordChars('.', '.');
        wordChars('-', '-');
        wordChars('#', '#');
        wordChars('_', '_');
        whitespaceChars(' ', ' ');
        commentChar('/');
        quoteChar('\'');
        quoteChar('"');
        eolIsSignificant(false);
    }

    public void setOriginalSyntax() {
        resetSyntax();
        wordChars('A', 'Z');
        wordChars('a', 'z');
        wordChars(' ', 'ÿ');
        parseNumbers();
        whitespaceChars(' ', ' ');
        commentChar('/');
        quoteChar('\'');
        quoteChar('"');
    }
}
