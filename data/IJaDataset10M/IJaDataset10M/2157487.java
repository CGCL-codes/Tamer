package nox.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import nox.ui.common.ObjectList;

public class DBReader {

    Statement statement;

    DBReader(Statement stmt) {
        this.statement = stmt;
    }

    public void doRead(ObjectList list, String tablename) {
        try {
            ResultSet results = statement.executeQuery("SELECT * FROM " + tablename);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
