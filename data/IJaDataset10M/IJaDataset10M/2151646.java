package org.exist.indexing.fulltext.utils;

import org.exist.dom.DocumentSet;
import org.exist.dom.NodeSet;
import org.exist.dom.QName;
import org.exist.xquery.XQueryContext;

/**
 * Implementations of this interface can be passed to method
* {@link org.exist.storage.TextSearchEngine#getNodes(XQueryContext context, DocumentSet docs, NodeSet contextSet, int axis, QName qname,
	        TermMatcher matcher, CharSequence startTerm)} 
 * to check if an index entry matches a given search term.
 * 
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public interface TermMatcher {

    public boolean matches(CharSequence term);
}
