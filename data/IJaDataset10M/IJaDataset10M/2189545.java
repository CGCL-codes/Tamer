package au.edu.educationau.opensource.dsm.obj;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import au.edu.educationau.opensource.dsm.util.EducationAuUtils;

/**
 * This is the search criteria object that holds the requirements of a client to
 * search the repositories. The adapters use this to convert these settings to a
 * format it's repository can understand.
 */
public class SearchCriteria extends java.lang.Object implements java.io.Serializable, java.lang.Cloneable {

    static Logger logger = Logger.getLogger(SearchCriteria.class.getName());

    /** Keywords XML(q) */
    private String query = "";

    /** CQL query - unmodified */
    private String cqlQuery = "";

    /** Just an array of the q objects */
    private String[] queryArray = new String[0];

    /** Source repositories XML(sr) */
    private String[] sources = new String[0];

    /** Case sensitive XML(cs) */
    private boolean caseSensitive = false;

    /** Thesauri XML(th) */
    private String[] thesauri = new String[0];

    /** Maximum results XML(mr) */
    private int maxResults = 100;

    /** Keyword constraint XML(kc) */
    private String keywordConstraint = "all";

    /** Search mode XML(mode) */
    private String mode = "search";

    /** Search strategy XML(ss) */
    private String searchStrategy = SMEvents.SEARCH_STRATEGY_WAIT_FOR_FASTEST;

    /** Repository search list to wait for if we use 'ss=waitfor' , parameter name is ss.sr */
    private String[] targetRepositories = new String[0];

    /** Maximum wait time for on a search*/
    private int maxWaitTime = 5;

    /**
	 * All custom parameters per adapter here. prefixed at web by '<adaptercode>.'
	 * Example edna.sector is the edna sector param.
	 */
    private Hashtable customParams = new Hashtable(5);

    /** The cache token (token) */
    private long token = -1;

    /** The usercode attempting this search criteria */
    private String userCode = "";

    /** The CQL tree that can be parsed to build a query */
    private String cqlTree = null;

    /**
	 * Set the token for this criteria to use to cache results
	 * 
	 * @param token
	 */
    public synchronized void setToken(long token) {
        this.token = token;
    }

    /**
	 * Returns the allocated token
	 */
    public long getToken() {
        return this.token;
    }

    /**
	 * Set the query string
	 * 
	 * @param query
	 */
    public void setQuery(String query) {
        query.replace('+', ' ');
        this.query = query.trim();
        if (this.query.indexOf(" ") > 0) {
            StringTokenizer st = new StringTokenizer(query, " ");
            queryArray = new String[st.countTokens()];
            int idx = 0;
            while (st.hasMoreTokens()) {
                String qrytxt = (String) st.nextToken();
                if (qrytxt.length() > 0) {
                    queryArray[idx] = qrytxt;
                }
                idx++;
            }
        } else {
            queryArray = new String[] { this.query };
        }
    }

    /**
	 * Returns the query string
	 */
    public String getQuery() {
        return this.query;
    }

    /**
	 * Returns the query in a String array of whole words
	 */
    public String[] getQueryArray() {
        return this.queryArray;
    }

    /**
	 * Sets the sources to search in as an array of source adapter codes
	 * 
	 * @param sources
	 */
    public void setSources(String[] sources) {
        this.sources = EducationAuUtils.removeDuplicates(sources);
        Arrays.sort(sources);
    }

    /**
	 * Returns the array of sources
	 */
    public String[] getSources() {
        return this.sources;
    }

    /**
	 * Set the case sensitivity of the search query
	 * 
	 * @param caseSensitive
	 */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
	 * Returns the case sensitivity of the search query
	 */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
	 * Sets the maximum number of results in the Most Relevant Results tab. Note
	 * this does not set the batch size of each adapter.
	 * 
	 * @param maxResults
	 */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    /**
	 * Returns the maximum number of results
	 */
    public int getMaxResults() {
        return this.maxResults;
    }

    /**
	 * Sets the thesauri to search in specified by an array of thesauri DB table
	 * names
	 * 
	 * @param thesauri
	 */
    public void setThesauri(String[] thesauri) {
        if (null != thesauri) {
            this.thesauri = thesauri;
            Arrays.sort(thesauri);
        }
    }

    /** Returns the thesauri DB table names to lookup */
    public String[] getThesauri() {
        return this.thesauri;
    }

    /**
	 * Sets the search query constraint - All of the query terms, any of the
	 * query terms and the exact query term
	 * 
	 * @param keywordConstraint
	 */
    public void setKeywordConstraint(String keywordConstraint) {
        this.keywordConstraint = keywordConstraint;
    }

