package code2html.impl.htmlcss;

import code2html.generic.*;

public class HtmlCssGutter extends GenericGutter {

    protected HtmlCssGutter() {
        this("#ffffff", "#8080c0", "#000000", 5);
    }

    public HtmlCssGutter(String bgColor, String fgColor, String highlightColor, int highlightInterval) {
        this(4, bgColor, fgColor, highlightColor, highlightInterval);
    }

    public HtmlCssGutter(int gutterSize, String bgColor, String fgColor, String highlightColor, int highlightInterval) {
        super(gutterSize, bgColor, fgColor, highlightColor, highlightInterval);
    }

    @Override
    public String format(int lineNumber) {
        return formatText(lineNumber, wrapText(lineNumber));
    }

    @Override
    public String formatEmpty(int lineNumber) {
        return formatText(lineNumber, spacer);
    }

    @Override
    public String style() {
        StringBuffer buf = new StringBuffer();
        buf.append(".gutter {\n").append("  background: " + this.bgColor + ";\n").append("  color: " + this.fgColor + ";\n").append("  border-right: 2px solid black ;\n").append("  margin-right: 5px ;\n").append("}\n").append(".gutterH {\n").append("  background: " + this.bgColor + ";\n").append("  color: " + this.highlightColor + ";\n").append("  border-right: 2px solid black ; \n").append("  margin-right: 5px ;\n").append("}\n");
        return buf.toString();
    }

    private String formatText(int lineNumber, String text) {
        StringBuffer buf = new StringBuffer();
        buf.append("<span class=\"").append(gutterStyle(lineNumber)).append("\">").append(text).append(" </span>");
        return buf.toString();
    }

    @Override
    public String getSpaceString() {
        return " ";
    }
}
