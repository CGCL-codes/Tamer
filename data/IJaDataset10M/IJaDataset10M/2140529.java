package org.archive.crawler.util;

import java.util.HashSet;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.UURI;
import org.archive.crawler.datamodel.UURISet;

/**
 * @author gojomo
 *
 */
public class MemUURISet extends HashSet implements UURISet {

    public long count() {
        return size();
    }

    public boolean contains(UURI u) {
        return contains((Object) u);
    }

    public boolean contains(CandidateURI curi) {
        return contains((Object) curi.getUURI());
    }

    public void add(UURI u) {
        add((Object) u);
    }

    public void remove(UURI u) {
        remove((Object) u);
    }

    public void add(CandidateURI curi) {
        add(curi.getUURI());
    }

    public void remove(CandidateURI curi) {
        remove(curi.getUURI());
    }
}
