package org.nexusbpm.service.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.nexusbpm.common.DataVisitationException;
import org.nexusbpm.common.DataVisitor;

public class DatabaseDataSet implements DataSet {

    private final transient ResultSet resultSet;

    public DatabaseDataSet(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public static DatabaseDataSet getInstance(final ResultSet resultSet) {
        return new DatabaseDataSet(resultSet);
    }

    public void accept(final DataVisitor visitor) throws DataVisitationException {
        try {
            final int length = resultSet.getMetaData().getColumnCount();
            final List data = new ArrayList();
            for (int i = 0; i < length; i++) {
                data.add(resultSet.getMetaData().getColumnName(i + 1));
            }
            visitor.visitColumns(data);
            while (resultSet.next()) {
                visitor.visitData(rowToList(resultSet, length));
            }
        } catch (SQLException sqle) {
            throw new DataVisitationException(sqle);
        }
    }

    private List rowToList(final ResultSet resultSet, final int columns) throws SQLException {
        final List outData = new ArrayList();
        for (int i = 0; i < columns; i++) {
            outData.add(resultSet.getObject(i + 1));
        }
        return outData;
    }
}
