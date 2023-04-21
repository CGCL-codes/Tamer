package org.dbunit;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.ext.oracle.OracleDataTypeFactory;

/**
 * @author Manuel Laflamme
 * @version $Revision: 1077 $
 * @since May 2, 2002
 */
public class OracleEnvironment extends DatabaseEnvironment {

    public OracleEnvironment(DatabaseProfile profile) throws Exception {
        super(profile);
    }

    protected void setupDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
    }

    public IDataSet getInitDataSet() throws Exception {
        ITable[] extraTables = { new DefaultTable("CLOB_TABLE"), new DefaultTable("BLOB_TABLE"), new DefaultTable("SDO_GEOMETRY_TABLE") };
        return new CompositeDataSet(super.getInitDataSet(), new DefaultDataSet(extraTables));
    }
}
