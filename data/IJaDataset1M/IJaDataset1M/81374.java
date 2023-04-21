package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSWildcard;

/**
 * Visits three kinds of {@link XSWildcard}.
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public interface XSWildcardVisitor {

    void any(XSWildcard.Any wc);

    void other(XSWildcard.Other wc);

    void union(XSWildcard.Union wc);
}
