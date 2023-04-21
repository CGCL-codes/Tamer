package net.sf.iqser.plugin.web.html.filters;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;

/**
 * A flexible TagName Filter that matches the tag name based on regex.
 * Tag name is lower case.
 */
public class RegExTagNameFilter implements NodeFilter {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1149925710166995095L;

    private String pattern;

    public RegExTagNameFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(Node node) {
        if (node instanceof Tag) {
            Tag tag = (Tag) node;
            if (!tag.isEndTag()) {
                return tag.getTagName().toLowerCase().matches(pattern);
            }
        }
        return false;
    }
}
