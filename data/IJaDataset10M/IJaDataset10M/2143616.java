package net.sf.compositor.util.doclet;

import java.util.HashSet;
import java.util.Set;

class HtmlMeta extends HtmlElement {

    private static final Set<String> s_attributeNames = new HashSet<String>() {

        {
            add("http-equiv");
            add("content");
            add("name");
        }
    };

    HtmlMeta(final String[] attributeDetails) {
        super("meta", attributeDetails, EMPTY_ELEMENT);
    }

    @Override
    protected boolean isValidAttribute(final String name) {
        return s_attributeNames.contains(name) || super.isValidAttribute(name);
    }
}
