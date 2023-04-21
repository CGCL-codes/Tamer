package org.datanucleus.store.ldap.query.filter;

/**
 * Base interface for LDAP filters.
 * 
 * Copied and adapted from Apache Directory shared-ldap.
 */
public interface Filter {

    /**
     * Tests to see if this filter is a leaf or branch node.
     * @return true if the filter is a leaf node, false otherwise
     */
    boolean isLeaf();
}
