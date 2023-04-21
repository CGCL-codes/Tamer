package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * A MySQL specialization of JDBCRepository.
 *
 * @todo remove as this is no longer needed.
 */
class MySQLRepository extends JDBCRepositoryImpl {

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    protected MySQLRepository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);
    }
}
