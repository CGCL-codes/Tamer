package de.str.prettysource.output;

import java.io.PrintWriter;
import org.apache.commons.lang.StringEscapeUtils;
import de.str.prettysource.OutputNode;

/**
 * {@link OutputNode} for HTML content. These nodes are used for {@link HtmlOutputFormat}
 * 
 * @author Denny.Strietzbaum
 */
public class HtmlOutputNode implements OutputNode {

    private String styleClass = null;

    private String styleValues = null;

    private String openingTag = "";

    private String closingTag = "";

    public HtmlOutputNode() {
    }

    public HtmlOutputNode(String styleClass) {
        this.setStyleClass(styleClass);
    }

    /**
	 * Returns html style values
	 * 
	 * @return style values
	 */
    public String getStyleValues() {
        return styleValues;
    }

    /**
	 * Sets html style values. This is a semicolon separated list of styles. (no style class, no
	 * opening and closing braces)
	 * 
	 * @param styleValues
	 *            style values
	 */
    public void setStyleValues(String styleValues) {
        this.styleValues = styleValues;
    }

    /**
	 * Returns the style class.
	 * 
	 * @return style class as string
	 */
    public String getStyleClass() {
        return styleClass;
    }

    /**
	 * Sets a style class.
	 * 
	 * @param styleClass
	 *            style class of this selection
	 */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
        if (styleClass != null && styleClass.trim().length() > 0) {
            this.openingTag = "<span class=\"" + styleClass + "\">";
            this.closingTag = "</span>";
        } else {
            this.openingTag = "";
            this.closingTag = "";
        }
    }

    /**
	 * Returns the opening tag.
	 * 
	 * @return opening html tag as String
	 */
    public String getOpeningTag() {
        return this.openingTag;
    }

    /**
	 * Returns the closing tag
	 * 
	 * @return closing html tag as String
	 */
    public String getClosingTag() {
        return this.closingTag;
    }

    public void printInputNodeContent(PrintWriter printer, String content) {
        String escaped = StringEscapeUtils.escapeHtml(content);
        printer.print(getOpeningTag());
        printer.print(escaped);
        printer.print(getClosingTag());
    }
}
