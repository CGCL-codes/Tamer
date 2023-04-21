package edu.vt.middleware.ldap.handler;

import java.util.Arrays;
import edu.vt.middleware.ldap.SearchRequest;

/**
 * Contains the attributes used to perform ldap searches.
 *
 * @author  Middleware Services
 * @version  $Revision: 2197 $ $Date: 2012-01-01 22:40:30 -0500 (Sun, 01 Jan 2012) $
 */
public class SearchCriteria {

    /** dn. */
    private String searchDn;

    /** filter. */
    private String searchFilter;

    /** filter arguments. */
    private Object[] filterArgs;

    /** return attributes. */
    private String[] returnAttrs;

    /** Default constructor. */
    public SearchCriteria() {
    }

    /**
   * Creates a new search criteria.
   *
   * @param  dn  to set
   */
    public SearchCriteria(final String dn) {
        searchDn = dn;
    }

    /**
   * Creates a new search criteria.
   *
   * @param  request  to set properties with
   */
    public SearchCriteria(final SearchRequest request) {
        setDn(request.getBaseDn());
        if (request.getSearchFilter() != null) {
            setFilter(request.getSearchFilter().getFilter());
            setFilterArgs(request.getSearchFilter().getFilterArgs().toArray());
        }
        setReturnAttrs(request.getReturnAttributes());
    }

    /**
   * Gets the dn.
   *
   * @return  dn
   */
    public String getDn() {
        return searchDn;
    }

    /**
   * Sets the dn.
   *
   * @param  dn  to set
   */
    public void setDn(final String dn) {
        searchDn = dn;
    }

    /**
   * Gets the filter.
   *
   * @return  filter
   */
    public String getFilter() {
        return searchFilter;
    }

    /**
   * Sets the filter.
   *
   * @param  filter  to set
   */
    public void setFilter(final String filter) {
        searchFilter = filter;
    }

    /**
   * Gets the filter arguments.
   *
   * @return  filter args
   */
    public Object[] getFilterArgs() {
        return filterArgs;
    }

    /**
   * Sets the filter arguments.
   *
   * @param  args  to set filter arguments
   */
    public void setFilterArgs(final Object[] args) {
        filterArgs = args;
    }

    /**
   * Gets the return attributes.
   *
   * @return  return attributes
   */
    public String[] getReturnAttrs() {
        return returnAttrs;
    }

    /**
   * Sets the return attributes.
   *
   * @param  attrs  to set return attributes
   */
    public void setReturnAttrs(final String[] attrs) {
        returnAttrs = attrs;
    }

    /**
   * Provides a descriptive string representation of this instance.
   *
   * @return  string representation
   */
    @Override
    public String toString() {
        return String.format("[%s@%d::dn=%s, filter=%s, filterArgs=%s, returnAttrs=%s]", getClass().getName(), hashCode(), searchDn, searchFilter, Arrays.toString(filterArgs), Arrays.toString(returnAttrs));
    }
}
