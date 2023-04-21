package com.dbxml.db.core.query.plan.logical;

/**
 * ElementName
 */
public final class ElementName extends Name {

    public static final ElementName Wildcard = new ElementName("*");

    public ElementName(String nsURI, String name) {
        super(nsURI, name);
    }

    public ElementName(String name) {
        super(name);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ElementName) return super.equals(obj); else return false;
    }
}
