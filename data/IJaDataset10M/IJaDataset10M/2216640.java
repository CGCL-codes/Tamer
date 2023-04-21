package edu.vt.middleware.ldap.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import edu.vt.middleware.ldap.Ldap;
import edu.vt.middleware.ldap.pool.LdapPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <code>SearchExecuter</code> contains the basic methods necessary for
 * performing a Ldap query.
 *
 * @author  Middleware Services
 * @version  $Revision: 1252 $ $Date: 2010-04-16 17:24:23 -0400 (Fri, 16 Apr 2010) $
 */
public class SearchExecutor {

    /** Identifier in search string that should be replaced with query data. */
    public static final String REGEX_QUERY = "@@@QUERY_1@@@";

    /** Identifier in search string that should be replaced with initial data. */
    public static final String REGEX_INITIAL = "@@@INITIAL_1@@@";

    /** Log for this class. */
    private static final Log LOG = LogFactory.getLog(SearchExecutor.class);

    /** Appended to every search to restrict result sets. */
    private String searchRestrictions;

    /**
   * Whether a query should use results from all searches or just the results
   * from the first match.
   */
    private boolean additive;

    /** Post processers for search results. */
    private List<PostProcessor> postProcessors = new ArrayList<PostProcessor>();

    /** Number of query terms for this search module. */
    private int termCount;

    /** Search strings for this search module. */
    private Map<Integer, String> queryTemplates = new HashMap<Integer, String>();

    /** Default constructor. */
    public SearchExecutor() {
    }

    /**
   * This creates a new <code>Search</code>.
   *
   * @param  tc  <code>int</code> number to query terms for this search
   */
    public SearchExecutor(final int tc) {
        this.termCount = tc;
    }

    /**
   * This returns the string to use for search restrictions.
   *
   * @return  <code>String</code>
   */
    public String getSearchRestrictions() {
        return this.searchRestrictions;
    }

    /**
   * This sets the string to use for search restrictions.
   *
   * @param  s  <code>String</code>
   */
    public void setSearchRestrictions(final String s) {
        this.searchRestrictions = s;
    }

    /**
   * Returns whether searches are additive.
   *
   * @return  <code>boolean</code>
   */
    public boolean isAdditive() {
        return this.additive;
    }

    /**
   * Sets whether searches should be additive.
   *
   * @param  b  whether searches are additive
   */
    public void setAdditive(final boolean b) {
        this.additive = b;
    }

    /**
   * This returns the post processors to run on search results.
   *
   * @return  <code>List</code> of post processors
   */
    public List<PostProcessor> getPostProcessors() {
        return this.postProcessors;
    }

    /**
   * This sets the post processors to run on search results.
   *
   * @param  l  list of post processors
   */
    public void setPostProcessors(final List<PostProcessor> l) {
        this.postProcessors = l;
    }

    /**
   * Returns the term count for this search.
   *
   * @return  <code>int</code>
   */
    public int getTermCount() {
        return this.termCount;
    }

    /**
   * Sets the term count for this search.
   *
   * @param  i  term count
   */
    public void setTermCount(final int i) {
        this.termCount = i;
    }

    /**
   * Returns the searches for this search.
   *
   * @return  <code>List</code> of attempt number to search string
   */
    public Map<Integer, String> getQueryTemplates() {
        return this.queryTemplates;
    }

    /**
   * Sets the searches for this search.
   *
   * @param  m  map of attempt number to search string
   */
    public void setQueryTemplates(final Map<Integer, String> m) {
        this.queryTemplates = m;
    }

