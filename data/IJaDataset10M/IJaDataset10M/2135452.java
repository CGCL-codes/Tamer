package org.ldaptive;

import org.ldaptive.cache.Cache;
import org.ldaptive.handler.HandlerResult;
import org.ldaptive.provider.SearchIterator;

/**
 * Executes an ldap search operation.
 *
 * @author  Middleware Services
 * @version  $Revision: 2362 $ $Date: 2012-04-26 13:33:29 -0400 (Thu, 26 Apr 2012) $
 */
public class SearchOperation extends AbstractSearchOperation<SearchRequest> {

    /**
   * Creates a new search operation.
   *
   * @param  conn  connection
   */
    public SearchOperation(final Connection conn) {
        super(conn, null);
    }

    /**
   * Creates a new search operation.
   *
   * @param  conn  connection
   * @param  c  cache
   */
    public SearchOperation(final Connection conn, final Cache<SearchRequest> c) {
        super(conn, c);
    }

    /** {@inheritDoc} */
    @Override
    protected Response<LdapResult> executeSearch(final SearchRequest request) throws LdapException {
        final LdapResult lr = new LdapResult(request.getSortBehavior());
        final SearchIterator si = getConnection().getProviderConnection().search(request);
        try {
            while (si.hasNext()) {
                final LdapEntry le = si.next();
                if (le != null) {
                    final HandlerResult hr = executeLdapEntryHandlers(request, le);
                    if (hr.getLdapEntry() != null) {
                        lr.addEntry(hr.getLdapEntry());
                    }
                    if (hr.getAbortSearch()) {
                        logger.debug("Aborting search on entry=%s", le);
                        break;
                    }
                }
            }
        } finally {
            si.close();
        }
        final Response<Void> response = si.getResponse();
        return new Response<LdapResult>(lr, response.getResultCode(), response.getMessage(), response.getMatchedDn(), response.getControls(), response.getReferralURLs());
    }
}
