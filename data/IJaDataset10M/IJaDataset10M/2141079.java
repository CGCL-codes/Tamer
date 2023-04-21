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
 * An abstract analyser for analysing sequence type objects in the database.
 * Sub-classes implement database engine specific logic to retrieve the
 * information.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-11-04
 * @version $Id: AbstractSequenceAnalyser.java 20 2007-11-10 00:40:51Z rakesh.vidyadharan $
 * @since Version 1.2
 */
abstract class AbstractSequenceAnalyser extends Analyser {

    /**
   * Create a new instance of the class using the specified connection
   * manager.
   *
   * @param manager The manager for obtaining database connections.
   */
    AbstractSequenceAnalyser(final ConnectionManager manager) {
        super(manager);
    }

    /**
   * Returns a collection of {@link SequenceMetaData} objects that contain
   * all information pertaining to the sequences in the specified schema.
   *
   * @see Analyser#analyse
   * @param parameters Must contain the <code>catalog/schema</code> from
   *   which sequence metadata are to be retrieved.
   */
    @Override
    public abstract Collection<SequenceMetaData> analyse(final MetaData... parameters) throws SQLException;
}
