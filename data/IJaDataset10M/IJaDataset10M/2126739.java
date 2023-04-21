package uk.org.ogsadai.converters.resultset.webrowset;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import uk.org.ogsadai.converters.resultset.ColumnStrategy;

/**
 * Catch-all strategy for unknown types.
 *
 * @author The OGSA-DAI Project Team
 */
public class UnknownColumnStrategy implements ColumnStrategy {

    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007.";

    public void convertField(StringBuffer output, ResultSet rs, int column) throws SQLException, IOException {
        try {
            output.append(rs.getString(column));
        } catch (SQLException e) {
            output.append(("OGSA-DAI Converter - Unknown Type: " + rs.getMetaData().getColumnType(column)));
        }
    }
}
