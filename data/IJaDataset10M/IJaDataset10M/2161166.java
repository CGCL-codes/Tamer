package edu.vt.middleware.ldap.search;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

/**
 * <code>QueryDataPostProcesser</code> adds relevant query information to search
 * results.
 *
 * @author  Middleware Services
 * @version  $Revision: 1027 $ $Date: 2009-10-29 14:58:59 -0400 (Thu, 29 Oct 2009) $
 */
public class QueryDataPostProcessor implements PostProcessor {

    /** Default constructor. */
    public QueryDataPostProcessor() {
    }

    /**
   * This performs post processing of ldap results.
   *
   * @param  queryResult  <code>QueryResult</code>
   *
   * @throws  NamingException  if an error occurs using the search result
   */
    public void processResult(final QueryResult queryResult) throws NamingException {
        final SearchResult searchResult = queryResult.getSearchResult();
        final String[] attrs = queryResult.getQueryAttributes();
        final Attributes resultAttrs = searchResult.getAttributes();
        if (attrs != null) {
            for (String s : attrs) {
                if (s != null) {
                    if (s.equalsIgnoreCase("ldapQuery")) {
                        resultAttrs.put("ldapQuery", queryResult.getLdapQuery());
                    } else if (s.equalsIgnoreCase("termCount")) {
                        resultAttrs.put("termCount", Integer.toString(queryResult.getTermCount()));
                    } else if (s.equalsIgnoreCase("searchIteration")) {
                        resultAttrs.put("searchIteration", Integer.toString(queryResult.getSearchIteration()));
                    } else if (s.equalsIgnoreCase("searchTime")) {
                        resultAttrs.put("searchTime", Long.toString(queryResult.getSearchTime()));
                    }
                }
            }
        } else {
            resultAttrs.put("ldapQuery", queryResult.getLdapQuery());
            resultAttrs.put("termCount", Integer.toString(queryResult.getTermCount()));
            resultAttrs.put("searchIteration", Integer.toString(queryResult.getSearchIteration()));
            resultAttrs.put("searchTime", Long.toString(queryResult.getSearchTime()));
        }
        searchResult.setAttributes(resultAttrs);
    }
}