    /**
   * This performs a Ldap search with the supplied <code>Query</code>.
   *
   * @param  ldapPool  <code>LdapPool</code> to use for searching
   * @param  query  <code>Query</code> to search for
   *
   * @return  <code>Iterator</code> - of search results
   *
   * @throws  PeopleSearchException  if an error occurs searching the Ldap
   */
    public Iterator<SearchResult> executeSearch(final LdapPool<Ldap> ldapPool, final Query query) throws PeopleSearchException {
        final SortedMap<Integer, SearchResult> m = new TreeMap<Integer, SearchResult>();
        final List<String> l = new ArrayList<String>();
        if (this.queryTemplates != null && this.queryTemplates.size() > 0) {
            final SearchThread[] threads = new SearchThread[this.queryTemplates.size()];
            for (int i = 1; i <= this.queryTemplates.size(); i++) {
                final String ldapQuery = this.buildLdapQuery(this.getQueryTemplate(query.getQueryParameters(), i), query.getSearchRestrictions());
                if (ldapQuery != null) {
                    threads[i - 1] = new SearchThread(ldapPool, ldapQuery, query.getQueryAttributes());
                } else {
                    threads[i - 1] = null;
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Performing search with " + threads.length + " threads");
            }
            int count = 0;
            boolean loop = true;
            while (loop && count < threads.length) {
                List<QueryResult> results = null;
                if (additive) {
                    results = this.doAdditiveSearch(threads);
                } else {
                    results = this.doIterativeSearch(threads[count]);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loop " + count + " found " + results.size() + " results");
                }
                for (int i = 0; i < results.size(); i++) {
                    int j = m.size();
                    final QueryResult qr = results.get(i);
                    if (qr != null) {
                        final SearchResult sr = qr.getSearchResult();
                        if (sr != null) {
                            if (!l.contains(sr.getName())) {
                                if (additive) {
                                    qr.setSearchIteration(i);
                                    qr.setTermCount(this.termCount);
                                    this.processResults(qr);
                                } else {
                                    qr.setSearchIteration(count);
                                    qr.setTermCount(this.termCount);
                                    this.processResults(qr);
                                }
                                m.put(new Integer(j++), sr);
                                l.add(sr.getName());
                                if (LOG.isDebugEnabled()) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Query " + (additive ? i : count) + " found: " + sr.getName());
                                    }
                                }
                            }
                        }
                    }
                }
                if (additive || !m.isEmpty()) {
                    loop = false;
                } else {
                    count++;
                }
            }
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("No searches configured");
            }
        }
        Iterator<SearchResult> i = null;
        if (query.getFromResult() != null) {
            if (query.getToResult() != null) {
                if (query.getFromResult().intValue() <= query.getToResult().intValue()) {
                    i = m.subMap(query.getFromResult(), query.getToResult()).values().iterator();
                }
            } else {
                i = m.tailMap(query.getFromResult()).values().iterator();
            }
        } else if (query.getToResult() != null) {
            i = m.headMap(query.getToResult()).values().iterator();
        } else {
            i = m.values().iterator();
        }
        return i;
    }

    /**
   * This performs an additive Ldap search.
   *
   * @param  threads  <code>SearchThread[]</code> to run search with
   *
   * @return  <code>List</code> of <code>QueryResult</code>
   */
    private List<QueryResult> doAdditiveSearch(final SearchThread[] threads) {
        final List<QueryResult> results = new ArrayList<QueryResult>(threads.length);
        for (SearchThread st : threads) {
            if (st != null) {
                st.startSearch();
            }
        }
        for (SearchThread st : threads) {
            if (st != null) {
                try {
                    st.join();
                } catch (InterruptedException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Interrupted waiting for search thread", e);
                    }
                }
            }
        }
        for (SearchThread st : threads) {
            if (st != null) {
                results.addAll(st.getResults());
            }
        }
        return results;
    }

    /**
   * This performs an iterative Ldap search.
   *
   * @param  thread  <code>SearchThread</code> to run search with
   *
   * @return  <code>List</code> of <code>QueryResult</code>
   */
    private List<QueryResult> doIterativeSearch(final SearchThread thread) {
        final List<QueryResult> results = new ArrayList<QueryResult>(1);
        if (thread != null) {
            thread.startSearch();
            try {
                thread.join();
            } catch (InterruptedException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Interrupted waiting for search thread", e);
                }
            }
            results.addAll(thread.getResults());
        }
        return results;
    }

    /**
   * This performs the processing of all search results. Post processers are run
   * and top results is used if configured.
   *
   * @param  queryResult  <code>QueryResult</code> to process
   *
   * @throws  PeopleSearchException  if an error occurs processing results
   */
    private void processResults(final QueryResult queryResult) throws PeopleSearchException {
        if (queryResult != null) {
            for (PostProcessor p : postProcessors) {
                try {
                    p.processResult(queryResult);
                } catch (NamingException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Error occured during post processing", e);
                    }
                    throw new PeopleSearchException(e);
                }
            }
        }
    }

    /**
   * This returns a ldap search string for the supply list of query parameters.
   * count represents the number of queries which have been attempted, thus far.
   *
   * @param  queryParams  <code>String[]</code> to search for
   * @param  count  <code>int</code> number of queries performed
   *
   * @return  <code>String</code> - ldap search string
   */
    private String getQueryTemplate(final String[] queryParams, final int count) {
        String query = null;
        if (queryParams != null && queryParams.length > 0 && count <= this.queryTemplates.size()) {
            query = this.queryTemplates.get(new Integer(count));
            if (query != null) {
                final String[] qp = (String[]) queryParams.clone();
                final String regexInitial = REGEX_INITIAL;
                final String[] qi = this.getInitials(qp);
                if (qi != null) {
                    for (int i = 1; i <= qi.length; i++) {
                        if (qi[i - 1] != null) {
                            query = query.replaceAll(regexInitial.replaceAll("1", Integer.toString(i)), Matcher.quoteReplacement(qi[i - 1]));
                        }
                    }
                }
                final String regexQuery = REGEX_QUERY;
                for (int i = 1; i <= qp.length; i++) {
                    if (qp[i - 1] != null) {
                        query = query.replaceAll(regexQuery.replaceAll("1", Integer.toString(i)), Matcher.quoteReplacement(qp[i - 1]));
                    }
                }
            }
        }
        return query;
    }

    /**
   * This converts an array of names into an array of initials.
   *
   * @param  queryParams  <code>String[]</code>
   *
   * @return  <code>String[]</code> - of initials
   */
    private String[] getInitials(final String[] queryParams) {
        final String[] initials = new String[queryParams.length];
        for (int i = 0; i < initials.length; i++) {
            try {
                if (queryParams[i] != null && !"".equals(queryParams[i])) {
                    initials[i] = queryParams[i].substring(0, 1);
                } else {
                    initials[i] = null;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                initials[i] = null;
            }
        }
        return initials;
    }

    /**
   * This attaches the search restrictions to the supply query.
   *
   * @param  query  <code>String</code> to search for
   * @param  dynamicRestrictions  <code>String</code> to limit search
   *
   * @return  <code>String</code> - modified query string
   */
    private String buildLdapQuery(final String query, final String dynamicRestrictions) {
        String search = null;
        if (query != null) {
            final StringBuffer s = new StringBuffer();
            if (dynamicRestrictions != null && searchRestrictions != null) {
                s.append("(&").append(searchRestrictions);
                s.append(dynamicRestrictions);
                s.append(query);
                s.append(")");
            } else if (dynamicRestrictions != null) {
                s.append("(&").append(dynamicRestrictions);
                s.append(query);
                s.append(")");
            } else if (searchRestrictions != null) {
                s.append("(&").append(searchRestrictions);
                s.append(query);
                s.append(")");
            } else {
                s.append(query);
            }
            search = s.toString();
        }
        return search;
    }
}
