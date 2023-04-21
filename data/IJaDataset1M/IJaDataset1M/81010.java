package org.apache.commons.dbcp;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A {@link Driver}-based implementation of {@link ConnectionFactory}.
 *
 * @author Rodney Waldhoff
 * @version $Revision: 479137 $ $Date: 2006-11-25 08:51:48 -0700 (Sat, 25 Nov 2006) $
 */
public class DriverConnectionFactory implements ConnectionFactory {

    public DriverConnectionFactory(Driver driver, String connectUri, Properties props) {
        _driver = driver;
        _connectUri = connectUri;
        _props = props;
    }

    public Connection createConnection() throws SQLException {
        return _driver.connect(_connectUri, _props);
    }

    protected Driver _driver = null;

    protected String _connectUri = null;

    protected Properties _props = null;

    public String toString() {
        return this.getClass().getName() + " [" + String.valueOf(_driver) + ";" + String.valueOf(_connectUri) + ";" + String.valueOf(_props) + "]";
    }
}
