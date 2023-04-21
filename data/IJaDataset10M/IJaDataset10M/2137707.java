package spider.util;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;

/**
 * @author Gautam Pant
 * @see HTMLParser
 *      Element of the stack that stores information while parsing HTML page
 */
public class StackElement {

    public MutableAttributeSet attribs = null;

    public HTML.Tag tag;

    public String text = null;

    public int type = 0;

    public StackElement(HTML.Tag tag, MutableAttributeSet attribs) {
        this.tag = tag;
        this.attribs = (MutableAttributeSet) attribs.copyAttributes();
        type = 1;
    }

    public StackElement(String text) {
        this.text = text;
        type = 0;
    }

    public StackElement(HTML.Tag tag) {
        this.tag = tag;
        type = 2;
    }
}
