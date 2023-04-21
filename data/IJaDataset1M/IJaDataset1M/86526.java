package com.dbxml.db.core.query.plan.logical;

/**
 * Name
 */
public abstract class Name extends LogicalQueryNode {

    private String nsURI;

    private String name;

    public Name(String nsURI, String name) {
        this.nsURI = nsURI;
        this.name = name;
    }

    public Name(String name) {
        this.name = name;
    }

    public String getNSURI() {
        return nsURI;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object obj) {
        Name n = (Name) obj;
        if (!name.equals(n.name)) return false;
        if (nsURI == null && n.nsURI == null) return true; else if (nsURI != null && n.nsURI != null) return nsURI.equals(n.nsURI); else return false;
    }
}
