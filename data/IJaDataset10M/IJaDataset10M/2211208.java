package uk.org.ogsadai.activity.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.KeyValueUnknownException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;

/**
 * An activity that retrieves the names of available tables from the database
 * indicated by the resource.
 * <p>
 * Activity inputs: none.
 * </p>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of {@link java.lang.String}. 
 * The names of the available tables. 
 * </li>
 * </ul>
  * <p>
 * Configuration parameters:
 * </p>
 * <li>
 * <code>table.types</code>. A list of JDBC table types separated by commas e.g.
 * <code>TABLE</code> or <code>TABLE,VIEW</code>. If ommited then 
 * <code>null</code> is assumed. This can be used to select which value is
 * passed to the {@link java.sql.DatabaseMetaData#getTables} method for cases
 * where drivers return system tables (for example) and these aren't to be
 * exposed to clients.
 * </li>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource:
 * </p>
 * <ul>
 * <li>
 * {@link uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider}. 
 * </li>
 * </ul>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * The activity queries the target data resource for meta-data in order to
 * retrieve the names of all available tables within the database. 
 * </li>
 * <li>
 * If there are no available tables, an empty OGSA-DAI list will be returned.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class GetAvailableTablesActivity extends ActivityBase implements ResourceActivity, ConfigurableActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2009.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(GetAvailableTablesActivity.class);

    /** 
     * Activity configuration key used to specify the table types
     * to extract.
     */
    private static final Key TABLE_TYPES = new Key("dai.table.types");

    /** 
     * Activity output name <code>data</code> - the available tables
     * (OGSA-DAI list of {@link java.lang.String}). 
     */
    public static final String OUTPUT_DATA = "data";

    /** The JDBC connection provider. */
    private JDBCConnectionProvider mResource;

    /** The database connection */
    private Connection mConnection;

    /** Types of table to get. */
    private String[] mTableTypes = null;

    /**
     * {@inheritDoc}
     */
    public Class getTargetResourceAccessorClass() {
        return JDBCConnectionProvider.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(final ResourceAccessor targetResource) {
        mResource = (JDBCConnectionProvider) targetResource;
    }

    /**
     * {@inheritDoc}
     */
    public void process() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(OUTPUT_DATA);
        BlockWriter output = getOutput(OUTPUT_DATA);
        try {
            mConnection = mResource.getConnection();
        } catch (JDBCConnectionUseException e) {
            throw new ActivitySQLException(e);
        }
        try {
            DatabaseMetaData metadata = mConnection.getMetaData();
            String catalog = mConnection.getCatalog();
            String schema = null;
            ResultSet rs = null;
            try {
                rs = metadata.getSchemas();
                if (rs.next()) {
                    schema = rs.getString(1);
                    rs.close();
                }
            } catch (Throwable e) {
                LOG.warn(ErrorID.JDBC_DRIVER_GET_SCHEMA_ERROR, new Object[] {});
                LOG.warn(e);
            }
            rs = metadata.getTables(catalog, schema, null, mTableTypes);
            output.write(ControlBlock.LIST_BEGIN);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                output.write(tableName);
            }
            output.write(ControlBlock.LIST_END);
        } catch (SQLException e) {
            throw new ActivitySQLUserException(e);
        } catch (PipeClosedException e) {
        } catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        } finally {
            try {
                if (mConnection != null) {
                    mResource.releaseConnection(mConnection);
                }
            } catch (JDBCConnectionUseException e) {
                LOG.warn(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void configureActivity(KeyValueProperties properties) {
        try {
            String tableTypesString = (String) properties.get(TABLE_TYPES);
            mTableTypes = tableTypesString.split(",");
        } catch (KeyValueUnknownException e) {
            mTableTypes = null;
        }
    }
}
