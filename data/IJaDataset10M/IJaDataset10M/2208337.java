package desmoj.extensions.db.dao;

import desmoj.core.report.DebugNote;
import desmoj.extensions.db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDebugDAO extends SingleDatasetDAO<DebugNote> implements SavableDAO<DebugNote> {

    public DBDebugDAO(DBConnection dbConnection, ExperimentDAO experimentDAO) {
        super(dbConnection, experimentDAO);
    }

    public PreparedStatement getSaveStatement(DebugNote note) throws SQLException {
        String sql = "INSERT INTO OC_DEBUG (TIME, ORIGIN, DESCRIPTION, EXPERIMENT_FK) " + "VALUES (?, ?, ?, ?)";
        PreparedStatement sqlstmnt = connection.createPreparedStatement(sql);
        sqlstmnt.setString(1, note.getTime());
        sqlstmnt.setString(2, note.getOrigin());
        sqlstmnt.setString(3, note.getDescription());
        sqlstmnt.setInt(4, parentDAO.getID());
        return sqlstmnt;
    }
}
