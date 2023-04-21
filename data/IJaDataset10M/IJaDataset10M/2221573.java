package net.sourceforge.javautil.datasource;

import javax.sql.DataSource;
import net.sourceforge.javautil.database.jpa.descriptor.IPersistenceUnit;

/**
 * A source that can generate or provide access to {@link DataSource}'s.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IDataSourceFactory {

    /**
	 * @param identifier The identifier of the data source
	 * @return The related non JTA data source
	 * 
	 * @see IPersistenceUnit#getNonJTADataSource()
	 */
    DataSource getNonJTADataSource(String identifier);

    /**
	 * @param identifier The identifier of the data source
	 * @return The JTA data source
	 * 
	 * @see IPersistenceUnit#getJTADataSource()
	 */
    DataSource getJTADataSource(String identifier);
}
