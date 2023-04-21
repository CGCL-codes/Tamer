package com.hp.hpl.jena.rdql.parser;

import com.hp.hpl.jena.rdql.Query;

public class Q_QuotedURI extends Q_URI {

    String seen = "";

    boolean isAbsolute = false;

    Q_QuotedURI(int id) {
        super(id);
    }

    Q_QuotedURI(RDQLParser p, int id) {
        super(p, id);
    }

    void set(String s) {
        seen = s;
    }

    public void jjtClose() {
        super._setURI(seen);
    }

    public void postParse(Query query) {
        super.postParse(query);
        if (!isAbsolute) absolute(query);
    }

    static final String prefixOperator = ":";

    private void absolute(Query query) {
        if (query == null) {
            isAbsolute = true;
            return;
        }
        int i = seen.indexOf(prefixOperator);
        if (i < 0) {
            isAbsolute = true;
            return;
        }
        String prefix = seen.substring(0, i);
        String full = query.getPrefix(prefix);
        if (full == null) {
            isAbsolute = true;
            return;
        }
        String remainder = seen.substring(i + prefixOperator.length());
        super._setURI(full + remainder);
        isAbsolute = true;
    }

    public static Q_URI makeURI(String s) {
        Q_URI uri = new Q_URI(0);
        uri._setURI(s);
        return uri;
    }

    public String asQuotedString() {
        return "<" + seen + ">";
    }

    public String asUnquotedString() {
        return seen;
    }

    public String valueString() {
        return super.getURI();
    }

    public String toString() {
        return seen;
    }
}
