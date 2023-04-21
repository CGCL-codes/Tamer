package net.sf.saxon.om;

import java.util.Iterator;

/**
 * Abstract class that supports lookup of a lexical QName to get the expanded QName.
 */
public interface NamespaceResolver {

    /**
     * Get the namespace URI corresponding to a given prefix. Return null
     * if the prefix is not in scope.
     * @param prefix the namespace prefix. May be the zero-length string, indicating
     * that there is no prefix. This indicates either the default namespace or the
     * null namespace, depending on the value of useDefault.
     * @param useDefault true if the default namespace is to be used when the
     * prefix is "". If false, the method returns "" when the prefix is "".
     * @return the uri for the namespace, or null if the prefix is not in scope.
     * The "null namespace" is represented by the pseudo-URI "".
    */
    public abstract String getURIForPrefix(String prefix, boolean useDefault);

    /**
     * Get an iterator over all the prefixes declared in this namespace context. This will include
     * the default namespace (prefix="") and the XML namespace where appropriate
     */
    public abstract Iterator iteratePrefixes();
}