    /** Returns the search query constraint */
    public String getKeywordConstraint() {
        return this.keywordConstraint;
    }

    /**
	 * Sets the seaerch strategy of this job. The only reason it is here is
	 * because it needs to be passed in from the web tier. The only two options
	 * at the moment are WAIT FOR ALL, where all repositories must return before
	 * the client sees any results, and WAIT FOR FASTEST, where the first
	 * adapter that returns results will be passed onto the client immediately
	 * while the rest await completion.
	 * 
	 * @param searchStrategy
	 */
    public void setSearchStrategy(String searchStrategy) {
        this.searchStrategy = searchStrategy.toLowerCase().trim();
    }

    /** Returns the search strategy s */
    public String getSearchStrategy() {
        return this.searchStrategy;
    }

    /**
	 * Sets the mode for this search job - search, thesauri, spelling Search
	 * strategy is ignored for thesauri and spelling
	 * 
	 * @param mode
	 */
    public void setMode(String mode) {
        this.mode = mode.toLowerCase();
    }

    /** Returns the mode of this search */
    public String getMode() {
        return this.mode;
    }

    /**
	 * Add a parameter/value pair that is specific to an adapter. The key is
	 * prefixed by <source code>. to differentiate identical parameters.
	 * 
	 * @param paramName
	 * @param value
	 */
    public void addCustomParam(String paramName, String value) {
        this.customParams.put(paramName, new String[] { value });
    }

    /**
	 * Add an array of parameter/value pair that is specific to an adapter. The
	 * key is prefixed by <source code>. to differentiate identical parameters.
	 * 
	 * @param paramName
	 * @param values
	 *            a String [] of values
	 */
    public void addCustomParamList(String paramName, String[] values) {
        this.customParams.put(paramName, values);
    }

    /** Checks if there are any custom parameters to process */
    public boolean hasCustomParams() {
        return this.customParams.size() > 0;
    }

    /** Returns the custom params set at the web end */
    public Enumeration getCustomParams() {
        return this.customParams.keys();
    }

    /**
	 * Helper method to get a particular custom parameter's value. Can return
	 * null.
	 * 
	 * @param key
	 */
    public String getCustomParamValue(String key) {
        String[] vals = (String[]) this.customParams.get(key);
        if (vals != null && vals.length >= 1) {
            return vals[0];
        } else {
            return null;
        }
    }

    /**
	 * Helper method to get a particular custom parameter's values as an array.
	 * Can return null.
	 * 
	 * @param key
	 */
    public String[] getCustomParamValues(String key) {
        return (String[]) this.customParams.get(key);
    }

    /**
	 * Helper method to get all custom params with a given prefix. Returns a map
	 * keyed on param name of String arrays, may be empty, never null.
	 * 
	 * @param key
	 */
    public Map getCustomParamValuesByPrefix(String prefix) {
        return EducationAuUtils.getSubmapByKeyPrefix(this.customParams, prefix, true);
    }

    /**
	 * Sets the user code for the search request. Can be used to pass on to
	 * clients in the backend adapters.
	 * 
	 * @param userCode
	 */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /** Returns the user code associated with this request */
    public String getUserCode() {
        return this.userCode;
    }

    /**
	 * Sets the CQL Query as obtained originally
	 * 
	 * @param cqlQuery
	 */
    public void setCQLQuery(String cqlQuery) {
        this.cqlQuery = cqlQuery;
    }

    /** Returns the CQL Query as obtained originally */
    public String getCQLQuery() {
        return this.cqlQuery;
    }

    /**
	 * Sets the CQL Tree for the search request. Can be used to pass on to
	 * clients in the backend adapters.
	 * 
	 * @param cqlTree
	 */
    public void setCQLTree(String cqlTree) {
        this.cqlTree = cqlTree;
    }

    /** Returns the user code associated with this request */
    public String getCQLTree() {
        return this.cqlTree;
    }

    /**
	 * Criteria matches if the token matches. You can change this to do more
	 * verbose checking. However, for most applications, once the search process
	 * is kicked off the parameters changing do very little to the output. Hence
	 * a new search might as well be initiated. Useful in an environment where
	 * multiple searches are conducted by one client.
	 * 
	 * @param o
	 */
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        } else {
            SearchCriteria sc = (SearchCriteria) o;
            return this.token == sc.getToken();
        }
    }

    /**
	 * Returns a shallow clone of this object. Arrays and Hashtables are shared
	 * between the original and the clone.
	 */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public String[] getTargetRepositories() {
        return targetRepositories;
    }

    public void setTargetRepositories(String[] targetRepositories) {
        this.targetRepositories = targetRepositories;
    }
}
