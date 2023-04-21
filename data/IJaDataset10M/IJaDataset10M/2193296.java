package br.com.felix.fwt.ui;

import java.io.PrintWriter;
import br.com.felix.fwt.ui.exception.ComponentRenderException;

/**
 * The Class HtmlWriter is a helper class to write some HTML tags. It uses
 * StringBuilder inside, so it helps writing long texts without concatenation
 * also.
 */
public class HtmlWriter extends Html {

    private static final long serialVersionUID = 555068169472856224L;

    /** The String Builder. */
    private StringBuilder strb;

    /**
	 * Instantiates a new html writer.
	 */
    public HtmlWriter() {
        strb = new StringBuilder();
    }

    /**
	 * Instantiates a new html writer.
	 * 
	 * @param text the text
	 */
    public HtmlWriter(String text) {
        strb = new StringBuilder(text);
    }

    /**
	 * Appends text within 'h1' tags.
	 * 
	 * @param text the text
	 * 
	 * @return the html writer
	 */
    public HtmlWriter header1(String text) {
        return tag("h1", text);
    }

    /**
	 * Starts a 'p' tag
	 * 
	 * @return the html writer
	 */
    public HtmlWriter paragraphStart() {
        strb.append("\n<p>\n");
        return this;
    }

    /**
	 * Ends a 'p' tag and starts a new one.
	 * 
	 * @return the html writer
	 */
    public HtmlWriter paragraphEndStart() {
        strb.append("\n</p>\n<p>");
        return this;
    }

    /**
	 * Ends a 'p' tag.
	 * 
	 * @return the html writer
	 */
    public HtmlWriter paragraphEnd() {
        strb.append("\n</p>");
        return this;
    }

    /**
	 * Appends text.
	 * 
	 * @param text the text
	 * 
	 * @return the html writer
	 */
    public HtmlWriter text(String text) {
        strb.append(text);
        return this;
    }

    @Override
    public void write(PrintWriter out) throws ComponentRenderException {
        out.write(strb.toString());
    }

    /**
	 * Appends text within 'em' tags.
	 * 
	 * @param string the string
	 * 
	 * @return the html writer
	 */
    public HtmlWriter emphasis(String string) {
        return tag("em", string);
    }

    /**
	 * Ordered list start.
	 * 
	 * @return the html writer
	 */
    public HtmlWriter orderedListStart() {
        strb.append("\n<ol>\n");
        return this;
    }

    /**
	 * Appends text within 'li' tags.
	 * 
	 * @param text the text
	 * 
	 * @return the html writer
	 */
    public HtmlWriter listItem(String text) {
        return tag("li", text);
    }

    /**
	 * Ordered list end.
	 * 
	 * @return the html writer
	 */
    public HtmlWriter orderedListEnd() {
        strb.append("\n</ol>");
        return this;
    }

    /**
	 * Appends text within 'h2' tags.
	 * 
	 * @param string the string
	 * 
	 * @return the html writer
	 */
    public HtmlWriter header2(String string) {
        return tag("h2", string);
    }

    /**
	 * Appends a 'ul' tag.
	 * */
    public HtmlWriter unorderedListStart() {
        strb.append("\n<ul>\n");
        return this;
    }

    /**
	 * Appends a '/ul' tag.
	 * */
    public HtmlWriter unorderedListEnd() {
        strb.append("\n</ul>\n");
        return this;
    }

    /**
	 * Erases all current content.
	 * */
    @Override
    public void clearInterface() {
        strb.delete(0, strb.length());
    }

    public HtmlWriter bold(String message) {
        return tag("b", message);
    }

    private HtmlWriter tag(String tag, String message) {
        strb.append("\n<").append(tag).append(">\n").append(message).append("\n</").append(tag).append(">");
        return this;
    }

    public BaseComponent paragraph(String string) {
        return tag("p", string);
    }
}
