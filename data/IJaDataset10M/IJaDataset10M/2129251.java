package edu.vt.middleware.ldap;

/**
 * Executes an ldap compare operation.
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
 */
public class CompareOperation extends AbstractOperation<CompareRequest, Boolean> {

    /**
   * Creates a new compare operation.
   *
   * @param  conn  connection
   */
    public CompareOperation(final Connection conn) {
        super(conn);
    }

    /** {@inheritDoc} */
    @Override
    protected Response<Boolean> invoke(final CompareRequest request) throws LdapException {
        return getConnection().getProviderConnection().compare(request);
    }

    /** {@inheritDoc} */
    @Override
    protected void initializeRequest(final CompareRequest request, final ConnectionConfig cc) {
    }
}
