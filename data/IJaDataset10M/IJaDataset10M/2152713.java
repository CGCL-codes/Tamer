package net.seagis.coverage.model;

import java.sql.SQLException;
import java.sql.ResultSet;
import org.opengis.parameter.ParameterValueGroup;
import net.seagis.catalog.CatalogException;
import net.seagis.catalog.Database;
import net.seagis.catalog.SingletonTable;

/**
 * Connection to the table of image {@linkplain Operation operations}.
 * 
 * @version $Id: OperationTable.java 162 2007-11-06 15:05:54Z desruisseaux $
 * @author Antoine Hnawia
 * @author Martin Desruisseaux
 */
public class OperationTable extends SingletonTable<Operation> {

    /** 
     * La table des paramètres des opérations. Ne sera construite que la première fois
     * où elle sera nécessaire.
     */
    private OperationParameterTable parameters;

    /** 
     * Creates an operation table.
     * 
     * @param database Connection to the database.
     */
    public OperationTable(final Database database) {
        super(new OperationQuery(database));
        setIdentifierParameters(((OperationQuery) query).byName, null);
    }

    /**
     * Creates an operation for the current row in the specified result set.
     *
     * @param  results The result set to read.
     * @return The entry for current row in the specified result set.
     * @throws CatalogException if an inconsistent record is found in the database.
     * @throws SQLException if an error occured while reading the database.
     */
    protected Operation createEntry(final ResultSet results) throws SQLException, CatalogException {
        final OperationQuery query = (OperationQuery) super.query;
        final String name = results.getString(indexOf(query.name));
        final String prefix = results.getString(indexOf(query.prefix));
        final String operation = results.getString(indexOf(query.operation));
        final String remarks = results.getString(indexOf(query.remarks));
        final OperationEntry entry = new OperationEntry(name, prefix, operation, remarks);
        final ParameterValueGroup values = entry.getParameters();
        if (values != null) {
            if (parameters == null) {
                parameters = getDatabase().getTable(OperationParameterTable.class);
            }
            parameters.fillValues(name, values);
        }
        return entry;
    }
}
