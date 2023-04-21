package com.incendiaryblue.cmslite.index;

import com.incendiaryblue.cmslite.Category;
import com.incendiaryblue.cmslite.LiveContent;
import com.incendiaryblue.storage.BusinessObjectList;
import com.incendiaryblue.storage.StorageManager;
import com.incendiaryblue.util.JDBCUtils;
import java.sql.*;
import java.util.*;

/**
 * Search engine
 *
 * <p>Reponsible for searching the index tables.</p>
 */
class SearchEngine {

    public static final boolean debug = true;

    public static final int SEARCH_CONTENT = 1;

    public static final int SEARCH_CATEGORIES = 2;

    public static final int SEARCH_CATEGORIES_BY_CONTENT = 3;

    /** The maximum number of search results to attempt to retreive. */
    public static final int maxResults = 250;

    public boolean isSearching() {
        System.out.println("SearchEngine: numSearching = " + numSearching);
        return numSearching > 0;
    }

    /**
	 * Perform a search.
	 *
	 * @param index The index to search.
	 * @param searchType One of SEARCH_CONTENT, SEARCH_CATEGORIES or SEATCH_CATEGORIES_BY_CONTENT.
	 * @param query The query to match.
	 * @param category The category to search under (only valid for content search), or null.
	 */
    public List search(Index index, int searchType, String query, Category category) throws SQLException, InterruptedException {
        if (debug) System.out.println("SearchEngine.search: " + query);
        if (category != null && searchType != SEARCH_CONTENT) throw new IllegalArgumentException("Parameter category must be null unless searching for content");
        preSearch();
        Connection conn = null;
        try {
            Set termStats = getTermStats(index, query);
            if (termStats == null || termStats.size() == 0) return Collections.EMPTY_LIST;
            String sql = makeSQL(index, termStats, searchType, category);
            if (debug) System.out.println("  sql == " + sql);
            conn = indexSearch.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List keyList = new ArrayList();
            while (rs.next()) keyList.add(JDBCUtils.getInteger(rs, 1));
            StorageManager sm;
            sm = searchType == SEARCH_CONTENT ? LiveContent.getLiveStorageManager() : Category.getStorageManager();
            return new BusinessObjectList(sm, query, keyList);
        } finally {
            postSearch();
            if (conn != null) indexSearch.releaseConnection(conn);
        }
    }

    private void preSearch() throws InterruptedException {
        synchronized (indexSearch) {
            indexSearch.waitForIndexBuilderNotUpdating();
            ++numSearching;
        }
    }

    private void postSearch() {
        synchronized (indexSearch) {
            --numSearching;
            indexSearch.notifyAll();
        }
    }

    /**
	 * Split the query up and lookup the terms.
	 *
	 * @return A set of TermStats objects, or null if any of the terms were not found.
	 */
    private Set getTermStats(Index index, String query) {
        List termNameList = indexSearch.processText(query);
        Set termStatsSet = new HashSet();
        for (Iterator i = termNameList.iterator(); i.hasNext(); ) {
            String s = (String) i.next();
            Term t = termHome.getTerm(s);
            if (t == null) return null;
            TermStats ts = termStatsHome.getTermStats(index, t);
            if (ts == null) return null;
            termStatsSet.add(ts);
        }
        return termStatsSet;
    }

    private String makeSQL(Index index, Set termStats, int searchType, Category category) throws SQLException, InterruptedException {
        StringBuffer buf = new StringBuffer();
        buf.append("SELECT ");
        if (searchType == SEARCH_CATEGORIES_BY_CONTENT) buf.append("DISTINCT ");
        buf.append("TOP ");
        buf.append(maxResults);
        buf.append(searchType == SEARCH_CATEGORIES_BY_CONTENT ? " D1.PARENT_ID" : " D1.OBJECT_ID");
        buf.append(" FROM ");
        for (int i = 0; i < termStats.size(); ++i) {
            if (i != 0) buf.append(", ");
            buf.append("INDEX_DATA AS D");
            buf.append(i + 1);
        }
        buf.append(" WHERE ");
        for (int i = 1; i < termStats.size(); ++i) {
            if (i != 1) buf.append(" AND ");
            buf.append("D");
            buf.append(i);
            buf.append(".OBJECT_ID = D");
            buf.append(i + 1);
            buf.append(".OBJECT_ID");
        }
        if (termStats.size() > 1) buf.append(" AND ");
        int i = 1;
        for (Iterator j = termStats.iterator(); j.hasNext(); ) {
            TermStats ts = (TermStats) j.next();
            buf.append("D");
            buf.append(i);
            buf.append(".INDEX_ID = ");
            buf.append(index.getDatabaseId());
            buf.append(" AND ");
            buf.append("D");
            buf.append(i);
            buf.append(".TERM_ID = ");
            buf.append(ts.getTermId());
            if (i != 1 || category == null) {
                buf.append(" AND ");
                buf.append("D");
                buf.append(i);
                buf.append(".PARENT_ID");
                if (searchType == SEARCH_CATEGORIES) buf.append(" = 0"); else buf.append(" != 0");
            }
            if (j.hasNext()) buf.append(" AND ");
            ++i;
        }
        if (category != null) {
            buf.append(" AND D1.PARENT_ID = ");
            buf.append(category.getPrimaryKey());
        }
        return buf.toString();
    }

    /**
	 * Create a new search engine that uses the specfied database
	 * and text processor.
	 */
    SearchEngine(IndexSearch is) {
        indexSearch = is;
        termHome = new TermHome("term");
        termStatsHome = new TermStatsHome("termStats");
    }

    private int numSearching = 0;

    private IndexSearch indexSearch;

    private TermHome termHome;

    private TermStatsHome termStatsHome;
}
