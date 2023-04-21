package org.sourceforge.zlang.model;

import org.sourceforge.zlang.model.XMLCreator;
import org.sourceforge.zlang.model.parser.SimpleNode;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * A comment.
 *
 * @author Tim Lebedkov
 * @version $Id: ZComment.java,v 1.2 2002/12/04 22:45:54 hilt2 Exp $
 */
public class ZComment extends ZElement {

    static final long serialVersionUID = -2852391330047038924L;

    private String text = "";

    /**
     * Constructor
     *
     * @param parent parent element
     * @param text comment's text
     */
    public ZComment(ZElement parent, String text) {
        super(parent);
        this.text = text;
    }

    /**
     * Sets new text
     *
     * @param text new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the text
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    public void printJava(IndentedWriter w) {
        printMultilineComment(w, text, false);
    }

    public void printXML(XMLCreator h) throws SAXException {
        h.e("comment");
        h.ch(text);
        h.ee();
    }

    public void load(Element el) {
        text = el.getFirstChild().getNodeValue();
    }

    public void load(SimpleNode el) {
        text = ZElement.collectCommentsBefore(el.firstToken);
    }
}
