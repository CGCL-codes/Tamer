package net.sf.crsx.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Variant of appendable that takes care of indentation and joining short fragments by converting newlines
 * to spaces if space permits.
 * 
 * @author <a href="http://www.research.ibm.com/people/k/krisrose">Kristoffer Rose</a>.
 * @version $Id: FormattingAppendable.java,v 1.9 2010/11/24 18:59:48 krisrose Exp $
 */
public class FormattingAppendable extends Writer {

    /** Spaces used for indentation. */
    static final String SPACES = "                                                                                                                                                                                                                                                          ";

    private final String m(String s) {
        return "";
    }

    /**
     * Create formatter.
     * If the appendable is already a formatter or null then it is simply returned unchanged.
     * @param appendable to send reformatted text to
     * @param width of lines
     * @param initialindentation to use for top-level formatting unit.
     */
    public static FormattingAppendable format(Appendable appendable, int width, int initialindentation) {
        if (appendable == null) return null;
        if (appendable instanceof FormattingAppendable) return (FormattingAppendable) appendable;
        return new FormattingAppendable(appendable, width, initialindentation);
    }

    /** Indentation string for level. */
    private static String indentation(int level) throws IOException {
        if (level < 0) return ""; else if (level < SPACES.length()) return SPACES.substring(0, level); else {
            StringBuilder b = new StringBuilder();
            do {
                b.append(SPACES);
                level -= SPACES.length();
            } while (level >= SPACES.length());
            b.append(SPACES.substring(0, level));
            return b.toString();
        }
    }

    /** Insert level indentation after every newline. */
    private String indent(String s, int level) throws IOException {
        return s.replaceAll("[ ]*\n[ ]*", "\n" + m("N") + indentation(level)).replaceAll("[ ]*\0+[ ]*", "\n" + m("Z") + indentation(level));
    }

    /** Target for the formatted text. */
    private final Appendable appendable;

    /** Maximal width of the formatted text. */
    private final int width;

    /** Current indent. */
    private int indent;

    /** Indents. */
    private Stack<Integer> indents = new Stack<Integer>();

    /** Current innermost formatting unit, or null if none. */
    private StringBuilder topOpening = null;

    /** Pushed formatting unit. */
    private List<StringBuilder> pushedOpenings = new LinkedList<StringBuilder>();

    /**
	 * Create formatter.
	 * @param appendable to send reformatted text to -- do not nest!
	 * @param width of lines
	 * @param initialindentation to use for top-level formatting unit.
	 */
    protected FormattingAppendable(Appendable appendable, int width, int initialindentation) {
        assert !(appendable instanceof FormattingAppendable);
        this.appendable = appendable;
        this.width = width;
        indent = initialindentation;
    }

    /** Increase the indentation and start a new formatting unit with open string. */
    public void open(String open) throws IOException {
        open(open, 1);
    }

    /** Increase the indentation by extra and start a new formatting unit with open string. */
    public void open(String open, int extra) throws IOException {
        indents.push(indent);
        indent += extra;
        if (topOpening != null) pushedOpenings.add(topOpening);
        topOpening = new StringBuilder();
        append(m("OPEN") + open);
    }

    /** End current formatting unit with close string, and decrease indentation. */
    public void close(String close) throws IOException {
        if (topOpening == null) {
            indent = indents.pop();
            appendable.append(m("CLOSE1") + close.replace("\0", "").replace("↲", "\n"));
        } else {
            String s = topOpening.toString();
            if (indent + s.length() < width) {
                String sWithClosing = (s + close).replace('\n', ' ').replace("\0", "").trim();
                if (pushedOpenings.isEmpty()) {
                    appendable.append(m("CLOSE2") + sWithClosing.replace("↲", "\n"));
                    indent = indents.pop();
                    topOpening = null;
                } else {
                    topOpening = pushedOpenings.remove(pushedOpenings.size() - 1);
                    topOpening.append(m("CLOSE3") + sWithClosing);
                    indent = indents.pop();
                }
            } else {
                for (int i = 0; i < pushedOpenings.size(); ++i) {
                    String unit = pushedOpenings.get(i).toString();
                    int unitIndent = indents.elementAt((indents.size() - pushedOpenings.size()) + i);
                    unit = indent(unit, unitIndent);
                    appendable.append(unit.replace("↲", "\n"));
                }
                pushedOpenings.clear();
                s = s.replace('\0', '\n');
                String shift = "";
                if (s.endsWith("\n")) {
                    shift = "\n";
                    s = s.substring(0, s.length() - 1);
                }
                s = indent(s, indent);
                appendable.append(m("CLOSE4") + s.replace("↲", "\n"));
                indent = indents.pop();
                close = close.replace('\0', '\n');
                appendable.append(indent(shift + close, indent).replace("↲", "\n"));
                topOpening = null;
            }
        }
    }

    /** Force vertical space with current indentation. */
    public void vspace(String space) throws IOException {
        append(m("VSPACE") + space + indentation(indent));
    }

    public Writer append(char c) throws IOException {
        if (topOpening == null) {
            if (c == '\n' || c == '\0') appendable.append("\n" + indentation(indent).replace("↲", "\n")); else if (c == '↲') appendable.append('\n'); else appendable.append(c);
        } else topOpening.append(c);
        return this;
    }

    public Writer append(CharSequence csq) throws IOException {
        for (int i = 0; i < csq.length(); ++i) append(csq.charAt(i));
        return this;
    }

    public Writer append(CharSequence csq, int start, int end) throws IOException {
        for (int i = start; i < end; ++i) append(csq.charAt(i));
        return this;
    }

    @Override
    public void flush() throws IOException {
        if (appendable instanceof Flushable) ((Flushable) appendable).flush();
    }

    @Override
    public void close() throws IOException {
        if (appendable instanceof Closeable) ((Closeable) appendable).close();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        int end = off + len;
        for (int i = off; i < end; ++i) append(cbuf[i]);
    }
}
