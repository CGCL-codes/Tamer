package pikes.html.xhtml;

import pikes.util.Assert;
import pikes.xml.XMLTag;

/**
 * <code>&lt;html/&gt;</code> tag, see <a href="http://www.w3.org/TR/html401/struct/global.html#h-7.3">HTML element</a>
 * in HTML specification. Body and Head have to be specifed at creation. Language and direction properties can be set.
 * @author Peter Bona
 * @see <a href="http://www.w3.org/TR/html401/struct/global.html#h-7.3">The HTML element in HTML specification</a>
 */
public class Html extends TagFactory implements I18N {

    private Body body = null;

    private Head head = null;

    /**
	 * Creates a Html tag.
	 * @param head
	 * @param body
	 */
    public Html(Head head, Body body) {
        Assert.notNull(head);
        Assert.notNull(body);
        this.head = head;
        this.body = body;
    }

    /**
	 * @return "html"
	 */
    @Override
    protected final CharSequence getTagName() {
        return "html";
    }

    @Override
    protected void decorateCreatedTag(XMLTag tag) {
        if (head != null) {
            head.decorateTag(tag);
        }
        if (body != null) {
            body.decorateTag(tag);
        }
    }
}
