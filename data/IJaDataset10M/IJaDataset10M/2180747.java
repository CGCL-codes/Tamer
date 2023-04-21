package org.ldaptive;

/**
 * Factory for creating connections.
 *
 * @author  Middleware Services
 * @version  $Revision: 2290 $ $Date: 2012-02-27 17:13:40 -0500 (Mon, 27 Feb 2012) $
 */
public interface ConnectionFactory {

    /**
   * Creates a new connection.
   *
   * @return  connection
   *
   * @throws  LdapException  if a connection cannot be returned
   */
    Connection getConnection() throws LdapException;
}
