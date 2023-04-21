package com.sptci.rwt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import com.sptci.util.CloseJDBCResources;

/**
 * An analyser for analysing sequence type objects in the database.  Used
 * to query the all_sequence data dictionary view in Oracle.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-11-04
 * @version $Id: OracleSequenceAnalyser.java 20 2007-11-10 00:40:51Z rakesh.vidyadharan $
 * @since Version 1.2
 */
class OracleSequenceAnalyser extends AbstractSequenceAnalyser {

    /**
   * Create a new instance of the class using the specified connection
   * manager.
   *
   * @param manager The manager for obtaining database connections.
   */
    OracleSequenceAnalyser(final ConnectionManager manager) {
        super(manager);
    }

    /**
   * Returns a collection of {@link SequenceMetaData} objects that contain
   * all information pertaining to the sequences in the specified schema.
   *
   * @see Analyser#analyse
   * @see #getNames
   * @param parameters Must contain the <code>catalog/schema</code> from
   *   which sequence metadata are to be retrieved.
   */
    @Override
    public Collection<SequenceMetaData> analyse(final MetaData... parameters) throws SQLException {
        final Collection<SequenceMetaData> collection = new ArrayList<SequenceMetaData>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            final CatalogueSchema cs = getNames(parameters[0]);
            connection = manager.open();
            if (cs.getSchema() == null) {
                final String query = "select * from all_sequences";
                System.out.format("OracleSequenceAnalyser: query: %s%n", query);
                statement = connection.prepareStatement(query);
            } else {
                final String query = "select * from all_sequences where sequence_owner = ?";
                System.out.format("OracleSequenceAnalyser: query: %s%n", query);
                statement = connection.prepareStatement(query);
                statement.setString(1, cs.getSchema());
            }
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final SequenceMetaData smd = new SequenceMetaData();
                smd.setName(resultSet.getString("sequence_name"));
                smd.setMaximum(resultSet.getString("max_value"));
                smd.setMinimum(resultSet.getString("min_value"));
                smd.setCyclePolicy(resultSet.getString("cycle_flag"));
                smd.setRoot((RootMetaData) parameters[0]);
                collection.add(smd);
            }
            ((RootMetaData) parameters[0]).setSequences(collection);
        } catch (SQLException sex) {
            logger.log(Level.INFO, "Error fetching sequence details from all_sequences", sex);
        } finally {
            CloseJDBCResources.close(resultSet);
            CloseJDBCResources.close(connection);
        }
        return collection;
    }
}
