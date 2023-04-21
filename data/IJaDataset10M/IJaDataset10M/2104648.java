package desmoj.extensions.db.visustorage.dao;

import desmoj.extensions.db.DBConnection;
import desmoj.extensions.db.dao.IDDAO;
import desmoj.extensions.db.dao.SavableDAO;
import desmoj.extensions.visualEvents.SetPositionEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetPositionDAO extends IDSingleDatasetDAO<SetPositionEvent> implements SavableDAO<SetPositionEvent> {

    public static final String tableName = "V_EVENTSETPOSITION";

    public SetPositionDAO(DBConnection dbConnection, VisualDAO visualDAO) {
        super(dbConnection, visualDAO);
    }

    protected String getTableName() {
        return tableName;
    }

    public PreparedStatement getSaveStatement(SetPositionEvent event) throws SQLException {
        String sql = "INSERT INTO " + getTableName() + " (ID, X, Y, Z, VISUAL_FK) " + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmnt = connection.createPreparedStatement(sql);
        stmnt.setInt(1, getID());
        stmnt.setDouble(2, event.getX());
        stmnt.setDouble(3, event.getY());
        stmnt.setDouble(4, event.getZ());
        stmnt.setInt(5, parentDAO.getID());
        return stmnt;
    }
}
