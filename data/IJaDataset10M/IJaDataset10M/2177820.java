package edu.vt.middleware.ldap;

/**
 * Executes an ldap rename operation.
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
 */
public class RenameOperation extends AbstractOperation<RenameRequest, Void> {

    /**
   * Creates a new rename operation.
   *
   * @param  conn  connection
   */
    public RenameOperation(final Connection conn) {
        super(conn);
    }

    /** {@inheritDoc} */
    @Override
    protected Response<Void> invoke(final RenameRequest request) throws LdapException {
        return getConnection().getProviderConnection().rename(request);
    }

    /** {@inheritDoc} */
    @Override
    protected void initializeRequest(final RenameRequest request, final ConnectionConfig cc) {
    }
}
