package org.exteca.web.search.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  <code>ResultSet</code> represents the entire result information for a search. This consists
 *  of a collection of <code>Document</code>, <code>Concept</code> and
 *  <code>RequestRange</code>.
 *
 *  @author Neetu Jain
 */
public class ResultSet {

    /** The Logger */
    Log log = LogFactory.getLog(ResultSet.class);

    private int totalCount;

    private int startRange;

    private int endRange;

    private Map documents;

    private Map concepts;

    private Map requestRanges;

    /**
     *  Constructor
     */
    public ResultSet() {
        documents = new TreeMap();
        concepts = new TreeMap();
        requestRanges = new TreeMap();
        totalCount = -1;
        startRange = -1;
        endRange = -1;
    }

    /**
     *  Gets all <code>Document</code>s returned within this range.
     *  The documents are ordered by document number.
     *  @return a <code>Document</code> collection
     */
    public Map getDocuments() {
        return documents;
    }

    /**
     *  Sets all <code>Document</code>s within this range.
     *  @param li a <code>Document</code> collection
     */
    public void setDocuments(Map li) {
        documents = li;
    }

    /**
     *  Gets all <code>Concept</code>s for all <code>Document</code> in this range.
     *  The Concepts are ordered alphabetically.
     *  @return a <code>Concept</code> collection
     */
    public Map getConcepts() {
        return concepts;
    }

    /**
     *  Sets all <code>Concept</code>s for all <code>Document</code> in this range.
     *  @param li a <code>Concept</code> collection
     */
    public void setConcepts(Map li) {
        concepts = li;
    }

    /**
     *  Gets all <code>RequestRange</code>s for this result set.
     *  @return a <code>RequestRange</code> collection
     */
    public Map getRequestRanges() {
        return requestRanges;
    }

    /**
     *  Sets all <code>RequestRange</code>s for this result set.
     *  The RequestRange are ordered by start range.
     *  @param li a <code>RequestRange</code> collection
     */
    public void setRequestRanges(Map li) {
        requestRanges = li;
    }

    /**
     *  Gets the total count of matches for this result set.
     *  @return a total count
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     *  Sets the total conut of matches for this result set.
     *  @param total total count
     */
    public void setTotalCount(int total) {
        totalCount = total;
    }

    /**
     *  Gets the current start range for the <code>RequestRange</code> being viewed
     *  @return a current start range
     */
    public int getStartRange() {
        return startRange;
    }

    /**
     *  Sets the current start range for the <code>RequestRange</code> being viewed
     *  @param start current start item
     */
    public void setStartRange(int start) {
        startRange = start;
    }

    /**
     *  Gets the current end item for the <code>RequestRange</code> being viewed
     *  @return a current end item
     */
    public int getEndRange() {
        return endRange;
    }

    /**
     *  Sets the current end item for the <code>RequestRange</code> being viewed
     *  @param end current end item
     */
    public void setEndRange(int end) {
        endRange = end;
    }

    /**
     *  Sets the ResultSet from data in the other ResultSet. This is always called
     *  when displaying a new result set e.g. page next, page previous.
     *  @param set The other ResultSet to populate the ResultSet with
     *  @param rankConcepts Whether or not to rank concepts based upon most
     *  frequently occurring.      
     */
    public void setResultSet(ResultSet rset, boolean rankConcepts) {
        this.setDocuments(rset.getDocuments());
        this.setRequestRanges(rset.getRequestRanges());
        this.totalCount = rset.getTotalCount();
        this.startRange = rset.getStartRange();
        this.endRange = rset.getEndRange();
        if (rankConcepts) {
            rset.orderConceptsByFrequency();
        }
        this.setConcepts(rset.getConcepts());
    }

    /**
     *  Clears all data from this ResultSet
     */
    public void clear() {
        this.getDocuments().clear();
        this.getConcepts().clear();
        this.getRequestRanges().clear();
        totalCount = -1;
        startRange = -1;
        endRange = -1;
    }

    /**
     *  Ramk the concepts in order of frequency so that the most frequent appears first.
     *
     */
    public void orderConceptsByFrequency() {
        SortedMap orderedConcepts = new TreeMap(new ConceptComparator());
        if (concepts != null && !concepts.isEmpty()) {
            Set set = concepts.keySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                String name = (String) i.next();
                Concept ci = (Concept) concepts.get(name);
                if (ci != null) {
                    if (log.isDebugEnabled()) log.debug(ci.getId() + ":" + ci.getFrequency());
                    orderedConcepts.put(ci, ci);
                }
            }
            this.setConcepts(orderedConcepts);
        }
    }
}
