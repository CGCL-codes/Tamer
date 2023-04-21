package edu.psu.citeseerx.myciteseer.dao;

import java.sql.SQLException;
import edu.psu.citeseerx.myciteseer.domain.MCSConfiguration;

/**
 * Provides transparent access to the personal portal configuration data
 * persistence storage.
 * @author Isaac Councill
 * @author Juan Pablo Fernandez Ramirez
 * @version $Rev: 827 $ $Date: 2008-12-19 09:56:50 -0500 (Fri, 19 Dec 2008) $
 */
public interface ConfigurationDAO {

    /**
	 * @return The personal portal configuration values
	 * @throws SQLException
	 */
    public MCSConfiguration getConfiguration() throws SQLException;

    /**
	 * Stores the new configuration values
	 * @param configuration
	 * @throws SQLException
	 */
    public void saveConfiguration(MCSConfiguration configuration) throws SQLException;
}
