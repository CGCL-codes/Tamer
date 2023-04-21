package desmoj.extensions.db.dao;

import desmoj.extensions.db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DBDataDistributionDAO<T> extends MultipleDatasetDAO<Vector<T>> implements LoadableDAO<Vector<T>> {

    private String tableName;

    public DBDataDistributionDAO(DBConnection connection, String tableName) {
        super(connection);
        this.tableName = tableName;
    }

    public PreparedStatement getLoadStatement() throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement stmnt = connection.createPreparedStatement(sql);
        return stmnt;
    }

    public Vector<T> toResult(ResultSet resultSet) throws SQLException {
        Vector<T> v = new Vector<T>();
        while (resultSet.next()) {
        }
        return v;
    }
}
