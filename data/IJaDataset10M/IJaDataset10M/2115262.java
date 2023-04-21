package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;

class DB2Type4VendorFactory extends AbstractVendorFactory {

    /**
     * This is the default port assumed by the DB2 driver.
     */
    private static final int DEFAULT_PORT = 50000;

    public DB2Type4VendorFactory() {
        super(JDBCRepositoryType.DB2_TYPE4);
    }

    public InternalJDBCRepository createRepository(InternalJDBCRepositoryConfiguration configuration) {
        return new JDBCRepositoryImpl(configuration);
    }

    public String getDriverSpecificURL(MCSDriverConfiguration configuration) throws RepositoryException {
        StringBuffer url = new StringBuffer("jdbc:" + JDBCRepositoryType.DB2_TYPE4.getSubProtocol() + "://").append(getHost(configuration));
        int port = configuration.getPort();
        url.append(':').append(port);
        url.append('/').append(getSource(configuration));
        if (logger.isDebugEnabled()) {
            logger.debug("Using DB2 JDBC Type 4 driver URL of: " + url);
        }
        return url.toString();
    }
}
