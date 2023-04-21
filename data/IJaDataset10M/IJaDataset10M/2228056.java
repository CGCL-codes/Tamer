package org.eclipse.jdt.internal.core.search;

import java.util.HashSet;
import java.util.Iterator;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.compiler.env.AccessRuleSet;

/**
 * Collects the resource paths reported by a client to this search requestor.
 */
public class PathCollector extends IndexQueryRequestor {

    public HashSet paths = new HashSet(5);

    public boolean acceptIndexMatch(String documentPath, SearchPattern indexRecord, SearchParticipant participant, AccessRuleSet access) {
        paths.add(documentPath);
        return true;
    }

    /**
	 * Returns the paths that have been collected.
	 */
    public String[] getPaths() {
        String[] result = new String[this.paths.size()];
        int i = 0;
        for (Iterator iter = this.paths.iterator(); iter.hasNext(); ) {
            result[i++] = (String) iter.next();
        }
        return result;
    }
}
