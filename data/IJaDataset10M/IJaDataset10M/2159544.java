package org.maestroframework.markup.html;

import java.util.Collection;

public class UnorderedList extends AbstractList {

    public UnorderedList() {
        super("ul");
        this.setForceShowCloseTag(true);
    }

    public UnorderedList(Collection<?> items) {
        super("ul", items);
    }

    public UnorderedList(Object... items) {
        super("ul", items);
    }
}
