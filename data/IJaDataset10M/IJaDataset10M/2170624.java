package com.frameworkset.commons.dbcp.datasources;

import javax.sql.PooledConnection;

/**
 * @version $Revision: 479137 $ $Date: 2006-11-25 08:51:48 -0700 (Sat, 25 Nov 2006) $
 */
final class PooledConnectionAndInfo {

    private final PooledConnection pooledConnection;

    private final String password;

    private final String username;

    private final UserPassKey upkey;

    PooledConnectionAndInfo(PooledConnection pc, String username, String password) {
        this.pooledConnection = pc;
        this.username = username;
        this.password = password;
        upkey = new UserPassKey(username, password);
    }

    final PooledConnection getPooledConnection() {
        return pooledConnection;
    }

    final UserPassKey getUserPassKey() {
        return upkey;
    }

    /**
     * Get the value of password.
     * @return value of password.
     */
    final String getPassword() {
        return password;
    }

    /**
     * Get the value of username.
     * @return value of username.
     */
    final String getUsername() {
        return username;
    }
}
