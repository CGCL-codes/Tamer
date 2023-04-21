package com.strongauth.skcengine;

import java.util.Vector;
import java.util.Iterator;

/**
 *  Needed to setup the namespace context for XPath processing
 */
public class SKCENamespaceContext implements javax.xml.namespace.NamespaceContext {

    private Vector<Namespace> v;

    /** Creates a new instance of SKCENamespaceContext */
    public SKCENamespaceContext() {
        v = new Vector();
        v.add(new Namespace("ds", "http://www.w3.org/2000/09/xmldsig#"));
        v.add(new Namespace("xenc", "http://www.w3.org/2001/04/xmlenc#"));
        v.add(new Namespace("skes", "http://strongkey.strongauth.com/SKES201101"));
        v.add(new Namespace("skles", "http://strongkeylite.strongauth.com/SKLES201009"));
    }

    @Override
    public String getNamespaceURI(String prefix) {
        String s = null;
        for (Namespace n : v) {
            if (n.prefix.equals(prefix.trim())) s = n.uri;
        }
        return s;
    }

    @Override
    public String getPrefix(String uri) {
        String s = null;
        for (Namespace n : v) {
            if (n.uri.equals(uri.trim())) s = n.prefix;
        }
        return s;
    }

    @Override
    public Iterator getPrefixes(String uri) {
        return v.iterator();
    }
}

class Namespace {

    String prefix;

    String uri;

    public Namespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }
}
