package com.limegroup.gnutella.gui.search;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import org.limewire.collection.ApproximateMatcher;
import org.limewire.collection.Comparators;

/** 
 * Used by TableLineModel to quickly find similar SearchResults.  This takes
 * advantage of the fact that two SearchResults' are similar only if their file
 * sizes are similar.  A typical TableLineGrouper is a set of SearchResults', 
 * {a1,..., an}.
 */
public final class TableLineGrouper {

    /** Maps sizes to lists of index/line pairs.  This list is needed in case
     *  there are multiple files with same size.  (Performance is severely degraded
     *  in this case.) */
    private SortedMap<Long, List<SearchResult>> map = new TreeMap<Long, List<SearchResult>>(Comparators.longComparator());

    /** Used to compare all filenames in this.  Ignores case, whitespace.  */
    private final ApproximateMatcher matcher;

    public TableLineGrouper() {
        this.matcher = new ApproximateMatcher(120);
        this.matcher.setIgnoreCase(true);
        this.matcher.setIgnoreWhitespace(true);
        this.matcher.setCompareBackwards(true);
    }

    /** Returns true if empty, i.e., cleared. */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        map.clear();
    }

    /** Returns an j s.t. list[j].match(line), 
     *  or -1 if no such i but the caller should keep searching
     *      (because no line in list is close in size)
     *  or -2 if no such i but the caller shouldn't keep searching
     *      (because no line in list is close in size)
     *  @requires elements of list are GrouperPair */
    private int matchHelper(List<SearchResult> list, SearchResult line) {
        assert list != null : "Trying to match null list";
        assert line != null : "Trying to match null line";
        for (int j = 0; j < list.size(); j++) {
            SearchResult line2 = list.get(j);
            int match = line2.match(line, matcher);
            if (match == 0) return j; else if (match == 2) return -2;
        }
        return -1;
    }

    /** Returns a line G in this s.t. G.similar(line), or null of no such line
     *  exists. */
    public SearchResult match(SearchResult line) {
        Long key = Long.valueOf(line.getSize());
        Iterator<List<SearchResult>> iter = map.tailMap(key).values().iterator();
        while (iter.hasNext()) {
            List<SearchResult> lines = iter.next();
            int ret = matchHelper(lines, line);
            if (ret >= 0) return lines.get(ret); else if (ret == -2) break;
        }
        SortedMap<Long, List<SearchResult>> map = this.map.headMap(key);
        while (true) {
            Long key2;
            try {
                key2 = map.lastKey();
            } catch (NoSuchElementException e) {
                break;
            }
            List<SearchResult> lines = map.get(key2);
            int ret = matchHelper(lines, line);
            if (ret >= 0) return lines.get(ret); else if (ret == -2) break;
            map = map.headMap(key2);
        }
        return null;
    }

    /**
     * Adds line to this.  Generally there should be no lines similar to line
     * in this, i.e., this.match(line)==null, but this isn't strictly required.
     *     @requires line not in this
     *     @modifies this 
     */
    public void add(SearchResult line) {
        assert line != null : "Attempting to add null line";
        Long key = Long.valueOf(line.getSize());
        List<SearchResult> lines = map.get(key);
        if (lines == null) {
            lines = new LinkedList<SearchResult>();
            map.put(key, lines);
        }
        lines.add(line);
    }
}
