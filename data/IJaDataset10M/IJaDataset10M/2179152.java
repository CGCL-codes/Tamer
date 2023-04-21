package edu.vt.middleware.ldap.handler;

import edu.vt.middleware.ldap.LdapEntry;
import edu.vt.middleware.ldap.LdapException;

/**
 * Provides post search processing of an ldap entry.
 *
 * @author  Middleware Services
 * @version  $Revision: 2193 $
 */
public interface LdapEntryHandler {

    /**
   * Process an entry from an ldap search.
   *
   * @param  criteria  search criteria used to perform the search
   * @param  entry  search result
   *
   * @return  handler result
   *
   * @throws  LdapException  if the LDAP returns an error
   */
    HandlerResult process(SearchCriteria criteria, LdapEntry entry) throws LdapException;
}
