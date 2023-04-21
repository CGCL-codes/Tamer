package com.softwaresmithy.metadata.impl;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

public class GoogleBooksNamespaceContext implements NamespaceContext {

    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Null prefix");
        } else if (prefix.equals("atom")) {
            return "http://www.w3.org/2005/Atom";
        } else if (prefix.equals("openSearch")) {
            return "http://a9.com/-/spec/opensearchrss/1.0/";
        } else if (prefix.equals("gbs")) {
            return "http://schemas.google.com/books/2008";
        } else if (prefix.equals("dc")) {
            return "http://purl.org/dc/terms";
        } else if (prefix.equals("batch")) {
            return "http://schemas.google.com/gdata/batch";
        } else if (prefix.equals("gd")) {
            return "http://schemas.google.com/g/2005";
        }
        return null;
    }

    @Override
    public String getPrefix(String namespaceURI) {
        return null;
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        return null;
    }
}
