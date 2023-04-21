package com.tonbeller.jpivot.table.span;

import org.olap4j.metadata.Property;

/**
 * @author av
 * @version fsaz
 */
public class IgnorePropertiesHierarchyHeaderFactory extends HierarchyHeaderFactory {

    Span previous;

    public Span create(Span span) {
        previous = super.create(span);
        return previous;
    }

    public void visitProperty(Property v) {
        header.setObject(previous.getObject());
    }
}
