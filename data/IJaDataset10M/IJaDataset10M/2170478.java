package net.sourceforge.kwaai.markup.element;

import net.sourceforge.kwaai.element.Attribute;
import net.sourceforge.kwaai.markup.AbstractElement;

/**
 * Anchor html element.
 *
 * @author Michal Capo
 */
public abstract class Link extends AbstractElement {

    public Link() {
        super();
        addAttribute(Attribute.HREF, getHref());
    }

    public Link(final String text) {
        super();
        setInnerContent(text);
        addAttribute(Attribute.HREF, getHref());
    }

    public Link(final String id, final String text) {
        super(id);
        setInnerContent(text);
        addAttribute(Attribute.HREF, getHref());
    }

    public abstract String getHref();
}
