package code2html.generic;

import java.awt.Color;

public abstract class GenericGutter {

    protected int gutterSize;

    protected char gutterBorder = ':';

    protected int gutterBorderSize = 1;

    protected String bgColor;

    protected String fgColor;

    protected Color bg;

    protected Color fg;

    protected String highlightColor;

    protected int highlightInterval;

    protected String spacer;

    protected GenericGutter() {
        this("#ffffff", "#000000", "#8080c0", 5);
    }

    public GenericGutter(String bgColor, String fgColor, String highlightColor, int highlightInterval) {
        this(4, bgColor, fgColor, highlightColor, highlightInterval);
    }

    public GenericGutter(int gutterSize, String bgColor, String fgColor, String highlightColor, int highlightInterval) {
        setGutterSize(gutterSize);
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.bg = Color.decode(bgColor);
        this.fg = Color.decode(fgColor);
        this.highlightColor = highlightColor;
        this.highlightInterval = highlightInterval;
    }

    public boolean isHighlighted(int lineNumber) {
        return (this.highlightInterval > 0) && (lineNumber % this.highlightInterval == 0);
    }

    public String gutterStyle(int lineNumber) {
        return isHighlighted(lineNumber) ? "gutterH" : "gutter";
    }

    public String getColorString(int lineNumber) {
        return isHighlighted(lineNumber) ? highlightColor : fgColor;
    }

    public int getGutterBorderSize() {
        return gutterBorderSize;
    }

    public int getGutterSize() {
        return gutterSize;
    }

    public int getSize() {
        return getGutterSize() + getGutterBorderSize();
    }

    public abstract String format(int lineNumber);

    public abstract String formatEmpty(int lineNumber);

    public abstract String style();

    public void setGutterSize(int gutterSize) {
        this.gutterSize = gutterSize;
        this.spacer = getSpacer();
    }

    public abstract String getSpaceString();

    public String getSpacer(int size) {
        String space = getSpaceString();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < size; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    public String getSpacer() {
        return getSpacer(gutterSize);
    }

    public String wrapText(int lineNumber) {
        StringBuffer buf = new StringBuffer();
        String s = Integer.toString(lineNumber);
        String spaces = getSpacer(getGutterSize() - s.length());
        buf.append(spaces).append(s);
        return buf.toString();
    }
}
