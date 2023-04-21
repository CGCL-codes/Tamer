package org.gwtoolbox.commons.util.client;

/**
 * @author Uri Boness
 */
public class PrettyStringBuilder {

    private final StringBuilder builder;

    private final int indentation;

    private int level = 0;

    private boolean indentNeeded = true;

    public PrettyStringBuilder() {
        this(4);
    }

    public PrettyStringBuilder(int indentation) {
        builder = new StringBuilder();
        this.indentation = indentation;
    }

    public PrettyStringBuilder append(String str) {
        indentIfNeeded();
        builder.append(str);
        return this;
    }

    public PrettyStringBuilder appendln(String str) {
        return append(str).appendln();
    }

    public PrettyStringBuilder appendln() {
        append("\n");
        indentNeeded = true;
        return this;
    }

    public PrettyStringBuilder append(Object object) {
        return append(String.valueOf(object));
    }

    public PrettyStringBuilder appendln(Object object) {
        return appendln(String.valueOf(object));
    }

    public PrettyStringBuilder append(CharSequence seq, int offset, int len) {
        indentIfNeeded();
        builder.append(seq, offset, len);
        return this;
    }

    public PrettyStringBuilder appendln(CharSequence seq, int offset, int len) {
        return append(seq, offset, len).appendln();
    }

    public PrettyStringBuilder append(char[] str) {
        indentIfNeeded();
        builder.append(str);
        return this;
    }

    public PrettyStringBuilder appendln(char[] str) {
        return append(str).appendln();
    }

    public PrettyStringBuilder indent() {
        level++;
        return this;
    }

    public PrettyStringBuilder outdent() {
        level--;
        return this;
    }

    public String toString() {
        return builder.toString();
    }

    private void indentIfNeeded() {
        if (indentNeeded) {
            indentNeeded = false;
            for (int i = 0; i < level * indentation; i++) {
                builder.append(" ");
            }
        }
    }
}
