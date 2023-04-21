package net.sourceforge.freejava.sio.indent;

import java.util.Arrays;

public class TextIndention implements ITextIndention {

    private int indentSize = 4;

    private int tabSize = 8;

    static final int SPACEONLY = 0;

    static final int TABONLY = 1;

    static final int MIXED = 2;

    private int mode;

    @Override
    public int getIndentSize() {
        return indentSize;
    }

    /**
     * @throws IllegalArgumentException
     *             If <code>indentSize</code> is negative.
     */
    @Override
    public void setIndentSize(int indentSize) {
        if (indentSize < 0) throw new IllegalArgumentException("indentSize is negative: " + indentSize);
        this.indentSize = indentSize;
        this.mode = SPACEONLY;
    }

    @Override
    public int getTabSize() {
        return tabSize;
    }

    /**
     * @throws IllegalArgumentException
     *             If <code>tabSize</code> is negative.
     */
    @Override
    public void setTabSize(int tabSize) {
        if (tabSize < 0) throw new IllegalArgumentException("tabSize is negative: " + tabSize);
        this.tabSize = tabSize;
        this.mode = TABONLY;
    }

    @Override
    public boolean isMixedMode() {
        return mode == MIXED;
    }

    @Override
    public void setMixedMode(boolean mixedMode) {
        if (mixedMode) this.mode = MIXED; else if (this.mode == MIXED) this.mode = SPACEONLY;
        setIndentLevel(indentLevel);
    }

    private int indentLevel;

    private int indent;

    private String linePrefix = "";

    @Override
    public int getIndentLevel() {
        return indentLevel;
    }

    /**
     * @throws IllegalArgumentException
     *             If <code>indentLevel</code> is negative.
     */
    @Override
    public void setIndentLevel(int indentLevel) {
        if (indentLevel < 0) throw new IllegalArgumentException("indent level is negative: " + indentLevel);
        this.indentLevel = indentLevel;
        indent = indentLevel * indentSize;
        switch(mode) {
            case SPACEONLY:
                linePrefix = repeat(indentLevel * indentSize, ' ');
                break;
            case TABONLY:
                linePrefix = repeat(indentLevel, '\t');
                break;
            case MIXED:
                int spaces = indentLevel * indentSize;
                int tabs = spaces / tabSize;
                spaces %= tabSize;
                linePrefix = repeat(tabs, '\t') + repeat(spaces, ' ');
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int increaseIndentLevel() {
        switch(mode) {
            case SPACEONLY:
                ++indentLevel;
                indent += indentSize;
                linePrefix += repeat(indentSize, ' ');
                break;
            case TABONLY:
                ++indentLevel;
                linePrefix += '\t';
                break;
            case MIXED:
                indentLevel++;
                int lastSpaces = indent;
                int newSpaces = indent += indentSize;
                int lastTabs = lastSpaces / tabSize;
                lastSpaces %= tabSize;
                int newTabs = newSpaces / tabSize;
                newSpaces %= tabSize;
                int addTabs = newTabs - lastTabs;
                if (addTabs == 0) linePrefix += repeat(newSpaces - lastSpaces, ' '); else linePrefix = linePrefix.substring(0, linePrefix.length() - lastSpaces) + repeat(addTabs, '\t') + repeat(newSpaces, ' ');
                break;
        }
        return indentLevel;
    }

    @Override
    public int decreaseIndentLevel() {
        if (indentLevel == 0) throw new IllegalStateException("Not indented at all");
        switch(mode) {
            case SPACEONLY:
                indentLevel--;
                indent -= indentSize;
                linePrefix = linePrefix.substring(0, linePrefix.length() - indentSize);
                break;
            case TABONLY:
                indentLevel--;
                linePrefix = linePrefix.substring(0, linePrefix.length() - 1);
                break;
            case MIXED:
                indentLevel--;
                int lastSpaces = indent;
                int newSpaces = indent -= indentSize;
                int lastTabs = lastSpaces / tabSize;
                lastSpaces %= tabSize;
                int newTabs = newSpaces / tabSize;
                newSpaces %= tabSize;
                int removeTabs = lastTabs - newTabs;
                if (removeTabs == 0) linePrefix = linePrefix.substring(0, linePrefix.length() - (lastSpaces - newSpaces)); else linePrefix = linePrefix.substring(0, linePrefix.length() - lastSpaces - removeTabs) + repeat(newSpaces, ' ');
                break;
        }
        return indentLevel;
    }

    static String repeat(int count, String pattern) {
        StringBuffer buf = new StringBuffer(pattern.length() * count);
        while (--count >= 0) buf.append(pattern);
        return buf.toString();
    }

    static String repeat(int count, char c) {
        char[] buf = new char[count];
        Arrays.fill(buf, c);
        return new String(buf);
    }

    @Override
    public String getCurrentLinePrefix() {
        return linePrefix;
    }

    @Override
    public void setCurrentLinePrefix(String linePrefix) {
        this.linePrefix = linePrefix;
    }

    @Override
    public String toString() {
        return linePrefix;
    }
}
